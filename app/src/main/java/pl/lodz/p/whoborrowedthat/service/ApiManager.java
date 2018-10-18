package pl.lodz.p.whoborrowedthat.service;

import android.arch.lifecycle.LiveData;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Borrow;
import pl.lodz.p.whoborrowedthat.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static AuthApi service;
    private static ApiManager apiManager;

    private ApiManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://who-borrowed-that.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(AuthApi.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void loginUser(String email, String password, Callback<User> callback) {
        Call<User> userCall = service.login(email,password);
        userCall.enqueue(callback);
    }

    public LiveData<List<Borrow>> getBorrowedStuff(User user){
        //TODO: call API enpoint to get borrowed stuff for spec user
        return null;
    }
}
