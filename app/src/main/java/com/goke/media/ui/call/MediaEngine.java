package com.goke.media.ui.call;

import org.webrtc.videoengine.ViERenderer;
import org.webrtc.videoengine.VideoCaptureAndroid;
import com.goke.media.jni.Java2CAPI;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import static androidx.core.content.ContextCompat.*;

public class MediaEngine {
    private final Java2CAPI j2c;
    private SurfaceView svLocal = null;
    private SurfaceView svRemote = null;
    private boolean vieRunning = false;
    private boolean useFrontCamera;

    public MediaEngine(Context context, final Java2CAPI j2c) {
        this.j2c = j2c;
        j2c.mediaCreate( context, 1 );

        //svLocal = new SurfaceView(this);
        //VideoCaptureAndroid.setLocalPreview(svLocal.getHolder());

        svLocal = ViERenderer.CreateRenderer( context, true );
        svRemote = ViERenderer.CreateRenderer( context, true );
        svLocal.setZOrderOnTop( true );
    }

    public void dispose() {
        svRemote = null;
        svLocal = null;
        j2c.mediaDelete();
    }

    public void videoStartRecv(int localport, LinearLayout llLocal, LinearLayout llRemote){
        if(vieRunning)
            return;

        llLocal.addView( svLocal );
        llRemote.addView( svRemote );
        j2c.videoStartRecv( localport, svRemote );
        vieRunning = true;
    }

    public void videoStartSend(int remoteport, String remoteaddr, String codec){
        j2c.videoStartSend( remoteport, remoteaddr, codec, svLocal);
    }

    public void videoStop(LinearLayout llLocal, LinearLayout llRemote){
        if(!vieRunning)
            return;

        j2c.videoStop();
        llLocal.removeView( svLocal );
        llRemote.removeView( svRemote );
        vieRunning = false;
    }

    public SurfaceView getRemoteSurfaceView() {
        return svRemote;
    }

    public SurfaceView getLocalSurfaceView() {
        return svLocal;
    }

    public boolean isRunning() {
        return vieRunning;
    }

    public void startCamera(Context context, LinearLayout llLocal, LinearLayout llRemote) {
    	svLocal = new SurfaceView(context);
        VideoCaptureAndroid.setLocalPreview( svLocal.getHolder());
    	llLocal.addView( svLocal );
        llRemote.addView( svRemote );
        j2c.videoStartCapture( svLocal );
    }

    public void stopCamera(LinearLayout llLocal, LinearLayout llRemote) {
        //j2c.videoStopCapture();
        llLocal.removeView( svLocal );
        llRemote.removeView( svRemote );
    }

    /*public void toggleCamera() {
        if (vieRunning) {
            stopCamera();
        }
        useFrontCamera = !useFrontCamera;
        if (vieRunning) {
            startCamera();
        }
    }

    private void startCamera() {
        svSend = new SurfaceView(context);
        VideoCaptureAndroid.setLocalPreview( svSend.getHolder());
        j2c.StartCapture( null );
    }

    private void stopCamera() {
        j2c.StopCapture();
        svSend = null;
    }

    private int getCameraIndex() {
        return useFrontCamera ? Camera.CameraInfo.CAMERA_FACING_FRONT :
                Camera.CameraInfo.CAMERA_FACING_BACK;
    }*/
}
