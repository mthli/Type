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

package io.github.mthli.type.widget.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Type {
    public static final int TYPE_IMAGE = 0x100;
    public static final int TYPE_TEXT = 0x101;
    public static final int TYPE_TITLE = 0x102;

    @IntDef({TYPE_IMAGE, TYPE_TEXT, TYPE_TITLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TypeValue {}

    private int type;

    public Type(@TypeValue int type) {
        this.type = type;
    }

    public final int getType() {
        return type;
    }
}
