package com.goke.media.jni;


//import android.content.Context;
//import android.util.Log;

import com.goke.media.ui.call.IStateListener;

public class C2JavaAPI implements IStateListener {

    public static IStateListener callStateListener = null;

    public static void setCallStateListener(IStateListener cb) {
        callStateListener = cb;
    }


    public int SdkCallback(int s, int c, String info, String jsonData) {
        if(callStateListener != null){
            callStateListener.SdkCallback(s,c,info, jsonData);
        }
        return 0;
    }
}
