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

import io.github.cgglyle.boson.graviton.api.GravitonLogInfoSpEL;
import io.github.cgglyle.boson.graviton.api.GravitonLogSpEL;
import io.github.cgglyle.boson.graviton.model.LogInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author Lyle
 * @since 2022/10/09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GravitonLogInfoValueParser implements GravitonLogInfoSpEL {
    private final GravitonLogSpEL gravitonLogSpEl;

    @Override
    public CompletableFuture<?> parser(LogInfo loginfo, Class<?> parserResultClass) {
        if (loginfo.isStatus()) {
            try {
                Object parser = gravitonLogSpEl.parser(loginfo.getJoinPoint(),
                        loginfo.getSuccess().toString(), loginfo.getOutParameter(), null, parserResultClass);
                loginfo.setBusinessLog(parser);
            } catch (Exception e) {
                log.error("SpEL解析出现异常" + e.getMessage(), e);
            }
        } else {
            try {
                Object parser = gravitonLogSpEl.parser(loginfo.getJoinPoint(),
                        loginfo.getFailure().toString(), null, loginfo.getErrorMsg(), parserResultClass);
                loginfo.setBusinessLog(parser);
            } catch (Exception e) {
                log.error("SpEL解析出现异常" + e.getMessage(), e);
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
