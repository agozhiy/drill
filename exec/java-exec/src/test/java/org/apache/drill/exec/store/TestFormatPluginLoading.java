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

package org.apache.drill.exec.store;

import org.apache.drill.common.logical.StoragePluginConfig;
import org.apache.drill.exec.ExecTest;
import org.apache.drill.exec.store.dfs.FileSystemConfig;
import org.apache.drill.exec.store.easy.text.TextFormatPlugin;
import org.apache.drill.exec.store.sys.PersistentStore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestFormatPluginLoading extends ExecTest {
  private final StoragePluginRegistryImpl storagePluginRegistry;
  private final PersistentStore<StoragePluginConfig> plugins;

  public TestFormatPluginLoading() throws Exception {
    storagePluginRegistry = new StoragePluginRegistryImpl(mockDrillbitContext());
    storagePluginRegistry.init();
    plugins = storagePluginRegistry.getStore();
  }

  @Test
  public void testAddStoragePlugins() {
    assertTrue("[storage1] is not present!", plugins.contains("storage1"));
    assertTrue("[storage2] is not present!", plugins.contains("storage2"));
    assertFalse("[storage3] is present!", plugins.contains("storage3"));
  }

  @Test
  public void testAddFirstFormat() {
    assertTrue("[format1] was not added to [storage1]",
        ((FileSystemConfig) plugins.get("storage1")).getFormats().containsKey("format1"));
  }

  @Test
  public void testAddFormatToExistingFormats() {
    assertTrue("[format1] was not added to [storage2]",
        ((FileSystemConfig) plugins.get("storage2")).getFormats().containsKey("format1"));
    assertTrue("[format2] was not added to [storage2]",
        ((FileSystemConfig) plugins.get("storage2")).getFormats().containsKey("format2"));
    assertTrue("[format3] was not added to [storage2]",
        ((FileSystemConfig) plugins.get("storage2")).getFormats().containsKey("format3"));
  }

  @Test
  public void testAddDuplicateFormat() {
    assertTrue("[format1] was replaced by duplicate at [storage2]!",
        ((FileSystemConfig) plugins.get("storage2")).getFormats().get("format1") instanceof TextFormatPlugin.TextFormatConfig);
  }

}
