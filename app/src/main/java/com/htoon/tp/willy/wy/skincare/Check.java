package com.htoon.tp.willy.wy.skincare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.htoon.tp.willy.wy.skincare.CallCamera.CallCameraDialog;
import com.htoon.tp.willy.wy.skincare.SharePreferences.SharedPreference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by wp on 18/11/2017.
 */

public class Check extends AppCompatActivity implements View.OnTouchListener {
    ImageView imgV;
    TextView devicePix;
    Bitmap bmp;
    String d = CallCameraDialog.directory;
    private boolean gt200 = false;
    private boolean gt600, gt2000;
    private double imgvH = 0, imgvW = 0;
    private double multrX = 0, multrY = 0;
    private int midX = 0, midY = 0;
    private int averageRed = 0, averageBlue = 0, averageGreen = 0;
    private int mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);
        Bundle bundle = getIntent().getExtras();
        mode = bundle.getInt("MODE");
        imgV = (ImageView) findViewById(R.id.img_check);
        devicePix = (TextView) findViewById(R.id.device_pixel);
        int px_dp = devicePix.getLayoutParams().width;

        //imgV.setImageResource(R.drawable.camera);
        try {
            bmp = BitmapFactory.decodeStream(new FileInputStream(new File(d, "MySkin.jpeg")));
            double hig = bmp.getHeight();
            double wid = bmp.getWidth();
            double ratio = wid / hig;

            midX = bmp.getWidth() / 2;
            midY = bmp.getHeight() / 2;

            calculateAverageValues(midX, midY);

            int type = CheckType(averageRed);
            Toast.makeText(getApplicationContext(), "Rating  :" + averageRed + "       :type      " + type, Toast.LENGTH_SHORT).show();
            String about_skin = AboutSkin(type);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Check.this);
            View mView = getLayoutInflater().inflate(R.layout.about_skin_dialog, null, false);
            TextView tvHeader = (TextView) mView.findViewById(R.id.txtHeader);
            TextView tvAbout = (TextView) mView.findViewById(R.id.txtAbout);
            TextView tvColor = (TextView) mView.findViewById(R.id.txtColorSimple);
            Button btn = (Button) mView.findViewById(R.id.btnOk);
            tvColor.setBackgroundColor(Color.rgb(averageRed, averageGreen, averageBlue));
            tvHeader.setText("Skin Type " + type + "  :Rating " + averageRed);
            tvAbout.setText(about_skin);
            //change btn text  depend on shp values
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    //recall dialog or not
                }
            });
            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();

            //Log.d("Willy",);
            Toast.makeText(getApplicationContext(), wid + " :width" + hig + " :height", Toast.LENGTH_SHORT).show();

            imgV.getLayoutParams().width = (int) ((212 * ratio) * px_dp);
            imgV.getLayoutParams().height = (int) (212 * px_dp);
            imgvH = imgV.getLayoutParams().height;
            imgvW = imgV.getLayoutParams().width;
            multrX = getMultrX(wid, imgvW);
            multrY = getMultrY(hig, imgvH);
            Toast.makeText(getApplicationContext(), "width" + imgvW + "height" + imgvH, Toast.LENGTH_SHORT).show();
           /* if (wid > 200) {
                gt200 = true;
                if(wid>600){
                    gt600 = true;
                    if(wid>2000)
                    {
                        gt2000=true;
                        imgV.getLayoutParams().width = bmp.getWidth()/4;
                        imgV.getLayoutParams().height = bmp.getHeight()/4;
                    }
                    else {

                        imgV.getLayoutParams().width = bmp.getWidth();
                        imgV.getLayoutParams().height = bmp.getHeight();
                    }
                }
                else {

                    imgV.getLayoutParams().width = bmp.getWidth() * 3;
                    imgV.getLayoutParams().height = bmp.getHeight() * 3;
                }
            } else {
                gt200 = false;
                imgV.getLayoutParams().width = bmp.getWidth() * 4;
                imgV.getLayoutParams().height = bmp.getHeight() * 4;
            }*/
            imgV.setImageBitmap(bmp);
            imgV.setOnTouchListener(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        try {
            int x = 0;
            int y = 0;
           /* if (gt200) {
                if(gt600){
                    if(gt2000){
                        x = ((int) motionEvent.getX())*4 ;
                        y = ((int) motionEvent.getY())*4;
                    }
                    else {
                        x = ((int) motionEvent.getX());
                        y = ((int) motionEvent.getY());
                    }
                }
                else {
                    x = ((int) motionEvent.getX()) / 3;
                    y = ((int) motionEvent.getY()) / 3;
                }
            } else {
                x = ((int) motionEvent.getX()) / 4;
                y = ((int) motionEvent.getY()) / 4;
            }*/
            x = (int) Math.ceil(motionEvent.getX() * multrX);
            y = (int) Math.ceil(motionEvent.getY() * multrY);

            Toast.makeText(getApplicationContext(), "X   " + x + "      Y" + y, Toast.LENGTH_SHORT).show();

            int touched_pixel = bmp.getPixel(x, y);
            int surround_xp = bmp.getPixel(x + 1, y);
            int surround_xm = bmp.getPixel(x - 1, y);
            int surround_yp = bmp.getPixel(x, y + 1);
            int surround_ym = bmp.getPixel(x, y - 1);
            int total_black = (Color.red(touched_pixel) + Color.red(surround_xp) + Color.red(surround_xm) + Color.red(surround_yp) + Color.red(surround_ym)) / 5;
            int total_green = (Color.green(touched_pixel) + Color.green(surround_xp) + Color.red(surround_xm) + Color.green(surround_yp) + Color.green(surround_ym)) / 5;
            int total_blue = (Color.blue(touched_pixel) + Color.blue(surround_xp) + Color.blue(surround_xm) + Color.blue(surround_yp) + Color.blue(surround_ym)) / 5;
            int type = CheckType(total_black);
            Toast.makeText(getApplicationContext(), "Rating  :" + total_black + "       :type      " + type, Toast.LENGTH_SHORT).show();
            String about_skin = AboutSkin(type);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Check.this);
            View mView = getLayoutInflater().inflate(R.layout.about_skin_dialog, null, false);
            TextView tvHeader = (TextView) mView.findViewById(R.id.txtHeader);
            TextView tvAbout = (TextView) mView.findViewById(R.id.txtAbout);
            TextView tvColor = (TextView) mView.findViewById(R.id.txtColorSimple);
            Button btn = (Button) mView.findViewById(R.id.btnOk);
            tvColor.setBackgroundColor(Color.rgb(total_black, total_green, total_blue));
            tvHeader.setText("Skin Type " + type + "  :Rating " + total_black);
            tvAbout.setText(about_skin);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();


        } catch (Exception e) {
        }
        return false;
    }

    double getMultrX(double bmpW, double imgW) {
        return bmpW / imgW;
    }

    double getMultrY(double bmpH, double imgH) {
        return bmpH / imgH;
    }

    private int CheckType(int black) {
        if (black < 256 + 10 && black > 231 + 10)
            return 1;
        else if (black < 232 + 10 && black > 221 + 10)
            return 2;
        else if (black < 222 + 10 && black > 175 + 10)
            return 3;
        else if (black < 176 + 10 && black > 139 + 10)
            return 4;
        else if (black < 140 + 10 && black > 64 + 10)
            return 5;
        else if (black < 65 + 10 && black > 8 + 10)
            return 6;
        return 0;
    }

    private String AboutSkin(int type) {
        String about = null;
        switch (type) {
            case 1:
                about = getResources().getString(R.string.skin_one);
                break;
            case 2:
                about = getResources().getString(R.string.skin_two);
                break;
            case 3:
                about = getResources().getString(R.string.skin_three);
                break;
            case 4:
                about = getResources().getString(R.string.skin_four);
                break;
            case 5:
                about = getResources().getString(R.string.skin_five);
                break;
            case 6:
                about = getResources().getString(R.string.skin_six);
                break;
            case 0:
                about = getResources().getString(R.string.skin_error);
                break;
        }
        return about;
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

