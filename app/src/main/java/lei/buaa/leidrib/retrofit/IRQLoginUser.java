package lei.buaa.leidrib.retrofit;

import lei.buaa.leidrib.bean.User;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lei on 3/26/16.
 * email: lileibh@gmail.com
 */
public interface IRQLoginUser {
    @GET("user")
    Call<User> getLoginUser();
}
