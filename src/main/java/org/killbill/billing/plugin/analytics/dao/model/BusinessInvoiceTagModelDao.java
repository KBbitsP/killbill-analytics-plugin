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

import java.util.UUID;

import javax.annotation.Nullable;

import org.killbill.billing.account.api.Account;
import org.killbill.billing.util.audit.AuditLog;
import org.killbill.billing.util.tag.Tag;
import org.killbill.billing.util.tag.TagDefinition;

public class BusinessInvoiceTagModelDao extends BusinessTagModelDao {

    private UUID invoiceId;

    public BusinessInvoiceTagModelDao() { /* When reading from the database */ }

    public BusinessInvoiceTagModelDao(final Account account,
                                      final Long accountRecordId,
                                      final Tag tag,
                                      final Long tagRecordId,
                                      final TagDefinition tagDefinition,
                                      @Nullable final AuditLog creationAuditLog,
                                      final Long tenantRecordId,
                                      @Nullable final ReportGroup reportGroup) {
        super(account,
              accountRecordId,
              tag,
              tagRecordId,
              tagDefinition,
              creationAuditLog,
              tenantRecordId,
              reportGroup);
        this.invoiceId = tag.getObjectId();
    }

    @Override
    public String getTableName() {
        return INVOICE_TAGS_TABLE_NAME;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BusinessInvoiceTagModelDao");
        sb.append("{invoiceId=").append(invoiceId);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final BusinessInvoiceTagModelDao that = (BusinessInvoiceTagModelDao) o;

        if (invoiceId != null ? !invoiceId.equals(that.invoiceId) : that.invoiceId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        return result;
    }
}
