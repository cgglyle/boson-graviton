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

package io.github.cgglyle.boson.graviton.model;

import lombok.Data;

/**
 * 日志模板
 *
 * @author Lyle
 * @since 2022/09/17
 */
@Data
public class Template {
    /**
     * 失败日志模板
     */
    private String failureTemplate;
    /**
     * 成功日志模板
     */
    private String successTemplate;
    /**
     * 时间格式
     */
    private String timeFormat;
}
