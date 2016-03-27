package lei.buaa.leidrib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lei.buaa.leidrib.LDApplication;
import lei.buaa.leidrib.R;
import lei.buaa.leidrib.config.KeyConfig;
import lei.buaa.leidrib.config.RequestCode;
import lei.buaa.leidrib.fragment.ContentFragment;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (LDApplication.getUser() == null)
            gotoLogin();
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.shots));
        titles.add(getString(R.string.debuts));
        titles.add("template");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        List<Fragment> fragments = new ArrayList<>();
        Fragment tmpFragment = new ContentFragment();
        Bundle tmpBundle = new Bundle();
        tmpBundle.putString(KeyConfig.SHOT_TYPE, KeyConfig.TYPE_ALL);
        tmpFragment.setArguments(tmpBundle);
        fragments.add(tmpFragment);
        tmpFragment = new ContentFragment();
        tmpBundle = new Bundle();
        tmpBundle.putString(KeyConfig.SHOT_TYPE, KeyConfig.TYPE_DEBUTS);
        tmpFragment.setArguments(tmpBundle);
        fragments.add(tmpFragment);
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(myAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, RequestCode.RC_FOR_LOGIN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_account) {
            startActivity(new Intent(this, AccountActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menu_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments = null;
        private List<String> titles = null;

        public MyAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            if (fragments != null)
                return fragments.size();
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (titles != null)
                return titles.get(position);
            return "default";
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

    }
}
