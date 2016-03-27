package lei.buaa.leidrib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lei.buaa.leidrib.R;
import lei.buaa.leidrib.config.KeyConfig;

/**
 * Created by lei on 3/26/16.
 * email: lileibh@gmail.com
 */
public class AboutActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.tv_github, R.id.tv_jianshu})
    void onClick(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        switch (view.getId()) {
            case R.id.tv_github:
                intent.putExtra(KeyConfig.URL, "https://github.com");
                break;
            default:
                intent.putExtra(KeyConfig.URL, "http://jianshu.com");
                break;
        }
        startActivity(intent);
    }
}
