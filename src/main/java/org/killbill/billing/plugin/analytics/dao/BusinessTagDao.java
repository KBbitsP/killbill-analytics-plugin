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

package org.killbill.billing.plugin.analytics.dao;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillDataSource;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillLogService;
import org.killbill.billing.plugin.analytics.AnalyticsRefreshException;
import org.killbill.billing.plugin.analytics.dao.factory.BusinessContextFactory;
import org.killbill.billing.plugin.analytics.dao.factory.BusinessTagFactory;
import org.killbill.billing.plugin.analytics.dao.model.BusinessModelDaosWithAccountAndTenantRecordId;
import org.killbill.billing.plugin.analytics.dao.model.BusinessTagModelDao;
import org.killbill.billing.util.callcontext.CallContext;
import org.osgi.service.log.LogService;
import org.skife.jdbi.v2.Transaction;
import org.skife.jdbi.v2.TransactionStatus;

public class BusinessTagDao extends BusinessAnalyticsDaoBase {

    private final BusinessTagFactory bTagFactory;

    public BusinessTagDao(final OSGIKillbillLogService logService,
                          final OSGIKillbillDataSource osgiKillbillDataSource) {
        super(logService, osgiKillbillDataSource);
        bTagFactory = new BusinessTagFactory();
    }

    public void update(final BusinessContextFactory businessContextFactory) throws AnalyticsRefreshException {
        logService.log(LogService.LOG_DEBUG, "Starting rebuild of Analytics tags for account " + businessContextFactory.getAccountId());

        final BusinessModelDaosWithAccountAndTenantRecordId<BusinessTagModelDao> tagModelDaos = bTagFactory.createBusinessTags(businessContextFactory);

        executeInTransaction(new Transaction<Void, BusinessAnalyticsSqlDao>() {
            @Override
            public Void inTransaction(final BusinessAnalyticsSqlDao transactional, final TransactionStatus status) throws Exception {
                updateInTransaction(tagModelDaos, transactional, businessContextFactory.getCallContext());
                return null;
            }
        });

        logService.log(LogService.LOG_DEBUG, "Finished rebuild of Analytics tags for account " + businessContextFactory.getAccountId());
    }

    private void updateInTransaction(final BusinessModelDaosWithAccountAndTenantRecordId<BusinessTagModelDao> tagModelDaos,
                                     final BusinessAnalyticsSqlDao transactional,
                                     final CallContext context) {
        for (final String tableName : BusinessTagModelDao.ALL_TAGS_TABLE_NAMES) {
            transactional.deleteByAccountRecordId(tableName,
                                                  tagModelDaos.getAccountRecordId(),
                                                  tagModelDaos.getTenantRecordId(),
                                                  context);
        }

        for (final BusinessTagModelDao tagModelDao : tagModelDaos.getBusinessModelDaos()) {
            transactional.create(tagModelDao.getTableName(), tagModelDao, context);
        }
    }
}
