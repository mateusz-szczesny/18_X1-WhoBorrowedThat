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

public class BorrowViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Stuff>> allBorrows;
    private final ApiManager apiManager;
    private List<Stuff> allStuffs;

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
                allStuffs = response.body();
                allBorrows.setValue(allStuffs);
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

    public void search(String searchText) {
        if(searchText == null && searchText.equals("")) {
            allBorrows.setValue(allStuffs);
        } else {
            if (allStuffs != null) {
                Pattern pattern = Pattern.compile(searchText.toLowerCase());
                List<Stuff> searchBorrows = new ArrayList<>();
                for (Stuff stuff : allStuffs) {
                    if (stuff.getName() != null) {
                        Matcher matcher = pattern.matcher(stuff.getName().toLowerCase());
                        if(matcher.find()) {
                            searchBorrows.add(stuff);
                        }
                    }
                }
                allBorrows.setValue(searchBorrows);
            }
        }
    }
}
