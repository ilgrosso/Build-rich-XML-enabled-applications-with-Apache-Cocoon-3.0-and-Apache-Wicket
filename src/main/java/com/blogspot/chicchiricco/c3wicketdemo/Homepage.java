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

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.cocoon.optional.pipeline.components.sax.neko.NekoGenerator;
import org.apache.cocoon.pipeline.NonCachingPipeline;
import org.apache.cocoon.sax.SAXPipelineComponent;
import org.apache.cocoon.sax.component.XSLTTransformer;
import org.apache.wicket.markup.html.WebPage;

public class Homepage extends WebPage {

    public Homepage()
            throws MalformedURLException {

        final CocoonSAXPipeline cocoonSitePipeline = new CocoonSAXPipeline(
                "cocoon-pipeline-component",
                new NonCachingPipeline<SAXPipelineComponent>());
        cocoonSitePipeline.addComponent(new NekoGenerator(new URL(
                "http://cocoon.apache.org/3.0/")));
        cocoonSitePipeline.addComponent(new XSLTTransformer(this.getClass().
                getResource("cocoon-site.xsl")));
        this.add(cocoonSitePipeline);

        final CocoonSAXPipeline wicketSitePipeline = new CocoonSAXPipeline(
                "wicket-pipeline-component",
                new NonCachingPipeline<SAXPipelineComponent>());
        wicketSitePipeline.addComponent(new NekoGenerator(new URL(
                "http://wicket.apache.org/")));
        wicketSitePipeline.addComponent(new XSLTTransformer(this.getClass().
                getResource("wicket-site.xsl")));
        this.add(wicketSitePipeline);
    }
}
