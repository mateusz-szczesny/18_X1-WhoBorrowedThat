package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;

import java.util.ArrayList;
import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper.getUserFormSP;

public class LentViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Stuff>> allLents;
    private final ApiManager apiManager;

    public LentViewModel(Application application) {
        super(application);
        apiManager = ApiManager.getInstance();
        allLents = new MutableLiveData<>();
        User user = getUserFormSP(application);

        if (user != null) {
            fetchData(user);
        }
    }

    public LiveData<List<Stuff>> getAllLents() {
        return allLents;
    }

    private void fetchData (User user) {
        apiManager.getStuff(ApiManager.StuffType.LENT, user, new Callback<List<Stuff>>() {
            @Override
            public void onResponse(@NonNull Call<List<Stuff>> call, @NonNull Response<List<Stuff>> response) {
                allLents.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Stuff>> call, @NonNull Throwable t) {
                allLents.setValue(new ArrayList<Stuff>());
            }
        });
    }

    public void refreshData(Application application) {
        User user = getUserFormSP(application);
        if (user != null) {
            fetchData(user);
        }
    }
}
