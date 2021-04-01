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

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.CatalogApiException;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.catalog.api.Duration;
import org.killbill.billing.catalog.api.Plan;
import org.killbill.billing.catalog.api.PlanPhase;
import org.killbill.billing.catalog.api.PriceList;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.catalog.api.TimeUnit;
import org.killbill.billing.plugin.analytics.utils.CurrencyConverter;
import org.killbill.billing.plugin.analytics.utils.Rounder;

/**
 * Describe a subscription for Analytics purposes
 */
public class BusinessSubscription {

    private static final Currency USD = Currency.valueOf("USD");

    private final String productName;
    private final String productType;
    private final String productCategory;
    private final String slug;
    private final String phase;
    private final String billingPeriod;
    private final BigDecimal price;
    private final BigDecimal convertedPrice;
    private final String priceList;
    private final BigDecimal mrr;
    private final BigDecimal convertedMrr;
    private final String currency;
    private final String service;
    private final String state;
    private final Boolean businessActive;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public BusinessSubscription(@Nullable final Plan currentPlan,
                                @Nullable final PlanPhase currentPhase,
                                @Nullable final PriceList priceList,
                                final Currency currency,
                                final LocalDate startDate,
                                final String service,
                                final String state,
                                final CurrencyConverter currencyConverter) {
        // TODO
        businessActive = true;

        this.priceList = priceList == null ? null : priceList.getName();

        // Record plan information
        if (currentPlan != null && currentPlan.getProduct() != null) {
            final Product product = currentPlan.getProduct();
            productName = product.getName();
            productCategory = product.getCategory().toString();
            productType = product.getCatalogName();
        } else {
            productName = null;
            productCategory = null;
            productType = null;
        }

        // Record phase information
        if (currentPhase != null) {
            slug = currentPhase.getName();

            if (currentPhase.getPhaseType() != null) {
                phase = currentPhase.getPhaseType().toString();
            } else {
                phase = null;
            }

            if (currentPhase.getRecurring() != null && currentPhase.getRecurring().getBillingPeriod() != null) {
                billingPeriod = currentPhase.getRecurring().getBillingPeriod().toString();
            } else {
                billingPeriod = null;
            }

            if (currentPhase.getRecurring() != null && currentPhase.getRecurring().getRecurringPrice() != null && currency != null) {
                BigDecimal tmpPrice;
                try {
                    tmpPrice = currentPhase.getRecurring().getRecurringPrice().getPrice(currency);
                } catch (CatalogApiException e) {
                    tmpPrice = null;
                }

                price = tmpPrice;
                if (tmpPrice != null) {
                    mrr = getMrrFromBillingPeriod(currentPhase.getRecurring().getBillingPeriod(), price);
                } else {
                    mrr = null;
                }
            } else {
                price = null;
                mrr = null;
            }
        } else {
            slug = null;
            phase = null;
            billingPeriod = null;
            price = null;
            mrr = null;
        }

        if (currency != null) {
            this.currency = currency.toString();
        } else {
            this.currency = null;
        }

        this.startDate = startDate;
        if (currentPhase != null) {
            final Duration duration = currentPhase.getDuration();
            this.endDate = duration == null || duration.getUnit() == TimeUnit.UNLIMITED ? null : startDate.plus(duration.toJodaPeriod());
        } else {
            this.endDate = null;
        }

        this.service = service;
        this.state = state;

        convertedPrice = currencyConverter.getConvertedValue(this.price, this.currency, startDate);
        convertedMrr = currencyConverter.getConvertedValue(this.mrr, this.currency, startDate);
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getSlug() {
        return slug;
    }

    public String getPhase() {
        return phase;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getConvertedPrice() {
        return convertedPrice;
    }

    public String getPriceList() {
        return priceList;
    }

    public BigDecimal getMrr() {
        return mrr;
    }

    public BigDecimal getConvertedMrr() {
        return convertedMrr;
    }

    public String getCurrency() {
        return currency;
    }

    public String getService() {
        return service;
    }

    public String getState() {
        return state;
    }

    public Boolean getBusinessActive() {
        return businessActive;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    static BigDecimal getMrrFromBillingPeriod(final BillingPeriod period, final BigDecimal price) {

        final int nbMonths;
        switch (period) {
            case MONTHLY:
                nbMonths = 1;
                break;
            case QUARTERLY:
                nbMonths = 3;
                break;
            case BIANNUAL:
                nbMonths = 6;
                break;
            case ANNUAL:
                nbMonths = 12;
                break;
            case BIENNIAL:
                nbMonths = 24;
                break;
            case DAILY:
            case WEEKLY:
            case BIWEEKLY:
            case THIRTY_DAYS:
            default:
                nbMonths = 0;
                break;
        }

        return nbMonths != 0 ?  price.divide(BigDecimal.valueOf(nbMonths), Rounder.SCALE, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BusinessSubscription{");
        sb.append("productName='").append(productName).append('\'');
        sb.append(", productType='").append(productType).append('\'');
        sb.append(", productCategory='").append(productCategory).append('\'');
        sb.append(", slug='").append(slug).append('\'');
        sb.append(", phase='").append(phase).append('\'');
        sb.append(", billingPeriod='").append(billingPeriod).append('\'');
        sb.append(", price=").append(price);
        sb.append(", convertedPrice=").append(convertedPrice);
        sb.append(", priceList='").append(priceList).append('\'');
        sb.append(", mrr=").append(mrr);
        sb.append(", convertedMrr=").append(convertedMrr);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", service='").append(service).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", businessActive=").append(businessActive);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
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

        final BusinessSubscription that = (BusinessSubscription) o;

        if (billingPeriod != null ? !billingPeriod.equals(that.billingPeriod) : that.billingPeriod != null) {
            return false;
        }
        if (businessActive != null ? !businessActive.equals(that.businessActive) : that.businessActive != null) {
            return false;
        }
        if (convertedMrr != null ? !(convertedMrr.compareTo(that.convertedMrr) == 0) : that.convertedMrr != null) {
            return false;
        }
        if (convertedPrice != null ? !(convertedPrice.compareTo(that.convertedPrice) == 0) : that.convertedPrice != null) {
            return false;
        }
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) {
            return false;
        }
        if (endDate != null ? (endDate.compareTo(that.endDate) != 0) : that.endDate != null) {
            return false;
        }
        if (mrr != null ? !(mrr.compareTo(that.mrr) == 0) : that.mrr != null) {
            return false;
        }
        if (phase != null ? !phase.equals(that.phase) : that.phase != null) {
            return false;
        }
        if (price != null ? !(price.compareTo(that.price) == 0) : that.price != null) {
            return false;
        }
        if (priceList != null ? !priceList.equals(that.priceList) : that.priceList != null) {
            return false;
        }
        if (productCategory != null ? !productCategory.equals(that.productCategory) : that.productCategory != null) {
            return false;
        }
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) {
            return false;
        }
        if (productType != null ? !productType.equals(that.productType) : that.productType != null) {
            return false;
        }
        if (service != null ? !service.equals(that.service) : that.service != null) {
            return false;
        }
        if (slug != null ? !slug.equals(that.slug) : that.slug != null) {
            return false;
        }
        if (startDate != null ? (startDate.compareTo(that.startDate) != 0) : that.startDate != null) {
            return false;
        }
        if (state != null ? !state.equals(that.state) : that.state != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = productName != null ? productName.hashCode() : 0;
        result = 31 * result + (productType != null ? productType.hashCode() : 0);
        result = 31 * result + (productCategory != null ? productCategory.hashCode() : 0);
        result = 31 * result + (slug != null ? slug.hashCode() : 0);
        result = 31 * result + (phase != null ? phase.hashCode() : 0);
        result = 31 * result + (billingPeriod != null ? billingPeriod.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (convertedPrice != null ? convertedPrice.hashCode() : 0);
        result = 31 * result + (priceList != null ? priceList.hashCode() : 0);
        result = 31 * result + (mrr != null ? mrr.hashCode() : 0);
        result = 31 * result + (convertedMrr != null ? convertedMrr.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (service != null ? service.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (businessActive != null ? businessActive.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
