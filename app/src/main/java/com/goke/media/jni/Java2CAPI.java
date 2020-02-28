package com.goke.media.jni;

//import android.content.Context;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

public class Java2CAPI {
    public Java2CAPI() {
        System.loadLibrary("cosdkjni");
        Log.d("cosdk", "Loading cosdkjni.so ok");
    }

    public native String cosdkVersion();
    public native int cosdkInit();
    public native int cosdkExit();
    public native int cosdkReg(String server, String proxy, String usr, String pwd);
    public native int cosdkUnreg();
    public native int cosdkInvite(String callee);
    public native int cosdkAnswer(int callid);
    public native int cosdkHangup(int callid);

    public native int mediaCreate(Context context, int hasVideo);
    public native int mediaDelete();
    public native int mediaStartRecv(int localport);
    public native int mediaStartSend(int remoteport, String remoteaddr, String codec);
    public native int mediaStop();  // stop both send and recv

    public native int videoStartRecv(int localport, SurfaceView recvview);
    public native int videoStartSend(int remoteport, String remoteaddr, String codec, SurfaceView sendview);
    public native int videoStop();  // stop both send and recv

    public native int videoStartCapture(SurfaceView sendview);
    public native int videoStopCapture();

}
