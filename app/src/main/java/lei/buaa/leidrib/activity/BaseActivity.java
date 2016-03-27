package lei.buaa.leidrib.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import lei.buaa.leidrib.ui.ProgressCircleDialog;

/**
 * Created by lei on 3/20/16.
 * email: lileibh@gmail.com
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressCircleDialog mProgDialog = null;

    protected void showProgressDialog() {
        if (mProgDialog == null) {
            mProgDialog = new ProgressCircleDialog(this);
        }
        mProgDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void dismissProgressDialog() {
        if (mProgDialog != null)
            mProgDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgDialog != null)
            mProgDialog.dismiss();
    }
}
