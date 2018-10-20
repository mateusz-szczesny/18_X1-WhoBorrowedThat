package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import pl.lodz.p.whoborrowedthat.helper.ConstHelper;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;

import static pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper.getUserFormSP;

public class LentViewModel extends AndroidViewModel {
    private final LiveData<List<Stuff>> allLents;
    private final ApiManager apiManager;

    public LentViewModel(Application application) {
        super(application);
        apiManager = ApiManager.getInstance();

        User user = getUserFormSP(application);

        if (user != null)
            allLents = apiManager.getStuff(ApiManager.StuffType.LENT, user);
        else
            allLents = null;
    }

    public LiveData<List<Stuff>> getAllLents() {
        return allLents;
    }
}
