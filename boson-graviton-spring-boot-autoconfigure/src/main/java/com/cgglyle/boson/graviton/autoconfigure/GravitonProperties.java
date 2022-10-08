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

package com.cgglyle.boson.graviton.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lyle
 * @since 2022/09/12
 */
@Data
@ConfigurationProperties(prefix = "spring.boson.graviton")
public class GravitonProperties {

    /**
     * 默认成功日志模板
     */
    private String defaultSuccessTemplate = "[日志] [开始时间]=[{{startTime}}] " +
            "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
            "[URL]=[{{url}}] [URI]=[{{uri}}] " +
            "[类名]=[{{className}}] [入参]=[{{inParameter}}] [出参]=[{{outParameter}}] ";
    /**
     * 默认失败日志模板
     */
    private String defaultFailureTemplate = "[日志] [开始时间]=[{{startTime}}] " +
            "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
            "[URL]=[{{url}}] [URI]=[{{uri}}] " +
            "[类名]=[{{className}}] [入参]=[{{inParameter}}] [异常]=[{{exception}}] ";
    /**
     * 默认日期格式
     */
    private String timeFormat = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 核心线程数
     */
    private int corePoolSize = 10;
    /**
     * 最大线程数
     */
    private int maxPoolSize = 20;
    /**
     * 队列大小
     */
    private int queueCapacity = 1000;
    /**
     * 线程名前缀
     */
    private String namePrefix = "Graviton-Log ";
}
