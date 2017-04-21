/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014 The Billing Project, LLC
 *
 * Ning licenses this file to you under the Apache License, version 2.0
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

import org.jooq.impl.DSL;
import org.killbill.billing.plugin.analytics.AnalyticsTestSuiteNoDB;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCases extends AnalyticsTestSuiteNoDB {

    @Test(groups = "fast")
    public void testCheckRegexp() throws Exception {
        Assert.assertEquals(getSQL("currency"), "select \"currency\"");
        Assert.assertEquals(getSQL("currency(USD)"), "select case when \"currency\" = 'USD' then 'USD'\n" +
                                                     "            else 'Other'\n" +
                                                     "       end \"currency\"");
        Assert.assertEquals(getSQL("currency(USD|BRL,GBP,EUR,MXN,AUD)"), "select case when \"currency\" = 'USD' then 'USD'\n" +
                                                                         "            when \"currency\" = 'BRL' then 'BRL,GBP,EUR,MXN,AUD'\n" +
                                                                         "            when \"currency\" = 'GBP' then 'BRL,GBP,EUR,MXN,AUD'\n" +
                                                                         "            when \"currency\" = 'EUR' then 'BRL,GBP,EUR,MXN,AUD'\n" +
                                                                         "            when \"currency\" = 'MXN' then 'BRL,GBP,EUR,MXN,AUD'\n" +
                                                                         "            when \"currency\" = 'AUD' then 'BRL,GBP,EUR,MXN,AUD'\n" +
                                                                         "            else 'Other'\n" +
                                                                         "       end \"currency\"");
        Assert.assertEquals(getSQL("currency(USD=Group 1|BRL,GBP,EUR,MXN,AUD=Group 2, with Europe)"), "select case when \"currency\" = 'USD' then 'Group 1'\n" +
                                                                                                      "            when \"currency\" = 'BRL' then 'Group 2, with Europe'\n" +
                                                                                                      "            when \"currency\" = 'GBP' then 'Group 2, with Europe'\n" +
                                                                                                      "            when \"currency\" = 'EUR' then 'Group 2, with Europe'\n" +
                                                                                                      "            when \"currency\" = 'MXN' then 'Group 2, with Europe'\n" +
                                                                                                      "            when \"currency\" = 'AUD' then 'Group 2, with Europe'\n" +
                                                                                                      "            else 'Other'\n" +
                                                                                                      "       end \"currency\"");
        Assert.assertEquals(getSQL("currency_with_underscore(USD|EUR)"), "select case when \"currency_with_underscore\" = 'USD' then 'USD'\n" +
                                                                         "            when \"currency_with_underscore\" = 'EUR' then 'EUR'\n" +
                                                                         "            else 'Other'\n" +
                                                                         "       end \"currency_with_underscore\"");
        Assert.assertEquals(getSQL("currency_with_underscore(USD|EUR|-)"), "select case when \"currency_with_underscore\" = 'USD' then 'USD'\n" +
                                                                           "            when \"currency_with_underscore\" = 'EUR' then 'EUR'\n" +
                                                                           "       end \"currency_with_underscore\"");
    }

    private String getSQL(final String input) {
        return DSL.select(Cases.of(input).getField()).toString();
    }
}
