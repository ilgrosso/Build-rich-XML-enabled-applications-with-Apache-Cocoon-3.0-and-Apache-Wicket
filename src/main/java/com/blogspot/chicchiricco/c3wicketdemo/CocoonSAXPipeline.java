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

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.configuration.Settings;
import org.apache.cocoon.pipeline.Pipeline;
import org.apache.cocoon.pipeline.ProcessingException;
import org.apache.cocoon.sax.SAXPipelineComponent;
import org.apache.cocoon.sax.SAXProducer;
import org.apache.cocoon.sax.component.XMLSerializer;
import org.apache.cocoon.servlet.RequestProcessor;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CocoonSAXPipeline extends WebComponent {

    private static final long serialVersionUID = 1L;
    private Pipeline<SAXPipelineComponent> pipeline;
    @SpringBean
    private Settings settings;

    CocoonSAXPipeline(String id, Pipeline<SAXPipelineComponent> pipeline) {
        super(id);
        this.pipeline = pipeline;
    }

    public void addComponent(SAXProducer component) {
        this.pipeline.addComponent(component);
    }

    @Override
    protected void onComponentTagBody(MarkupStream markupStream,
            ComponentTag openTag) {

        this.pipeline.addComponent(createXMLSerializer());

        HttpServletRequest request =
                ((WebRequest) RequestCycle.get().getRequest()).
                getHttpServletRequest();
        HttpServletResponse response =
                ((WebResponse) RequestCycle.get().getResponse()).
                getHttpServletResponse();
        ServletContext servletContext = WebApplication.get().getServletContext();
        Map<String, Object> parameters = RequestProcessor.prepareParameters(
                request, response, this.settings, servletContext);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.pipeline.setup(baos, parameters);

        try {
            this.pipeline.execute();
        } catch (Exception e) {
            throw new ProcessingException(
                    "Error while execution a Cocoon pipeline.", e);
        }

        this.replaceComponentTagBody(markupStream, openTag,
                new String(baos.toByteArray()));
    }

    private static XMLSerializer createXMLSerializer() {
        XMLSerializer xmlSerializer = new XMLSerializer();

        Properties format = new Properties();
        format.put("encoding", "UTF-8");
        format.put("method", "xml");
        format.put("omit-xml-declaration", "yes");
        xmlSerializer.setFormat(format);

        return xmlSerializer;
    }
}