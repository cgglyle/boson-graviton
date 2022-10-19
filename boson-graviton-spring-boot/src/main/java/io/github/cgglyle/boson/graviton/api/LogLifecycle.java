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
 * 日志生命周期
 *
 * @author lyle
 * @since 2022/09/14
 */
public interface LogLifecycle {
    /**
     * 启动日志服务
     */
    void start();

    /**
     * 暂停日志服务
     */
    void stop();

    /**
     * 判断日志服务是否在运行
     *
     * @return {@code true}所有服务正在运行，反之有服务不在运行
     */
    boolean isRunning();
}
