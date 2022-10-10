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

package io.github.cgglyle.boson.graviton.service;

import io.github.cgglyle.boson.graviton.api.LogPrintfService;
import io.github.cgglyle.boson.graviton.model.LogInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@EnableAsync
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
        printf(logInfo);
    }

    /**
     * 异步日志打印服务
     *
     * @param logInfo 日志信息
     */
    @Override
    public void asyncLog(LogInfo logInfo) {
        printf(logInfo);
    }

    private void printf(LogInfo logInfo) {
        if (logInfo.isEnableSystem()) {
            if (logInfo.isStatus()) {
                if (logInfo.isEnableOrderNo()) {
                    log.info(logInfo.getOrderNo() + templateInterpreter.interpreter(logInfo));
                } else {
                    log.info(templateInterpreter.interpreter(logInfo));
                }
            } else {
                if (logInfo.isEnableOrderNo()) {
                    log.error(logInfo.getOrderNo() + templateInterpreter.interpreter(logInfo));
                } else {
                    log.error(templateInterpreter.interpreter(logInfo));
                }
            }
        }
        if (logInfo.isEnableBusiness() && (StringUtils.hasText(logInfo.getSuccess()) && logInfo.isStatus()) || (StringUtils.hasText(logInfo.getFailure()) && !logInfo.isStatus())) {
            if (logInfo.getSpELFuture() != null) {
                CompletableFuture.allOf(logInfo.getSpELFuture()).join();
            }
            if (logInfo.isStatus() && logInfo.getSuccess() != null && StringUtils.hasText(logInfo.getSuccess())) {
                if (logInfo.isEnableOrderNo()) {
                    log.info(logInfo.getOrderNo() + logInfo.getBusinessLog().toString());
                } else {
                    log.info(logInfo.getBusinessLog().toString());
                }
            } else if (!logInfo.isStatus() && logInfo.getFailure() != null && StringUtils.hasText(logInfo.getFailure())) {
                if (logInfo.isEnableOrderNo()) {
                    log.warn(logInfo.getOrderNo() + logInfo.getBusinessLog().toString());
                } else {
                    log.warn(logInfo.getBusinessLog().toString());
                }
            }
        }
    }
}
