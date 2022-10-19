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
        // 判断是否启用系统日志
        if (logInfo.isEnableSystem()) {
            // 判断执行状态，成功还是失败
            if (logInfo.isStatus()) {
                // 判断是否启动订单号
                if (logInfo.isEnableOrderNo()) {
                    log.info(logInfo.getOrderNo() + templateInterpreter.interpreter(logInfo));
                } else {
                    log.info(templateInterpreter.interpreter(logInfo));
                }
            } else {
                // 判断是否启动订单号
                if (logInfo.isEnableOrderNo()) {
                    log.error(logInfo.getOrderNo() + templateInterpreter.interpreter(logInfo));
                } else {
                    log.error(templateInterpreter.interpreter(logInfo));
                }
            }
        }
        // 判断是否启动业务日志且成功模板和失败模板是否存在
        if (logInfo.isEnableBusiness() && (StringUtils.hasText(logInfo.getSuccess()) && logInfo.isStatus()) || (StringUtils.hasText(logInfo.getFailure()) && !logInfo.isStatus())) {
            // 判断异步调用是否为空
            if (logInfo.getSpELFuture() != null) {
                // 等待调用结束
                CompletableFuture.allOf(logInfo.getSpELFuture()).join();
            }
            // 判断执行结果，并判断结果时候为空
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
