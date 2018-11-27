package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private List<Stuff> allStuffs;

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
                allStuffs = response.body();
                allLents.setValue(allStuffs);
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

    public void search(String searchText) {
        if(searchText == null && searchText.equals("")) {
            allLents.setValue(allStuffs);
        } else {
            if (allStuffs != null) {
                Pattern pattern = Pattern.compile(searchText.toLowerCase());
                List<Stuff> searchLents = new ArrayList<>();
                for (Stuff stuff : allStuffs) {
                    if (stuff.getName() != null) {
                        Matcher matcher = pattern.matcher(stuff.getName().toLowerCase());
                        if(matcher.find()) {
                            searchLents.add(stuff);
                        }
                    }
                }
                allLents.setValue(searchLents);
            }
        }
    }
}
