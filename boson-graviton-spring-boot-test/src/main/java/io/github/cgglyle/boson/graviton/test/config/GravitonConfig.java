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
                                        GravitonLogSpEL gravitonLogSpEL) {
        return new GravitonLogAspect(logControllerService, logScheduler, gravitonLogSpEL);
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
        template.setFailureTemplate("[??????] [????????????]=[{{startTime}}] " +
                "[????????????]=[{{endTime}}] [??????]=[{{consumeTime}}ms] " +
                "[URL]=[{{url}}] [URI]=[{{uri}}] " +
                "[??????]=[{{className}}] [??????]=[{{inParameter}}] [??????]=[{{exception}}]");
        template.setSuccessTemplate("[??????] [????????????]=[{{startTime}}]" +
                "[????????????]=[{{endTime}}] [??????]=[{{consumeTime}}ms] " +
                "[URL]=[{{url}}] [URI]=[{{uri}}] " +
                "[??????]=[{{className}}] [??????]=[{{inParameter}}] [??????]=[{{outParameter}}]");
        template.setTimeFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return new TemplateInterpreter(template);
    }
}
