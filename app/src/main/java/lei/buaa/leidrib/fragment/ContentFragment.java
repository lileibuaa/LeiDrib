package lei.buaa.leidrib.fragment;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import lei.buaa.leidrib.R;
import lei.buaa.leidrib.adapter.ContentAdapter;
import lei.buaa.leidrib.bean.Shot;
import lei.buaa.leidrib.config.KeyConfig;
import lei.buaa.leidrib.config.ResponseCode;
import lei.buaa.leidrib.retrofit.IRQShotList;
import lei.buaa.leidrib.utils.DaoUtils;
import lei.buaa.leidrib.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lei on 3/21/16.
 * email: lileibh@gmail.com
 */
public class ContentFragment extends BaseFragment {
    @Bind(R.id.rcv_content)
    RecyclerView mRcvContent;
    @Bind(R.id.srl_container)
    SwipeRefreshLayout mSrlContainer;
    @BindDimen(R.dimen.dimen_15)
    int mDimen15;
    @BindDimen(R.dimen.dimen_5)
    int mDimen5;
    @BindDimen(R.dimen.dimen_80)
    int mDimen80;
    private List<Shot> mShots;
    private ContentAdapter mAdapter;
    private int mPageNum;
    private boolean mLoadMore;
    private String mType;
    private int mCircleOffset, mCircleEndOffset;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, content);
        return content;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null)
            mType = getArguments().getString(KeyConfig.SHOT_TYPE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mCircleOffset = (int) (metrics.density * 64);//default offset in dips found in SwipeRefreshLayout
        mCircleEndOffset = metrics.heightPixels - mDimen80;
        mLoadMore = false;
        mSrlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum = 1;
                loadShots(true);
            }
        });
        mShots = new ArrayList<>();
        mAdapter = new ContentAdapter(getContext(), mShots);
//        mRcvContent.setItemAnimator(new CustomAnimator());
        mRcvContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.left = mDimen15;
                    outRect.right = mDimen5;
                } else {
                    outRect.left = mDimen5;
                    outRect.right = mDimen15;
                }
                outRect.top = 0;
                outRect.bottom = 0;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
            }
        });
        mRcvContent.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRcvContent.setAdapter(mAdapter);
        mRcvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && !mSrlContainer.isRefreshing()
                        && layoutManager.findLastCompletelyVisibleItemPosition()
                        == layoutManager.getItemCount() - 1) {
                    loadMoreShots();
                } else if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    mSrlContainer.setProgressViewEndTarget(false, mCircleOffset);
                }
            }
        });
        mSrlContainer.post(new Runnable() {
            @Override
            public void run() {
                mSrlContainer.setRefreshing(true);
                mPageNum = 1;
                loadShots(true);
            }
        });
    }

    private void loadMoreShots() {
        if (!mLoadMore) {
            Toast.makeText(getContext(), R.string.can_not_load_more, Toast.LENGTH_SHORT).show();
        } else {
            mSrlContainer.setProgressViewEndTarget(false, mCircleEndOffset);
            mSrlContainer.setRefreshing(true);
            mPageNum++;
            loadShots(false);
        }
    }

    private void loadShots(final boolean clearData) {
        RetrofitUtils.getRetrofit().create(IRQShotList.class)
                .loadShotList(mType, mPageNum)
                .enqueue(new Callback<List<Shot>>() {
                    @Override
                    public void onResponse(Call<List<Shot>> call, Response<List<Shot>> response) {
                        mSrlContainer.setRefreshing(false);
                        if (response.code() == ResponseCode.STATUS_OK) {
                            String headerLink = response.headers().get("link");
                            if (headerLink.contains("next"))
                                mLoadMore = true;
                            else mLoadMore = false;
                            DaoUtils.setShots(response.body());
                            if (clearData)
                                mShots.clear();
                            mShots.addAll(response.body());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), R.string.load_fail, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Shot>> call, Throwable t) {
                        mSrlContainer.setRefreshing(false);
                        Toast.makeText(getContext(), R.string.load_fail, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}