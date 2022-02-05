package com.example.magdadmilbat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
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

    /* IMAGE PROCESSING VARIABLES */
    static ArrayList<Double> greenHeight = new ArrayList<Double>();
    static ArrayList<Double> blueHeight = new ArrayList<Double>();
    static ArrayList<Double> orangeHeight = new ArrayList<Double>();

    static ArrayList<Double> greenAirTime = new ArrayList<Double>();
    static ArrayList<Double> blueAirTime = new ArrayList<Double>();
    static ArrayList<Double> orangeAirTime = new ArrayList<Double>();

    static boolean greenInAir = false, orangeInAir = false, blueInAir = false;

    static Scalar[][] rgbRange= new Scalar[3][2];
    static double ballArea = Double.MAX_VALUE;
    private CameraBridgeViewBase mOpenCvCameraView;
    int RANGE = 30;
    Mat procImg;
    Mat circles;
    static int initialY = -1;
    boolean isDone = false;
    static double greenBallFrames = 0, blueBallFrames = 0, orangeBallFrames = 0;
    /* --------------------------------------------------------------------------------------------------- */

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

    /*
        Initialization of variables, properties and checking for permissions.
     */
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
        CountDownTimer count = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                return;
            }

            @Override
            public void onFinish() {
                isDone = true;
            }
        };
        count.start();
    }

    /*
        Initialization of the OpenCV Library.
     */
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

    /*
        Must be implemented in order to extend the view listener.
     */
    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    /*
        Must be implemented in order to extend the view listener.
     */
    @Override
    public void onCameraViewStopped() {
    }

    /**
     * Gets each camera frame and handles it accordingly.
     * @param inputFrame the current frame.
     * @return the frame after it's marked.
     */
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba(); // we get the frame in rgb.

        if(isDone) {
            initialY = getFrameData(frame);
        }

        frame = initialY == -1 ? frame : findContoursAndDraw(frame);
        frame = drawLine(frame, new Point(0, frame.height() - 100), new Point(frame.width(), frame.height() - 100));
        frame = drawLine(frame, new Point(0, frame.height() - 300), new Point(frame.width(), frame.height() - 300));
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
        int sensitivity = 22;

        Imgproc.HoughCircles(procImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 80, 95.0, 26.0, 40, 100);
        if(circles.cols() == 0){
            return -1;
        }
        double[] c; // a circle.
        Point center; // the circle's center.
        double[] rgb; // the circle's color.

        for(int i = 0; i <= 2; i++){
            c = circles.get(0, i);
            center = new Point(Math.round(c[0]), Math.round(c[1]));
            rgb = img.get((int)center.y, (int)center.x);

            rgbRange[i][0] = new Scalar(rgb[0] - sensitivity, rgb[1] - sensitivity, rgb[2] - sensitivity); // we set the lower rgb bound of the ball.
            rgbRange[i][1] = new Scalar(rgb[0] + sensitivity, rgb[1] + sensitivity, rgb[2] + sensitivity); // we set the higher rgb bound of the ball.
            ballArea = Math.min(ballArea, Math.PI * c[2] * c[2]);
        }

        return (int)Math.round(circles.get(0, 0)[1]); // we return the initial Y --> This will be improved.
    }

    /**
     * This function gets an image (which is a singular frame), finds the balls, draws them and returns the frame.
     * @param img the image to analyze.
     * @return processed image.
     */
    private static Mat findContoursAndDraw(Mat img){
        /*
         * We divide the frame to 3 separate frames, each representing one ball.
         */
        Mat green = new Mat();
        Mat blue = new Mat();
        Mat orange = new Mat();
        /* --------------------------------------------------------------------------------------------------------- */

        /*
         * Each frame will consist only of the pixels in the color range of each ball.
         */
        Core.inRange(img, rgbRange[0][0], rgbRange[0][1], green);
        Core.inRange(img, rgbRange[1][0], rgbRange[1][1], orange);
        Core.inRange(img, rgbRange[2][0], rgbRange[2][1], blue);
        /* --------------------------------------------------------------------------------------------------------- */

        /*
         * Creating separate contour lists for each of the frames.
         */
        List<MatOfPoint> greenContours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> blueContours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> orangeContours = new ArrayList<MatOfPoint>();
        /* --------------------------------------------------------------------------------------------------------- */

        /*
         * We find the contours of each color.
         */
        Imgproc.findContours(orange, greenContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(blue, blueContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(green, greenContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        /* --------------------------------------------------------------------------------------------------------- */

        double maxArea = ballArea * 0.4;
        float[] radius = new float[1];
        Point center = new Point();

        /*
         * We iterate over each of the contours in each color and only mark the contours that can be the balls.
         * We do that by factoring the area of each contour.
         */
        for (int i = 0; i < greenContours.size(); i++) {
            MatOfPoint cnt = greenContours.get(i);
            if (Imgproc.contourArea(cnt) > maxArea) {
                MatOfPoint2f c2f = new MatOfPoint2f(cnt.toArray());
                Imgproc.minEnclosingCircle(c2f, center, radius);
                if(radius[0] * radius[0] * Math.PI <= (ballArea * 1.9)) {
                    Imgproc.circle(img, center, (int) radius[0], new Scalar(0, 0, 255), 3, 8, 0);
                    double currHeight = Math.abs(center.y - initialY);
                    greenHeight.add(currHeight);
                    if(currHeight > 30){
                        if(!greenInAir) greenInAir = true;
                        greenBallFrames++;
                    }
                    else{
                        if(greenInAir){
                            greenInAir = false;
                            greenAirTime.add(greenBallFrames);
                            greenBallFrames = 0; 
                        } 
                    }
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
                    double currHeight = Math.abs(center.y - initialY);
                    blueHeight.add(currHeight);
                    if(currHeight > 30){
                        if(!blueInAir) blueInAir = true;
                        blueBallFrames++;
                    }
                    else{
                        if(blueInAir){
                            blueInAir = false;
                            blueAirTime.add(greenBallFrames);
                            blueBallFrames = 0; 
                        } 
                    }
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
                    double currHeight = Math.abs(center.y - initialY);
                    orangeHeight.add(currHeight);
                    if(currHeight > 30){
                        if(!orangeInAir) orangeInAir = true;
                        orangeBallFrames++;
                    }
                    else{
                        if(orangeInAir){
                            orangeInAir = false;
                            orangeAirTime.add(orangeBallFrames);
                            orangeBallFrames = 0; 
                        }
                    }
                }
            }
        }
        /* --------------------------------------------------------------------------------------------------------- */

        return img;
    }

    /*
        FRONTEND.
     */
    @Override
    public void onClick(View view) {
        return;
    }

    /*
        FRONTEND - But, this function checks if we had gotten camera permissions, if we did not, it asks for them,
        if we did, it updates the camera view accordingly.
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // Ensure that this result is for the camera permission request
        if (requestCode == PERMISSIONS_READ_CAMERA) {
            // Check if the request was granted or denied
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The request was granted --> tell the camera view
                mOpenCvCameraView.setCameraPermissionGranted();
            } else {
                // The request was denied --> tell the user and exit the application
                Toast.makeText(this, "Camera permission required.",
                        Toast.LENGTH_LONG).show();
                this.finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public double getMaxHeight(int color){
       double maxHeight = 0;
       ArrayList<Double> temp = color == 1 ? greenHeight : color == 2 ? blueHeight : color == 3 ? orangeHeight : null;
       for(int i = 0; i < temp.size(); i++){
            double currHeight = temp.get(i);
            maxHeight = maxHeight > currHeight ? maxHeight : currHeight;
       }
       return maxHeight;
    }

    public double getOverallTime(int color){
       double overAllTime = 0;
       ArrayList<Double> temp = color == 1 ? greenAirTime : color == 2 ? blueAirTime : color == 3 ? orangeAirTime : null;
       for(int i = 0; i < temp.size(); i++){
            overAllTime += temp.get(i);
       }

       return overAllTime;
        
    }

    private Mat drawLine(Mat img, Point p1, Point p2){
        Imgproc.line(img, p1, p2, new Scalar(0, 255, 0));
        return img;
    }


}
