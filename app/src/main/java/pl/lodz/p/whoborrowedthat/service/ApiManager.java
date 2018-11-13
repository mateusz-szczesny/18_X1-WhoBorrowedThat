package pl.lodz.p.whoborrowedthat.service;

import android.arch.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.lodz.p.whoborrowedthat.coverter.CustomConverterFactory;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static AuthApi authService;
    private static DataApi dataService;
    private static ApiManager apiManager;

    public enum StuffType { BORROWED, LENT };

    private ApiManager() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://who-borrowed-that.herokuapp.com/")
                .addConverterFactory(new CustomConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
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

    public MutableLiveData<List<Stuff>> getStuff(StuffType stuffType, User user) {
        switch (stuffType) {
            case BORROWED:
                return getBorrowedStuff(user);
            case LENT:
                return getLentStuff(user);
            default:
                return null;
        }
    }

    private MutableLiveData<List<Stuff>> getLentStuff(User user) {
        final MutableLiveData<List<Stuff>> data = new MutableLiveData<>();
        dataService.getLentThingsByUserEmail(user.getToken(), user.getEmail()).enqueue(new Callback<List<Stuff>>() {
            @Override
            public void onResponse(Call<List<Stuff>> call, Response<List<Stuff>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Stuff>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    private MutableLiveData<List<Stuff>> getBorrowedStuff(User user) {
        final MutableLiveData<List<Stuff>> data = new MutableLiveData<>();
        dataService.getBorrowedThingsByUserEmail(user.getToken(), user.getEmail()).enqueue(new Callback<List<Stuff>>() {
            @Override
            public void onResponse(Call<List<Stuff>> call, Response<List<Stuff>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Stuff>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public void registerUser(String email, String password, String passwordConfirmation, Callback<User> callback) {
        Call<User> userCall = authService.register(email, password, passwordConfirmation);
        userCall.enqueue(callback);
    }

    public void addBorrows(User user, Stuff stuff, Callback<Object> callback) {
        SimpleDateFormat a  = new SimpleDateFormat("dd-MM-YYYY");
        Call<Object> userCall = dataService.addBorrows(user.getToken(), user.getEmail(),
                String.valueOf(user.getId()), String.valueOf(stuff.getBorrower().getId()),
                stuff.getName(),a.format(stuff.getRentalDate()), a.format(stuff.getReturnDate()));
        userCall.enqueue(callback);
    }

    public MutableLiveData<List<UserRelation>> getUserRelations(User user) {
        final MutableLiveData<List<UserRelation>> data = new MutableLiveData<>();
        dataService.getUserRelations(user.getToken(), user.getEmail(), user.getId()).enqueue(new Callback<List<UserRelation>>() {
            @Override
            public void onResponse(Call<List<UserRelation>> call, Response<List<UserRelation>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<UserRelation>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}