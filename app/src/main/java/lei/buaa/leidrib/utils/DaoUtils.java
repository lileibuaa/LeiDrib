package lei.buaa.leidrib.utils;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import lei.buaa.leidrib.LDApplication;
import lei.buaa.leidrib.bean.Comment;
import lei.buaa.leidrib.bean.Images;
import lei.buaa.leidrib.bean.LoginUser;
import lei.buaa.leidrib.bean.Shot;
import lei.buaa.leidrib.bean.User;
import lei.buaa.leidrib.greenDao.DaoMaster;
import lei.buaa.leidrib.greenDao.DaoSession;

/**
 * Created by lei on 3/19/16.
 * email: lileibh@gmail.com
 */
public class DaoUtils {
    private static DaoSession mDaoSession = null;

    private static DaoSession getDaoSession() {
        if (mDaoSession == null) {
            DaoMaster.DevOpenHelper helper =
                    new DaoMaster.DevOpenHelper(LDApplication.getInstance(),
                            "LeiDrib-db", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            mDaoSession = daoMaster.newSession();
        }
        return mDaoSession;
    }

    public static LoginUser getLoginUser() {
        List<LoginUser> users = getDaoSession().getLoginUserDao().loadAll();
        if (users != null && users.size() > 0)
            return users.get(0);
        return null;
    }

    public static void setLoginUser(LoginUser user) {
        getDaoSession().getLoginUserDao().deleteAll();
        if (user != null) {
            getDaoSession().getLoginUserDao().insert(user);
        }
    }

    public static void setShots(List<Shot> shots) {
        if (shots == null)
            return;
        for (Shot shot : shots) {
            if (shot.getImagesLocal() != null) {
                long imageId = setImages(shot.getImagesLocal());
                shot.setImage_id(imageId);
            }
            if (shot.getUserLocal() != null) {
                long userId = setUser(shot.getUserLocal());
                shot.setUser_id(userId);
            }
            getDaoSession().getShotDao().insertOrReplace(shot);
        }
    }

    public static long setImages(Images images) {
        return getDaoSession().getImagesDao().insertOrReplace(images);
    }

    public static Shot getShot(long shotId) {
        return getDaoSession().getShotDao().loadDeep(shotId);
    }

    public static void setComments(List<Comment> comments, Long id) {
        if (comments == null || comments.size() == 0)
            return;
        for (Comment temCom : comments) {
            if (temCom.getUserLocal() != null) {
                temCom.setUser_id(setUser(temCom.getUserLocal()));
            }
            temCom.setShot_id(id);
            setComment(temCom);
        }
    }

    public static long setUser(User user) {
        return getDaoSession().getUserDao().insertOrReplace(user);
    }

    private static void setComment(Comment comment) {
        getDaoSession().getCommentDao().insertOrReplace(comment);
    }
}
