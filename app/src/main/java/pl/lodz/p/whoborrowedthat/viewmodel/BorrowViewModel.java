package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;

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
            allBorrows.setValue(apiManager.getStuff(ApiManager.StuffType.BORROWED, user));
        }
    }

    public LiveData<List<Stuff>> getAllBorrows() {
        return allBorrows;
    }

    public void refreshData(Application application) {
        User user = getUserFormSP(application);
        if (user != null) {
            allBorrows.setValue(apiManager.getStuff(ApiManager.StuffType.BORROWED, user));
        }
    }
}
