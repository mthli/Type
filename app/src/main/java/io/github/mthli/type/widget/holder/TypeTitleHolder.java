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

package io.github.mthli.type.widget.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

import io.github.mthli.type.R;
import io.github.mthli.type.widget.model.TypeTitle;
import rx.functions.Action1;

public class TypeTitleHolder extends RecyclerView.ViewHolder {
    private AppCompatEditText title;
    private TypeTitle type;

    public TypeTitleHolder(@NonNull View view) {
        super(view);
        this.title = (AppCompatEditText) view.findViewById(R.id.title);
        bind();
    }

    public void inject(TypeTitle type) {
        this.type = type;
        title.setText(type.getTitle());
    }

    private void bind() {
        RxTextView.afterTextChangeEvents(title).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent event) {
                type.setTitle(event.editable().toString());
            }
        });
    }
}
