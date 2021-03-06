package lei.buaa.leidrib.bean;

import java.util.List;
import lei.buaa.leidrib.greenDao.DaoSession;
import de.greenrobot.dao.DaoException;

import lei.buaa.leidrib.greenDao.ShotDao;
import lei.buaa.leidrib.greenDao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "USER".
 */
public class User {

    private Long id;
    /** Not-null value. */
    private String name;
    /** Not-null value. */
    private String username;
    /** Not-null value. */
    private String avatar_url;
    private String bio;
    private Integer followers_count;
    private Integer followings_count;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient UserDao myDao;

    private List<Shot> shotList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String name, String username, String avatar_url, String bio, Integer followers_count, Integer followings_count) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.avatar_url = avatar_url;
        this.bio = bio;
        this.followers_count = followers_count;
        this.followings_count = followings_count;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    /** Not-null value. */
    public String getUsername() {
        return username;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Not-null value. */
    public String getAvatar_url() {
        return avatar_url;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(Integer followers_count) {
        this.followers_count = followers_count;
    }

    public Integer getFollowings_count() {
        return followings_count;
    }

    public void setFollowings_count(Integer followings_count) {
        this.followings_count = followings_count;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Shot> getShotList() {
        if (shotList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ShotDao targetDao = daoSession.getShotDao();
            List<Shot> shotListNew = targetDao._queryUser_ShotList(id);
            synchronized (this) {
                if(shotList == null) {
                    shotList = shotListNew;
                }
            }
        }
        return shotList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetShotList() {
        shotList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
