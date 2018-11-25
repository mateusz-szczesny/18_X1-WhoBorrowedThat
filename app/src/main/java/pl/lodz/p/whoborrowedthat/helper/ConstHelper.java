package pl.lodz.p.whoborrowedthat.helper;

import java.util.regex.Pattern;

public class ConstHelper {
    public static final String USER__SP = "USER_SP";
    public static final String USER_DATA__SP = "USER_DATA";
    public static final String USER_LOGIN_STATUS__SP = "USER_LOGIN_STATUS";
    public static final String STUFF_BUNDLE__KEY = "STUFF_BNDL_KEY";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
