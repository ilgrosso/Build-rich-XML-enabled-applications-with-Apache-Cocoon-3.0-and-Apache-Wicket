/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blogspot.chicchiricco.c3wicketdemo;

import org.apache.cocoon.wicket.target.CocoonSitemap;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.examples.ajax.builtin.Index;
import org.apache.wicket.markup.html.AjaxServerAndClientTimeFilter;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class DemoWebapp extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return Homepage.class;
    }

    @Override
    protected void init() {
        super.init();

        // setup Spring
        this.addComponentInstantiationListener(
                new SpringComponentInjector(this));

        // mounts
        this.mount(new CocoonSitemap("/sitemap", "/sitemap.xmap"));
        this.mount(
                new HybridUrlCodingStrategy("/ajax", Index.class));
        this.mount(
                new HybridUrlCodingStrategy("/404.html", Error404Page.class));

        // settings
        this.getMarkupSettings().setStripWicketTags(true);

        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getRequestCycleSettings().addResponseFilter(new AjaxServerAndClientTimeFilter());
        getDebugSettings().setAjaxDebugModeEnabled(true);
    }

    public static DemoWebapp get() {
        return (DemoWebapp) Application.get();
    }
}
