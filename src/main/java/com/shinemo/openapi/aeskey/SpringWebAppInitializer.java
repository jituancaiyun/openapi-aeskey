/*
 * (C) Copyright 2015-2017 the original author or authors.
 *
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
 *
 * Contributors:
 *     ohun@live.cn (夜色)
 */

package com.shinemo.openapi.aeskey;

import com.shinemo.openapi.client.web.OpenApiAesKeyServlet;
import com.shinemo.openapi.client.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by ohun on 2017/4/19.
 *
 * @author ohun@live.cn (夜色)
 */
public final class SpringWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        if (context == null) return;

        OpenApiAesKeyServlet openApiAesKeyServlet = context.getBean(OpenApiAesKeyServlet.class);

        if (openApiAesKeyServlet == null) return;


        ServletRegistration.Dynamic registration = servletContext.addServlet("OpenApiAesKeyServlet", openApiAesKeyServlet);
        registration.addMapping("/queryKey");

        System.err.println("===================================================================");
        System.err.println("===============OPEN-API-AES-KEY SERVER START SUCCESS===============");
        System.err.println("===================================================================");
    }

}
