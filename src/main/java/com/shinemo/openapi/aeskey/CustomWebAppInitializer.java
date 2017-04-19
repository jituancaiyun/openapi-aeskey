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

import com.shinemo.openapi.client.OpenApiClient;
import com.shinemo.openapi.client.OpenApiConfiguration;
import com.shinemo.openapi.client.aes.DefaultAesKeyService;
import com.shinemo.openapi.client.aes.db.MysqlAesKeyDao;
import com.shinemo.openapi.client.service.AuthApiService;
import com.shinemo.openapi.client.web.OpenApiAesKeyServlet;
import com.shinemo.openapi.client.web.WebApplicationInitializer;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;

/**
 * Created by ohun on 2017/4/19.
 *
 * @author ohun@live.cn (夜色)
 */
public final class CustomWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        OpenApiAesKeyServlet openApiAesKeyServlet = createOpenApiAesKeyServlet();

        ServletRegistration.Dynamic registration = servletContext.addServlet("OpenApiAesKeyServlet", openApiAesKeyServlet);
        registration.addMapping("/queryKey");

        System.err.println("===================================================================");
        System.err.println("===============OPEN-API-AES-KEY SERVER START SUCCESS===============");
        System.err.println("===================================================================");
    }

    public static OpenApiAesKeyServlet createOpenApiAesKeyServlet() {
        OpenApiClient openApiClient = createConfig().create();
        AuthApiService authApiService = openApiClient.createApiService(AuthApiService.class);

        MysqlAesKeyDao aesKeyDao = new MysqlAesKeyDao();
        aesKeyDao.setDataSource(createDataSource());
        aesKeyDao.init();

        DefaultAesKeyService defaultAesKeyService = new DefaultAesKeyService();
        defaultAesKeyService.setAesKeyDao(aesKeyDao);
        defaultAesKeyService.setAuthApiService(authApiService);
        defaultAesKeyService.init();

        OpenApiAesKeyServlet openApiAesKeyServlet = new OpenApiAesKeyServlet();
        openApiAesKeyServlet.setOpenApiClient(openApiClient);
        openApiAesKeyServlet.setAesKeyService(defaultAesKeyService);
        return openApiAesKeyServlet;
    }

    public static OpenApiConfiguration createConfig() {
        OpenApiConfiguration configuration = new OpenApiConfiguration();
        configuration.setBaseUrl(Configs.getString("openapi.http.baseUrl"));
        configuration.setAppId(Configs.getInt("openapi.appId"));
        configuration.setAppSecret(Configs.getString("openapi.appSecret"));
        configuration.setConnectTimeoutMillis(Configs.getInt("openapi.http.connectTimeoutMillis"));
        configuration.setMaxRetry(1);
        return configuration;
    }

    public static DataSource createDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(Configs.getString("openapi.jdbc.driverClassName"));
        dataSource.setUrl(Configs.getString("openapi.jdbc.url"));
        dataSource.setUsername(Configs.getString("openapi.jdbc.username"));
        dataSource.setPassword(Configs.getString("openapi.jdbc.password"));
        dataSource.setMaxTotal(Configs.getInt("openapi.jdbc.maxTotal"));
        dataSource.setMaxIdle(Configs.getInt("openapi.jdbc.maxIdle"));
        dataSource.setMaxWaitMillis(Configs.getInt("openapi.jdbc.maxWaitMillis"));
        return dataSource;
    }
}
