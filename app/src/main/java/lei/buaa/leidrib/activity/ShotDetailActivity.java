package lei.buaa.leidrib.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lei.buaa.leidrib.R;
import lei.buaa.leidrib.adapter.ShotDetailAdapter;
import lei.buaa.leidrib.bean.Comment;
import lei.buaa.leidrib.bean.Shot;
import lei.buaa.leidrib.config.KeyConfig;
import lei.buaa.leidrib.config.ResponseCode;
import lei.buaa.leidrib.retrofit.IRQCommentList;
import lei.buaa.leidrib.ui.ProgressCircleDrawable;
import lei.buaa.leidrib.utils.DaoUtils;
import lei.buaa.leidrib.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lei on 3/21/16.
 * email: lileibh@gmail.com
 */
public class ShotDetailActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rcv_content)
    RecyclerView mRcvContent;
    @Bind(R.id.iv_content)
    SimpleDraweeView mIvContent;
    private ShotDetailAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        long shotId = getIntent().getLongExtra(KeyConfig.SHOT_ID, 0l);
        Shot shot = DaoUtils.getShot(shotId);
        mRcvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShotDetailAdapter(this, shot);
        mRcvContent.setAdapter(mAdapter);
        if (shot.getImages() != null) {
            String url = shot.getImages().getHidpi();
            if (TextUtils.isEmpty(url))
                url = shot.getImages().getNormal();
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setProgressBarImage(new ProgressCircleDrawable(this))
                    .build();
            mIvContent.setHierarchy(hierarchy);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(url)
                    .setAutoPlayAnimations(true)
                    .build();
            mIvContent.setController(controller);
        }
        loadComment(shot);
    }

    private void loadComment(final Shot shot) {
        RetrofitUtils.getRetrofit().create(IRQCommentList.class)
                .loadComments(shot.getId(), 100)
                .enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                        if (response.code() == ResponseCode.STATUS_OK
                                && response.body() != null
                                && response.body().size() > 0) {
                            DaoUtils.setComments(response.body(), shot.getId());
                            shot.resetCommentList();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ShotDetailActivity.this, R.string.can_not_load_more, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                        Toast.makeText(ShotDetailActivity.this, R.string.can_not_load_more, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
