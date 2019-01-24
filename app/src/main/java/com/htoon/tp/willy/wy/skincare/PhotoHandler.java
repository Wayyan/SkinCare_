package com.htoon.tp.willy.wy.skincare;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.widget.Toast;

/**
 * Created by wp on 04/12/2017.
 */

public class PhotoHandler implements PictureCallback {
    private final Context context;

    public PhotoHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
    }
}
