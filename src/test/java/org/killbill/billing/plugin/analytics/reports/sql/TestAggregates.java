/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2020 Equinix, Inc
 * Copyright 2014-2020 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.plugin.analytics.reports.sql;

import org.killbill.billing.plugin.analytics.AnalyticsTestSuiteNoDB;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestAggregates extends AnalyticsTestSuiteNoDB {

    @Test(groups = "fast")
    public void testCheckRegexp() throws Exception {
        Assert.assertNull(Aggregates.of("foo"));
        Assert.assertEquals(Aggregates.of("sum(fee)").toString(), "sum(\"fee\")");
        Assert.assertEquals(Aggregates.of("avg(fee)").toString(), "avg(\"fee\")");
        Assert.assertEquals(Aggregates.of("count(fee)").toString(), "count(\"fee\")");
        Assert.assertEquals(Aggregates.of("count(distinct fee)").toString(), "count(distinct \"fee\")");
        Assert.assertEquals(Aggregates.of("sum(fee_with_underscores)").toString(), "sum(\"fee_with_underscores\")");
    }
}
