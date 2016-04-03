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

import android.support.annotation.Nullable;
import android.text.Spanned;

import io.github.mthli.type.widget.model.Type;

public class TypeBlock extends Type {
    private Spanned content;

    private boolean isBullet;
    private boolean isList;
    private boolean isQuote;

    public TypeBlock(@Nullable Spanned content) {
        super(TYPE_TEXT);
        this.content = content;
    }

    public Spanned getContent() {
        return content;
    }

    public void setContent(@Nullable Spanned content) {
        this.content = content;
    }

    public boolean isBullet() {
        return isBullet;
    }

    public void setBullet(boolean isBullet) {
        this.isBullet = isBullet;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean isList) {
        this.isList = isList;
    }

    public boolean isQuote() {
        return isQuote;
    }

    public void setQuote(boolean isQuote) {
        this.isQuote = isQuote;
    }
}
