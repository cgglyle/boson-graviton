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

package io.github.cgglyle.boson.graviton.service.logger;

import io.github.cgglyle.boson.graviton.annotaion.GravitonLog;
import io.github.cgglyle.boson.graviton.api.GravitonLogInfoSpEL;
import io.github.cgglyle.boson.graviton.api.LogUserService;
import io.github.cgglyle.boson.graviton.model.LogContext;
import org.aspectj.lang.JoinPoint;

/**
 * 默认提供的日志处理服务
 *
 * @author lyle
 * @since 2022/09/10
 */
public class NoWebLogger extends Logger {

    public NoWebLogger(LogUserService logUserService, GravitonLogInfoSpEL logInfoSpEL) {
        super(logUserService, logInfoSpEL);
    }
}
