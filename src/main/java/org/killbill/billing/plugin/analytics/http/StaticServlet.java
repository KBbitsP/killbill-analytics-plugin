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

package org.killbill.billing.plugin.analytics.http;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.killbill.billing.plugin.analytics.api.user.AnalyticsUserApi;
import org.killbill.billing.plugin.analytics.reports.ReportsUserApi;

import com.google.common.io.Resources;

// Handle /plugins/killbill-analytics/static/<resourceName>
public class StaticServlet extends BaseServlet {

    public StaticServlet(final AnalyticsUserApi analyticsUserApi, final ReportsUserApi reportsUserApi) {
        super(analyticsUserApi, reportsUserApi);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String resourceName = (String) req.getAttribute(RESOURCE_NAME_ATTRIBUTE);
        if (resourceName == null) {
            return;
        }

        doHandleStaticResource(resourceName, resp);
    }

    private void doHandleStaticResource(final String resourceName, final HttpServletResponse resp) throws IOException {
        final URL resourceUrl = Resources.getResource("static/" + resourceName);

        final String[] parts = resourceName.split("/");
        if (parts.length >= 2) {
            if (parts[0].equals("javascript")) {
                resp.setContentType("application/javascript");
            } else if (parts[0].equals("styles")) {
                resp.setContentType("text/css");
            }
            Resources.copy(resourceUrl, resp.getOutputStream());
        } else {
            Resources.copy(resourceUrl, resp.getOutputStream());
            resp.setContentType("text/html");
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
