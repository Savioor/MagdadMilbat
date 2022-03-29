package com.example.magdadmilbat;

import org.opencv.core.Size;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import org.opencv.core.Size;
import android.content.Intent;
import android.content.SharedPreferences;
import android.animation.ValueAnimator;
import org.opencv.core.Size;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import androidx.core.app.ActivityCompat;
import org.opencv.android.JavaCameraView;
import java.io.IOException;


import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ExercisePage extends Activity implements View.OnClickListener, JavaCameraView.CvCameraViewListener2 {
    Button btnBack, btnFeedback;
    private final int PERMISSIONS_READ_CAMERA=1;
    private final int REQUEST_CODE = 2;
    private static final String TAG = "MyActivity";

    TextView tvRepetition, tvExercise;

    static SharedPreferences spBreath ;

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
    private JavaCameraView mOpenCvCameraView;
    private SurfaceHolder mSurfaceHolder;
    boolean mPreviewRunning = false;
    int RANGE = 30;
    Mat procImg;
    Mat circles;
    static int initialY = -1;
    boolean isDone = false;
    static double greenBallFrames = 0, blueBallFrames = 0, orangeBallFrames = 0;
    boolean started = false;
    static boolean hasanim = false;
    static ValueAnimator anim;
    static ScaleAnimation animScale;
    static TranslateAnimation animTrans;
    static TranslateAnimation animTrans2;
    static TranslateAnimation animTrans3;
    static TranslateAnimation animTrans4;
    static TranslateAnimation animTrans5;
    static TranslateAnimation animTrans6;
    static View cricleView;
    static View cricleView2;
    static View cricleView3;
    static View cricleView4;
    static View cricleView5;
    static View cricleView6;
    static TextView remarksText;
    static float oldXscale = 1.0f;
    static double duration = 0.0;
    Thread t;
    Runnable r;
    Handler handler1;
    static double framH;
//    static double fakeHeight;
    static boolean isRepEnd= true;
    static double lastOrangeHeight;
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
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnFeedback = (Button) findViewById(R.id.btnFeedback);
        btnFeedback.setOnClickListener(this);
        cricleView = findViewById(R.id.cricleView);
        cricleView2 = findViewById(R.id.cricleView2);
        cricleView3 = findViewById(R.id.cricleView3);
        cricleView4 = findViewById(R.id.cricleView4);
        cricleView5 = findViewById(R.id.cricleView5);
        cricleView6 = findViewById(R.id.cricleView6);
        remarksText = findViewById(R.id.remarkstext);
        spBreath = getSharedPreferences("settingsBreath", 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.HelloOpenCvView);

        verifyPermissions();

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

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                duration++;
//            Random rand = new Random();
//            double randomValue = 200 + (600 - 200) * rand.nextDouble();
//            fakeHeight = randomValue;
            }
        },0,1000);
        startWaitingAnim();

//        handler1 = new Handler(){
//            @Override
//            public void handleMessage(Message msg){
//                super.handleMessage(msg);
//                int a = msg.what;
//                if(a == 1){
//                    breathAnimation();
//                }
//            }
//        };
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
    public void onClick(View view) {
        if (view == btnBack) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (view == btnFeedback) {
            Intent intent = new Intent(this, Feedback.class);
            intent.putExtra("greenAirTime",getOverallTime(1));
            intent.putExtra("blueAirTime",getOverallTime(2));
            intent.putExtra("orangeAirTime",getOverallTime(3));
            intent.putExtra("greenMaxHeight",getMaxHeight(1));
            intent.putExtra("blueMaxHeight",getMaxHeight(2));
            intent.putExtra("orangeMaxHeight",getMaxHeight(3));
            intent.putExtra("greenRepSuccess",repsSuccess(1));
            intent.putExtra("blueRepSuccess",repsSuccess(2));
            intent.putExtra("orangeRepSuccess",repsSuccess(3));
            intent.putExtra("duration",duration);
            startActivity(intent);
        }
    }
    private void verifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permission");
        String[] permissions = {Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ExercisePage.this,
                    permissions, REQUEST_CODE);
            Toast.makeText(this, "Camera permission required.",
                    Toast.LENGTH_LONG).show();
        }
        else{
            // here the open camera code must be
            mOpenCvCameraView.setCameraPermissionGranted();
            mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
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
    public Mat onCameraFrame(JavaCameraView.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba(); // we get the frame in rgb.
        Mat resizedFrame = new Mat();
        Point center = new Point(frame.width() / 2, frame.height() / 2);
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(center,90, 1);
        Imgproc.warpAffine(frame, resizedFrame, rotationMatrix, frame.size(), Imgproc.WARP_INVERSE_MAP);

        frame = resizedFrame;
        if(isDone) {
            initialY = getFrameData(frame);
        }

        frame = initialY == -1 ? frame : findContoursAndDraw(frame);
        if(initialY == -1){
            frame = drawLine(frame, new Point(0, frame.height() - 100), new Point(frame.width(), frame.height() - 100));
            frame = drawLine(frame, new Point(0, frame.height() - 300), new Point(frame.width(), frame.height() - 300));
        }
        framH = frame.height()-300;

        Log.d("testHeight", String.valueOf(orangeHeight.size()));
        if(orangeHeight.size() >1){
            lastOrangeHeight  = orangeHeight.get(orangeHeight.size()-1);

            if(isRepEnd == false && lastOrangeHeight < 400)
                isRepEnd =true;

            r = new Runnable() {
                @Override
                public void run() {
                    lastOrangeHeight  = orangeHeight.get(orangeHeight.size()-1);
                    if(lastOrangeHeight > 400 && isRepEnd){
                        isRepEnd = false;
                        breathAnimation();
                    }
//                    Message msg = new Message();
//                    double lastOrangeHeight  = orangeHeight.get(orangeHeight.size()-1);
////                    double preOrangeHeight  = orangeHeight.get(orangeHeight.size()-2);
//                    double preOrangeHeight = framH;
//                    if(Math.abs(preOrangeHeight-lastOrangeHeight) < 0.5)
//                        msg.what = 0;
//                    else
//                        msg.what = 1;
//
//                    handler1.sendMessage(msg);
                }
            };
            t= new Thread(r);
            t.start();
        }
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
        // procImg = procImg.submat(procImg.height() - 300, procImg.height() - 100, 0, procImg.width());
        Mat circles = new Mat();
        int sensitivity = 22;
        //Imgproc.HoughCircles(procImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 80, 95.0, 26.0, 40, 100);
        while(circles.width() < 3) {
            Imgproc.HoughCircles(procImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30, 95, 55.0, procImg.width() / 20, procImg.width() / 6);
            if (circles.width() == 0) {
                return -1;
            }
            double[] c; // a circle.
            Point center; // the circle's center.
            double[] rgb; // the circle's color.
            try {
                for (int i = 0; i <= circles.width(); i++) {
                    c = circles.get(0, i);
                    center = new Point(Math.round(c[0]), Math.round(c[1]));
                    rgb = img.get((int) center.y, (int) center.x);
                    if(i <= 2)
                        rgbRange[i][0] = new Scalar(rgb[0] - sensitivity, rgb[1] - sensitivity, rgb[2] - sensitivity); // we set the lower rgb bound of the ball./rgbRange[i][1] = new Scalar(rgb[0] + sensitivity, rgb[1] + sensitivity, rgb[2] + sensitivity); // we set the higher rgb bound of the ball.
                    ballArea = Math.min(ballArea, Math.PI * c[2] * c[2]);
                    Imgproc.circle(img, center, (int) c[2], new Scalar(255, 0, 0), 5);
                }
            } catch (NullPointerException e) {
                return -1;
            }
        }
        return (int)Math.round(circles.get(0, 0)[1]); // we return the initial Y --> This will be improved.
    }

    public static boolean reachedReps(){
        int numOfReps = Integer.parseInt(spBreath.getString("numberOfrep", null));
        if(numOfReps == greenAirTime.size()){
            return true;
        }
        return false;
    }

    public static int repsSuccess(int color){
        int numOfReps = 0;
        ArrayList<Double> temp = color == 1 ? greenAirTime : color == 2 ? blueAirTime : color == 3 ? orangeAirTime : null;
        numOfReps = temp.size();
        return  numOfReps;
    }

    public static int getDifficulty(){
        return Integer.parseInt(spBreath.getString("difficulty", null));
    }

    public static int getDuration(){
        return Integer.parseInt(spBreath.getString("duration", null));
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
        FRONTEND - But, this function checks if we had gotten camera permissions, if we did not, it asks for them,
        if we did, it updates the camera view accordingly.
     */
//    @Override
//    public void onRequestPermissionsResult(
//            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        // Ensure that this result is for the camera permission request
//        if (requestCode == PERMISSIONS_READ_CAMERA) {
//            // Check if the request was granted or denied
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // The request was granted --> tell the camera view
//                mOpenCvCameraView.setCameraPermissionGranted();
//            } else {
//                // The request was denied --> tell the user and exit the application
//                Toast.makeText(this, "Camera permission required.",
//                        Toast.LENGTH_LONG).show();
//                this.finish();
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

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

    public void breathAnimation(){
//        double lastOrangeHeight  = orangeHeight.get(orangeHeight.size()-1);
//        float scale = (float) (Math.abs(lastOrangeHeight - initialY) / (initialY - 200));
//            Random rand = new Random();
//            double randomValue = 0 + (400 - 0) * rand.nextDouble();
//            float scale = (float) ((randomValue - 0) / (400 - 0));
//        animTrans = new TranslateAnimation(Animation.ABSOLUTE,Math.abs(cricleView.getTranslationX()*oldXscale),Animation.ABSOLUTE,Math.abs(cricleView.getTranslationX()*scale),Animation.ABSOLUTE,Math.abs(cricleView.getTranslationY()*oldXscale),Animation.ABSOLUTE,Math.abs(cricleView.getTranslationY()*scale));
//        animTrans2 = new TranslateAnimation(Animation.ABSOLUTE,-(cricleView2.getTranslationX()*oldXscale),Animation.ABSOLUTE,-(cricleView2.getTranslationX()*scale),Animation.ABSOLUTE,Math.abs(cricleView2.getTranslationY()*oldXscale),Animation.ABSOLUTE,Math.abs(cricleView2.getTranslationY()*scale));
//        animTrans3 = new TranslateAnimation(Animation.ABSOLUTE,-(cricleView3.getTranslationX()*oldXscale),Animation.ABSOLUTE,-(cricleView3.getTranslationX()*scale),Animation.ABSOLUTE,Math.abs(cricleView3.getTranslationY()*oldXscale),Animation.ABSOLUTE,Math.abs(cricleView3.getTranslationY()*scale));
//        animTrans4 = new TranslateAnimation(Animation.ABSOLUTE,-(cricleView4.getTranslationX()*oldXscale),Animation.ABSOLUTE,-(cricleView4.getTranslationX()*scale),Animation.ABSOLUTE,-(cricleView4.getTranslationY()*oldXscale),Animation.ABSOLUTE,-(cricleView4.getTranslationY()*scale));
//        animTrans5 = new TranslateAnimation(Animation.ABSOLUTE,Math.abs(cricleView5.getTranslationX()*oldXscale),Animation.ABSOLUTE,Math.abs(cricleView5.getTranslationX()*scale),Animation.ABSOLUTE,-(cricleView5.getTranslationY()*oldXscale),Animation.ABSOLUTE,-(cricleView5.getTranslationY()*scale));
//        animTrans6 = new TranslateAnimation(Animation.ABSOLUTE,-(cricleView6.getTranslationX()*oldXscale),Animation.ABSOLUTE,-(cricleView6.getTranslationX()*scale),Animation.ABSOLUTE,-(cricleView6.getTranslationY()*oldXscale),Animation.ABSOLUTE,-(cricleView6.getTranslationY()*scale));
        animTrans = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,-35,Animation.ABSOLUTE,0,Animation.ABSOLUTE,-50);
        animTrans2 = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,35,Animation.ABSOLUTE,0,Animation.ABSOLUTE,-50);
        animTrans3 = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,-60,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
        animTrans4 = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,60,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
        animTrans5 = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,-35,Animation.ABSOLUTE,0,Animation.ABSOLUTE,50);
        animTrans6 = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,35,Animation.ABSOLUTE,0,Animation.ABSOLUTE,50);
        animTrans.setDuration(1000);
        animTrans2.setDuration(1000);
        animTrans3.setDuration(1000);
        animTrans4.setDuration(1000);
        animTrans5.setDuration(1000);
        animTrans6.setDuration(1000);
//        animScale = new ScaleAnimation(oldXscale, scale, oldXscale, scale , Animation.ABSOLUTE, (float)0.5, Animation.ABSOLUTE, (float)0.5);
//        animScale.setDuration(1000);
//        cricleView.startAnimation(animScale);
//        cricleView2.startAnimation(animScale);
//        cricleView3.startAnimation(animScale);
//        cricleView4.startAnimation(animScale);
//        cricleView5.startAnimation(animScale);
//        cricleView6.startAnimation(animScale);
//        animScale.setFillAfter(true);
//        animTrans.setFillAfter(true);
//        animTrans2.setFillAfter(true);
//        animTrans3.setFillAfter(true);
//        animTrans4.setFillAfter(true);
//        animTrans5.setFillAfter(true);
//        animTrans6.setFillAfter(true);
        animTrans.setRepeatMode(Animation.REVERSE);
        animTrans2.setRepeatMode(Animation.REVERSE);
        animTrans3.setRepeatMode(Animation.REVERSE);
        animTrans4.setRepeatMode(Animation.REVERSE);
        animTrans5.setRepeatMode(Animation.REVERSE);
        animTrans6.setRepeatMode(Animation.REVERSE);
        animTrans.setRepeatCount(1);
        animTrans2.setRepeatCount(1);
        animTrans3.setRepeatCount(1);
        animTrans4.setRepeatCount(1);
        animTrans5.setRepeatCount(1);
        animTrans6.setRepeatCount(1);

        cricleView.startAnimation(animTrans);
        cricleView2.startAnimation(animTrans2);
        cricleView3.startAnimation(animTrans3);
        cricleView4.startAnimation(animTrans4);
        cricleView5.startAnimation(animTrans5);
        cricleView6.startAnimation(animTrans6);
//        oldXscale = scale;
    }
    public void startWaitingAnim(){
        anim = ValueAnimator.ofFloat(0.4f, 1.0f);
        anim.setDuration(1000);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = Float.parseFloat(animation.getAnimatedValue().toString());
                remarksText.setAlpha(scale);
                remarksText.setAlpha(scale);
            }
        });
        anim.start();
    }
}