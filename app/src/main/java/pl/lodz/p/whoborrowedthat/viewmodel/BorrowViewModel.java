package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper.getUserFormSP;

public class BorrowViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Stuff>> allBorrows;
    private final ApiManager apiManager;

    public BorrowViewModel(Application application) {
        super(application);

        apiManager = ApiManager.getInstance();
        allBorrows = new MutableLiveData<>();
        User user = getUserFormSP(application);
        if (user != null) {
            fetchData(user);
        }
    }

    public LiveData<List<Stuff>> getAllBorrows() {
        return allBorrows;
    }

    private void fetchData (User user) {
        apiManager.getStuff(ApiManager.StuffType.BORROWED, user, new Callback<List<Stuff>>() {
            @Override
            public void onResponse(@NonNull Call<List<Stuff>> call, @NonNull Response<List<Stuff>> response) {
                allBorrows.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Stuff>> call, @NonNull Throwable t) {
                allBorrows.setValue(new ArrayList<Stuff>());
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
