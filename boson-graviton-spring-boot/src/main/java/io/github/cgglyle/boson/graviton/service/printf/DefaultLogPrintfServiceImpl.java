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

package io.github.cgglyle.boson.graviton.service.printf;

import io.github.cgglyle.boson.graviton.api.LogPrintfService;
import io.github.cgglyle.boson.graviton.model.LogContext;
import io.github.cgglyle.boson.graviton.model.LogLevelEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.StringUtils;

import java.util.concurrent.CompletableFuture;

/**
 * 默认日志打印服务
 *
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
     * @param logContext 日志信息
     */
    @Override
    public void log(LogContext logContext) {
        printf(logContext);
    }
@Async
    private void printf(LogContext logContext) {

    }

    private void printf(LogLevelEnum logLevelEnum, String str) {
        if (logLevelEnum == LogLevelEnum.INFO) {
            log.info(str);
            return;
        }
        if (logLevelEnum == LogLevelEnum.WARN) {
            log.warn(str);
            return;
        }
        if (logLevelEnum == LogLevelEnum.ERROR) {
            log.error(str);
            return;
        }
        if (logLevelEnum == LogLevelEnum.DEBUG) {
            log.debug(str);
            return;
        }
    }
}
