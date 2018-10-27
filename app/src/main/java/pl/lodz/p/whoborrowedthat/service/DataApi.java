package pl.lodz.p.whoborrowedthat.service;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Stuff;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface DataApi {

    @GET("v1/borrows/borrower")
    Call<List<Stuff>> getBorrowedThingsByUserEmail(
            @Header("X-User-Token") String token,
            @Header("X-User-Email") String email
    );

    @GET("v1/borrows/owner")
    Call<List<Stuff>> getLentThingsByUserEmail(
            @Header("X-User-Token") String token,
            @Header("X-User-Email") String email
    );
}
