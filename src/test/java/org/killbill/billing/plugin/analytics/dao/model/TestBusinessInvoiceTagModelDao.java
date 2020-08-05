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

package org.killbill.billing.plugin.analytics.dao.model;

import org.killbill.billing.plugin.analytics.AnalyticsTestSuiteNoDB;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestBusinessInvoiceTagModelDao extends AnalyticsTestSuiteNoDB {

    @Test(groups = "fast")
    public void testConstructor() throws Exception {
        final BusinessInvoiceTagModelDao invoiceTagModelDao = new BusinessInvoiceTagModelDao(account,
                                                                                             accountRecordId,
                                                                                             tag,
                                                                                             tagRecordId,
                                                                                             tagDefinition,
                                                                                             auditLog,
                                                                                             tenantRecordId,
                                                                                             reportGroup);
        verifyBusinessModelDaoBase(invoiceTagModelDao, accountRecordId, tenantRecordId);
        Assert.assertEquals(invoiceTagModelDao.getCreatedDate(), tag.getCreatedDate());
        Assert.assertEquals(invoiceTagModelDao.getTagRecordId(), tagRecordId);
        Assert.assertEquals(invoiceTagModelDao.getInvoiceId(), tag.getObjectId());
        Assert.assertEquals(invoiceTagModelDao.getName(), tagDefinition.getName());
    }
}
