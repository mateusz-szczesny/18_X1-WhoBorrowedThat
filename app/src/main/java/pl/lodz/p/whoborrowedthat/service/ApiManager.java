package pl.lodz.p.whoborrowedthat.service;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Borrow;
import pl.lodz.p.whoborrowedthat.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static AuthApi authService;
    private static DataApi dataService;
    private static ApiManager apiManager;

    public enum Stuff { BORROWED, LENT };

    private ApiManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://who-borrowed-that.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authService = retrofit.create(AuthApi.class);
        dataService = retrofit.create(DataApi.class);

    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void loginUser(String email, String password, Callback<User> callback) {
        Call<User> userCall = authService.login(email,password);
        userCall.enqueue(callback);
    }

    public MutableLiveData<List<Borrow>> getStuff(Stuff stuff, User user) {
        switch (stuff) {
            case BORROWED:
                return getBorrowedStuff(user);
            case LENT:
                return getLentStuff(user);
            default:
                return null;
        }
    }

    private MutableLiveData<List<Borrow>> getLentStuff(User user) {
        final MutableLiveData<List<Borrow>> data = new MutableLiveData<>();
        dataService.getLentThingsByUserId(user.getToken(), user.getId()).enqueue(new Callback<List<Borrow>>() {
            @Override
            public void onResponse(Call<List<Borrow>> call, Response<List<Borrow>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Borrow>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    private MutableLiveData<List<Borrow>> getBorrowedStuff(User user) {
        final MutableLiveData<List<Borrow>> data = new MutableLiveData<>();
        dataService.getBorrowedThingsByUserId(user.getToken(), user.getId()).enqueue(new Callback<List<Borrow>>() {
            @Override
            public void onResponse(Call<List<Borrow>> call, Response<List<Borrow>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Borrow>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
