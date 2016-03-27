package lei.buaa.leidrib.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lei on 3/19/16.
 * you can you up...
 */
public interface IRQOauth {
    @FormUrlEncoded
    @POST("oauth/token")
    Call<OauthBean> postToOauth(@Field("client_id") String clientId,
                                @Field("client_secret") String clientSecret,
                                @Field("code") String code);
}
