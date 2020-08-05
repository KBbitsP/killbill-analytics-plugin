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

package org.killbill.billing.plugin.analytics.api;

import org.killbill.billing.ObjectType;
import org.killbill.billing.plugin.analytics.AnalyticsTestSuiteNoDB;
import org.killbill.billing.plugin.analytics.dao.model.BusinessAccountTagModelDao;
import org.killbill.billing.plugin.analytics.dao.model.BusinessBundleTagModelDao;
import org.killbill.billing.plugin.analytics.dao.model.BusinessInvoicePaymentTagModelDao;
import org.killbill.billing.plugin.analytics.dao.model.BusinessInvoiceTagModelDao;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestBusinessTag extends AnalyticsTestSuiteNoDB {

    @Test(groups = "fast")
    public void testConstructorAccount() throws Exception {
        final BusinessAccountTagModelDao businessAccountTagModelDao = new BusinessAccountTagModelDao(account,
                                                                                                     accountRecordId,
                                                                                                     tag,
                                                                                                     tagRecordId,
                                                                                                     tagDefinition,
                                                                                                     auditLog,
                                                                                                     tenantRecordId,
                                                                                                     reportGroup);
        final BusinessTag businessTag = BusinessTag.create(businessAccountTagModelDao);
        verifyBusinessTag(businessTag);
        Assert.assertEquals(businessTag.getObjectType(), ObjectType.ACCOUNT);
    }

    @Test(groups = "fast")
    public void testConstructorBundle() throws Exception {
        final BusinessBundleTagModelDao businessBundleTagModelDao = new BusinessBundleTagModelDao(account,
                                                                                                  accountRecordId,
                                                                                                  bundle,
                                                                                                  tag,
                                                                                                  tagRecordId,
                                                                                                  tagDefinition,
                                                                                                  auditLog,
                                                                                                  tenantRecordId,
                                                                                                  reportGroup);
        final BusinessTag businessTag = BusinessTag.create(businessBundleTagModelDao);
        verifyBusinessTag(businessTag);
        Assert.assertEquals(businessTag.getObjectType(), ObjectType.BUNDLE);
    }

    @Test(groups = "fast")
    public void testConstructorInvoice() throws Exception {
        final BusinessInvoiceTagModelDao businessInvoiceTagModelDao = new BusinessInvoiceTagModelDao(account,
                                                                                                     accountRecordId,
                                                                                                     tag,
                                                                                                     tagRecordId,
                                                                                                     tagDefinition,
                                                                                                     auditLog,
                                                                                                     tenantRecordId,
                                                                                                     reportGroup);
        final BusinessTag businessTag = BusinessTag.create(businessInvoiceTagModelDao);
        verifyBusinessTag(businessTag);
        Assert.assertEquals(businessTag.getObjectType(), ObjectType.INVOICE);
    }

    @Test(groups = "fast")
    public void testConstructorPayment() throws Exception {
        final BusinessInvoicePaymentTagModelDao invoicePaymentTagModelDao = new BusinessInvoicePaymentTagModelDao(account,
                                                                                                                  accountRecordId,
                                                                                                                  tag,
                                                                                                                  tagRecordId,
                                                                                                                  tagDefinition,
                                                                                                                  auditLog,
                                                                                                                  tenantRecordId,
                                                                                                                  reportGroup);
        final BusinessTag businessTag = BusinessTag.create(invoicePaymentTagModelDao);
        verifyBusinessTag(businessTag);
        Assert.assertEquals(businessTag.getObjectType(), ObjectType.INVOICE_PAYMENT);
    }

    private void verifyBusinessTag(final BusinessTag accountTag) {
        verifyBusinessEntityBase(accountTag);
        Assert.assertEquals(accountTag.getCreatedDate(), accountTag.getCreatedDate());
        Assert.assertEquals(accountTag.getName(), accountTag.getName());
        Assert.assertEquals(accountTag.getObjectId(), accountTag.getObjectId());
    }
}
