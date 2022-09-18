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

package com.cgglyle.boson.graviton.service;

import com.cgglyle.boson.graviton.api.LogPrintfService;
import com.cgglyle.boson.graviton.model.LogInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

/**
 * @author lyle
 * @since 2022/09/10
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultLogPrintfServiceImpl implements LogPrintfService {
    private final TemplateInterpreter templateInterpreter;

    /**
     * 日志打印服务
     *
     * @param logInfo 日志信息
     */
    @Override
    public void log(LogInfo logInfo) {
        log.info(templateInterpreter.interpreter(logInfo));
    }

    /**
     * 异步日志打印服务
     *
     * @param logInfo 日志信息
     */
    @Async("gravitonLogPool")
    @Override
    public void asyncLog(LogInfo logInfo) {
        log.info(templateInterpreter.interpreter(logInfo));
    }
}
