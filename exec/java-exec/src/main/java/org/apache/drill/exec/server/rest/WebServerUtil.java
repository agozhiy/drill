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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class WebServerUtil {

  public static String getFormattedString(Object value, TypeProtos.MinorType minorType) {
    switch (minorType) {
      case TIMESTAMP:
        if (value instanceof LocalDateTime) {
          DateTimeFormatter formatter = new DateTimeFormatterBuilder()
              .appendPattern("yyyy-MM-dd HH:mm:ss")
              .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
              .toFormatter();
          return ((LocalDateTime) value).format(formatter);
        } else {
          return value.toString();
        }
      default:
        return value.toString();
    }
  }
}
