package lei.buaa.leidrib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lei.buaa.leidrib.LDApplication;
import lei.buaa.leidrib.R;
import lei.buaa.leidrib.bean.LoginUser;
import lei.buaa.leidrib.config.KeyConfig;
import lei.buaa.leidrib.config.RequestCode;
import lei.buaa.leidrib.config.ResponseCode;
import lei.buaa.leidrib.config.URLConfig;
import lei.buaa.leidrib.retrofit.IRQOauth;
import lei.buaa.leidrib.retrofit.OauthBean;
import lei.buaa.leidrib.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lei on 3/21/16.
 * email: lileibh@gmail.com
 */
public class LoginActivity extends BaseActivity {
    private boolean mReadyToExit = false;
    @Bind(R.id.bt_login)
    Button mBtLogin;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
    }

    @OnClick(R.id.bt_login)
    void onLoginClick(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(KeyConfig.URL, URLConfig.OAUTH_URL + "?"
                + KeyConfig.CLIENT_ID + "="
                + KeyConfig.CLIENT_ID_VALUE);
        startActivityForResult(intent, RequestCode.RC_FOR_OAUTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.RC_FOR_OAUTH && resultCode == RESULT_OK) {
            showProgressDialog();
            RetrofitUtils.getOauthRetrofit().create(IRQOauth.class)
                    .postToOauth(KeyConfig.CLIENT_ID_VALUE, KeyConfig.CLIENT_SECRET_VALUE,
                            data.getStringExtra(KeyConfig.CODE))
                    .enqueue(new Callback<OauthBean>() {
                        @Override
                        public void onResponse(Call<OauthBean> call, Response<OauthBean> response) {
                            dismissProgressDialog();
                            if (response.code() == ResponseCode.STATUS_OK
                                    && response.body() != null
                                    && !TextUtils.isEmpty(response.body().access_token)) {
                                LoginUser loginUser = new LoginUser();
                                loginUser.setAccess_token(response.body().access_token);
                                LDApplication.setUser(loginUser);
                                setResult(RESULT_OK);
                                finish();
                            } else
                                Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<OauthBean> call, Throwable t) {
                            dismissProgressDialog();
                            Log.d("retrofit_info", t.getMessage());
                            Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mReadyToExit) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            } else {
                mReadyToExit = true;
                mBtLogin.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mReadyToExit = false;
                    }
                }, 2 * DateUtils.SECOND_IN_MILLIS);
                Snackbar.make(mBtLogin, R.string.exit_hint, Snackbar.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
