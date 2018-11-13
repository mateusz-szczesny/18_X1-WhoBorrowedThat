package pl.lodz.p.whoborrowedthat.service;

import java.util.Date;
import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @GET("v1/user_relations/{id}")
    Call<List<UserRelation>> getUserRelations(
            @Header("X-User-Token") String token,
            @Header("X-User-Email") String email,
            @Path("id") long id
    );

    @POST("v1/borrows")
    @FormUrlEncoded
    Call<Object> addBorrows(
            @Header("X-User-Token") String token,
            @Header("X-User-Email") String email,
            @Field("owner_id") String ownerId,
            @Field("borrower_id") String borrowerId,
            @Field("name") String name,
            @Field("rental_date") String rentalDate,
            @Field("estimated_return_date") String estimatedReturnDate
    );
}
