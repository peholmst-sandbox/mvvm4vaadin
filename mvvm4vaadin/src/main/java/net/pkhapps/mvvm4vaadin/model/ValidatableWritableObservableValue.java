/*
 * Copyright (c) 2023 Petter Holmström
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

/**
 * An extended version of {@link WritableObservableValue} that supports validation. If an invalid value is offered to
 * {@link #setValue(Object)}, the value is not changed and {@link #isInvalid()} becomes true.
 *
 * @param <T> the type of the value contained inside the observable value.
 */
public interface ValidatableWritableObservableValue<T> extends WritableObservableValue<T> {

    /**
     * Checks if an invalid value has been offered to {@link #setValue(Object)} or not.
     *
     * @return true if the value was invalid, false if it was valid.
     */
    boolean isInvalid();

    /**
     * An observable value containing the value of {@link #isInvalid()}.
     *
     * @return an observable value containing true if the value is invalid and false if the value is valid.
     */
    ObservableValue<Boolean> invalid();

    /**
     * The error message to show to the user if the field is {@link #isInvalid() invalid}. If no error message has been
     * set, or the field is valid, this method always returns {@code null}.
     *
     * @return the error message or {@code null}.
     */
    String getErrorMessage();

    /**
     * An observable value containing the value of {@link #getErrorMessage()}.
     *
     * @return an observable value containing the error message if the field is invalid.
     */
    ObservableValue<String> errorMessage();
}
