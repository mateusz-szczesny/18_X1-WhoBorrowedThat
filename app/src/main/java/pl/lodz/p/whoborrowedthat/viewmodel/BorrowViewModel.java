package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;

import static pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper.getUserFormSP;

public class BorrowViewModel extends AndroidViewModel {
    private final LiveData<List<Stuff>> allBorrows;
    private final ApiManager apiManager;

    public BorrowViewModel(Application application) {
        super(application);

        apiManager = ApiManager.getInstance();

        User user = getUserFormSP(application);
        if (user != null)
            allBorrows = apiManager.getStuff(ApiManager.StuffType.BORROWED, user);
        else
            allBorrows = null;
    }

    public LiveData<List<Stuff>> getAllBorrows() {
        return allBorrows;
    }


}
