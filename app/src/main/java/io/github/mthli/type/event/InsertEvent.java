/*
 * Copyright 2016 Matthew Lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.github.mthli.type.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;

import io.github.mthli.type.widget.model.Type;

public class InsertEvent {
    private int type;
    private int position;
    private Spanned content;

    public InsertEvent(@Type.TypeValue int type, int position, @Nullable Spanned content) {
        this.type = type;
        this.position = position;
        this.content = content != null ? content : new SpannableString("");
    }

    @Type.TypeValue
    public int getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    @NonNull
    public Spanned getContent() {
        return content;
    }
}
