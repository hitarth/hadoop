/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.yarn.server.sharedcachemanager.metrics;

import static org.junit.Assert.assertEquals;

import org.apache.hadoop.conf.Configuration;
import org.junit.Before;
import org.junit.Test;

public class TestCleanerMetrics {

  Configuration conf = new Configuration();
  CleanerMetrics cleanerMetrics;

  @Before
  public void init() {
    CleanerMetrics.initSingleton(conf);
    cleanerMetrics = CleanerMetrics.getInstance();
  }

  @Test
  public void testMetricsOverMultiplePeriods() {
    simulateACleanerRun();
    assertMetrics(4, 4, 1, 1);
    simulateACleanerRun();
    assertMetrics(4, 8, 1, 2);
  }

  public void simulateACleanerRun() {
    cleanerMetrics.reportCleaningStart();
    cleanerMetrics.reportAFileProcess();
    cleanerMetrics.reportAFileDelete();
    cleanerMetrics.reportAFileProcess();
    cleanerMetrics.reportAFileProcess();
  }

  void assertMetrics(int proc, int totalProc, int del, int totalDel) {
    assertEquals(
        "Processed files in the last period are not measured correctly", proc,
        cleanerMetrics.getProcessedFiles());
    assertEquals("Total processed files are not measured correctly",
        totalProc, cleanerMetrics.getTotalProcessedFiles());
    assertEquals(
        "Deleted files in the last period are not measured correctly", del,
        cleanerMetrics.getDeletedFiles());
    assertEquals("Total deleted files are not measured correctly",
        totalDel, cleanerMetrics.getTotalDeletedFiles());
  }
}
