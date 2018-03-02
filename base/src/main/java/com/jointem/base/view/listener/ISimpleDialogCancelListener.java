/*
 * Copyright 2013 Inmite s.r.o. (www.inmite.eu).
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

package com.jointem.base.view.listener;

/**
 * @author Kevin.Li
 * @ClassName: ISimpleDialogCancelListener
 * @Description:
 * @date 2015-10-20 下午3:24:03
 */
public interface ISimpleDialogCancelListener {
    void onCanceled(String tag);
}