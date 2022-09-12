/*
 * Copyright 2022 Cgglyle
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
 */

package com.cgglyle.boson.graviton.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lyle
 * @since 2022/09/10
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "spring.boson.graviton")
public class GravitonConfig {
    /**
     * 是否开启异步日志 默认为异步
     */
    private boolean asynchronousLog = true;

    /**
     * 默认成功日志模板
     */
    private String defaultSuccessTemplate = "[日志] [开始时间]=[{{startTime}}] " +
            "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
            "[类名]=[{{className}}] [入参]=[{{inParameter}}] [出参]=[{{outParameter}}] ";
    /**
     * 默认失败日志模板
     */
    private String defaultFailureTemplate = "[日志] [开始时间]=[{{startTime}}] " +
            "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
            "[类名]=[{{className}}] [入参]=[{{inParameter}}] [异常]=[{{exception}}] ";
}
