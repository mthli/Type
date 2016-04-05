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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewLayoutChangeEvent;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.mthli.type.R;
import io.github.mthli.type.event.BlockEvent;
import io.github.mthli.type.event.BoldEvent;
import io.github.mthli.type.event.BulletEvent;
import io.github.mthli.type.event.FormatEvent;
import io.github.mthli.type.event.ItalicEvent;
import io.github.mthli.type.event.QuoteEvent;
import io.github.mthli.type.event.StrikethroughEvent;
import io.github.mthli.type.event.UnderlineEvent;
import io.github.mthli.type.util.RxBus;
import io.github.mthli.type.widget.StatusImageButton;
import io.github.mthli.type.widget.adapter.TypeAdapter;
import io.github.mthli.type.widget.model.Type;
import io.github.mthli.type.widget.model.TypeBlock;
import io.github.mthli.type.widget.model.TypeTitle;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends RxAppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final long SYSTEM_UI_DELAY = 1l;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TypeAdapter typeAdapter;
    private List<Type> typeList;

    private LinearLayoutCompat controlPanel;
    private StatusImageButton bulletButton;
    private StatusImageButton quoteButton;
    private AppCompatImageButton attachmentButton;
    private AppCompatImageButton dotsButton;
    private AppCompatImageButton playButton;

    private LinearLayoutCompat stylePanel;
    private StatusImageButton boldButton;
    private StatusImageButton italicButton;
    private StatusImageButton underlineButton;
    private StatusImageButton strikethroughButton;
    private StatusImageButton linkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRootLayout();
        setupRecyclerView();
        setupControlPanel();
        setupStylePanel();
    }

    private void setupRootLayout() {
        RxView.layoutChangeEvents(findViewById(R.id.root))
                .delay(SYSTEM_UI_DELAY, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .compose(this.<ViewLayoutChangeEvent>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<ViewLayoutChangeEvent>() {
                    @Override
                    public void call(ViewLayoutChangeEvent viewLayoutChangeEvent) {
                        int oldHeight = viewLayoutChangeEvent.oldBottom() - viewLayoutChangeEvent.oldTop();
                        int newHeight = viewLayoutChangeEvent.bottom() - viewLayoutChangeEvent.top();
                        if (newHeight > oldHeight) {
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                        }
                    }
                });
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // TODO
        typeList = new LinkedList<>();
        typeList.add(new TypeTitle(null));
        typeList.add(new TypeBlock(null));
        typeList.add(new TypeBlock(null));

        typeAdapter = new TypeAdapter(this, typeList);
        recyclerView.setAdapter(typeAdapter);
    }

    private void setupControlPanel() {
        controlPanel = (LinearLayoutCompat) findViewById(R.id.control);
        bulletButton = (StatusImageButton) findViewById(R.id.bullet);
        quoteButton = (StatusImageButton) findViewById(R.id.quote);
        attachmentButton = (AppCompatImageButton) findViewById(R.id.attachment);
        dotsButton = (AppCompatImageButton) findViewById(R.id.dots);
        playButton = (AppCompatImageButton) findViewById(R.id.play);

        bulletButton.setOnClickListener(this);
        quoteButton.setOnClickListener(this);
        attachmentButton.setOnClickListener(this);
        dotsButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

        bulletButton.setOnLongClickListener(this);
        quoteButton.setOnLongClickListener(this);
        attachmentButton.setOnLongClickListener(this);
        dotsButton.setOnLongClickListener(this);
        playButton.setOnLongClickListener(this);

        RxBus.getInstance().toObservable(BlockEvent.class)
                .subscribe(new Action1<BlockEvent>() {
                    @Override
                    public void call(BlockEvent event) {
                        bulletButton.setActivated(event.isBullet());
                        quoteButton.setActivated(event.isQuote());
                    }
                });
    }

    private void setupStylePanel() {
        stylePanel = (LinearLayoutCompat) findViewById(R.id.style);
        boldButton = (StatusImageButton) findViewById(R.id.bold);
        italicButton = (StatusImageButton) findViewById(R.id.italic);
        underlineButton = (StatusImageButton) findViewById(R.id.underline);
        strikethroughButton = (StatusImageButton) findViewById(R.id.strikethrough);
        linkButton = (StatusImageButton) findViewById(R.id.link);

        boldButton.setOnClickListener(this);
        italicButton.setOnClickListener(this);
        underlineButton.setOnClickListener(this);
        strikethroughButton.setOnClickListener(this);
        linkButton.setOnClickListener(this);

        boldButton.setOnLongClickListener(this);
        italicButton.setOnLongClickListener(this);
        underlineButton.setOnLongClickListener(this);
        strikethroughButton.setOnLongClickListener(this);
        linkButton.setOnLongClickListener(this);

        RxBus.getInstance().toObservable(FormatEvent.class)
                .subscribe(new Action1<FormatEvent>() {
                    @Override
                    public void call(FormatEvent event) {
                        boldButton.setActivated(event.isBold());
                        italicButton.setActivated(event.isItalic());
                        underlineButton.setActivated(event.isUnderline());
                        strikethroughButton.setActivated(event.isStrikethrough());
                        linkButton.setActivated(event.isLink());
                    }
                });
    }

    @Override
    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        switchToStylePanel();
    }

    @Override
    public void onActionModeFinished(ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        switchToControlPanel();
    }

    private void switchToControlPanel() {
        stylePanel.animate().alpha(0.0f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        stylePanel.setAlpha(1.0f);
                        stylePanel.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        stylePanel.setAlpha(0.0f);
                        stylePanel.setVisibility(View.GONE);
                    }
                }).start();

        controlPanel.animate().alpha(1.0f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        controlPanel.setAlpha(0.0f);
                        controlPanel.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd (Animator animator){
                        controlPanel.setAlpha(1.0f);
                        controlPanel.setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    private void switchToStylePanel() {
        controlPanel.animate().alpha(0.0f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        controlPanel.setAlpha(1.0f);
                        controlPanel.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        controlPanel.setAlpha(0.0f);
                        controlPanel.setVisibility(View.GONE);
                    }
                }).start();

        stylePanel.animate().alpha(1.0f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        stylePanel.setAlpha(0.0f);
                        stylePanel.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        stylePanel.setAlpha(1.0f);
                        stylePanel.setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    @Override
    public void onClick(View view) {
        if (view == bulletButton) {
            RxBus.getInstance().post(new BulletEvent());
        } else if (view == quoteButton) {
            RxBus.getInstance().post(new QuoteEvent());
        } else if (view == attachmentButton) {
            // TODO
        } else if (view == dotsButton) {
            // TODO
        } else if (view == playButton) {
            // TODO
        } else if (view == boldButton) {
            RxBus.getInstance().post(new BoldEvent());
        } else if (view == italicButton) {
            RxBus.getInstance().post(new ItalicEvent());
        } else if (view == underlineButton) {
            RxBus.getInstance().post(new UnderlineEvent());
        } else if (view == strikethroughButton) {
            RxBus.getInstance().post(new StrikethroughEvent());
        } else if (view == linkButton) {
            // TODO
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int stringResId = 0;

        if (view == bulletButton) {
            stringResId = R.string.toast_bullet;
        } else if (view == quoteButton) {
            stringResId = R.string.toast_quote;
        } else if (view == attachmentButton) {
            stringResId = R.string.toast_attachment;
        } else if (view == dotsButton) {
            stringResId = R.string.toast_dots;
        } else if (view == playButton) {
            stringResId = R.string.toast_play;
        } else if (view == boldButton) {
            stringResId = R.string.toast_bold;
        } else if (view == italicButton) {
            stringResId = R.string.toast_italic;
        } else if (view == underlineButton) {
            stringResId = R.string.toast_underline;
        } else if (view == strikethroughButton) {
            stringResId = R.string.toast_strikethrough;
        } else if (view == linkButton) {
            stringResId = R.string.toast_link;
        }

        if (stringResId != 0) {
            Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
