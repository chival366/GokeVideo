package com.goke.media.ui.call;

import android.content.Context;
import android.view.SurfaceView;

import com.goke.media.jni.Java2CAPI;

import org.webrtc.videoengine.ViERenderer;
import org.webrtc.videoengine.VideoCaptureAndroid;

public class MediaEngine {
    private final Context context;
    private final Java2CAPI j2c;
    private SurfaceView svLocal = null;
    private SurfaceView svRemote = null;
    private boolean vieRunning = false;
    private boolean useFrontCamera;

    public MediaEngine(Context context, Java2CAPI j2c) {
        this.context = context;
        this.j2c = j2c;
        j2c.mediaCreate( context, 1 );

        svLocal = ViERenderer.CreateRenderer( context, true );
        svRemote = ViERenderer.CreateRenderer( context, true );
    }

    public void dispose() {
        if(vieRunning){
            videoStop();
        }
        svRemote = null;
        svLocal = null;
        j2c.mediaDelete();
    }

    public void videoStartRecv(int localport){
        j2c.videoStartRecv( localport, svRemote );
        vieRunning = true;
    }

    public void videoStartSend(int remoteport, String remoteaddr, String codec){
        j2c.videoStartSend( remoteport, remoteaddr, codec, svLocal);
    }

    public void videoStop(){
        j2c.videoStop();
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

    public void startCamera() {
        //svSend = new SurfaceView(context);
        //VideoCaptureAndroid.setLocalPreview( svSend.getHolder());
        j2c.videoStartCapture( svLocal );
    }

    public void stopCamera() {
        j2c.videoStopCapture();
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
