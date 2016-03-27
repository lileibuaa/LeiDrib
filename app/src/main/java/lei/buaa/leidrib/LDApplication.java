package lei.buaa.leidrib;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import lei.buaa.leidrib.bean.LoginUser;
import lei.buaa.leidrib.utils.DaoUtils;

/**
 * Created by lei on 3/19/16.
 * email: lileibh@gmail.com
 */
public class LDApplication extends Application {
    private static LoginUser mLoginUser = null;
    private static LDApplication INSTANCE = null;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Fresco.initialize(this);
    }

    public static LDApplication getInstance() {
        return INSTANCE;
    }

    public static LoginUser getUser() {
        if (mLoginUser == null) {
            mLoginUser = DaoUtils.getLoginUser();
        }
        return mLoginUser;
    }

    public static void setUser(LoginUser user) {
        mLoginUser = user;
        DaoUtils.setLoginUser(user);
    }
}
