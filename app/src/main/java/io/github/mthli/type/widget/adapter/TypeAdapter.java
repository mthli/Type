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

package io.github.mthli.type.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.github.mthli.type.R;
import io.github.mthli.type.widget.holder.TypeBlockHolder;
import io.github.mthli.type.widget.model.Type;

public class TypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Type> list;

    public TypeAdapter(@NonNull Context context, @NonNull List<Type> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @Type.TypeValue int viewType) {
        if (viewType == Type.TYPE_IMAGE) {
            return null;
        } else if (viewType == Type.TYPE_TITLE) {
            return null;
        } else {
            return new TypeBlockHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_block, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
