package com.example.iconfinder.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String authHead = "Authorization";

    @GET("iconsets/{iconSetId}/icons")
    Call<ResponseBody> mainIconsApi(@Header(authHead) String userToken,@Path("iconSetId") int iconSetId,@Query("count") int count);

    @GET("iconsets")
    Call<ResponseBody> allIconSet(@Header(authHead) String userToken,@Query("count") int count,@Query("premium") int premium,@Query("after") Integer after);

    @GET("icons/search")
    Call<ResponseBody> searchIcon(@Header(authHead) String userToken,@Query("query") String query,@Query("count") int count,@Query("premium") int premium,@Query("offset") Integer offset);

}
