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

package com.cgglyle.boson.graviton.exception;

/**
 * 日志容器上下文异常
 *
 * @author lyle
 * @since 2022/09/14
 */
public class LogContextException extends RuntimeException{
    public LogContextException(String message) {
        super(message);
    }

    public LogContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogContextException(Throwable cause) {
        super(cause);
    }

    public LogContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
