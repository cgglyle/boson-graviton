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

import io.github.cgglyle.boson.graviton.api.ConfigurableLogScheduler;
import io.github.cgglyle.boson.graviton.api.KeyConfigurableLogScheduler;
import io.github.cgglyle.boson.graviton.api.LogPrintfService;
import io.github.cgglyle.boson.graviton.api.LogScheduler;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象日志调度器
 *
 * @author lyle
 * @since 2022/09/14
 */
public abstract class AbstractLogScheduler implements ConfigurableLogScheduler, KeyConfigurableLogScheduler, LogScheduler {
    /**
     * 日志服务容器，所有单体日志服务都将存放进容器管理。
     */
    protected static final List<LogPrintfService> LOG_PRINTF_SERVICE_LIST = new ArrayList<>();

    /**
     * 日志服务容器，可以个性化设置日志的调度方式，由<code>Key String</code>作为日志服务的标头。
     * <p>
     * 在将日志服务放进容器内时可以指定日志服务的归属范围。
     */
    protected static final Map<String, List<LogPrintfService>> LOG_PRINTF_SERVICE_MAP = new HashMap<>();

    /**
     * 获取对应前缀的服务列表
     * <p>
     * 如果不存在就会创建一个含有前缀的列表。
     * <p>所有添加服务都必须调用此方法，以保证列表唯一
     *
     * @param prefix 服务前缀
     * @return 服务列表，如果<code>prefix</code>传入为空，将会返回<code>null</code>
     */
    protected List<LogPrintfService> getListByPrefix(String prefix) {
        if (!StringUtils.hasText(prefix)) {
            return null;
        }
        if (!LOG_PRINTF_SERVICE_MAP.containsKey(prefix)) {
            ArrayList<LogPrintfService> logPrintfServices = new ArrayList<>();
            LOG_PRINTF_SERVICE_MAP.put(prefix, logPrintfServices);
        }
        return LOG_PRINTF_SERVICE_MAP.get(prefix);
    }
}
