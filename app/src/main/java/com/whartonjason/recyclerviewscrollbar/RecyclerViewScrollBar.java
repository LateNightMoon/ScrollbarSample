package com.whartonjason.recyclerviewscrollbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;


public class RecyclerViewScrollBar extends View {

    private RecyclerView mRecyclerView;

    private int mWidth;

    private int mHeight;

    private Drawable mTrackHorizontal;

    private Drawable mThumbHorizontal;

    private Rect mTrackRect = new Rect();

    private Rect mThumbRect = new Rect();

    private float mThumbScale = 0.00F;

    private float mScrollScale = 0.00F;

    private final int SCROLL_LOCATION_START = 1;

    private final int SCROLL_LOCATION_END = 2;

    private final int SCROLL_LOCATION_SCROLL = 3;
    /**
     * View的当前位置,起点 终点 或滚动中
     */
    private int mScrollLocation = SCROLL_LOCATION_START;

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


    public RecyclerViewScrollBar(Context context) {
        this(context, null);
    }

    public RecyclerViewScrollBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("Recycle")
    public RecyclerViewScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewScrollBar);
        mTrackHorizontal = array.getDrawable(R.styleable.RecyclerViewScrollBar_scrollbarTrackHorizontal);
        mThumbHorizontal = array.getDrawable(R.styleable.RecyclerViewScrollBar_scrollbarThumbHorizontal);
    }

    public void bindRecyclerView(@Nullable RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return;
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            setupCallbacks();
        }
    }

    private void setupCallbacks() {
        mRecyclerView.addOnScrollListener(mScrollListener);
        //监听View的视图树变化,防止初始化未获取到滚动条比例
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                computeScrollScale();
            }
        });
        computeScrollScale();
    }

    public void computeScrollScale() {
        if (mRecyclerView == null) return;
        //RecyclerView已显示宽度
        float mScrollExtent = mRecyclerView.computeHorizontalScrollExtent();
        //RecyclerView实际宽度
        float mScrollRange = mRecyclerView.computeHorizontalScrollRange();
        if (mScrollRange != 0 && mScrollExtent != 0) {
            mThumbScale = mScrollExtent / mScrollRange;
        }
        //RecyclerView已经滚动的距离
        float mScrollOffset = mRecyclerView.computeHorizontalScrollOffset();
        if (mScrollOffset != 0 && mScrollRange != 0) {
            mScrollScale = mScrollOffset / mScrollRange;
        }
        //RecyclerView可以滚动的距离
        float mCanScrollDistance = mScrollRange - mScrollExtent;
        if (mScrollOffset == 0) {
            mScrollLocation = SCROLL_LOCATION_START;
        } else if (mCanScrollDistance == mScrollOffset) {
            mScrollLocation = SCROLL_LOCATION_END;
        } else {
            mScrollLocation = SCROLL_LOCATION_SCROLL;
        }
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measureHandler(widthMeasureSpec);
        mHeight = measureHandler(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
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
        if (mTrackHorizontal != null) {
            mTrackRect.set(0, 0, mWidth, mHeight);
            mTrackHorizontal.setBounds(mTrackRect);
            mTrackHorizontal.draw(canvas);
        }
        //left坐标
        int mThumbLeft = (int) (mScrollScale * mWidth);
        //right坐标
        int mThumbRight = (int) (mThumbLeft + mWidth * mThumbScale);
        //添加状态是为了修复滚动差距
        if (mThumbHorizontal != null) {
            switch (mScrollLocation) {
                case SCROLL_LOCATION_START:
                    mThumbRect.set(0, 0, mThumbRight, mHeight);
                    break;
                case SCROLL_LOCATION_SCROLL:
                    mThumbRect.set(mThumbLeft, 0, mThumbRight, mHeight);
                    break;
                case SCROLL_LOCATION_END:
                    mThumbRect.set(mThumbLeft, 0, mWidth, mHeight);
                    break;
            }
            mThumbHorizontal.setBounds(mThumbRect);
            mThumbHorizontal.draw(canvas);
        }
    }
}
