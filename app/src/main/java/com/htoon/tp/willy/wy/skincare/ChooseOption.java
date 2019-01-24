package com.htoon.tp.willy.wy.skincare;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.view.WindowManager.LayoutParams;

import com.htoon.tp.willy.wy.skincare.CallCamera.CallCameraDialog;

/**
 * Created by wp on 04/02/2018.
 */

public class ChooseOption extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera ccamera = null;
    private SurfaceView csurfaceView = null;
    private SurfaceHolder csurfaceHolder = null;
    // private android.hardware.Camera.PictureCallback cjpegCallback;
    private PictureCallback cjpegCallback;
    private android.hardware.Camera.ShutterCallback cshutterCallback;
    // private Camera.Parameters parameters=null;
    private Button cbtnTake;
    static String cdir;
    private ProgressBar progressBar;
    private int MODE_ONE=1,MODE_TWO=2;
private TextView txtcam;
private static final int PERMISSION_REQUEST_CAPTURE_IMAGE=1;
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Camera.Parameters parameters;
        //try {
        ccamera = android.hardware.Camera.open();
        //  } catch (RuntimeException e) {
        //   Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
        //  }
        try {
            parameters = ccamera.getParameters();
            parameters.setPreviewFrameRate(20);
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
              //  Toast.makeText(getApplicationContext(), size.width + " " + size.height, Toast.LENGTH_SHORT).show();
                if (600 <= size.width & size.width <= 1920) {
                    // Toast.makeText(getApplicationContext(),"it is ok", Toast.LENGTH_SHORT).show();
                    parameters.setPreviewSize(size.width, size.height);
                    //  parameters.setPictureSize(size.width, size.height);
                   break;
               }

            }

           for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                 Toast.makeText(getApplicationContext(),size.width+" "+size.height, Toast.LENGTH_SHORT).show();
                // if (600 <= size.width & size.width <= 1920) {
                //   Toast.makeText(getApplicationContext(),"is ok", Toast.LENGTH_SHORT).show();
                //   parameters.setPreviewSize(size.width, size.height);
                if(size.width<1080)
                {
                    Toast.makeText(getApplicationContext(),"Width less 3000", Toast.LENGTH_SHORT).show();
                    parameters.setPictureSize(3120,3120);
                break;}
                else
                { parameters.setPictureSize(size.width, size.height);
                break;}
                // }

            }
            //  parameters.setPreviewSize();
           // parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            // parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
            ccamera.setParameters(parameters);
            ccamera.setDisplayOrientation(90);
            try {
                ccamera.setPreviewDisplay(surfaceHolder);
                ccamera.startPreview();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        ccamera.stopPreview();
        ccamera.release();
        ccamera = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){

            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CAPTURE_IMAGE);
            }
        }
        else{

        }



        findViewById(R.id.option_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i;
               // i = new Intent(ChooseOption.this, MainActivity.class);
              //  startActivity(i);

                CallCameraDialog cmd=new CallCameraDialog(ChooseOption.this,MODE_ONE);
                cmd.callCamera();
            }
        });
        findViewById(R.id.option_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallCameraDialog cmd=new CallCameraDialog(ChooseOption.this,MODE_TWO);
                cmd.callCamera();
            }
        });
      /*  findViewById(R.id.option_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(ChooseOption.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.callcamera);
                LayoutParams parms=new LayoutParams();
                parms.copyFrom(dialog.getWindow().getAttributes());
                parms.width=LayoutParams.WRAP_CONTENT;
                parms.height=LayoutParams.WRAP_CONTENT;
                csurfaceView = (SurfaceView) dialog.findViewById(R.id.csurfaceView);
                cbtnTake = (Button) dialog.findViewById(R.id.btnc_capture);
                txtcam=(TextView)dialog.findViewById(R.id.ccam);
                progressBar=(ProgressBar)dialog.findViewById(R.id.cprogress);
                progressBar.setVisibility(View.INVISIBLE);
                csurfaceHolder = csurfaceView.getHolder();
                csurfaceHolder.addCallback(ChooseOption.this);
                csurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                dialog.show();
                dialog.getWindow().setAttributes(parms);
                cbtnTake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ccamera.takePicture(null, null, cjpegCallback);
                        // }
                        // catch (Exception e){
                        // Toast.makeText(getApplicationContext(),e+"error",Toast.LENGTH_SHORT).show();
                        // }
                        //refreshCamera();
                               // Intent i = new Intent(ChooseOption.this, Check.class);
                               // startActivity(i);
                        //refreshCamera();
                        // Toast.makeText(getApplicationContext(),dir,Toast.LENGTH_SHORT).show();
                        txtcam.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        cbtnTake.setText("SAVING");

                    }
                });
                cjpegCallback = new Camera.PictureCallback() {
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
                            cdir = file.getAbsolutePath();
                            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getApplicationContext(), e + "error", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), e + "error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            txtcam.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            cbtnTake.setText("CAPTURE");
                            Intent i = new Intent(ChooseOption.this, Check.class);
                             startActivity(i);
                        }
                    }
                };
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CAPTURE_IMAGE:{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(),"Permission Denied.Thank You!!",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
        return;
    }
}
