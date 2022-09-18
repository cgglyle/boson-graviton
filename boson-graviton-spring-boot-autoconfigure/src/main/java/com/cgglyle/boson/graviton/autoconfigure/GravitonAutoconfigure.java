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

package com.cgglyle.boson.graviton.autoconfigure;

import com.cgglyle.boson.graviton.api.LogControllerService;
import com.cgglyle.boson.graviton.api.LogPrintfService;
import com.cgglyle.boson.graviton.api.LogScheduler;
import com.cgglyle.boson.graviton.model.Template;
import com.cgglyle.boson.graviton.service.DefaultLogPrintfServiceImpl;
import com.cgglyle.boson.graviton.service.DefaultLogSchedulerImpl;
import com.cgglyle.boson.graviton.service.DefaultWebLogControllerServiceImpl;
import com.cgglyle.boson.graviton.service.TemplateInterpreter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lyle
 * @since 2022/09/18
 */
@AutoConfiguration
@EnableConfigurationProperties(GravitonProperties.class)
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.cgglyle.boson.graviton.aop")
@ConditionalOnProperty(prefix = "spring.boson.graviton", name = "enable", havingValue = "true")
public class GravitonAutoconfigure {
    private final GravitonProperties properties;

    @ConditionalOnMissingBean(LogControllerService.class)
    @ConditionalOnWebApplication
    @Bean
    LogControllerService webLogControllerService() {
        return new DefaultWebLogControllerServiceImpl();
    }

    @ConditionalOnMissingBean(LogControllerService.class)
    @ConditionalOnNotWebApplication
    @Bean
    LogControllerService LogControllerService() {
        return new DefaultWebLogControllerServiceImpl();
    }

    @ConditionalOnMissingBean(LogScheduler.class)
    @Bean
    LogScheduler logScheduler() {
        return new DefaultLogSchedulerImpl(logPrintfService());
    }

    @ConditionalOnMissingBean(LogPrintfService.class)
    @Bean
    LogPrintfService logPrintfService(){
        return new DefaultLogPrintfServiceImpl(templateInterpreter());
    }

    @ConditionalOnMissingBean(TemplateInterpreter.class)
    @Bean
    TemplateInterpreter templateInterpreter() {
        Template template = new Template();
        template.setFailureTemplate(properties.getDefaultFailureTemplate());
        template.setSuccessTemplate(properties.getDefaultSuccessTemplate());
        return new TemplateInterpreter(template);
    }
}
