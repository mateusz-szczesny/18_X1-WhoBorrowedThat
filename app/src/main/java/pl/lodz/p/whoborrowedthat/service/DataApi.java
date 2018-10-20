package pl.lodz.p.whoborrowedthat.service;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Borrow;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataApi {

    @GET("v1/stuff/borrowed")
    Call<List<Borrow>> getBorrowedThingsByUserId(@Query("token") String token,
                                                 @Query("id") String userId);

    @GET("v1/stuff/lent")
    Call<List<Borrow>> getLentThingsByUserId(@Query("token") String token,
                                             @Query("id") String userId);
}
