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

package com.cgglyle.boson.graviton.test.config;

import com.cgglyle.boson.graviton.aop.GravitonLogAspect;
import com.cgglyle.boson.graviton.api.LogControllerService;
import com.cgglyle.boson.graviton.api.LogPrintfService;
import com.cgglyle.boson.graviton.api.LogScheduler;
import com.cgglyle.boson.graviton.model.Template;
import com.cgglyle.boson.graviton.service.DefaultLogPrintfServiceImpl;
import com.cgglyle.boson.graviton.service.DefaultLogSchedulerImpl;
import com.cgglyle.boson.graviton.service.DefaultWebLogControllerServiceImpl;
import com.cgglyle.boson.graviton.service.TemplateInterpreter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lyle
 * @since 2022/09/17
 */
@Configuration
public class GravitonConfig {
    @Bean
    GravitonLogAspect gravitonLogAspect() {
        return new GravitonLogAspect(logControllerService(), logScheduler());
    }

    @Bean
    LogControllerService logControllerService() {
        return new DefaultWebLogControllerServiceImpl();
    }

    @Bean
    LogScheduler logScheduler() {
        return new DefaultLogSchedulerImpl(logPrintfService());
    }

    @Bean
    LogPrintfService logPrintfService() {
        return new DefaultLogPrintfServiceImpl(templateInterpreter());
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
        return new TemplateInterpreter(template);
    }
}
