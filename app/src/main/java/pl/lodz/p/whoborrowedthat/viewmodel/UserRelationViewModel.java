package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper.getUserFormSP;

public class UserRelationViewModel extends AndroidViewModel {
    private final MutableLiveData<List<UserRelation>> allRelations;
    private final ApiManager apiManager;

    public UserRelationViewModel(Application application) {
        super(application);
        apiManager = ApiManager.getInstance();
        allRelations = new MutableLiveData<>();
        User user = getUserFormSP(application);

        if (user != null)
            fetchData(user);
    }

    private void fetchData (User user) {
        apiManager.getUserRelations(user, new Callback<List<UserRelation>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserRelation>> call, @NonNull Response<List<UserRelation>> response) {
                allRelations.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<UserRelation>> call, @NonNull Throwable t) {
                allRelations.setValue(new ArrayList<UserRelation>());
            }
        });
    }

    public LiveData<List<UserRelation>> getRelations() {
        return allRelations;
    }

    public void refreshData(Application application) {
        User user = getUserFormSP(application);
        if (user != null) {
            fetchData(user);
        }
    }
}
