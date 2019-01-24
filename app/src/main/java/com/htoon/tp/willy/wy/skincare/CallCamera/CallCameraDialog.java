package com.htoon.tp.willy.wy.skincare.CallCamera;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.telecom.Call;
import android.view.SurfaceHolder;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.view.WindowManager.LayoutParams;

import com.htoon.tp.willy.wy.skincare.Check;
import com.htoon.tp.willy.wy.skincare.ChooseOption;
import com.htoon.tp.willy.wy.skincare.R;
import com.htoon.tp.willy.wy.skincare.SharePreferences.SharedPreference;

/**
 * Created by wp on 11/04/2018.
 */

public class CallCameraDialog implements SurfaceHolder.Callback {
    Context context;
    public static String directory;
    private Camera ccamera = null;
    private SurfaceView csurfaceView = null;
    private SurfaceHolder csurfaceHolder = null;
    // private android.hardware.Camera.PictureCallback cjpegCallback;
    private Camera.PictureCallback cjpegCallback;
    private android.hardware.Camera.ShutterCallback cshutterCallback;
    // private Camera.Parameters parameters=null;
    private Button cbtnTake;
    private ProgressBar progressBar;
    private TextView txtcam;
    private ImageView imgv;
    private RelativeLayout view1;
    private RelativeLayout view2;
    private int mode;
    private int midX = 0, midY = 0;
    private int averageRed = 0, averageBlue = 0, averageGreen = 0;
    private Bitmap bmp;
    SharedPreference store;
    private TextView firstColor,secondColor,result;
    private Button btnDone;
    private RelativeLayout view3;

    public CallCameraDialog(Context context,int mode) {
        this.context = context;
        this.mode=mode;
    }

    public String getDir() {
        return this.directory;
    }

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
            parameters.setRotation(90);
            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                //  Toast.makeText(getApplicationContext(), size.width + " " + size.height, Toast.LENGTH_SHORT).show();
                if (600 <= size.width && size.width <= 1920) {
                    // Toast.makeText(getApplicationContext(),"it is ok", Toast.LENGTH_SHORT).show();
                    parameters.setPreviewSize(size.width, size.height);
                    //  parameters.setPictureSize(size.width, size.height);
                    break;
                }

            }

            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                Toast.makeText(context, size.width + " " + size.height, Toast.LENGTH_SHORT).show();
                // if (600 <= size.width & size.width <= 1920) {
                //   Toast.makeText(getApplicationContext(),"is ok", Toast.LENGTH_SHORT).show();
                //   parameters.setPreviewSize(size.width, size.height);
                if (size.width < 1080) {
                    Toast.makeText(context, "Width less 3000", Toast.LENGTH_SHORT).show();
                    parameters.setPictureSize(3120, 3120);
                    break;
                } else {
                    parameters.setPictureSize(size.width, size.height);
                    break;
                }
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
                Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
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

    public void refreshCamera() {
        if (csurfaceHolder.getSurface() == null) {
            return;
        }
        try {
            ccamera.stopPreview();
        } catch (Exception e) {
            Toast.makeText(context, "Error Stopping", Toast.LENGTH_SHORT).show();
        }
        try {
            ccamera.setPreviewDisplay(csurfaceHolder);
            ccamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callCamera() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.callcamera);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutParams parms = new LayoutParams();
        parms.copyFrom(dialog.getWindow().getAttributes());
        parms.width = LayoutParams.WRAP_CONTENT;
        parms.height = LayoutParams.WRAP_CONTENT;
        csurfaceView = (SurfaceView) dialog.findViewById(R.id.csurfaceView);
        cbtnTake = (Button) dialog.findViewById(R.id.btnc_capture);
        txtcam = (TextView) dialog.findViewById(R.id.ccam);
        progressBar = (ProgressBar) dialog.findViewById(R.id.cprogress);
        imgv = (ImageView) dialog.findViewById(R.id.dialog_imgV);
        view1 = (RelativeLayout) dialog.findViewById(R.id.view_one);
        view2 = (RelativeLayout) dialog.findViewById(R.id.view_two);
        view3=(RelativeLayout)dialog.findViewById(R.id.view_three);
        firstColor=(TextView)dialog.findViewById(R.id.firstColor);
        secondColor=(TextView)dialog.findViewById(R.id.secondColor);
        result=(TextView)dialog.findViewById(R.id.txtResult);
        progressBar.setVisibility(View.INVISIBLE);
        csurfaceHolder = csurfaceView.getHolder();
        csurfaceHolder.addCallback(CallCameraDialog.this);
        csurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        dialog.show();
        dialog.getWindow().setAttributes(parms);
        dialog.findViewById(R.id.retake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCamera();
                view2.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.VISIBLE);
            }
        });
        dialog.findViewById(R.id.goTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //use bundle data transfer here
                if(mode==1) {
                    Intent i = new Intent(context, Check.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("MODE", mode);
                    i.putExtras(bundle);
                    context.startActivity(i);
                    dialog.dismiss();
                }
                else{
                    saveIn();

                }
            }
        });
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
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                onPicTake(bytes, camera);
            }

            public void onPicTake(byte[] bytes, Camera camera) {
                Toast.makeText(context, "onPictureTaken", Toast.LENGTH_SHORT).show();
                File file = null;
                FileOutputStream fos = null;

                try {
                    file = context.getFilesDir();
                    fos = context.openFileOutput("MySkin.jpeg", context.MODE_PRIVATE);
                    fos.write(bytes);
                    directory = file.getAbsolutePath();
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(context, e + "error", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(context, e + "error", Toast.LENGTH_SHORT).show();
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
                    view1.setVisibility(View.INVISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    try {
                        Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(new File(directory, "MySkin.jpeg")));
                        imgv.setImageBitmap(bmp);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    void saveIn(){
        store=new SharedPreference(context);
        try {
            bmp = BitmapFactory.decodeStream(new FileInputStream(new File(directory, "MySkin.jpeg")));
            double hig = bmp.getHeight();
            double wid = bmp.getWidth();
            midX = bmp.getWidth() / 2;
            midY = bmp.getHeight() / 2;
            calculateAverageValues(midX, midY);
            if(store.getValue1R()==-1) {
                store.setValue1(averageRed, averageGreen, averageBlue);
                view2.setVisibility(View.INVISIBLE);
                refreshCamera();
                view1.setVisibility(View.VISIBLE);
            }
            else if(store.getValue2R()==-2){
                store.setValue2(averageRed,averageGreen,averageBlue);
                int red1=store.getValue1R();
                int red2=store.getValue2R();
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.INVISIBLE);
                firstColor.setBackgroundColor(Color.rgb(store.getValue1R(),store.getValue1G(),store.getValue1B()));
                secondColor.setBackgroundColor(Color.rgb(store.getValue2R(),store.getValue2G(),store.getValue2B()));
                store.setValue1(-1,-1,-1);
                store.setValue2(-2,-2,-2);
                view3.setVisibility(View.VISIBLE);
                if(red1<red2) {
                    Toast.makeText(context,red1+"<"+red2,Toast.LENGTH_SHORT).show();
                    result.setText("First Win");
                }
                else
                {Toast.makeText(context,red1+">"+red2,Toast.LENGTH_SHORT).show();
                    result.setText("Second Win");}
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    void calculateAverageValues(int miX, int miY) {
        int startX = miX - 50;
        int endX = miX + 50;
        int startY = miY - 50;
        int endY = miY + 50;
        int count = 0;
        int totalRed = 0, totalBlue = 0, totalGreen = 0;
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                int pix = bmp.getPixel(i, j);
                totalRed += Color.red(pix);
                totalBlue += Color.blue(pix);
                totalGreen += Color.green(pix);
                count++;
            }
        }
        averageRed = totalRed / count;
        averageBlue = totalBlue / count;
        averageGreen = totalGreen / count;

    }
}

