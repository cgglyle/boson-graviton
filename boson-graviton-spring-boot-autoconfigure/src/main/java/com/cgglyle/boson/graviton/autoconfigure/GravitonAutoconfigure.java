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

import com.cgglyle.boson.graviton.aop.GravitonLogAspect;
import com.cgglyle.boson.graviton.api.*;
import com.cgglyle.boson.graviton.model.Template;
import com.cgglyle.boson.graviton.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自动配置处理项
 *
 * @author Lyle
 * @since 2022/09/18
 */
@EnableAsync
@Configuration
@AutoConfigureOrder(Integer.MIN_VALUE)
@EnableConfigurationProperties(GravitonProperties.class)
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.cgglyle.boson.graviton.*")
public class GravitonAutoconfigure {
    private final GravitonProperties properties;

    @ConditionalOnMissingBean
    @Bean
    GravitonLogAspect gravitonLogAspect(LogControllerService logControllerService,
                                        LogScheduler logScheduler,
                                        GravitonLogSpEL gravitonLogSpEL) {
        return new GravitonLogAspect(logControllerService, logScheduler, gravitonLogSpEL);
    }

    /**
     * 日志调度器
     */
    @ConditionalOnMissingBean
    @Bean
    LogScheduler logScheduler(LogPrintfService logPrintfService) {
        return new DefaultLogSchedulerImpl(logPrintfService);
    }

    /**
     * 日志打印服务
     */
    @ConditionalOnMissingBean
    @Bean
    LogPrintfService logPrintfService(TemplateInterpreter templateInterpreter) {
        return new DefaultLogPrintfServiceImpl(templateInterpreter);
    }

    /**
     * 模板解析器
     */
    @ConditionalOnMissingBean
    @Bean
    TemplateInterpreter templateInterpreter() {
        Template template = new Template();
        template.setFailureTemplate(properties.getDefaultFailureTemplate());
        template.setSuccessTemplate(properties.getDefaultSuccessTemplate());
        template.setTimeFormat(properties.getTimeFormat());
        return new TemplateInterpreter(template);
    }

    /**
     * 如果不是web项目就注入
     */
    @ConditionalOnMissingBean
    @ConditionalOnNotWebApplication
    @Bean
    LogControllerService logControllerService(@Nullable LogUserService logUserService) {
        return new DefaultLogControllerServiceImpl(logUserService);
    }

    /**
     * 如果是web项目就注入
     */
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication
    @Bean
    LogControllerService webLogControllerService(@Nullable LogUserService logUserService) {
        return new DefaultWebLogControllerServiceImpl(logUserService);
    }

    /**
     * 线程池
     */
    @ConditionalOnMissingBean(name = "gravitonLogPool")
    @Bean(name = "gravitonLogPool")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(properties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(properties.getNamePrefix());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
