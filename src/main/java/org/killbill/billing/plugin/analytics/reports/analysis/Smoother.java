/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2016 Groupon, Inc
 * Copyright 2014-2016 The Billing Project, LLC
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

package org.killbill.billing.plugin.analytics.reports.analysis;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.killbill.billing.plugin.analytics.json.XY;

import com.google.common.base.Function;
import com.google.common.base.Strings;

public abstract class Smoother {

    private final Map<String, Map<String, List<XY>>> dataForReports;
    private final DateGranularity dateGranularity;

    public static enum SmootherType {
        AVERAGE_WEEKLY,
        AVERAGE_MONTHLY,
        SUM_WEEKLY,
        SUM_MONTHLY;

        public Smoother createSmoother(final Map<String, Map<String, List<XY>>> dataForReports) {
            switch (this) {
                case AVERAGE_WEEKLY:
                    return new AverageSmoother(dataForReports, DateGranularity.WEEKLY);
                case AVERAGE_MONTHLY:
                    return new AverageSmoother(dataForReports, DateGranularity.MONTHLY);
                case SUM_WEEKLY:
                    return new SummingSmoother(dataForReports, DateGranularity.WEEKLY);
                case SUM_MONTHLY:
                    return new SummingSmoother(dataForReports, DateGranularity.MONTHLY);
                default:
                    return null;
            }
        }
    }

    public static SmootherType fromString(@Nullable final String smootherName) {
        if (Strings.isNullOrEmpty(smootherName)) {
            return null;
        } else {
            return SmootherType.valueOf(smootherName.toUpperCase());
        }
    }

    public Smoother(final Map<String, Map<String, List<XY>>> dataForReports, final DateGranularity dateGranularity) {
        this.dataForReports = dataForReports;
        this.dateGranularity = dateGranularity;
    }

    public abstract float computeSmoothedValue(float accumulator, int accumulatorSize);

    // Assume the data is already sorted
    public void smooth() {
        for (final Map<String, List<XY>> dataForReport : dataForReports.values()) {
            for (final String pivotName : dataForReport.keySet()) {
                final List<XY> dataForPivot = dataForReport.get(pivotName);
                final List<XY> smoothedData = smooth(dataForPivot);
                dataForReport.put(pivotName, smoothedData);
            }
        }
    }

    public Map<String, Map<String, List<XY>>> getDataForReports() {
        return dataForReports;
    }

    private List<XY> smooth(final List<XY> inputData) {
        switch (dateGranularity) {
            case WEEKLY:
                return smooth(inputData,
                              new Function<XY, DateTime>() {
                                  @Override
                                  public DateTime apply(final XY input) {
                                      return input.getxDate().withDayOfWeek(DateTimeConstants.MONDAY);
                                  }
                              }
                             );
            case MONTHLY:
                return smooth(inputData,
                              new Function<XY, DateTime>() {
                                  @Override
                                  public DateTime apply(final XY input) {
                                      return input.getxDate().withDayOfMonth(1);
                                  }
                              }
                             );
            default:
                return inputData;
        }
    }

    private List<XY> smooth(final List<XY> inputData, final Function<XY, DateTime> truncator) {
        final List<XY> smoothedData = new LinkedList<XY>();

        DateTime currentTruncatedDate = truncator.apply(inputData.get(0));
        Float accumulator = (float) 0;
        int accumulatorSize = 0;
        for (final XY xy : inputData) {
            final DateTime zeTruncatedDate = truncator.apply(xy);
            //noinspection ConstantConditions
            if (zeTruncatedDate.compareTo(currentTruncatedDate) != 0) {
                smoothedData.add(new XY(currentTruncatedDate, computeSmoothedValue(accumulator, accumulatorSize)));
                accumulator = (float) 0;
                accumulatorSize = 0;
            }

            accumulator += xy.getY();
            accumulatorSize++;
            currentTruncatedDate = zeTruncatedDate;
        }

        return smoothedData;
    }
}
