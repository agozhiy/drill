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

package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * This class is needed for compatibility with recent versions of protobuf.
 * There was a class with the same name that was package local but in version 3.6.0 it became private.
 * Contains an instance of {@link ByteString.LiteralByteString} and delegates all method calls to it.
 */
class LiteralByteString extends ByteString {

  private final ByteString wrappedString;
  byte[] bytes;

  LiteralByteString(byte[] bytes) {
    wrappedString = ByteString.wrap(bytes);
    bytes = wrappedString.toByteArray();
  }

  LiteralByteString(byte[] bytes, int offset, int length) {
    wrappedString = ByteString.wrap(bytes, offset, length);
  }

  @Override
  public byte byteAt(int i) {
    return wrappedString.byteAt(i);
  }

  @Override
  public int size() {
    return wrappedString.size();
  }

  @Override
  public ByteString substring(int i, int i1) {
    return wrappedString.substring(i, i1);
  }

  @Override
  protected void copyToInternal(byte[] bytes, int i, int i1, int i2) {
    wrappedString.copyToInternal(bytes, i, i1, i2);
  }

  @Override
  public void copyTo(ByteBuffer byteBuffer) {
    wrappedString.copyTo(byteBuffer);
  }

  @Override
  public void writeTo(OutputStream outputStream) throws IOException {
    wrappedString.writeTo(outputStream);
  }

  @Override
  void writeToInternal(OutputStream outputStream, int i, int i1) throws IOException {
    wrappedString.writeToInternal(outputStream, i, i1);
  }

  @Override
  void writeTo(ByteOutput byteOutput) throws IOException {
    wrappedString.writeTo(byteOutput);
  }

  @Override
  public ByteBuffer asReadOnlyByteBuffer() {
    return wrappedString.asReadOnlyByteBuffer();
  }

  @Override
  public List<ByteBuffer> asReadOnlyByteBufferList() {
    return wrappedString.asReadOnlyByteBufferList();
  }

  @Override
  protected String toStringInternal(Charset charset) {
    return wrappedString.toStringInternal(charset);
  }

  @Override
  public boolean isValidUtf8() {
    return wrappedString.isValidUtf8();
  }

  @Override
  protected int partialIsValidUtf8(int i, int i1, int i2) {
    return wrappedString.partialIsValidUtf8(i, i1, i2);
  }

  @Override
  public boolean equals(Object o) {
    return wrappedString.equals(o);
  }

  @Override
  public InputStream newInput() {
    return wrappedString.newInput();
  }

  @Override
  public CodedInputStream newCodedInput() {
    return wrappedString.newCodedInput();
  }

  @Override
  protected int getTreeDepth() {
    return wrappedString.getTreeDepth();
  }

  @Override
  protected boolean isBalanced() {
    return wrappedString.isBalanced();
  }

  @Override
  protected int partialHash(int i, int i1, int i2) {
    return wrappedString.partialHash(i, i1, i2);
  }
}
