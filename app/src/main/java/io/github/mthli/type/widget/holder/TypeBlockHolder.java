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
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.mthli.type.R;
import io.github.mthli.type.widget.text.KnifeText;

public class TypeBlockHolder extends RecyclerView.ViewHolder {
    private View quote;
    private View bullet;
    private View space;
    private KnifeText content;

    public TypeBlockHolder(@NonNull View view) {
        super(view);
        this.quote = view.findViewById(R.id.quote);
        this.bullet = view.findViewById(R.id.bullet);
        this.space = view.findViewById(R.id.space);
        this.content = (KnifeText) view.findViewById(R.id.content);
    }
}
