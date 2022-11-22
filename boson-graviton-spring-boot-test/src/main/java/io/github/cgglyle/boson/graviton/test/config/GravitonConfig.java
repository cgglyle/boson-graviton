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

package io.github.cgglyle.boson.graviton.test.config;

import io.github.cgglyle.boson.graviton.aop.GravitonLogAspect;
import io.github.cgglyle.boson.graviton.api.*;
import io.github.cgglyle.boson.graviton.model.Template;
import io.github.cgglyle.boson.graviton.service.DefaultLogPrintfServiceImpl;
import io.github.cgglyle.boson.graviton.service.DefaultLogSchedulerImpl;
import io.github.cgglyle.boson.graviton.service.DefaultWebLogControllerServiceImpl;
import io.github.cgglyle.boson.graviton.service.TemplateInterpreter;
import io.github.cgglyle.boson.graviton.service.mq.LogPrintfConsume;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * @author Lyle
 * @since 2022/09/17
 */
@Configuration
public class GravitonConfig {
    @Bean
    GravitonLogAspect gravitonLogAspect(LogControllerService logControllerService, LogScheduler logScheduler,
                                        GravitonLogSpEL gravitonLogSpEL, TemplateInterpreter templateInterpreter) {
        return new GravitonLogAspect(logControllerService, logScheduler, gravitonLogSpEL, new LogPrintfConsume(templateInterpreter));
    }

    @Bean
    LogControllerService logControllerService(@Nullable LogUserService logUserService, GravitonLogInfoSpEL gravitonLogInfoSpEL) {
        return new DefaultWebLogControllerServiceImpl(logUserService, gravitonLogInfoSpEL);
    }

    @Bean
    LogScheduler logScheduler(LogPrintfService logPrintfService) {
        return new DefaultLogSchedulerImpl(logPrintfService);
    }

    @Bean
    LogPrintfService logPrintfService(TemplateInterpreter templateInterpreter) {
        return new DefaultLogPrintfServiceImpl(templateInterpreter);
    }

    @Bean
    TemplateInterpreter templateInterpreter() {
        Template template = new Template();
        template.setFailureTemplate("[日志] [开始时间]=[{{startTime}}] " +
                "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
                "[URL]=[{{url}}] [URI]=[{{uri}}] " +
                "[类名]=[{{className}}] [入参]=[{{inParameter}}] [异常]=[{{exception}}]");
        template.setSuccessTemplate("[日志] [开始时间]=[{{startTime}}]" +
                "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
                "[URL]=[{{url}}] [URI]=[{{uri}}] " +
                "[类名]=[{{className}}] [入参]=[{{inParameter}}] [出参]=[{{outParameter}}]");
        template.setTimeFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return new TemplateInterpreter(template);
    }
}
