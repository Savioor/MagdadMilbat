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
    private final int PERMISSIONS_READ_CAMERA = 1;
    private final int REQUEST_CODE = 2;
    private static final String TAG = "MyActivity";

    TextView tvRepetition, tvExercise;

    static SharedPreferences spBreath;

    /* IMAGE PROCESSING VARIABLES */
    static ArrayList<Double> greenHeight = new ArrayList<Double>();
    static ArrayList<Double> blueHeight = new ArrayList<Double>();
    static ArrayList<Double> orangeHeight = new ArrayList<Double>();

    static ArrayList<Double> greenAirTime = new ArrayList<Double>();
    static ArrayList<Double> blueAirTime = new ArrayList<Double>();
    static ArrayList<Double> orangeAirTime = new ArrayList<Double>();


    static boolean greenInAir = false, orangeInAir = false, blueInAir = false;

    static Scalar[][] rgbRange = new Scalar[3][2];
    static Scalar[][] hsvRange = new Scalar[3][2];

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
    static boolean isRepEnd = true;
    static double lastOrangeHeight;
    static int ballToUse;
    static int repCounter = 0;
    static ArrayList<Integer> repDuration = new ArrayList<Integer>();
    static ArrayList<Integer> repMaxHeight = new ArrayList<Integer>();
    static boolean orangeInRange = false;
    static boolean blueInRange = false;
    static int blueDuration = 0;
    static int orangeDuration = 0;
    static int blueHeightSetting;
    static int orangeHeightSetting;
    /* --------------------------------------------------------------------------------------------------- */

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
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
        tvRepetition = findViewById(R.id.tvRepetition);
        tvRepetition.setText(String.valueOf(repCounter));
        spBreath = getSharedPreferences("settingsBreath", 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.HelloOpenCvView);
        blueHeightSetting = Integer.parseInt(spBreath.getString("difficultyBlue", null));
        orangeHeightSetting = Integer.parseInt(spBreath.getString("difficultyOrange", null));

        verifyPermissions();
        String orangeChecked = spBreath.getString("orange", null);
        if (Boolean.valueOf(orangeChecked) == true)
            ballToUse = 2;
        else
            ballToUse = 3;

        CountDownTimer count = new CountDownTimer(5000, 1000) {
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

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                duration++;

                if (repsSuccess(ballToUse) > repCounter) {
                    repEnd();
                }

                if (blueInRange) {
                    blueDuration++;
                }
                if (orangeInRange) {
                    orangeDuration++;
                }
//            Random rand = new Random();
//            double randomValue = 200 + (600 - 200) * rand.nextDouble();
//            fakeHeight = randomValue;
            }
        }, 0, 1000);
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
        if (OpenCVLoader.initDebug()) {
            mLoaderCallback.onManagerConnected((BaseLoaderCallback.SUCCESS));
        } else {
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
            intent.putExtra("greenAirTime", getOverallTime(1));
            intent.putExtra("blueAirTime", getOverallTime(2));
            intent.putExtra("orangeAirTime", getOverallTime(3));
            intent.putExtra("greenMaxHeight", getMaxHeight(1));
            intent.putExtra("blueMaxHeight", getMaxHeight(2));
            intent.putExtra("orangeMaxHeight", getMaxHeight(3));
            intent.putExtra("greenRepSuccess", repsSuccess(1));
            intent.putExtra("blueRepSuccess", repsSuccess(2));
            intent.putExtra("orangeRepSuccess", repsSuccess(3));
            intent.putExtra("duration", duration);
            intent.putIntegerArrayListExtra("repDuration", repDuration);
            intent.putIntegerArrayListExtra("repMaxHeight", repMaxHeight);
            startActivity(intent);
        }
    }

    private void verifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permission");
        String[] permissions = {Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ExercisePage.this,
                    permissions, REQUEST_CODE);
            Toast.makeText(this, "Camera permission required.",
                    Toast.LENGTH_LONG).show();
        } else {
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
    public void onPause() {
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
     *
     * @param inputFrame the current frame.
     * @return the frame after it's marked.
     */
    @Override
    public Mat onCameraFrame(JavaCameraView.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba(); // we get the frame in rgb.
        Mat resizedFrame = new Mat();
        Point center = new Point(frame.width() / 2, frame.height() / 2);
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, 90, 1);
        Imgproc.warpAffine(frame, resizedFrame, rotationMatrix, frame.size(), Imgproc.WARP_INVERSE_MAP);

        frame = resizedFrame;


        int line_upper_bound = 550, line_bottom_bound = 50;
        int first_line = 10 * frame.width() / 30, second_line = frame.width() / 2, third_line = 19 * frame.width() / 30;

        if (isDone && initialY == -1) {
            initialY = getFrameData(frame, first_line, second_line, third_line);
//            initialY = 2;
        }

//        frame = initialY == -1 ? frame : findContoursAndDraw(frame);
//        frame = initialY == -1 ? frame : setMask(frame);


        if (initialY == -1) {
            frame = drawLine(frame, new Point(0, frame.height() - line_upper_bound), new Point(frame.width(), frame.height() - line_upper_bound));
            frame = drawLine(frame, new Point(0, frame.height() - line_bottom_bound), new Point(frame.width(), frame.height() - line_bottom_bound));
            frame = drawLine(frame, new Point(0, 600), new Point(frame.width(), 600));


            frame = drawLine(frame, new Point(first_line, 0), new Point(first_line, frame.height()));
            frame = drawLine(frame, new Point(second_line, 0), new Point(second_line, frame.height()));
            frame = drawLine(frame, new Point(third_line, 0), new Point(third_line, frame.height()));
        } else {
//            getColumn(1, frame, first_line, 600, line_bottom_bound);
            getColumn(2, frame, second_line, 600, line_bottom_bound);
            getColumn(3, frame, third_line, 600, line_bottom_bound);

        }

//        getColumn(1, frame, first_line,640,140);
//        getColumn(2, frame, second_line,640,140);
//        getColumn(3, frame, third_line,640,140);
//        frame = setMask(frame);
        framH = frame.height() - 300;

        if (orangeHeight.size() > 1) {
            lastOrangeHeight = orangeHeight.get(orangeHeight.size() - 1);
            if (!isRepEnd && lastOrangeHeight < 400)
                isRepEnd = true;

            r = new Runnable() {
                @Override
                public void run() {
                    lastOrangeHeight = orangeHeight.get(orangeHeight.size() - 1);
                    if (lastOrangeHeight > 400 && isRepEnd) {
                        isRepEnd = false;
                        breathAnimation();
                    }

                }
            };
            t = new Thread(r);
            t.start();
        }
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);

        return frame;
    }

    public static Mat getColumn(int colunmNum, Mat frame, int line, int floor_index, int top_index) {
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);

        double[] arr = new double[frame.height()];
        int col = line;
        // red = 0, green = 1, blue = 2
        int color = colunmNum == 1 ? 1 : colunmNum == 2 ? 0 : colunmNum == 3 ? 2 : 0;
//        double max_rgb_index = floor_index, max_rgb_val = 0;
        double min_dist_index = floor_index, min__dist_val = 120000;
        int sum = 0;
        for (int row = floor_index; row > top_index; row--) {
            arr[row] = norm(hsvMat.get(col, row), hsvRange[color][0]);
        }

        for (int row = floor_index; row > top_index; row--) {
            sum = 0;
            for (int i =-5; i < 5; i++) {
                if (arr[row + i] != 0)
                    sum += arr[row + i];
            }
            if (sum < min__dist_val) {
                min_dist_index = row;
                min__dist_val =sum;
            }
        }

        if (color == 0)
            Imgproc.circle(frame, new Point(col, min_dist_index), (int) 10, new Scalar(255, 0, 0), 5);
        else if (color == 1)
            Imgproc.circle(frame, new Point(col, min_dist_index), (int) 10, new Scalar(0, 255, 0), 5);
        else
            Imgproc.circle(frame, new Point(col, min_dist_index), (int) 10, new Scalar(0, 0, 255), 5);
        return frame;
    }

    public static double norm(double[] d1, Scalar s1) {
        return Math.sqrt(Math.pow(d1[0] - s1.val[0], 2) +
                Math.pow(d1[1] - s1.val[1], 2) +
                Math.pow(d1[2] - s1.val[2], 2));
    }

    public static Mat setMask(Mat frame) {
//        Mat temp= new Mat();
//        Imgproc.cvtColor(frame,temp,40);
//        Scalar low= new Scalar(10,0,0);
//        Scalar high= new Scalar(20,255,255);
//        Mat mask = new Mat();
//        inRange(temp,low,high,mask);
//        frame.setTo(new Scalar(0,0,255),mask);

        //

        Mat temp = new Mat();
        Imgproc.cvtColor(frame, temp, Imgproc.COLOR_RGB2HSV);
        Scalar low = rgbRange[2][0];
        Scalar high = rgbRange[2][1];

        Mat mask = new Mat();
        Mat img = new Mat();
        Core.inRange(temp, low, high, mask);
        img.setTo(new Scalar(0, 0, 255), mask);

        return img;
    }

    /**
     * This function prepares the image for the HoughCircles algorithm.
     *
     * @param img the image we prepare.
     * @return the prepared image.
     */
    public static Mat prepareImage(Mat img) {
        Mat dest = new Mat(); // Creating a destination Matrix (Image) for the grayscale.
        Imgproc.cvtColor(img, dest, Imgproc.COLOR_RGB2HSV); // We grayscale the image to get it to binary form
        Imgproc.medianBlur(dest, dest, 3); // We blur the grayscale image using a kernel size of 3.

        return dest;
    }

    /**
     * This function takes the first frame and gets the initial position of the balls. It returns the initial Y axis position.
     *
     * @param img the frame we analyze.
     * @return the initial Y axis position.
     */
    private static int getFrameData(Mat img, int first_line, int second_line, int third_line) {
        Mat procImg = prepareImage(img);
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(img, hsvMat, Imgproc.COLOR_RGB2HSV);
        Scalar hsv;
        Mat circles = new Mat();
        int sensitivity = 12;
//        while (circles.width() < 3) {
        Imgproc.HoughCircles(procImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30, 95, 55.0, procImg.width() / 20, procImg.width() / 6);
        if (circles.width() == 0) {
            return -1;
        }
        int x;
        x = 2;
        double[] c; // a circle.
        Point center; // the circle's center.
        double[] rgb, rgb_min, hsvArr; // the circle's color.
        try {
            for (int i = 0; i < circles.width(); i++) {
                c = circles.get(0, i);
                center = new Point(Math.round(c[0]), Math.round(c[1]));
                int radius = (int) c[2];
                Imgproc.circle(img, center, (int) c[2], new Scalar(255, 0, 0), 5);
                rgb = img.get((int) center.x, (int) center.y);
                rgb_min = img.get((int) center.x - radius + 10, (int) center.y);

                hsvArr = hsvMat.get((int) center.x, (int) center.y);

                if (i <= 2) {
                    if (center.x > first_line - radius && center.x < first_line + radius) {
                        greenHeight.add(center.y);
                        rgbRange[0][0] = new Scalar(rgb[0], rgb[1], rgb[2]);
                        rgbRange[0][1] = new Scalar(rgb_min[0], rgb_min[1], rgb_min[2]);

                        hsvRange[0][0] = new Scalar(hsvArr[0], hsvArr[1], hsvArr[2]);
                        ;
//                        hsvRange[0][1] = new Scalar(hsv[0], hsv[1], hsv[2]);

                    } else if (center.x > second_line - radius && center.x < second_line + radius) {
                        orangeHeight.add(center.y);
                        orangeInRange = true;
//                        onArrOrangeChange();
                        rgbRange[1][0] = new Scalar(rgb[0], rgb[1], rgb[2]);
                        rgbRange[1][1] = new Scalar(rgb_min[0], rgb_min[1], rgb_min[2]);

                        hsvRange[1][0] = new Scalar(hsvArr[0], hsvArr[1], hsvArr[2]);
                        ;

                    } else if (center.x > third_line - radius && center.x < third_line + radius) {
                        blueHeight.add(center.y);
//                        onArrBlueChange();
                        blueInRange = true;

                        rgbRange[2][0] = new Scalar(rgb[0], rgb[1], rgb[2]);
                        rgbRange[2][1] = new Scalar(rgb_min[0], rgb_min[1], rgb_min[2]);

                        hsvRange[2][0] = new Scalar(hsvArr[0], hsvArr[1], hsvArr[2]);
                        ;

                    }
                }
                ballArea = Math.min(ballArea, Math.PI * radius * radius);
            }
        } catch (NullPointerException e) {
            return -1;
        }
//        }
        if (blueInRange && orangeInRange) {
            return 2;
        }
        return -1;// we return the initial Y --> This will be improved.
    }


    public static Scalar getAvgHSV(int x, int y, int r, Mat hsvFrame) {
        double[] temp;
        int counter = 0, hAvg = 0, sAvg = 0, vAvg = 0;
        for (int i = 0; i < (int) Math.sqrt(2) * r; i++) {
            for (int j = 0; j < (int) Math.sqrt(2) * r; j++) {
                counter++;
                temp = hsvFrame.get((int) (x - r / Math.sqrt(2) + i), (int) (x - r / Math.sqrt(2) + j));
                hAvg += temp[0];
                sAvg += temp[1];
                vAvg += temp[2];
            }
        }
        hAvg /= counter;
        sAvg /= counter;
        vAvg /= counter;
        return new Scalar(hAvg, sAvg, vAvg);
    }

    public static boolean reachedReps() {
        int numOfReps = Integer.parseInt(spBreath.getString("numberOfrep", null));
        return numOfReps == greenAirTime.size();
    }

    public static int repsSuccess(int color) {
        int numOfReps = 0;
        ArrayList<Double> temp = color == 1 ? greenAirTime : color == 2 ? blueAirTime : color == 3 ? orangeAirTime : null;
        numOfReps = temp.size();
        return numOfReps;
    }

    public static int getDifficulty() {
        return Integer.parseInt(spBreath.getString("difficulty", null));
    }

    public static int getDuration() {
        return Integer.parseInt(spBreath.getString("duration", null));
    }

    /**
     * This function gets an image (which is a singular frame), finds the balls, draws them and returns the frame.
     *
     * @param img the image to analyze.
     * @return processed image.
     */
    private static Mat findContoursAndDraw(Mat img) {
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
//        Core.inRange(img, rgbRange[0][0], rgbRange[0][1], green);
        Core.inRange(img, rgbRange[1][0], rgbRange[1][1], orange);
        Core.inRange(img, rgbRange[2][0], rgbRange[2][1], blue);
        /* --------------------------------------------------------------------------------------------------------- */

        /*
         * Creating separate contour lists for each of the frames.
         */
//        List<MatOfPoint> greenContours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> blueContours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> orangeContours = new ArrayList<MatOfPoint>();
        /* --------------------------------------------------------------------------------------------------------- */

        /*
         * We find the contours of each color.
         */
        Imgproc.findContours(orange, orangeContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(blue, blueContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//        Imgproc.findContours(green, greenContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        /* --------------------------------------------------------------------------------------------------------- */

        double maxArea = ballArea * 0.4;
        float[] radius = new float[1];
        Point center = new Point();

        /*
         * We iterate over each of the contours in each color and only mark the contours that can be the balls.
         * We do that by factoring the area of each contour.
         */
        for (int i = 0; i < orangeContours.size(); i++) {
            MatOfPoint cnt = orangeContours.get(i);
            if (Imgproc.contourArea(cnt) > maxArea) {
                MatOfPoint2f c2f = new MatOfPoint2f(cnt.toArray());
                Imgproc.minEnclosingCircle(c2f, center, radius);
                if (radius[0] * radius[0] * Math.PI <= (ballArea * 1.9)) {
                    Imgproc.circle(img, center, (int) radius[0], new Scalar(0, 0, 255), 3, 8, 0);
                    double currHeight = Math.abs(center.y - initialY);
                    orangeHeight.add(currHeight);
                    if (currHeight > 30) {
                        if (!orangeInAir) orangeInAir = true;
                        orangeBallFrames++;
                    } else {
                        if (orangeInAir) {
                            orangeInAir = false;
                            orangeAirTime.add(orangeBallFrames);
                            orangeBallFrames = 0;
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
                if (radius[0] * radius[0] * Math.PI <= (ballArea * 1.9)) {
                    Imgproc.circle(img, center, (int) radius[0], new Scalar(0, 0, 255), 3, 8, 0);
                    double currHeight = Math.abs(center.y - initialY);
                    blueHeight.add(currHeight);
                    if (currHeight > 30) {
                        if (!blueInAir) blueInAir = true;
                        blueBallFrames++;
                    } else {
                        if (blueInAir) {
                            blueInAir = false;
                            blueAirTime.add(greenBallFrames);
                            blueBallFrames = 0;
                        }
                    }
                }
            }
        }

        /* --------------------------------------------------------------------------------------------------------- */

        return img;
    }


    public double getMaxHeight(int color) {
        double maxHeight = 0;
        ArrayList<Double> temp = color == 1 ? greenHeight : color == 2 ? blueHeight : color == 3 ? orangeHeight : null;
        for (int i = 0; i < temp.size(); i++) {
            double currHeight = temp.get(i);
            maxHeight = Math.max(maxHeight, currHeight);
        }
        return maxHeight;
    }

    public double getOverallTime(int color) {
        double overAllTime = 0;
        ArrayList<Double> temp = color == 1 ? greenAirTime : color == 2 ? blueAirTime : color == 3 ? orangeAirTime : null;
        for (int i = 0; i < temp.size(); i++) {
            overAllTime += temp.get(i);
        }
        return overAllTime;

    }

    private Mat drawLine(Mat img, Point p1, Point p2) {
        Imgproc.line(img, p1, p2, new Scalar(0, 255, 0));
        return img;
    }

    public void repEnd() {
        repCounter++;
        tvRepetition.setText(String.valueOf(repCounter));
        int ballDuration = ballToUse == 2 ? blueDuration : orangeDuration;
        double ballMaxHeight = getMaxHeight(ballToUse);
        float prec = (float) Math.abs((ballMaxHeight - initialY) / (initialY - 200));
        repDuration.add(ballDuration);
        repMaxHeight.add((int) prec * 10);
        blueHeight.clear();
        orangeHeight.clear();
    }

    public static void onArrBlueChange() {
        double lastBlueHeight = blueHeight.get(blueHeight.size() - 1);
        float prec = (float) Math.abs((lastBlueHeight - initialY) / (initialY - 200));
        if (blueHeightSetting > (prec * 10) && prec != 0)
            blueInRange = true;
        else
            blueInRange = false;
    }

    public static void onArrOrangeChange() {
        double lastOrangeHeight = orangeHeight.get(orangeHeight.size() - 1);
        float prec = (float) Math.abs((lastOrangeHeight - initialY) / (initialY - 200));
        if (orangeHeightSetting > (prec * 10) && prec != 0)
            orangeInRange = true;
        else
            orangeInRange = false;
    }

    public void breathAnimation() {
        animTrans = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -35, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -50);
        animTrans2 = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 35, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -50);
        animTrans3 = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -60, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        animTrans4 = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 60, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        animTrans5 = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -35, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 50);
        animTrans6 = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 35, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 50);
        animTrans.setDuration(1000);
        animTrans2.setDuration(1000);
        animTrans3.setDuration(1000);
        animTrans4.setDuration(1000);
        animTrans5.setDuration(1000);
        animTrans6.setDuration(1000);

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
    }

    public void startWaitingAnim() {
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