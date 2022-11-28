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
import io.github.cgglyle.boson.graviton.model.LogInfo;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认提供的日志处理服务
 * <p>
 * 在是WEB项目的时候启用
 *
 * @author Lyle
 * @since 2022/09/16
 */
public class WebLogger extends Logger {

    public WebLogger(LogUserService logUserService, GravitonLogInfoSpEL logInfoSpEL) {
        super(logUserService, logInfoSpEL);
    }

    /**
     * 日志前置处理
     * <p>
     * 可以通过织入点获得被注解标记的函数的信息
     *
     * @param joinPoint   织入点信息
     * @param gravitonLog 注解信息
     * @param logContext  日志信息
     */
    @Override
    public void preprocessing(JoinPoint joinPoint, GravitonLog gravitonLog, LogContext logContext) {
        super.preprocessing(joinPoint,gravitonLog,logContext);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        LogInfo logInfo = logContext.getLogInfo();
        if (requestAttributes != null) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) requestAttributes.
                    resolveReference(RequestAttributes.REFERENCE_REQUEST);
            if (httpServletRequest != null) {
                logInfo.setUri(httpServletRequest.getRequestURI());
                logInfo.setUrl(httpServletRequest.getRequestURL().toString());
                logInfo.setIp(httpServletRequest.getRemoteAddr());
            }
        }
    }
}
