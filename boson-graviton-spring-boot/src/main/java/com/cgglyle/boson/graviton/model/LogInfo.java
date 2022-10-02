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

package com.cgglyle.boson.graviton.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 日志信息
 * <p>
 * 所有成员都将用在日志模板中，并实现{@link com.cgglyle.boson.graviton.api.LogControllerService}来实现赋值
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
    private String className;
    private Object inParameter;
    private Object outParameter;
    private Throwable exception;
    private String successTemplate;
    private String failureTemplate;
    private boolean status;
    private boolean async;
    private String timeFormat;
}
