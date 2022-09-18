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

package com.cgglyle.boson.graviton.annotaion;

import com.cgglyle.boson.graviton.autoconfigure.GravitonAutoconfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动{@code Graviton}
 *
 * 通过注解启动后配置文件中{@code boson.graviton.enable}将失效。
 *
 * @author Lyle
 * @since 2022/09/18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GravitonAutoconfigure.class)
public @interface EnableGraviton {
}
