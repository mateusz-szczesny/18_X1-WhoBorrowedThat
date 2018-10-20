package pl.lodz.p.whoborrowedthat.service;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Stuff;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataApi {

    @GET("v1/stuff/borrowed")
    Call<List<Stuff>> getBorrowedThingsByUserId(@Query("token") String token,
                                                @Query("id") String userId);

    @GET("v1/stuff/lent")
    Call<List<Stuff>> getLentThingsByUserId(@Query("token") String token,
                                            @Query("id") String userId);
}
