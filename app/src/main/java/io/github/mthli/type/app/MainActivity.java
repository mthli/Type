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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.mthli.type.R;
import io.github.mthli.type.event.BlockEvent;
import io.github.mthli.type.event.BoldEvent;
import io.github.mthli.type.event.BulletEvent;
import io.github.mthli.type.event.DeleteEvent;
import io.github.mthli.type.event.DotsEvent;
import io.github.mthli.type.event.ImageEvent;
import io.github.mthli.type.event.InsertEvent;
import io.github.mthli.type.event.FormatEvent;
import io.github.mthli.type.event.ItalicEvent;
import io.github.mthli.type.event.QuoteEvent;
import io.github.mthli.type.event.StrikethroughEvent;
import io.github.mthli.type.event.UnderlineEvent;
import io.github.mthli.type.util.ImageUtils;
import io.github.mthli.type.util.RxBus;
import io.github.mthli.type.widget.StatusImageButton;
import io.github.mthli.type.widget.adapter.TypeAdapter;
import io.github.mthli.type.widget.model.Type;
import io.github.mthli.type.widget.model.TypeBlock;
import io.github.mthli.type.widget.model.TypeDots;
import io.github.mthli.type.widget.model.TypeImage;
import io.github.mthli.type.widget.model.TypeTitle;
import io.github.mthli.type.widget.text.KnifeText;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity implements View.OnClickListener, View.OnLongClickListener,
        RecyclerView.OnChildAttachStateChangeListener {

    private static final long SYSTEM_UI_DELAY = 1000l; // ms
    private static final int REQUEST_IMAGE = 0x01;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TypeAdapter typeAdapter;
    private List<Type> typeList;
    private int targetPosition = -1;

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
        setupReactiveX();
    }

    private void setupRootLayout() {
        RxView.layoutChangeEvents(findViewById(R.id.root))
                .delay(SYSTEM_UI_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
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

        typeAdapter = new TypeAdapter(this, typeList);
        recyclerView.setAdapter(typeAdapter);
        recyclerView.setItemAnimator(null);
        recyclerView.addOnChildAttachStateChangeListener(this);
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

    private void setupReactiveX() {
        RxBus.getInstance().toObservable(DeleteEvent.class)
                .subscribe(new Action1<DeleteEvent>() {
                    @Override
                    public void call(DeleteEvent event) {
                        onDeleteEvent(event);
                    }
                });

        RxBus.getInstance().toObservable(InsertEvent.class)
                .subscribe(new Action1<InsertEvent>() {
                    @Override
                    public void call(InsertEvent event) {
                        onInsertEvent(event);
                    }
                });
    }

    private void onInsertEvent(InsertEvent event) {
        int position = event.getPosition();
        if (position < 1 || position >= typeList.size()) {
            return;
        }

        switch (event.getType()) {
            case Type.TYPE_BLOCK:
                onInsertBlock(event);
                break;
            case Type.TYPE_DOTS:
                onInsertDots(event);
                break;
            case Type.TYPE_IMAGE:
                onInsertImage(event);
                break;
            default:
                return;
        }

        int first = layoutManager.findFirstCompletelyVisibleItemPosition();
        int last = layoutManager.findLastCompletelyVisibleItemPosition();
        if (targetPosition > last) {
            recyclerView.scrollToPosition(targetPosition);
        } else if (first <= targetPosition && targetPosition <= last) {
            recyclerView.scrollToPosition(first);
        }
    }

    private void onInsertBlock(InsertEvent event) {
        targetPosition = event.getPosition() + 1;
        typeList.add(targetPosition, new TypeBlock(event.getSuffix()));
        typeAdapter.notifyItemInserted(targetPosition);
    }

    private void onInsertDots(InsertEvent event) {
        if (event.getPrefix().length() <= 0) {
            targetPosition = event.getPosition();
            typeList.set(targetPosition, new TypeDots());
            typeList.add(++targetPosition, new TypeBlock(event.getSuffix()));
            typeAdapter.notifyDataSetChanged();
        } else {
            targetPosition = event.getPosition() + 1;
            typeList.add(targetPosition, new TypeDots());
            typeList.add(++targetPosition, new TypeBlock(event.getSuffix()));
            typeAdapter.notifyItemRangeInserted(event.getPosition() + 1, 2);
        }
    }

    private void onInsertImage(InsertEvent event) {
        if (event.getPrefix().length() <= 0) {
            targetPosition = event.getPosition();
            typeList.set(targetPosition, new TypeImage(event.getBitmap()));
            typeList.add(++targetPosition, new TypeBlock(event.getSuffix()));
            typeAdapter.notifyDataSetChanged();
        } else {
            targetPosition = event.getPosition() + 1;
            typeList.add(targetPosition, new TypeImage(event.getBitmap()));
            typeList.add(++targetPosition, new TypeBlock(event.getSuffix()));
            typeAdapter.notifyItemRangeInserted(event.getPosition() + 1, 2);
        }
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        if (layoutManager.getPosition(view) == targetPosition) {
            targetPosition = -1;

            KnifeText knifeText = (KnifeText) view.findViewById(R.id.content);
            if (knifeText != null) {
                knifeText.requestFocus();
                knifeText.setSelection(0);
            } else {
                view.requestFocus();
            }
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        // DO NOTHING HERE
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.clearOnChildAttachStateChangeListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_IMAGE) {
            return;
        }

        if (resultCode != RESULT_OK || data == null || data.getData() == null) {
            Toast.makeText(this, R.string.toast_attachment_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            insertImageAsync(bitmap);
        } catch (IOException i) {
            Toast.makeText(this, R.string.toast_attachment_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void insertImageAsync(final Bitmap bitmap) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap fix = ImageUtils.fixBitmap(MainActivity.this, bitmap);
                Drawable inset = ImageUtils.insetBitmap(MainActivity.this, fix);
                Bitmap result = ImageUtils.drawable2Bitmap(inset);
                subscriber.onNext(result);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap result) {
                RxBus.getInstance().post(new ImageEvent(result));
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
            onClickAttachment();
        } else if (view == dotsButton) {
            RxBus.getInstance().post(new DotsEvent());
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

    private void onClickAttachment() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_IMAGE);
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

    // TODO ========================================================================================

    private void onDeleteEvent(DeleteEvent event) {
        int position = event.getPosition();
        if (position <= 1 || position >= typeList.size()) {
            return;
        }

        switch (event.getType()) {
            case Type.TYPE_BLOCK:
                onDeleteBlock(event);
                break;
            case Type.TYPE_DOTS:
                onDeleteDots(event);
                break;
            case Type.TYPE_IMAGE:
                onDeleteImage(event);
                break;
            default:
                return;
        }

        // TODO
    }

    private void onDeleteBlock(DeleteEvent event) {

    }

    private void onDeleteDots(DeleteEvent event) {

    }

    private void onDeleteImage(DeleteEvent event) {

    }
}
