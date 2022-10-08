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

package io.github.cgglyle.boson.graviton.api;

import java.util.Collection;

/**
 * 调度器配置接口
 *
 * @author lyle
 * @since 2022/09/14
 */
public interface ConfigurableLogScheduler {
    /**
     * 添加一个日志服务到调度器
     *
     * @param service 日志服务
     */
    void addPrintfService(LogPrintfService service);

    /**
     * 添加一个集合的日志服务到调度器
     *
     * @param servicesList 日志服务集合
     */
    void addPrintfService(Collection<LogPrintfService> servicesList);

    /**
     * 删除一个日志服务从调度器
     *
     * @param service 日志服务
     */
    void deletedPrintfService(LogPrintfService service);
}
