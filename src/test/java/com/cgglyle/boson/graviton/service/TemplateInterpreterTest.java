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

package com.cgglyle.boson.graviton.service;

import com.cgglyle.boson.graviton.config.GravitonConfig;
import com.cgglyle.boson.graviton.exception.LogException;
import com.cgglyle.boson.graviton.model.LogInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@Slf4j
@ExtendWith(SpringExtension.class)
class TemplateInterpreterTest {
    @InjectMocks
    private TemplateInterpreter interpreter;
    @Mock
    private GravitonConfig config;
    private final LogInfo info = new LogInfo();

    @BeforeEach
    void setUp() {
        info.setStartTime(LocalDateTime.of(2022, 9, 10, 11, 12, 11));
        info.setEndTime(LocalDateTime.of(2022, 9, 10, 11, 12, 12));
        info.setConsumeTime(1L);
        info.setClassName("TestClass");
        info.setException(new RuntimeException("test exception"));
        Mockito.when(config.getDefaultSuccessTemplate()).thenReturn(
                "[日志] [开始时间]=[{{startTime}}] " +
                        "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
                        "[类名]=[{{className}}] [入参]=[{{inParameter}}] [出参]=[{{outParameter}}] "
        );
        Mockito.when(config.getDefaultFailureTemplate()).thenReturn(
                "[日志] [开始时间]=[{{startTime}}] " +
                        "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
                        "[类名]=[{{className}}] [入参]=[{{inParameter}}] [异常]=[{{exception}}] "
        );
    }

    @Test
    void testInterpreter() {
        info.setStatus(true);
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[null] [出参]=[null] ",
                interpreter.interpreter(info), "空模板，空入参,空出参测试失败");
        info.setOutParameter("out");
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[null] [出参]=[out] ",
                interpreter.interpreter(info), "空模板，空入参测试失败");
        info.setInParameter("in");
        info.setOutParameter(null);
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[in] [出参]=[null] ",
                interpreter.interpreter(info), "空模板，空出参测试失败");
        info.setOutParameter("out");
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[in] [出参]=[out] ",
                interpreter.interpreter(info), "空模板，全参失败");
        info.setStatus(false);
        info.setInParameter(null);
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[null] [异常]=[java.lang.RuntimeException: test exception] ",
                interpreter.interpreter(info), "空模板，空入参,空出参测试失败");
        info.setOutParameter("out");
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[null] [异常]=[java.lang.RuntimeException: test exception] ",
                interpreter.interpreter(info), "空模板，空入参测试失败");
        info.setInParameter("in");
        info.setOutParameter(null);
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[in] [异常]=[java.lang.RuntimeException: test exception] ",
                interpreter.interpreter(info), "空模板，空出参测试失败");
        info.setOutParameter("out");
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[in] [异常]=[java.lang.RuntimeException: test exception] ",
                interpreter.interpreter(info), "空模板，全参失败");
        info.setSuccessTemplate("[日志] [开始时间]=[{{startTime}}] " +
                "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
                "[类名]=[{{className}}] [入参]=[{{inParameter}}] [出参]=[{{outParameter}}] ");
        info.setFailureTemplate("[日志] [开始时间]=[{{startTime}}] " +
                "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
                "[类名]=[{{className}}] [入参]=[{{inParameter}}] [异常]=[{{exception}}] ");
        info.setStatus(true);
        info.setInParameter(null);
        info.setOutParameter(null);
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[null] [出参]=[null] ",
                interpreter.interpreter(info), "空入参,空出参测试失败");
        info.setOutParameter("out");
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[null] [出参]=[out] ",
                interpreter.interpreter(info), "空入参测试失败");
        info.setInParameter("in");
        info.setOutParameter(null);
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[in] [出参]=[null] ",
                interpreter.interpreter(info), "空出参测试失败");
        info.setOutParameter("out");
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[in] [出参]=[out] ",
                interpreter.interpreter(info), "全参失败");
        info.setStatus(false);
        info.setInParameter(null);
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[null] [异常]=[java.lang.RuntimeException: test exception] ",
                interpreter.interpreter(info), "空入参,空出参测试失败");
        info.setOutParameter("out");
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[null] [异常]=[java.lang.RuntimeException: test exception] ",
                interpreter.interpreter(info), "空入参测试失败");
        info.setInParameter("in");
        info.setOutParameter(null);
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[in] [异常]=[java.lang.RuntimeException: test exception] ",
                interpreter.interpreter(info), "空出参测试失败");
        info.setOutParameter("out");
        Assertions.assertEquals("[日志] [开始时间]=[2022-09-10T11:12:11] " +
                        "[结束时间]=[2022-09-10T11:12:12] [耗时]=[1ms] " +
                        "[类名]=[TestClass] [入参]=[in] [异常]=[java.lang.RuntimeException: test exception] ",
                interpreter.interpreter(info), "全参失败");
        info.setStatus(true);
        info.setSuccessTemplate("[日志] [开始时间]=[{{startTime}}] " +
                "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
                "[类名]=[{{className}}] [入参]=[{{inParameter}}] [出参]=[{{out}}] ");
        Assertions.assertThrows(LogException.class, () -> interpreter.interpreter(info), "期望出现无标识异常");
        info.setStatus(false);
        info.setFailureTemplate("[日志] [开始时间]=[{{startTime}}] " +
                "[结束时间]=[{{endTime}}] [耗时]=[{{consumeTime}}ms] " +
                "[类名]=[{{className}}] [入参]=[{{inParameter}}] [异常]=[{{exception}}] [测试无标识]={{test}}");
        Assertions.assertThrows(LogException.class, () -> interpreter.interpreter(info), "期望出现无标识异常");
    }
}