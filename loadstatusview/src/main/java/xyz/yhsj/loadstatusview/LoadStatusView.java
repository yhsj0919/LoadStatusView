package xyz.yhsj.loadstatusview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import xyz.yhsj.loadstatusview.listener.OnStatusPageClickListener;


public class LoadStatusView extends FrameLayout {

    public static final String TAG = "LoadStatusView";

    public static final int VIEW_STATE_CONTENT = 0;

    public static final int VIEW_STATE_ERROR = 1;

    public static final int VIEW_STATE_EMPTY = 2;

    public static final int VIEW_STATE_LOADING = 3;

    private TextView tv_error;
    private TextView tv_empty;
    private TextView tv_loading;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_STATE_CONTENT, VIEW_STATE_ERROR, VIEW_STATE_EMPTY, VIEW_STATE_LOADING})
    public @interface ViewState {
    }

    private LayoutInflater mInflater;

    private View mContentView;

    private View mLoadingView;

    private View mErrorView;

    private View mEmptyView;

    private OnStatusPageClickListener listener;

    @ViewState
    private int mViewState = VIEW_STATE_CONTENT;

    public LoadStatusView(Context context) {
        this(context, null);
    }

    public LoadStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoadStateView);

        int loadingViewResId = typedArray.getResourceId(R.styleable.LoadStateView_loadingView, -1);
        if (loadingViewResId > -1) {
            mLoadingView = mInflater.inflate(loadingViewResId, this, false);
            addView(mLoadingView, mLoadingView.getLayoutParams());
        } else {
            mLoadingView = mInflater.inflate(R.layout.loading, this, false);
            tv_loading = (TextView) mLoadingView.findViewById(R.id.message_info);
            addView(mLoadingView, mLoadingView.getLayoutParams());
            bindEvent(mLoadingView);
        }

        int emptyViewResId = typedArray.getResourceId(R.styleable.LoadStateView_emptyView, -1);
        if (emptyViewResId > -1) {
            mEmptyView = mInflater.inflate(emptyViewResId, this, false);
            addView(mEmptyView, mEmptyView.getLayoutParams());
        } else {
            mEmptyView = mInflater.inflate(R.layout.empty, this, false);
            tv_empty = (TextView) mEmptyView.findViewById(R.id.message_info);
            addView(mEmptyView, mEmptyView.getLayoutParams());
            bindEvent(mEmptyView);
        }

        int errorViewResId = typedArray.getResourceId(R.styleable.LoadStateView_errorView, -1);
        if (errorViewResId > -1) {
            mErrorView = mInflater.inflate(errorViewResId, this, false);
            addView(mErrorView, mErrorView.getLayoutParams());
        } else {
            mErrorView = mInflater.inflate(R.layout.error, this, false);
            tv_error = (TextView) mErrorView.findViewById(R.id.message_info);
            addView(mErrorView, mErrorView.getLayoutParams());
            bindEvent(mErrorView);
        }

        int viewState = typedArray.getInt(R.styleable.LoadStateView_defState, VIEW_STATE_CONTENT);

        switch (viewState) {
            case VIEW_STATE_CONTENT:
                mViewState = VIEW_STATE_CONTENT;
                break;

            case VIEW_STATE_ERROR:
                mViewState = VIEW_STATE_ERROR;
                break;

            case VIEW_STATE_EMPTY:
                mViewState = VIEW_STATE_EMPTY;
                break;

            case VIEW_STATE_LOADING:
                mViewState = VIEW_STATE_LOADING;
                break;
        }

        typedArray.recycle();

    }


    /**
     * 绑定点击事件，只支持默认视图
     *
     * @param view
     */
    private void bindEvent(View view) {
        if (view != null) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {

                        switch (mViewState) {
                            case VIEW_STATE_EMPTY:
                                listener.onEmpty();
                                break;
                            case VIEW_STATE_ERROR:
                                listener.onError();
                                break;
                        }
                    }
                }
            });
        }

    }

    /**
     * 点击事件回调，只支持默认视图
     *
     * @param mListener
     */
    public void setOnStatusPageClickListener(OnStatusPageClickListener mListener) {
        this.listener = mListener;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) throw new IllegalArgumentException("Content view is not defined");
        setView();
    }


    @Override
    public void addView(View child) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }


    /**
     * 根据状态获取视图
     *
     * @param state
     * @return
     */
    @Nullable
    public View getView(@ViewState int state) {
        switch (state) {
            case VIEW_STATE_LOADING:
                return mLoadingView;

            case VIEW_STATE_CONTENT:
                return mContentView;

            case VIEW_STATE_EMPTY:
                return mEmptyView;

            case VIEW_STATE_ERROR:
                return mErrorView;

            default:
                return mContentView;
        }
    }


    /**
     * 获取当前状态
     *
     * @return
     */
    @ViewState
    public int getViewState() {
        return mViewState;
    }


    /**
     * 设置视图状态
     *
     * @param state
     */
    public void setViewState(@ViewState int state) {
        setViewState(state, null);
    }

    /**
     * 设置视图状态
     *
     * @param state
     */
    public void setViewState(@ViewState int state, String msg) {
        if (state != mViewState) {
            mViewState = state;

            switch (mViewState) {
                case VIEW_STATE_LOADING:

                    if (tv_loading != null && msg != null) {
                        tv_loading.setText(msg);
                    } else {
                        Log.e("TAG", "only default view can set message");
                    }
                    break;

                case VIEW_STATE_EMPTY:
                    if (tv_empty != null && msg != null) {
                        tv_empty.setText(msg);
                    } else {
                        Log.e("TAG", "only default view can set message");
                    }
                    break;

                case VIEW_STATE_ERROR:
                    if (tv_error != null && msg != null) {
                        tv_error.setText(msg);
                    } else {
                        Log.e("TAG", "only default view can set message");
                    }
                    break;
            }

            setView();
        }
    }


    private void setView() {
        switch (mViewState) {
            case VIEW_STATE_LOADING:
                if (mLoadingView == null) {
                    throw new NullPointerException("Loading View");
                }

                mLoadingView.setVisibility(View.VISIBLE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;

            case VIEW_STATE_EMPTY:
                if (mEmptyView == null) {
                    throw new NullPointerException("Empty View");
                }

                mEmptyView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                break;

            case VIEW_STATE_ERROR:
                if (mErrorView == null) {
                    throw new NullPointerException("Error View");
                }

                mErrorView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;

            case VIEW_STATE_CONTENT:
            default:
                if (mContentView == null) {
                    throw new NullPointerException("Content View");
                }

                mContentView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;
        }
    }


    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }
        return view != mLoadingView && view != mErrorView && view != mEmptyView;
    }


    /**
     * 设置视图，状态
     *
     * @param view
     * @param state
     * @param switchToState
     */
    public void setViewForState(View view, @ViewState int state, boolean switchToState) {
        switch (state) {
            case VIEW_STATE_LOADING:
                if (mLoadingView != null) removeView(mLoadingView);
                mLoadingView = view;
                addView(mLoadingView);
                break;

            case VIEW_STATE_EMPTY:
                if (mEmptyView != null) removeView(mEmptyView);
                mEmptyView = view;
                addView(mEmptyView);
                break;

            case VIEW_STATE_ERROR:
                if (mErrorView != null) removeView(mErrorView);
                mErrorView = view;
                addView(mErrorView);
                break;

            case VIEW_STATE_CONTENT:
                if (mContentView != null) removeView(mContentView);
                mContentView = view;
                addView(mContentView);
                break;
        }

        if (switchToState) setViewState(state);
    }


    /**
     * 设置视图状态
     *
     * @param view
     * @param state
     */
    public void setViewForState(View view, @ViewState int state) {
        setViewForState(view, state, false);
    }

    /**
     * 设置视图状态
     *
     * @param layoutRes
     * @param state
     * @param switchToState
     */
    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state, boolean switchToState) {
        if (mInflater == null) mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    /**
     * 设置视图状态
     *
     * @param layoutRes
     * @param state
     */
    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state) {
        setViewForState(layoutRes, state, false);
    }
}
