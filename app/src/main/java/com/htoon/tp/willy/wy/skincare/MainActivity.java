package com.htoon.tp.willy.wy.skincare;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera camera = null;
    private SurfaceView surfaceView = null;
    private SurfaceHolder surfaceHolder = null;
    // private android.hardware.Camera.PictureCallback jpegCallback;
    private PictureCallback jpegCallback;
    private android.hardware.Camera.ShutterCallback shutterCallback;
    // private Camera.Parameters parameters=null;
    private Button btnTake;
    static String dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        btnTake = (Button) findViewById(R.id.btn_capture);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //try{
                camera.takePicture(null, null, jpegCallback);
                // }
                // catch (Exception e){
                // Toast.makeText(getApplicationContext(),e+"error",Toast.LENGTH_SHORT).show();
                // }
                //refreshCamera();
                final android.support.design.widget.Snackbar snackbar = android.support.design.widget.Snackbar.make(findViewById(R.id.activity_main), "Your Image Is Ready.", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Go Next", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, Check.class);
                        startActivity(i);
                    }
                });
                snackbar.show();
                //refreshCamera();
                // Toast.makeText(getApplicationContext(),dir,Toast.LENGTH_SHORT).show();


            }
        });
        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] bytes, final Camera camera) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                onPicTake(bytes, camera);
            }

            public void onPicTake(byte[] bytes, Camera camera) {
                Toast.makeText(getApplicationContext(), "onPictureTaken", Toast.LENGTH_SHORT).show();
                File file = null;
                FileOutputStream fos = null;

                try {
                    file = getFilesDir();
                    fos = openFileOutput("MySkin.jpeg", MODE_PRIVATE);
                    fos.write(bytes);
                    dir = file.getAbsolutePath();
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), e + "error", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };

    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            Toast.makeText(getApplicationContext(), "Returning", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            camera.stopPreview();
            Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Stopping", Toast.LENGTH_SHORT).show();
        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            Toast.makeText(getApplicationContext(), "Starting", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {

            Toast.makeText(getApplicationContext(), "Error Starting", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Camera.Parameters parameters;
        //try {
        camera = android.hardware.Camera.open();
        //  } catch (RuntimeException e) {
        //   Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
        //  }
        try {
            parameters = camera.getParameters();
            parameters.setPreviewFrameRate(20);
            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                Toast.makeText(getApplicationContext(),size.width+" "+size.height, Toast.LENGTH_SHORT).show();
               if (600 <= size.width && size.width <= 1920) {
                  //  Toast.makeText(getApplicationContext(),"is ok"+size.width+" "+size.height, Toast.LENGTH_SHORT).show();
                   parameters.setPreviewSize(size.width, size.height);
                  //  parameters.setPictureSize(size.width, size.height);
                    break;
                }

            }
            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                Toast.makeText(getApplicationContext(),size.width+" "+size.height, Toast.LENGTH_SHORT).show();
               // if (600 <= size.width & size.width <= 1920) {
                   // Toast.makeText(getApplicationContext(),"is ok", Toast.LENGTH_SHORT).show();
                 //   parameters.setPreviewSize(size.width, size.height);
                if(size.width<1080)
                {parameters.setPictureSize(3120,3120);}
                else
                    parameters.setPictureSize(size.width, size.height);
                    break;
               // }

            }
          //  parameters.setPictureSize(412,412);
            //  parameters.setPreviewSize();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            // parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}
