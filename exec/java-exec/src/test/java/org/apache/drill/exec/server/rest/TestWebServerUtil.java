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
package org.apache.drill.exec.server.rest;

import org.apache.drill.common.types.TypeProtos;
import org.junit.Test;
import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;

public class TestWebServerUtil {

  @Test
  public void testTimestampWithThreeDigitMillis() {
    String formattedValue = WebServerUtil.getFormattedString(
        LocalDateTime.of(2012, 11, 5, 13, 0, 30, 12000000),
        TypeProtos.MinorType.TIMESTAMP);
    assertTrue(formattedValue.equals("2012-11-05 13:00:30.012"));
  }

  @Test
  public void testTimestampWithTwoDigitMillis() {
    String formattedValue = WebServerUtil.getFormattedString(
        LocalDateTime.of(2012, 11, 5, 13, 0, 30, 120000000),
        TypeProtos.MinorType.TIMESTAMP);
    assertTrue(formattedValue.equals("2012-11-05 13:00:30.12"));
  }

  @Test
  public void testTimestampWithNoMillis() {
    String formattedValue = WebServerUtil.getFormattedString(
        LocalDateTime.of(2012, 11, 5, 13, 0, 30),
        TypeProtos.MinorType.TIMESTAMP);
    assertTrue(formattedValue.equals("2012-11-05 13:00:30.0"));
  }

  @Test
  public void testString() {
    String formattedValue = WebServerUtil.getFormattedString(
        "2012-11-05 13:00:30.0",
        TypeProtos.MinorType.VARCHAR);
    assertTrue(formattedValue.equals("2012-11-05 13:00:30.0"));
  }
}
