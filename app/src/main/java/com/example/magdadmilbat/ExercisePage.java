package com.example.magdadmilbat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.MagdadMilbat.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExercisePage extends Activity implements View.OnClickListener, CameraBridgeViewBase.CvCameraViewListener2 {
    Button btnBack, btnFeedback;
    private final int PERMISSIONS_READ_CAMERA=1;
    TextView tvRepetition, tvExercise;

    static Scalar[][] rgbRange= new Scalar[3][2];
    static double ballArea;
    private CameraBridgeViewBase mOpenCvCameraView;
    int numOfFrames = 0;
    int RANGE = 30;
    Mat procImg;
    Mat circles;
    int initialY = 0;
    int firstBallFramesCounter = 0, secondBallFramesCounter = 0, thirdBallFramesCounter = 0, countOfBalls;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Camera permission required.",
                    Toast.LENGTH_LONG).show();
        } else {
            mOpenCvCameraView.setCameraPermissionGranted();
        }
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug())
        {
            mLoaderCallback.onManagerConnected((BaseLoaderCallback.SUCCESS));
        }
        else
        {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        }
    }


    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();
        if(numOfFrames == 0){
            initialY = getFrameData(frame);
        }

        frame = findContoursAndDraw(frame);
        numOfFrames++;

        return frame;
    }

    /**
     * This function prepares the image for the HoughCircles algorithm.
     * @param img the image we prepare.
     * @return the prepared image.
     */
    public static Mat prepareImage(Mat img){
        Mat dest = new Mat(); // Creating a destination Matrix (Image) for the grayscale.
        Imgproc.cvtColor(img, dest, Imgproc.COLOR_RGB2GRAY); // We grayscale the image to get it to binary form
        Imgproc.medianBlur(dest, dest, 3); // We blur the grayscale image using a kernel size of 3.
        return dest;
    }

    /**
     * This function takes the first frame and gets the initial position of the balls. It returns the initial Y axis position.
     * @param img the frame we analyze.
     * @return the initial Y axis position.
     */
    private static int getFrameData(Mat img) {
        Mat procImg = prepareImage(img);
        Mat circles = new Mat();


        Imgproc.HoughCircles(procImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 80, 95.0, 26.0, 40, 100);

        double[] c = circles.get(0, 0); // Getting the circle, c is in the format of: {x, y, radius}.
        Point center = new Point(Math.round(c[0]), Math.round(c[1]));
        double[] rgb = img.get((int)center.y, (int)center.x);
        int sensitivity = 22;
        rgbRange[0][0] = new Scalar(rgb[0] - sensitivity, rgb[1] - sensitivity, rgb[2] - sensitivity);
        rgbRange[0][1] = new Scalar(rgb[0] + sensitivity, rgb[1] + sensitivity, rgb[2] + sensitivity);
        ballArea = Math.PI * c[2] * c[2];


        c = circles.get(0, 1);
        center = new Point(Math.round(c[0]), Math.round(c[1]));
        rgb = img.get((int)center.y, (int)center.x);
        rgbRange[1][0] = new Scalar(rgb[0] - sensitivity, rgb[1] - sensitivity, rgb[2] - sensitivity);
        rgbRange[1][1] = new Scalar(rgb[0] + sensitivity, rgb[1] + sensitivity, rgb[2] + sensitivity);
        ballArea = Math.min(ballArea, Math.PI * c[2] * c[2]);

        c = circles.get(0, 2);
        center = new Point(Math.round(c[0]), Math.round(c[1]));
        rgb = img.get((int)center.y, (int)center.x);
        rgbRange[2][0] = new Scalar(rgb[0] - sensitivity, rgb[1] - sensitivity, rgb[2] - sensitivity);
        rgbRange[2][1] = new Scalar(rgb[0] + sensitivity, rgb[1] + sensitivity, rgb[2] + sensitivity);
        ballArea = Math.min(ballArea, Math.PI * c[2] * c[2]);


        return (int)Math.round(circles.get(0, 0)[1]);
    }

    private static Mat findContoursAndDraw(Mat img){
        Mat green = new Mat();
        Mat blue = new Mat();
        Mat orange = new Mat();

        Core.inRange(img, rgbRange[0][0], rgbRange[0][1], green);
        Core.inRange(img, rgbRange[1][0], rgbRange[1][1], orange);
        Core.inRange(img, rgbRange[2][0], rgbRange[2][1], blue);


        List<MatOfPoint> greenContours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> blueContours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> orangeContours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(orange, greenContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(blue, blueContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(green, greenContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = ballArea * 0.4; // 500 for second vid
        float[] radius = new float[1];
        Point center = new Point();
        for (int i = 0; i < greenContours.size(); i++) {
            MatOfPoint cnt = greenContours.get(i);
            if (Imgproc.contourArea(cnt) > maxArea) {
                MatOfPoint2f c2f = new MatOfPoint2f(cnt.toArray());
                Imgproc.minEnclosingCircle(c2f, center, radius);
                if(radius[0] * radius[0] * Math.PI <= (ballArea * 1.9)) {
                    Imgproc.circle(img, center, (int) radius[0], new Scalar(0, 0, 255), 3, 8, 0);
                }
            }
        }

        for (int i = 0; i < blueContours.size(); i++) {
            MatOfPoint cnt = blueContours.get(i);
            if (Imgproc.contourArea(cnt) > maxArea) {
                MatOfPoint2f c2f = new MatOfPoint2f(cnt.toArray());
                Imgproc.minEnclosingCircle(c2f, center, radius);
                if(radius[0] * radius[0] * Math.PI <= (ballArea * 1.9)) {
                    Imgproc.circle(img, center, (int) radius[0], new Scalar(0, 0, 255), 3, 8, 0);
                }
            }
        }

        for (int i = 0; i < orangeContours.size(); i++) {
            MatOfPoint cnt = orangeContours.get(i);
            if (Imgproc.contourArea(cnt) > maxArea) {
                MatOfPoint2f c2f = new MatOfPoint2f(cnt.toArray());
                Imgproc.minEnclosingCircle(c2f, center, radius);
                if(radius[0] * radius[0] * Math.PI <= (ballArea * 1.9)) {
                    Imgproc.circle(img, center, (int) radius[0], new Scalar(0, 0, 255), 3, 8, 0);
                }
            }
        }

        return img;
    }

    @Override
    public void onClick(View view) {
        return;
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // Ensure that this result is for the camera permission request
        if (requestCode == PERMISSIONS_READ_CAMERA) {
            // Check if the request was granted or denied
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The request was granted -> tell the camera view
                mOpenCvCameraView.setCameraPermissionGranted();
            } else {
                // The request was denied -> tell the user and exit the application
                Toast.makeText(this, "Camera permission required.",
                        Toast.LENGTH_LONG).show();
                this.finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}