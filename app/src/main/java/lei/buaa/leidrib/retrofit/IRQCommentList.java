package lei.buaa.leidrib.retrofit;

import java.util.List;

import lei.buaa.leidrib.bean.Comment;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lei on 3/21/16.
 * email: lileibh@gmail.com
 */
public interface IRQCommentList {
    @GET("shots/{id}/comments")
    Call<List<Comment>> loadComments(@Path("id") long shotId, @Query("per_page") int perPage);
}
