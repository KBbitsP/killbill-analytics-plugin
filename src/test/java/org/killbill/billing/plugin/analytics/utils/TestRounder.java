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

package org.killbill.billing.plugin.analytics.utils;

import java.math.BigDecimal;

import org.killbill.billing.plugin.analytics.AnalyticsTestSuiteNoDB;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestRounder extends AnalyticsTestSuiteNoDB {

    @Test(groups = "fast")
    public void testRound() throws Exception {
        Assert.assertEquals(Rounder.round(null), 0.0);
        Assert.assertEquals(Rounder.round(BigDecimal.ZERO), 0.0);
        Assert.assertEquals(Rounder.round(BigDecimal.ONE), 1.0);
        Assert.assertEquals(Rounder.round(BigDecimal.TEN), 10.0);
        Assert.assertEquals(Rounder.round(BigDecimal.valueOf(1.33333)), 1.3333);
        Assert.assertEquals(Rounder.round(BigDecimal.valueOf(4444.33333)), 4444.3333);
        Assert.assertEquals(Rounder.round(BigDecimal.valueOf(10.11111)), 10.1111);
        Assert.assertEquals(Rounder.round(BigDecimal.valueOf(10.11115)), 10.1112);
        Assert.assertEquals(Rounder.round(BigDecimal.valueOf(10.11116)), 10.1112);
    }
}
