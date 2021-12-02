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

public abstract class AbstractObservable<EVENT> implements Observable<EVENT> {

    private ListenerCollection<EVENT> listeners;

    protected ListenerCollection<EVENT> getListeners() {
        if (listeners == null) {
            listeners = new ListenerCollection<>(this);
        }
        return listeners;
    }

    protected void fireEvent(EVENT event) {
        if (listeners != null) {
            listeners.fireEvent(event);
        }
    }
}
