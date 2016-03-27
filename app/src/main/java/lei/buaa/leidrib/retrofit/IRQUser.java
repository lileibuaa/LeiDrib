package lei.buaa.leidrib.retrofit;

import lei.buaa.leidrib.bean.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lei on 3/19/16.
 * email: lileibh@gmail.com
 */
public interface IRQUser {
    @GET("users/{userId}")
    Call<User> getUser(@Path("userId") long userId);
}
