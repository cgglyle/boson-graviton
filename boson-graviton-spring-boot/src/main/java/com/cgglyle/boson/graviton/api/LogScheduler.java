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

package com.cgglyle.boson.graviton.api;

import com.cgglyle.boson.graviton.model.LogInfo;

import java.util.List;

/**
 * Graviton 日志调度器
 *
 * @author lyle
 * @since 2022/09/12
 */
public interface LogScheduler {

    /**
     * 获得日志服务列表
     *
     * @return 日志服务列表
     */
    List<LogPrintfService> printfServiceList();

    /**
     * 打印日志
     *
     * @param info 日志信息
     * @param isAsync 是否异步操作
     */
    void startPrintf(LogInfo info, boolean isAsync);

}
