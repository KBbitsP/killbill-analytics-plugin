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

package org.killbill.billing.plugin.analytics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import org.killbill.billing.osgi.libs.killbill.OSGIConfigPropertiesService;
import org.killbill.commons.concurrent.Executors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;

public class BusinessExecutor {

    private static final String ANALYTICS_REFRESH_NB_THREADS_PROPERTY = "org.killbill.billing.plugin.analytics.refresh.nbThreads";

    public static ExecutorService newCachedThreadPool(final OSGIConfigPropertiesService osgiConfigPropertiesService) {
        final int nbThreads = getNbThreads(osgiConfigPropertiesService);
        return newCachedThreadPool(nbThreads, "osgi-analytics-refresh");
    }

    public static ExecutorService newCachedThreadPool(final int nbThreads, final String name) {
        // Note: we don't use the default rejection handler here (AbortPolicy) as we always want the tasks to be executed
        return Executors.newCachedThreadPool(0,
                                             nbThreads,
                                             name,
                                             60L,
                                             TimeUnit.SECONDS,
                                             new CallerRunsPolicy());

    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(final String name) {
        return Executors.newSingleThreadScheduledExecutor(name,
                                                          new CallerRunsPolicy());
    }

    @VisibleForTesting
    static int getNbThreads(OSGIConfigPropertiesService osgiConfigPropertiesService) {
        final String nbThreadsMaybeNull = Strings.emptyToNull(osgiConfigPropertiesService.getString(ANALYTICS_REFRESH_NB_THREADS_PROPERTY));
        return nbThreadsMaybeNull == null ? 100 : Integer.valueOf(nbThreadsMaybeNull);
    }
}
