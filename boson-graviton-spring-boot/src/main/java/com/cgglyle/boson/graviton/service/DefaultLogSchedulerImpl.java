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

import com.cgglyle.boson.graviton.api.LogPrintfService;
import com.cgglyle.boson.graviton.model.LogInfo;

import java.util.Collection;
import java.util.List;

/**
 * 默认日志调度控制器
 *
 * @author lyle
 * @since 2022/09/12
 */
public class DefaultLogSchedulerImpl extends AbstractLogScheduler {

    public DefaultLogSchedulerImpl() {
    }

    public DefaultLogSchedulerImpl(Collection<LogPrintfService> servicesList){
        LOG_PRINTF_SERVICE_LIST.addAll(servicesList);
    }

    public DefaultLogSchedulerImpl(LogPrintfService service){
        LOG_PRINTF_SERVICE_LIST.add(service);
    }

    @Override
    public void addPrintfService(LogPrintfService service) {
        LOG_PRINTF_SERVICE_LIST.add(service);
    }

    @Override
    public void addPrintfService(Collection<LogPrintfService> servicesList) {
        LOG_PRINTF_SERVICE_LIST.addAll(servicesList);
    }

    @Override
    public void deletedPrintfService(LogPrintfService service) {
        LOG_PRINTF_SERVICE_LIST.remove(service);
    }

    @Override
    public List<LogPrintfService> printfServiceList() {
        return LOG_PRINTF_SERVICE_LIST;
    }

    @Override
    public void startPrintf(LogInfo info, boolean isAsync) {
        for (LogPrintfService logPrintfService : LOG_PRINTF_SERVICE_LIST) {
            if (isAsync) {
                logPrintfService.asyncLog(info);
            } else {
                logPrintfService.log(info);
            }
        }
    }

    @Override
    public void addPrintfService(String prefix, LogPrintfService service) {
        List<LogPrintfService> listByPrefix = getListByPrefix(prefix);
        listByPrefix.add(service);
    }

    @Override
    public void addPrintfService(String prefix, Collection<LogPrintfService> servicesList) {
        List<LogPrintfService> listByPrefix = getListByPrefix(prefix);
        listByPrefix.addAll(servicesList);
    }

    @Override
    public void deletedPrintfService(String prefix, LogPrintfService service) {
        List<LogPrintfService> listByPrefix = getListByPrefix(prefix);
        listByPrefix.remove(service);
    }
}
