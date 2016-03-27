package lei.buaa.leidrib.retrofit;

import java.util.List;

import lei.buaa.leidrib.bean.Shot;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lei on 3/21/16.
 * email: lileibh@gmail.com
 */
public interface IRQShotList {
    @GET("shots")
    Call<List<Shot>> loadShotList(@Query("list") String type,@Query("page") int mPageNum);
}
