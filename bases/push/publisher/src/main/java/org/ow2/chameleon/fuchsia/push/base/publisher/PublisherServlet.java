package org.ow2.chameleon.fuchsia.push.base.publisher;

/*
 * #%L
 * OW2 Chameleon - Fuchsia Base PUbSubHubbub Publisher
 * %%
 * Copyright (C) 2009 - 2014 OW2 Chameleon
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import org.ow2.chameleon.fuchsia.core.constant.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublisherServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(PublisherServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Thread.currentThread().setContextClassLoader(SyndFeed.class.getClassLoader());

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("atom_1.0");
        feed.setTitle("Sample Feed (created with ROME)");
        feed.setLink("http://www.example.com");

        List<SyndLink> links = new ArrayList<SyndLink>();

        SyndLinkImpl hubLink = new SyndLinkImpl();
        hubLink.setRel("hub");
        hubLink.setHref("http://localhost:8080/hub/main");

        links.add(hubLink);

        feed.setLinks(links);
        feed.setDescription("This feed has been created using ROME");
        feed.setFeedType("atom_0.3");//rss_2.0

        List entries = new ArrayList();
        SyndEntry entry;
        SyndContent description;

        entry = new SyndEntryImpl();
        entry.setTitle("ROME v1.0");
        entry.setLink("http://wiki.java.net/bin/view/Javawsxml/Rome01");
        entry.setPublishedDate(new Date());
        description = new SyndContentImpl();
        description.setType(MediaType.TEXT_PLAIN);
        description.setValue("Initial release of ROME");
        entry.setDescription(description);
        entries.add(entry);

        feed.setEntries(entries);

        SyndFeedOutput output = new SyndFeedOutput();

        try {
            output.output(feed, resp.getWriter());
            resp.getWriter().close();
        } catch (FeedException e) {
            LOG.error("Failed to create feed", e);
        }

        resp.getWriter().write(feed.createWireFeed().toString());

    }

    /*
            @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            String url=req.getParameter("url");

            ContentNotification cn=new ContentNotification("publish",url);

            CloseableHttpClient httpclient = HttpClients.createDefault();

            String hub = "http://localhost:8080/hub";

            HttpPost httpPost = new HttpPost(hub);

            httpPost.setEntity(new UrlEncodedFormEntity(cn.toRequesParameters()));

            System.out.println("publisher --> Sending new post to the HUB:"+ hub);

            CloseableHttpResponse response1 = httpclient.execute(httpPost);

            System.out.println("publisher --> got response:"+response1.getStatusLine().getStatusCode());

        }
     */
}
