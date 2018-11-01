/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2018 Groupon, Inc
 * Copyright 2014-2018 The Billing Project, LLC
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

package org.killbill.billing.plugin.analytics.http;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.killbill.billing.plugin.analytics.api.user.AnalyticsUserApi;
import org.killbill.billing.plugin.analytics.reports.ReportsUserApi;

public class ServletRouter extends BaseServlet {

    private static final String REPORTS_RESOURCES = "reports";

    private final Pattern REPORTS_PATTERN = Pattern.compile("/" + REPORTS_RESOURCES + "(/(" + STRING_PATTERN + "))?" + "(/refresh)?");
    private final Pattern ANALYTICS_PATTERN = Pattern.compile("/(" + UUID_PATTERN + ")");

    private final ReportsServlet reportsServlet;
    private final AnalyticsServlet analyticsServlet;

    public ServletRouter(final AnalyticsUserApi analyticsUserApi, final ReportsUserApi reportsUserApi) {
        super(analyticsUserApi, reportsUserApi);
        this.reportsServlet = new ReportsServlet(analyticsUserApi, reportsUserApi);
        this.analyticsServlet = new AnalyticsServlet(analyticsUserApi, reportsUserApi);
    }

    @Override
    protected void doOptions(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        forward(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        forward(req, resp);
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        forward(req, resp);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        forward(req, resp);
    }

    // Lame - we should rather use the built-in forward mechanism but I'm not sure how to create
    // the dispatchers without a web.xml: getServletContext().getNamedDispatcher("...").forward(req, resp);
    private void forward(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final String pathInfo = req.getPathInfo();
        Matcher matcher;

        matcher = REPORTS_PATTERN.matcher(pathInfo);
        if (matcher.matches()) {
            // matcher.group(1) is /reportName
            req.setAttribute(REPORT_NAME_ATTRIBUTE, matcher.group(2));
            req.setAttribute(SHOULD_REFRESH, matcher.groupCount() > 2 && "/refresh".equals(matcher.group(3)));
            reportsServlet.service(req, resp);
            return;
        }

        matcher = ANALYTICS_PATTERN.matcher(pathInfo);
        if (matcher.matches()) {
            final String kbAccountIdString = matcher.group(1);

            final UUID kbAccountId;
            try {
                kbAccountId = UUID.fromString(kbAccountIdString);
            } catch (final IllegalArgumentException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID for kb account id: " + kbAccountIdString);
                return;
            }

            req.setAttribute(KB_ACCOUNT_ID_ATTRIBUTE, kbAccountId);
            analyticsServlet.service(req, resp);
            return;
        }

        resp.sendError(404, "Resource " + pathInfo + " not found");
    }
}
