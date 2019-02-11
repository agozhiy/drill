/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.drill.exec.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class ProtobufPatcher {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProtobufPatcher.class);

  private static boolean patched;

  public static synchronized void patch() {
    if (!patched) {
      try {
        patchByteString();
        patchGeneratedMessageLite();
        patchGeneratedMessageLiteBuilder();
        patched = true;
      } catch (Exception e) {
        logger.warn("Unable to patch Protobuf.", e);
      }
    }
  }


  /**
   * HBase client overrides methods from {@link com.google.protobuf.ByteString},
   * that were made final in version 3.6.0 of protobuf.
   * This method removes the final modifiers.
   *
   * @throws NotFoundException if unable to find a method or class to patch.
   * @throws CannotCompileException if unable to compile the patched class.
   */
  private static void patchByteString() throws NotFoundException, CannotCompileException {
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.get("com.google.protobuf.ByteString");
    removeFinal(cc.getDeclaredMethod("toString"));
    removeFinal(cc.getDeclaredMethod("hashCode"));
    removeFinal(cc.getDeclaredMethod("iterator"));
    cc.toClass();
  }

  /**
   * MapR-DB client extends {@link com.google.protobuf.GeneratedMessageLite} and overrides some methods,
   * that were made final in version 3.6.0 of protobuf.
   * This method removes the final modifiers.
   *
   * @throws NotFoundException if unable to find a method or class to patch.
   * @throws CannotCompileException if unable to compile the patched method body.
   */
  private static void patchGeneratedMessageLite() throws NotFoundException, CannotCompileException {
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.get("com.google.protobuf.GeneratedMessageLite");
    removeFinal(cc.getDeclaredMethod("getParserForType"));
    removeFinal(cc.getDeclaredMethod("isInitialized"));

    // The method was removed, but it is used in com.mapr.fs.proto.Dbserver.
    // Adding it back.
    cc.addMethod(CtNewMethod.make("protected void makeExtensionsImmutable() { }", cc));

    // A constructor with this signature was removed. Adding it back.
    cc.addConstructor(CtNewConstructor.make("protected GeneratedMessageLite(com.google.protobuf.GeneratedMessageLite.Builder builder) { }", cc));

    // This single method was added instead of several abstract methods.
    // MapR-DB client doesn't use it, but it was added in overridden equals() method.
    // Adding default implementation.
    CtMethod cm = cc.getDeclaredMethod("dynamicMethod", new CtClass[] {
        cp.get("com.google.protobuf.GeneratedMessageLite$MethodToInvoke"),
        cp.get("java.lang.Object"),
        cp.get("java.lang.Object")});
    addImplementation(cm, "if ($1.equals(com.google.protobuf.GeneratedMessageLite.MethodToInvoke.GET_DEFAULT_INSTANCE)) {" +
                                  "  return this;" +
                                  "} else {" +
                                  "  return null;" +
                                  "}");
    cc.toClass();
  }

  /**
   * MapR-DB client extends {@link com.google.protobuf.GeneratedMessageLite.Builder} and overrides some methods,
   * that were made final in version 3.6.0 of protobuf.
   * This method removes the final modifiers.
   * Also, adding back a default constructor that was removed.
   *
   * @throws NotFoundException if unable to find a method or class to patch.
   * @throws CannotCompileException if unable to add a default constructor.
   */
  private static void patchGeneratedMessageLiteBuilder() throws NotFoundException, CannotCompileException {
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.get("com.google.protobuf.GeneratedMessageLite$Builder");
    removeFinal(cc.getDeclaredMethod("isInitialized"));
    removeFinal(cc.getDeclaredMethod("clear"));
    cc.addConstructor(CtNewConstructor.defaultConstructor(cc));
    cc.toClass();
  }

  /**
   * Removes final modifier from a given method.
   *
   * @param ctMethod method which need to be non-final.
   */
  private static void removeFinal(CtMethod ctMethod) {
    int modifiers = Modifier.clear(ctMethod.getModifiers(), Modifier.FINAL);
    ctMethod.setModifiers(modifiers);
  }


  /**
   * Removes abstract modifier and adds implementation to a given method.
   *
   * @param ctMethod method to process.
   * @param methodBody method implementation.
   * @throws CannotCompileException if unable to compile given method body.
   */
  private static void addImplementation(CtMethod ctMethod, String methodBody) throws CannotCompileException {
    ctMethod.setBody(methodBody);
    int modifiers = Modifier.clear(ctMethod.getModifiers(), Modifier.ABSTRACT);
    ctMethod.setModifiers(modifiers);
  }
}
