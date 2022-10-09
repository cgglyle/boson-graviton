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
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateInterpreterTest {
    private static final String SUCCESS_TEMPLATE = "[日志] [开始时间]=[{{startTime}}] " +
            "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
            "[URL]=[{{url}}] [URI]=[{{uri}}] " +
            "[类名]=[{{className}}] [入参]=[{{inParameter}}] [出参]=[{{outParameter}}]";
    private static final String FAILURE_TEMPLATE = "[日志] [开始时间]=[{{startTime}}] " +
            "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
            "[URL]=[{{url}}] [URI]=[{{uri}}] " +
            "[类名]=[{{className}}] [入参]=[{{inParameter}}] [异常]=[{{exception}}]";
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private final LogInfo info = new LogInfo();
    private TemplateInterpreter templateInterpreterUnderTest;

    @BeforeEach
    void setUp() {
        templateInterpreterUnderTest = new TemplateInterpreter(SUCCESS_TEMPLATE, FAILURE_TEMPLATE, TIME_FORMAT);
        TempInParameter tempInParameter = new TempInParameter("in parameter", "in value");
        TempOutParameter tempOutParameter = new TempOutParameter("out parameter", "out value");
        info.setStartTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        info.setEndTime(LocalDateTime.of(2020, 1, 1, 0, 0, 5));
        info.setConsumeTime(5L);
        info.setUrl("url");
        info.setUri("uri");
        info.setClassName("className");
        info.setInParameter(tempInParameter);
        info.setOutParameter(tempOutParameter);
        info.setException(new Exception("message"));
        info.setSuccessTemplate("[日志] [开始时间]=[{{startTime}}]");
        info.setFailureTemplate("[入参]=[{{inParameter}}] [异常]=[{{exception}}]");
        info.setStatus(false);
        info.setAsync(false);
        info.setEnableSystem(false);
        info.setTimeFormat("yyyy-MM-dd");
    }

    @Test
    void testInterpreter_normalStatus() {
        // Setup
        info.setStatus(true);
        // Run the test
        final String result = templateInterpreterUnderTest.interpreter(info);

        // Verify the results
        assertThat(result).isEqualTo("[日志] [开始时间]=[2020-01-01]");
    }

    @Test
    void testInterpreter_exceptionStatus() {
        // Setup
        info.setStatus(false);
        // Run the test
        final String result = templateInterpreterUnderTest.interpreter(info);

        // Verify the results
        assertThat(result).isEqualTo("[入参]=[TemplateInterpreterTest.TempInParameter(name=in parameter, value=in value)] [异常]=[java.lang.Exception: message]");
    }

    @Test
    void testInterpreter_noSuccessTemplate() {
        // Setup
        info.setStatus(true);
        info.setSuccessTemplate(null);
        // Run the test
        final String result = templateInterpreterUnderTest.interpreter(info);

        // Verify the results
        assertThat(result).isEqualTo("[日志] [开始时间]=[2020-01-01] [结束时间]=[2020-01-01] [耗时]=[5ms] [URL]=[url] [URI]=[uri] [类名]=[className] [入参]=[TemplateInterpreterTest.TempInParameter(name=in parameter, value=in value)] [出参]=[TemplateInterpreterTest.TempOutParameter(name=out parameter, value=out value)]");
    }

    @Test
    void testInterpreter_noFailureTemplate() {
        // Setup
        info.setStatus(false);
        info.setFailureTemplate(null);
        // Run the test
        final String result = templateInterpreterUnderTest.interpreter(info);

        // Verify the results
        assertThat(result).isEqualTo("[日志] [开始时间]=[2020-01-01] [结束时间]=[2020-01-01] [耗时]=[5ms] [URL]=[url] [URI]=[uri] [类名]=[className] [入参]=[TemplateInterpreterTest.TempInParameter(name=in parameter, value=in value)] [异常]=[java.lang.Exception: message]");
    }

    @Test
    void testInterpreter_noParams() {
        info.setStatus(true);
        info.setSuccessTemplate("{{test}}");
        Assertions.assertThrows(LogException.class, () -> templateInterpreterUnderTest.interpreter(info), "预期返回LogException异常");
    }

    @Test
    void testInterpreter_noParamsTemplate() {
        info.setStartTime(null);
        info.setStatus(true);

        // Run the test
        final String result = templateInterpreterUnderTest.interpreter(info);

        // Verify the results
        assertThat(result).isEqualTo("[日志] [开始时间]=[null]");
    }

    @Data
    @AllArgsConstructor
    private class TempInParameter {
        private String name;
        private String value;
    }

    @Data
    @AllArgsConstructor
    private class TempOutParameter {
        private String name;
        private String value;
    }
}
