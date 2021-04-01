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

import org.joda.time.DateTime;
import org.killbill.billing.ObjectType;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.entitlement.api.SubscriptionBundle;
import org.killbill.billing.util.audit.AuditLog;
import org.killbill.billing.util.customfield.CustomField;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class BusinessFieldModelDao extends BusinessModelDaoBase {

    protected static final String ACCOUNT_FIELDS_TABLE_NAME = "analytics_account_fields";
    protected static final String BUNDLE_FIELDS_TABLE_NAME = "analytics_bundle_fields";
    protected static final String INVOICE_FIELDS_TABLE_NAME = "analytics_invoice_fields";
    protected static final String INVOICE_PAYMENT_FIELDS_TABLE_NAME = "analytics_invoice_payment_fields";
    protected static final String PAYMENT_FIELDS_TABLE_NAME = "analytics_payment_fields";
    protected static final String PAYMENT_METHOD_FIELDS_TABLE_NAME = "analytics_payment_method_fields";
    protected static final String TRANSACTION_FIELDS_TABLE_NAME = "analytics_transaction_fields";

    @SuppressFBWarnings("MS_MUTABLE_ARRAY")
    public static final String[] ALL_FIELDS_TABLE_NAMES = new String[]{ACCOUNT_FIELDS_TABLE_NAME, BUNDLE_FIELDS_TABLE_NAME, INVOICE_FIELDS_TABLE_NAME, INVOICE_PAYMENT_FIELDS_TABLE_NAME, PAYMENT_FIELDS_TABLE_NAME, PAYMENT_METHOD_FIELDS_TABLE_NAME, TRANSACTION_FIELDS_TABLE_NAME};

    private Long customFieldRecordId;
    private String name;
    private String value;

    public static BusinessFieldModelDao create(final Account account,
                                               final Long accountRecordId,
                                               @Nullable final SubscriptionBundle bundle,
                                               final CustomField customField,
                                               final Long customFieldRecordId,
                                               @Nullable final AuditLog creationAuditLog,
                                               final Long tenantRecordId,
                                               @Nullable final ReportGroup reportGroup) {
        if (ObjectType.ACCOUNT.equals(customField.getObjectType())) {
            return new BusinessAccountFieldModelDao(account,
                                                    accountRecordId,
                                                    customField,
                                                    customFieldRecordId,
                                                    creationAuditLog,
                                                    tenantRecordId,
                                                    reportGroup);
        } else if (ObjectType.BUNDLE.equals(customField.getObjectType())) {
            return new BusinessBundleFieldModelDao(account,
                                                   accountRecordId,
                                                   bundle,
                                                   customField,
                                                   customFieldRecordId,
                                                   creationAuditLog,
                                                   tenantRecordId,
                                                   reportGroup);
        } else if (ObjectType.INVOICE.equals(customField.getObjectType())) {
            return new BusinessInvoiceFieldModelDao(account,
                                                    accountRecordId,
                                                    customField,
                                                    customFieldRecordId,
                                                    creationAuditLog,
                                                    tenantRecordId,
                                                    reportGroup);
        } else if (ObjectType.INVOICE_PAYMENT.equals(customField.getObjectType())) {
            return new BusinessInvoicePaymentFieldModelDao(account,
                                                           accountRecordId,
                                                           customField,
                                                           customFieldRecordId,
                                                           creationAuditLog,
                                                           tenantRecordId,
                                                           reportGroup);
        } else if (ObjectType.PAYMENT.equals(customField.getObjectType())) {
            return new BusinessPaymentFieldModelDao(account,
                                                    accountRecordId,
                                                    customField,
                                                    customFieldRecordId,
                                                    creationAuditLog,
                                                    tenantRecordId,
                                                    reportGroup);
        } else if (ObjectType.PAYMENT_METHOD.equals(customField.getObjectType())) {
            return new BusinessPaymentMethodFieldModelDao(account,
                                                          accountRecordId,
                                                          customField,
                                                          customFieldRecordId,
                                                          creationAuditLog,
                                                          tenantRecordId,
                                                          reportGroup);
        } else if (ObjectType.TRANSACTION.equals(customField.getObjectType())) {
            return new BusinessTransactionFieldModelDao(account,
                                                        accountRecordId,
                                                        customField,
                                                        customFieldRecordId,
                                                        creationAuditLog,
                                                        tenantRecordId,
                                                        reportGroup);
        } else {
            // We don't care
            return null;
        }
    }

    public BusinessFieldModelDao() { /* When reading from the database */ }

    public BusinessFieldModelDao(final Long customFieldRecordId,
                                 final String name,
                                 final String value,
                                 final DateTime createdDate,
                                 final String createdBy,
                                 final String createdReasonCode,
                                 final String createdComments,
                                 final UUID accountId,
                                 final String accountName,
                                 final String accountExternalKey,
                                 final Long accountRecordId,
                                 final Long tenantRecordId,
                                 @Nullable final ReportGroup reportGroup) {
        super(createdDate,
              createdBy,
              createdReasonCode,
              createdComments,
              accountId,
              accountName,
              accountExternalKey,
              accountRecordId,
              tenantRecordId,
              reportGroup);
        this.customFieldRecordId = customFieldRecordId;
        this.name = name;
        this.value = value;
    }

    public BusinessFieldModelDao(final Account account,
                                 final Long accountRecordId,
                                 final CustomField customField,
                                 final Long customFieldRecordId,
                                 @Nullable final AuditLog creationAuditLog,
                                 final Long tenantRecordId,
                                 @Nullable final ReportGroup reportGroup) {
        this(customFieldRecordId,
             customField.getFieldName(),
             customField.getFieldValue(),
             customField.getCreatedDate(),
             creationAuditLog != null ? creationAuditLog.getUserName() : null,
             creationAuditLog != null ? creationAuditLog.getReasonCode() : null,
             creationAuditLog != null ? creationAuditLog.getComment() : null,
             account.getId(),
             account.getName(),
             account.getExternalKey(),
             accountRecordId,
             tenantRecordId,
             reportGroup);
    }

    public Long getCustomFieldRecordId() {
        return customFieldRecordId;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BusinessFieldModelDao");
        sb.append("{customFieldRecordId=").append(customFieldRecordId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
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

        final BusinessFieldModelDao that = (BusinessFieldModelDao) o;

        if (customFieldRecordId != null ? !customFieldRecordId.equals(that.customFieldRecordId) : that.customFieldRecordId != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (customFieldRecordId != null ? customFieldRecordId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
