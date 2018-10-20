package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;

public class LentViewModel extends AndroidViewModel {
    private final LiveData<List<Stuff>> allLents;
    private final ApiManager apiManager;

    public LentViewModel(Application application) {
        super(application);
        apiManager = ApiManager.getInstance();
        allLents = apiManager.getStuff(ApiManager.Stuff.LENT, new User());
    }

    public LiveData<List<Stuff>> getAllLents() {
        return allLents;
    }
}
