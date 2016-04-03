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

package io.github.mthli.type.app;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.LinkedList;
import java.util.List;

import io.github.mthli.type.R;
import io.github.mthli.type.widget.adapter.TypeAdapter;
import io.github.mthli.type.widget.model.Type;
import io.github.mthli.type.widget.model.TypeBlock;
import io.github.mthli.type.widget.model.TypeTitle;

public class MainActivity extends RxAppCompatActivity {
    private RecyclerView recycler;
    private TypeAdapter adapter;
    private List<Type> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecycler();
    }

    private void setupRecycler() {
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        list = new LinkedList<>();
        list.add(new TypeTitle(null));
        list.add(new TypeBlock(null));
        list.add(new TypeBlock(null));

        adapter = new TypeAdapter(this, list);
        recycler.setAdapter(adapter);
    }
}
