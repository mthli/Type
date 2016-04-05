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

import com.jakewharton.rxbinding.view.RxView;

import io.github.mthli.type.R;
import io.github.mthli.type.event.BlockEvent;
import io.github.mthli.type.util.RxBus;
import io.github.mthli.type.widget.model.TypeBlock;
import io.github.mthli.type.widget.text.KnifeText;
import rx.functions.Action1;

public class TypeBlockHolder extends RecyclerView.ViewHolder {
    private View quote;
    private View bullet;
    private KnifeText content;
    private TypeBlock type;

    public TypeBlockHolder(@NonNull View view) {
        super(view);
        this.quote = view.findViewById(R.id.quote);
        this.bullet = view.findViewById(R.id.bullet);
        this.content = (KnifeText) view.findViewById(R.id.content);
        bind();
    }

    public void inject(TypeBlock type) {
        this.type = type;
        quote.setVisibility(type.isQuote() ? View.VISIBLE : View.GONE);
        bullet.setVisibility(type.isBullet() ? View.VISIBLE : View.GONE);
    }

    private void bind() {
        RxView.focusChanges(content).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean focus) {
                if (!focus) {
                    return;
                }

                BlockEvent event = new BlockEvent();
                event.setBullet(type.isBullet());
                event.setQuote(type.isQuote());
                RxBus.getInstance().post(event);
            }
        });
    }
}
