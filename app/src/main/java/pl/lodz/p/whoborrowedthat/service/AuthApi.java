package pl.lodz.p.whoborrowedthat.service;

import pl.lodz.p.whoborrowedthat.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthApi {

    @Headers({"Content-Type: application/json"})

    @POST("v1/sessions")
    @FormUrlEncoded
    Call<User> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("v1/users")
    @FormUrlEncoded
    Call<User> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String passwordConfirmation
    );
}
