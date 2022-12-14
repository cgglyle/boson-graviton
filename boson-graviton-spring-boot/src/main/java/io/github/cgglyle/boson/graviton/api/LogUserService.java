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

package io.github.cgglyle.boson.graviton.api;

/**
 * 获取用户名称
 *
 * @author Lyle
 * @since 2022/10/05
 */
public interface LogUserService {

    /**
     * 获取用户信息
     * <p>
     * 注意，此方法会较晚执行，SpEL在方法中的username上下文存入可能会被覆盖。
     */
    String getUsername();
}
