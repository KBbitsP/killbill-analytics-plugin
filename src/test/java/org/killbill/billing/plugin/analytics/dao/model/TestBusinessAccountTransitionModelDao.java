/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2022 Equinix, Inc
 * Copyright 2014-2022 The Billing Project, LLC
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

package org.killbill.billing.plugin.analytics.dao.model;

import org.joda.time.DateTime;
import org.killbill.billing.plugin.analytics.AnalyticsTestSuiteNoDB;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestBusinessAccountTransitionModelDao extends AnalyticsTestSuiteNoDB {

    @Test(groups = "fast")
    public void testEquals() throws Exception {
        final DateTime startDate = new DateTime("2012-06-21");
        final DateTime endDate = new DateTime("2012-07-21");
        final BusinessAccountTransitionModelDao accountTransitionModelDao = new BusinessAccountTransitionModelDao(account,
                                                                                                                  accountRecordId,
                                                                                                                  serviceName,
                                                                                                                  stateName,
                                                                                                                  startDate,
                                                                                                                  blockingStateRecordId,
                                                                                                                  endDate,
                                                                                                                  auditLog,
                                                                                                                  tenantRecordId,
                                                                                                                  reportGroup);
        verifyBusinessModelDaoBase(accountTransitionModelDao, accountRecordId, tenantRecordId);
        Assert.assertEquals(accountTransitionModelDao.getCreatedDate(), auditLog.getCreatedDate());
        Assert.assertEquals(accountTransitionModelDao.getBlockingStateRecordId(), blockingStateRecordId);
        Assert.assertEquals(accountTransitionModelDao.getService(), serviceName);
        Assert.assertEquals(accountTransitionModelDao.getState(), stateName);
        Assert.assertEquals(accountTransitionModelDao.getStartDate(), startDate);
        Assert.assertEquals(accountTransitionModelDao.getEndDate(), endDate);
    }
}
