package com.example.home.cameraapi;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceView sv;
    SurfaceHolder sh;
    Camera.PictureCallback rawCallback,jpegCallback;
    Camera.ShutterCallback shutterCallback;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sv=(SurfaceView)findViewById(R.id.surfaceView);
        sh=sv.getHolder();
        bt=(Button)findViewById(R.id.button);
        sh.addCallback(this);
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    captureImage(v);
                }
                catch (Exception e)
                {

                }
            }
        });
        jpegCallback=new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream=null;
                try{

                    outStream=new FileOutputStream(String.format("/sdcard%d.jpg"));
                        outStream.write(data);
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                refreshCamera();
            }
        };


    }
    public void captureImage(View v) throws IOException {
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (sh.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        }

        catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(sh);
            camera.startPreview();
        }
        catch (Exception e) {
        }
    }
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        }

        catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

        //Camera.Parameters param;
        //param = camera.getParameters();
        //param.setPreviewSize(352, 288);
        //camera.setParameters(param);

        try {
            camera.setPreviewDisplay(sh);
            camera.startPreview();
        }

        catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

}
