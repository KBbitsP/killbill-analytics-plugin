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

package org.killbill.billing.plugin.analytics.api;

import org.joda.time.DateTime;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.plugin.analytics.AnalyticsTestSuiteNoDB;
import org.killbill.billing.plugin.analytics.dao.model.BusinessSubscription;
import org.killbill.billing.plugin.analytics.dao.model.BusinessSubscriptionEvent;
import org.killbill.billing.plugin.analytics.dao.model.BusinessSubscriptionTransitionModelDao;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestBusinessSubscriptionTransition extends AnalyticsTestSuiteNoDB {

    @Test(groups = "fast")
    public void testConstructor() throws Exception {
        final DateTime startDate = new DateTime("2012-06-05");

        final BusinessSubscriptionEvent event = BusinessSubscriptionEvent.valueOf("START_ENTITLEMENT_BASE");
        final BusinessSubscription previousSubscription = null;
        final BusinessSubscription nextSubscription = new BusinessSubscription(null, phase, priceList, Currency.GBP, startDate, serviceName, stateName, currencyConverter);
        final BusinessSubscriptionTransitionModelDao subscriptionTransitionModelDao = new BusinessSubscriptionTransitionModelDao(account,
                                                                                                                                 accountRecordId,
                                                                                                                                 bundle,
                                                                                                                                 subscriptionTransition,
                                                                                                                                 subscriptionEventRecordId,
                                                                                                                                 event,
                                                                                                                                 previousSubscription,
                                                                                                                                 nextSubscription,
                                                                                                                                 currencyConverter,
                                                                                                                                 auditLog,
                                                                                                                                 tenantRecordId,
                                                                                                                                 reportGroup);
        final BusinessSubscriptionTransition businessSubscriptionTransition = new BusinessSubscriptionTransition(subscriptionTransitionModelDao);

        verifyBusinessEntityBase(businessSubscriptionTransition);
        Assert.assertEquals(businessSubscriptionTransition.getCreatedDate().compareTo(subscriptionTransitionModelDao.getCreatedDate()), 0);
        Assert.assertEquals(businessSubscriptionTransition.getBundleId(), subscriptionTransitionModelDao.getBundleId());
        Assert.assertEquals(businessSubscriptionTransition.getBundleExternalKey(), subscriptionTransitionModelDao.getBundleExternalKey());
        Assert.assertEquals(businessSubscriptionTransition.getSubscriptionId(), subscriptionTransitionModelDao.getSubscriptionId());
        Assert.assertEquals(businessSubscriptionTransition.getRequestedTimestamp().compareTo(subscriptionTransitionModelDao.getRequestedTimestamp()), 0);
        Assert.assertEquals(businessSubscriptionTransition.getEvent(), subscriptionTransitionModelDao.getEvent());

        Assert.assertNull(businessSubscriptionTransition.getPrevProductName());
        Assert.assertNull(businessSubscriptionTransition.getPrevProductType());
        Assert.assertNull(businessSubscriptionTransition.getPrevProductCategory());
        Assert.assertNull(businessSubscriptionTransition.getPrevSlug());
        Assert.assertNull(businessSubscriptionTransition.getPrevPhase());
        Assert.assertNull(businessSubscriptionTransition.getPrevBillingPeriod());
        Assert.assertNull(businessSubscriptionTransition.getPrevPrice());
        Assert.assertNull(businessSubscriptionTransition.getConvertedPrevPrice());
        Assert.assertNull(businessSubscriptionTransition.getPrevPriceList());
        Assert.assertNull(businessSubscriptionTransition.getPrevMrr());
        Assert.assertNull(businessSubscriptionTransition.getConvertedPrevMrr());
        Assert.assertNull(businessSubscriptionTransition.getPrevCurrency());
        Assert.assertNull(businessSubscriptionTransition.getPrevBusinessActive());
        Assert.assertNull(businessSubscriptionTransition.getPrevStartDate());
        Assert.assertNull(businessSubscriptionTransition.getPrevService());
        Assert.assertNull(businessSubscriptionTransition.getPrevState());

        Assert.assertEquals(businessSubscriptionTransition.getNextProductName(), subscriptionTransitionModelDao.getNextProductName());
        Assert.assertEquals(businessSubscriptionTransition.getNextProductType(), subscriptionTransitionModelDao.getNextProductType());
        Assert.assertEquals(businessSubscriptionTransition.getNextProductCategory(), subscriptionTransitionModelDao.getNextProductCategory());
        Assert.assertEquals(businessSubscriptionTransition.getNextSlug(), subscriptionTransitionModelDao.getNextSlug());
        Assert.assertEquals(businessSubscriptionTransition.getNextPhase(), subscriptionTransitionModelDao.getNextPhase());
        Assert.assertEquals(businessSubscriptionTransition.getNextBillingPeriod(), subscriptionTransitionModelDao.getNextBillingPeriod());
        Assert.assertEquals(businessSubscriptionTransition.getNextPrice().compareTo(subscriptionTransitionModelDao.getNextPrice()), 0);
        Assert.assertEquals(businessSubscriptionTransition.getConvertedNextPrice().compareTo(subscriptionTransitionModelDao.getConvertedNextPrice()), 0);
        Assert.assertEquals(businessSubscriptionTransition.getNextPriceList(), subscriptionTransitionModelDao.getNextPriceList());
        Assert.assertEquals(businessSubscriptionTransition.getNextMrr().compareTo(subscriptionTransitionModelDao.getNextMrr()), 0);
        Assert.assertEquals(businessSubscriptionTransition.getConvertedNextMrr().compareTo(subscriptionTransitionModelDao.getConvertedNextMrr()), 0);
        Assert.assertEquals(businessSubscriptionTransition.getNextCurrency(), subscriptionTransitionModelDao.getNextCurrency());
        Assert.assertEquals(businessSubscriptionTransition.getNextBusinessActive(), subscriptionTransitionModelDao.getNextBusinessActive());
        Assert.assertEquals(businessSubscriptionTransition.getNextStartDate().compareTo(subscriptionTransitionModelDao.getNextStartDate()), 0);
        Assert.assertNull(businessSubscriptionTransition.getNextEndDate());
        Assert.assertEquals(businessSubscriptionTransition.getNextService(), subscriptionTransitionModelDao.getNextService());
        Assert.assertEquals(businessSubscriptionTransition.getNextState(), subscriptionTransitionModelDao.getNextState());

        Assert.assertEquals(businessSubscriptionTransition.getConvertedCurrency(), subscriptionTransitionModelDao.getConvertedCurrency());
    }
}
