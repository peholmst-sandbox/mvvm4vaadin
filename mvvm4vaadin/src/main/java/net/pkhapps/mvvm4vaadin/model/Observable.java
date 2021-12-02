/*
 * Copyright (c) 2021 Petter Holmström
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

package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;

import java.io.Serializable;

public interface Observable<EVENT> extends Serializable {

    default Registration addListener(SerializableConsumer<? super EVENT> listener) {
        return addListener(listener, true);
    }

    Registration addListener(SerializableConsumer<? super EVENT> listener, boolean fireInitialEvent);

    default void addWeakListener(SerializableConsumer<? super EVENT> listener) {
        addWeakListener(listener, true);
    }

    void addWeakListener(SerializableConsumer<? super EVENT> listener, boolean fireInitialEvent);
}
