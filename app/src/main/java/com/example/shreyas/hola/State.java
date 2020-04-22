package com.example.shreyas.hola;

public class State {

    public static final int NEW=1;
    public static final int OLD=2;
    private static int state = 1;
    private static ContactDisplay otherUser;

    public static int getState() {
        return state;
    }
    public static void setState(int val) {
        state=val;
    }

    public static ContactDisplay getOtherUser() {
        return otherUser;
    }
    public static void setOtherUser(ContactDisplay otheruser) {
        otherUser = otheruser;
    }

}
