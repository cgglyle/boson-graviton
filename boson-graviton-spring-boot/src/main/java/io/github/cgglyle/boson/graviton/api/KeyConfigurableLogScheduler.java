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
 * 含前缀的日志调度控制
 *
 * @author Lyle
 * @since 2022/09/16
 */
public interface KeyConfigurableLogScheduler {

    /**
     * 添加一个日志服务到调度器
     * <p>
     * 指定一个日志服务前缀，服务前缀会作为日志服务的分组条件
     *
     * @param prefix  日志前缀
     * @param service 日志服务
     */
    void addPrintfService(String prefix, LogPrintfService service);

    /**
     * 添加一个集合的日志服务到调度器
     * <p>
     * 指定一个日志服务前缀，服务前缀会作为日志服务的分组条件
     *
     * @param prefix       日志前缀
     * @param servicesList 日志服务集合
     */
    void addPrintfService(String prefix, Collection<LogPrintfService> servicesList);

    /**
     * 删除一个日志服务从调度器
     * <p>
     * 指定一个日志服务前缀，服务前缀会作为日志服务的分组条件
     *
     * @param service 日志服务
     */
    void deletedPrintfService(String prefix, LogPrintfService service);
}
