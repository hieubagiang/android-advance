package com.hieu.bai1camera;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;
import android.widget.Toast;

public class Utils {
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera)
    {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation)
        {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        }
        else
        { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public static void setCameraZoomIn(Activity activity,Camera camera, int zoom) {
        if(checkCameraCanZoom(camera)) {
            Camera.Parameters params = camera.getParameters();
            params.setZoom(zoom);
            camera.setParameters(params);
        }
        Toast.makeText(activity, "Zoom supprot =  " + checkCameraCanZoom(camera), Toast.LENGTH_SHORT).show();
    }
    public static boolean checkCameraCanZoom(Camera camera) {
        Camera.Parameters params = camera.getParameters();
        return params.isZoomSupported();
    }
}
