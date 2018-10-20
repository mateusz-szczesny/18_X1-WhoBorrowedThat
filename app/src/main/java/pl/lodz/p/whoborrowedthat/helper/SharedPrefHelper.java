package pl.lodz.p.whoborrowedthat.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import pl.lodz.p.whoborrowedthat.model.User;

public class SharedPrefHelper {

    public static User getUserFormSP(Application application){
        SharedPreferences sharedPref = application.getSharedPreferences(ConstHelper.USER__SP, Context.MODE_PRIVATE);
        return new Gson().fromJson(sharedPref.getString(ConstHelper.USER_DATA__SP, ""), User.class);
    }
}
