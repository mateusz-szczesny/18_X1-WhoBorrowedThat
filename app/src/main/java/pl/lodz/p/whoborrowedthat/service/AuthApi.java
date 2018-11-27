package pl.lodz.p.whoborrowedthat.service;


import pl.lodz.p.whoborrowedthat.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

interface AuthApi {
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
            @Field("password_confirmation") String passwordConfirmation,
            @Field("username") String username
    );

    @PATCH("v1/users")
    @FormUrlEncoded
    Call<Void> setUsername(
            @Header("X-User-Token") String token,
            @Header("X-User-Email") String email,
            @Field("username") String username
    );
}
