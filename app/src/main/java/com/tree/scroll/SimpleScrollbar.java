package com.tree.scroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.math.BigDecimal;

/**
 * @author whartonjason
 */
public class SimpleScrollbar extends View {

    private static final int START = 0x1;

    private static final int END = START << 1;

    private static final int SCROLLING = START << 2;

    private int mScrollState = START;

    private Rect trackRect = new Rect();

    private Rect thumbRect = new Rect();

    /**
     * 轨迹 background
     */
    private Drawable trackBackground;

    /**
     * 指示 background
     */
    private Drawable thumbBackground;

    private float thumbScale = 0f;

    private float scrollScale = 0f;

    private RecyclerView mRecyclerView;

    private final RecyclerView.OnScrollListener mScrollListener =
            new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    computeScrollScale();
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    computeScrollScale();
                    postInvalidate();
                }
            };


    public SimpleScrollbar(Context context) {
        this(context, null);
    }

    public SimpleScrollbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("Recycle")
    public SimpleScrollbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SimpleScrollbar);
        trackBackground = array.getDrawable(R.styleable.SimpleScrollbar_trackBackground);
        thumbBackground = array.getDrawable(R.styleable.SimpleScrollbar_thumbBackground);
        array.recycle();
    }

    public void bindRecyclerView(@Nullable RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return;
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            setCallbacks();
        }
    }

    private void setCallbacks() {
        mRecyclerView.addOnScrollListener(mScrollListener);
        //view tree 监听
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                computeScrollScale();
            }
        });
        computeScrollScale();
    }

    public void computeScrollScale() {
        if (mRecyclerView == null) {
            return;
        }
        //Already show extent.
        float scrollExtent = mRecyclerView.computeHorizontalScrollExtent();
        //View max width.
        float scrollRange = mRecyclerView.computeHorizontalScrollRange();
        if (scrollRange != 0 && scrollExtent != 0) {
            thumbScale = scrollExtent / scrollRange;
        }
        float offset = mRecyclerView.computeHorizontalScrollOffset();
        if (offset != 0 && scrollRange != 0) {
            scrollScale = offset / scrollRange;
        }
        float toEndExtent = scrollRange - scrollExtent;
        if (offset == 0) {
            mScrollState = START;
        } else if (BigDecimal.valueOf(toEndExtent).equals(BigDecimal.valueOf(offset))) {
            mScrollState = END;
        } else {
            mScrollState = SCROLLING;
        }
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureHandler(widthMeasureSpec), measureHandler(heightMeasureSpec));
    }

    private int measureHandler(int measureSpec) {
        int result = 0;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = Math.max(size, result);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (trackBackground != null) {
            trackRect.set(0, 0, width, height);
            trackBackground.setBounds(trackRect);
            trackBackground.draw(canvas);
        }
        int thumbLeft = (int) (scrollScale * width);
        int thumbRight = (int) (thumbLeft + width * thumbScale);
        //添加 state 是修复滚动计算差
        if (thumbBackground != null) {
            switch (mScrollState) {
                case START:
                    thumbRect.set(0, 0, thumbRight, height);
                    break;
                case SCROLLING:
                    thumbRect.set(thumbLeft, 0, thumbRight, height);
                    break;
                case END:
                    thumbRect.set(thumbLeft, 0, width, height);
                    break;
                default:
            }
            thumbBackground.setBounds(thumbRect);
            thumbBackground.draw(canvas);
        }
    }

}
