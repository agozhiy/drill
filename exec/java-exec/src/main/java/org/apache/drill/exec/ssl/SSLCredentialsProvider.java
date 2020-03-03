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
package org.apache.drill.exec.ssl;

import java.util.Optional;
import java.util.function.BiFunction;

abstract class SSLCredentialsProvider {

  private static final String MAPR_CREDENTIALS_PROVIDER_CLIENT = "org.apache.drill.exec.ssl.SSLCredentialsProviderMaprClient";
  private static final String MAPR_CREDENTIALS_PROVIDER_SERVER = "org.apache.drill.exec.ssl.SSLCredentialsProviderMaprServer";

  static SSLCredentialsProvider getSSLCredentialsProvider(BiFunction<String, String, String> getPropertyMethod, SSLConfig.Mode mode, boolean useMapRSSLConfig) {
    return useMapRSSLConfig ?
        Optional.ofNullable(getMaprCredentialsProvider(mode))
            .orElse(new SSLCredentialsProviderImpl(getPropertyMethod)) :
        new SSLCredentialsProviderImpl(getPropertyMethod);
  }

  private static SSLCredentialsProvider getMaprCredentialsProvider(SSLConfig.Mode mode) {
    try {
      return (SSLCredentialsProvider) Class.forName(getMaprCredentialsProviderClassName(mode)).newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      // This is expected if built without MapR profile
      return null;
    }
  }

  private static String getMaprCredentialsProviderClassName(SSLConfig.Mode mode) {
    switch (mode) {
      case SERVER:
        return MAPR_CREDENTIALS_PROVIDER_SERVER;
      case CLIENT:
        return MAPR_CREDENTIALS_PROVIDER_CLIENT;
    }
    return "";
  }

  abstract String getTrustStoreType(String propertyName, String defaultValue);

  abstract String getTrustStoreLocation(String propertyName, String defaultValue);

  abstract String getTrustStorePassword(String propertyName, String defaultValue);

  abstract String getKeyStoreType(String propertyName, String defaultValue);

  abstract String getKeyStoreLocation(String propertyName, String defaultValue);

  abstract String getKeyStorePassword(String propertyName, String defaultValue);

  abstract String getKeyPassword(String propertyName, String defaultValue);

  private static class SSLCredentialsProviderImpl extends SSLCredentialsProvider {

    private BiFunction<String, String, String> getPropertyMethod;

    private SSLCredentialsProviderImpl(BiFunction<String, String, String> getPropertyMethod) {
      this.getPropertyMethod = getPropertyMethod;
    }

    @Override
    String getTrustStoreType(String propertyName, String defaultValue) {
      return getPropertyMethod.apply(propertyName, defaultValue);
    }

    @Override
    String getTrustStoreLocation(String propertyName, String defaultValue) {
      return getPropertyMethod.apply(propertyName, defaultValue);
    }

    @Override
    String getTrustStorePassword(String propertyName, String defaultValue) {
      return getPropertyMethod.apply(propertyName, defaultValue);
    }

    @Override
    String getKeyStoreType(String propertyName, String defaultValue) {
      return getPropertyMethod.apply(propertyName, defaultValue);
    }

    @Override
    String getKeyStoreLocation(String propertyName, String defaultValue) {
      return getPropertyMethod.apply(propertyName, defaultValue);
    }

    @Override
    String getKeyStorePassword(String propertyName, String defaultValue) {
      return getPropertyMethod.apply(propertyName, defaultValue);
    }

    @Override
    String getKeyPassword(String propertyName, String defaultValue) {
      return getPropertyMethod.apply(propertyName, defaultValue);
    }
  }
}
