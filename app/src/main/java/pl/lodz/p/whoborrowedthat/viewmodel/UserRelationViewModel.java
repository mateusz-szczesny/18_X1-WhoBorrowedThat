package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import pl.lodz.p.whoborrowedthat.service.ApiManager;

import static pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper.getUserFormSP;

public class UserRelationViewModel extends AndroidViewModel {
    private final LiveData<List<UserRelation>> allRelations;
    private final ApiManager apiManager;

    public UserRelationViewModel(Application application) {
        super(application);
        apiManager = ApiManager.getInstance();

        User user = getUserFormSP(application);

        if (user != null)
            allRelations = apiManager.getUserRelations(user);
        else
            allRelations = null;
    }

    public LiveData<List<UserRelation>> getRelations() {
        return allRelations;
    }
}
