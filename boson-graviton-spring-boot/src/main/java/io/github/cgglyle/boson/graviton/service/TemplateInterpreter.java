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

import io.github.cgglyle.boson.graviton.exception.LogException;
import io.github.cgglyle.boson.graviton.model.LogInfo;
import io.github.cgglyle.boson.graviton.model.Template;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志模板解释
 *
 * @author lyle
 * @since 2022/09/11
 */
public class TemplateInterpreter {
    private static final String TYPE = "(?<=\\{\\{).*?(?=}})";
    /**
     * 默认成功日志模板
     */
    private final String defaultSuccessTemplate;
    /**
     * 默认失败日志模板
     */
    private final String defaultFailureTemplate;
    /**
     * 时间格式
     */
    private final String timeFormat;

    public TemplateInterpreter(String defaultSuccessTemplate, String defaultFailureTemplate, String timeFormat) {
        this.defaultFailureTemplate = defaultFailureTemplate;
        this.defaultSuccessTemplate = defaultSuccessTemplate;
        this.timeFormat = timeFormat;
    }

    public TemplateInterpreter(Template template) {
        this.defaultFailureTemplate = template.getFailureTemplate();
        this.defaultSuccessTemplate = template.getSuccessTemplate();
        this.timeFormat = template.getTimeFormat();
    }

    /**
     * 日志信息携带模板进入后会进行模板解释，将信息附着到模板上。
     *
     * @param info 日志信息
     * @return 已经解析完成的可以直接打印的日志
     */
    public String interpreter(LogInfo info) {
        String template;
        // 无模板时，使用默认模板
        if (info.isStatus()) {
            template = info.getSuccessTemplate();
            if (!StringUtils.hasText(template)) {
                template = defaultSuccessTemplate;
            }
        } else {
            template = info.getFailureTemplate();
            if (!StringUtils.hasText(template)) {
                template = defaultFailureTemplate;
            }
        }
        // 提取模板的标识
        List<Temp> templates = new ArrayList<>();
        Pattern compile = Pattern.compile(TYPE);
        Matcher matcher = compile.matcher(template);
        while (matcher.find()) {
            templates.add(Temp.builder().template(matcher.group()).startIndex(matcher.start()).endIndex(matcher.end()).build());
        }
        Class<? extends LogInfo> aClass = info.getClass();
        String tempStr = template;
        // 标识存入数据
        for (Temp temp : templates) {
            Field declaredField;
            try {
                declaredField = aClass.getDeclaredField(temp.getTemplate());
            } catch (NoSuchFieldException e) {
                throw new LogException(e);
            }
            try {
                declaredField.setAccessible(true);
                Object o = declaredField.get(info);
                if (o == null) {
                    tempStr = StringUtils.replace(tempStr, "{{" + temp.getTemplate() + "}}", "null");
                    continue;
                }
                if (o instanceof LocalDateTime) {
                    String format;
                    if (StringUtils.hasText(info.getTimeFormat())) {
                        format = ((LocalDateTime) o).format(DateTimeFormatter.ofPattern(info.getTimeFormat()));
                    } else {
                        format = ((LocalDateTime) o).format(DateTimeFormatter.ofPattern(timeFormat));
                    }
                    tempStr = StringUtils.replace(tempStr, "{{" + temp.getTemplate() + "}}", format);
                    continue;
                }
                tempStr = StringUtils.replace(tempStr, "{{" + temp.getTemplate() + "}}", o.toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.hasText(info.getOrderNo())) {
            if (StringUtils.hasText(info.getSuccess())) {
                info.setSuccess(info.getOrderNo() + info.getSuccess());
            }
            if (StringUtils.hasText(info.getFailure())) {
                info.setFailure(info.getOrderNo() + info.getFailure());
            }
            return info.getOrderNo() + tempStr;
        }
        return tempStr;
    }

    @Data
    @Builder
    private static class Temp {
        private String template;
        private int startIndex;
        private int endIndex;
    }
}
