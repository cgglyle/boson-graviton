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

package io.github.cgglyle.boson.graviton.model;

import io.github.cgglyle.boson.graviton.api.LogControllerService;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * 日志信息
 * <p>
 * 所有成员都将用在日志模板中，并实现{@link LogControllerService}来实现赋值
 *
 * @author lyle
 * @since 2022/09/10
 */
@Component
@Data
public class LogInfo {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long consumeTime;
    private String url;
    private String uri;
    private String ip;
    private String className;
    private Object inParameter;
    private Object outParameter;
    private Throwable exception;
    private String successTemplate;
    private String failureTemplate;
    private boolean status;
    private boolean async;
    private boolean enableSystem;
    private boolean enableBusiness;
    private boolean enableOrderNo;
    private LogLevelEnum systemErrorLogLevel;
    private LogLevelEnum systemSuccessLogLevel;
    private LogLevelEnum businessErrorLogLevel;
    private LogLevelEnum businessSuccessLogLevel;
    private String timeFormat;
    private String success;
    private String failure;
    private Object systemLog;
    private Object businessLog;
    private String orderNo;
    private String username;
    private JoinPoint joinPoint;
    private String errorMsg;
    private CompletableFuture<?> spELFuture;
}
