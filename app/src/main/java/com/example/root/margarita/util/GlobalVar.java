package com.example.root.margarita.util;

import android.app.Application;
import android.location.Location;

public class GlobalVar extends Application {

    static private String phone;
    static private String auth_token;
    static private Boolean LOGGED_IN,REGISTER;
    static private Location LOCATION;
    static public String URL = "http://fe5a748b.ngrok.io/";
    private static final String SHAREDPREF_USER = "USER_CREDENTIALS";


    static public String getSharedPreferenceName() { return SHAREDPREF_USER; }

    static public String getphone() {
        return GlobalVar.phone;
    }

    static public void setphone(String name) {
        GlobalVar.phone = phone;
    }

    static public String getToken() {
        return GlobalVar.auth_token;
    }

    static public void setToken(String auth_token) {
        GlobalVar.auth_token = auth_token;
    }

    static public Boolean getLoggedIn() {
        return GlobalVar.LOGGED_IN;
    }

    static public void setLoggedIn(Boolean bool) {
        GlobalVar.LOGGED_IN = bool;
    }

    static public Boolean getRegister() {
        return GlobalVar.REGISTER;
    }

    static public void setRegister(Boolean bool){ GlobalVar.REGISTER = bool;}
}
