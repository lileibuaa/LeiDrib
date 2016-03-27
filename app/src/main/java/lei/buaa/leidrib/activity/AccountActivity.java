package lei.buaa.leidrib.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lei.buaa.leidrib.LDApplication;
import lei.buaa.leidrib.R;
import lei.buaa.leidrib.bean.User;
import lei.buaa.leidrib.config.KeyConfig;
import lei.buaa.leidrib.config.ResponseCode;
import lei.buaa.leidrib.retrofit.IRQLoginUser;
import lei.buaa.leidrib.retrofit.IRQUser;
import lei.buaa.leidrib.utils.RetrofitUtils;
import lei.buaa.leidrib.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lei on 3/19/16.
 * email: lileibh@gmail.com
 */
public class AccountActivity extends BaseActivity {
    private static final long LOGIN_USER = -1;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.iv_avatar)
    SimpleDraweeView mIvAvatar;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_bio)
    TextView mTvBio;
    @Bind(R.id.tv_follower)
    TextView mTvFollower;
    @Bind(R.id.tv_following)
    TextView mTvFollowing;
    @Bind(R.id.bt_logout)
    Button mBtLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        long userId = getIntent().getLongExtra(KeyConfig.USER_ID, LOGIN_USER);
        if (userId != LOGIN_USER)
            mBtLogout.setVisibility(View.GONE);
        getUserInfo(userId);
    }

    private void getUserInfo(long userId) {
        showProgressDialog();
        Callback<User> callback = new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dismissProgressDialog();
                if (response.code() == ResponseCode.STATUS_OK) {
                    updateView(response.body());
                } else
                    Toast.makeText(AccountActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(AccountActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
                Log.d("retrofit_info", t.getMessage());
            }
        };
        if (userId == LOGIN_USER) {
            RetrofitUtils.getRetrofit().create(IRQLoginUser.class)
                    .getLoginUser()
                    .enqueue(callback);
        } else {
            RetrofitUtils.getRetrofit().create(IRQUser.class)
                    .getUser(userId)
                    .enqueue(callback);
        }
    }

    private void updateView(User user) {
        mTvName.setText(user.getName());
        mIvAvatar.setImageURI(Uri.parse(user.getAvatar_url()));
        mTvBio.setText(StringUtils.formatHtml(user.getBio()));
        mTvFollower.setText(String.valueOf(user.getFollowers_count()));
        mTvFollowing.setText(String.valueOf(user.getFollowings_count()));
    }

    @OnClick(R.id.bt_logout)
    void onLogoutClick(View view) {
        LDApplication.setUser(null);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
