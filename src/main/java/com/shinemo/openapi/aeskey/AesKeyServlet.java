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
import com.shinemo.openapi.client.aes.AesKeyService;
import com.shinemo.openapi.client.aes.DefaultAesKeyService;
import com.shinemo.openapi.client.aes.db.MysqlAesKeyDao;
import com.shinemo.openapi.client.aes.domain.AesKeyEntity;
import com.shinemo.openapi.client.common.OpenApiResult;
import com.shinemo.openapi.client.service.AuthApiService;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.shinemo.openapi.client.common.Const.LOG;

/**
 * Created by ohun on 2017/4/18.
 *
 * @author ohun@live.cn (夜色)
 */
public final class AesKeyServlet extends HttpServlet {

    private AesKeyService aesKeyService;

    private OpenApiClient openApiClient;

    @Override
    public void destroy() {
        super.destroy();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("token");
        String uid = req.getHeader("uid");
        String orgId = req.getHeader("orgId");
        String timestamp = req.getHeader("timestamp");
        String ids = req.getParameter("keyIds");

        if (token == null || uid == null || orgId == null || timestamp == null) {
            this.writeResult(resp, OpenApiResult.failure(400, "参数错误"));
            return;
        }

        try {
            OpenApiResult<List<AesKeyEntity>> result = this.aesKeyService.getAesKeyByClient(orgId, uid, token, Long.parseLong(timestamp), ids);
            this.writeResult(resp, result);
        } catch (Exception e) {
            this.writeResult(resp, OpenApiResult.failure(500, "服务器内部错误"));
            LOG.error("get aes key error, ids={}, orgId={}, uid={}, token={}, timestamp={}", ids, orgId, uid, token, timestamp, e);
        }
    }

    private void writeResult(HttpServletResponse response, OpenApiResult<?> result) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        openApiClient.config().getGson().toJson(result, writer);
        writer.close();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        OpenApiClient apiClient = createConfig().create();
        AuthApiService authApiService = apiClient.createApiService(AuthApiService.class);

        MysqlAesKeyDao aesKeyDao = new MysqlAesKeyDao();
        aesKeyDao.setDataSource(createDataSource());
        aesKeyDao.init();

        DefaultAesKeyService defaultAesKeyService = new DefaultAesKeyService();
        defaultAesKeyService.setAesKeyDao(aesKeyDao);
        defaultAesKeyService.setAuthApiService(authApiService);
        defaultAesKeyService.init();

        openApiClient = apiClient;
        aesKeyService = defaultAesKeyService;

        System.err.println("===================================================================");
        System.err.println("===============OPEN-API-AES-KEY SERVER START SUCCESS===============");
        System.err.println("===================================================================");
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
