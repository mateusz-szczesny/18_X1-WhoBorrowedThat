package pl.lodz.p.whoborrowedthat.service;

import android.arch.lifecycle.LiveData;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Borrow;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;

public interface DataApi {

    @GET("v1/endpoint/to/stuff")
    @FormUrlEncoded
    LiveData<List<Borrow>> getBorrowedThingsByUserId(@Field("token") String token,
                                                     @Field("id") String userId);
}
