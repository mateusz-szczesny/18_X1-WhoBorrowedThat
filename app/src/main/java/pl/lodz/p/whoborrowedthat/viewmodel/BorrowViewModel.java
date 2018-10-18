package pl.lodz.p.whoborrowedthat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import pl.lodz.p.whoborrowedthat.model.Borrow;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.service.ApiManager;

public class BorrowViewModel extends AndroidViewModel {
    private final LiveData<List<Borrow>> allBorrows;
    private final ApiManager apiManager;

    public BorrowViewModel(Application application) {
        super(application);
        apiManager = ApiManager.getInstance();
        allBorrows = apiManager.getBorrowedStuff(new User());
    }

    public LiveData<List<Borrow>> getAllBorrows() {
        return allBorrows;
    }
}