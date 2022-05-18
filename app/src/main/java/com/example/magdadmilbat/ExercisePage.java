package com.example.magdadmilbat;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
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
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Random;

public class ExercisePage extends Activity implements View.OnClickListener, JavaCameraView.CvCameraViewListener2 {

    Button btnBack, btnFeedback;
    private final int PERMISSIONS_READ_CAMERA = 1;
    private final int REQUEST_CODE = 2;
    private final int RANGE = 30;
    private static final String TAG = "MyActivity";

    private static boolean isUp = false;

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

    Mat procImg;
    Mat circles;
    boolean isDone = false;
    static double greenBallFrames = 0, blueBallFrames = 0, orangeBallFrames = 0;


    static boolean isRepEntirelyComplete = false;

    /* ANIMATION VARIABLES */
    boolean started = false;
    static boolean hasanim = false;
    static ValueAnimator anim;
    static ScaleAnimation animScale;
    static TranslateAnimation animTrans, animTrans2, animTrans3, animTrans4, animTrans5, animTrans6;
    static View cricleView, cricleView2, cricleView3, cricleView4, cricleView5, cricleView6;
    static TextView remarksText;
    static float oldXscale = 1.0f;
    static long allExrDuration = 0;
    static double duration = 0.0, framH, lastOrangeHeight;
    static double requiredTime;
    static int ballToUse, repCounter = 0, blueDuration = 0, orangeDuration = 0, blueHeightSetting, orangeHeightSetting, initialY = 0;
    static Thread t;
    static Runnable r;
    Handler handler1;
    static boolean isRepEnd = true, orangeInRange = false, blueInRange = false;
    static ArrayList<Integer> repDuration = new ArrayList<Integer>();
    static int lastRepDuration = 0;
    static ArrayList<Integer> repMaxHeight = new ArrayList<Integer>();
    static int goodReputations = 0;
    static SoundPool sp;
    static int coin;

    static String[] feedbackDown, feedbackUp, feedbackAfterUp, feedbackFinish, feedbackStart;

    static boolean detectBlue, detectOrange, detectGreen;

    static long timeBallInAir = 0;

    static String repsNumTarget = "";


    /* --------------------------------------------------------------------------------------------------- */
    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
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


    public static void breathAnimation() {
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

        animTrans.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        cricleView.startAnimation(animTrans);
        cricleView2.startAnimation(animTrans2);
        cricleView3.startAnimation(animTrans3);
        cricleView4.startAnimation(animTrans4);
        cricleView5.startAnimation(animTrans5);
        cricleView6.startAnimation(animTrans6);
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

    /**
     * this function draws horizontal lines on the frame.
     *
     * @param height height of the frame.
     * @param width  width of the frame.
     */
    public static void drawHorizontalLines(Mat frame, int height, int width) {
//        final int LINE_UPPER_BOUND = 550, LINE_LOWER_BOUND = 50;
        final double LINE_UPPER_PERC = 0.236, LINE_LOWER_PERC = 0.93;
        drawLine(frame, new Point(0, height * LINE_UPPER_PERC), new Point(width, height * LINE_UPPER_PERC));
        drawLine(frame, new Point(0, height * LINE_LOWER_PERC), new Point(width, height * LINE_LOWER_PERC));
//        drawLine(frame, new Point(0, 600), new Point(width, 600));
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
     * This function prepares the image for the HoughCircles algorithm.
     *
     * @param img the image we prepare.
     * @return the prepared image.
     */
    public static Mat prepareImage(Mat img) {
        Mat dest = new Mat(); // Creating a destination Matrix (Image) for the grayscale.
        Imgproc.cvtColor(img, dest, Imgproc.COLOR_RGB2GRAY); // We grayscale the image to get it to binary form
        Imgproc.medianBlur(dest, dest, 3); // We blur the grayscale image using a kernel size of 3.
        return dest;
    }

    public static void playSoundSuccess() {
        sp.play(coin, 1, 1, 0, 0, 1);
    }

    /**
     * this function draws vertical lines on the frame.
     *
     * @param frame
     * @param FIRST_LINE
     * @param SECOND_LINE
     * @param THIRD_LINE
     */
    public static void drawVerticalLines(Mat frame, int FIRST_LINE, int SECOND_LINE, int THIRD_LINE) {
        int height = frame.height(), width = frame.width();
        drawLine(frame, new Point(FIRST_LINE, 0), new Point(FIRST_LINE, height));
        drawLine(frame, new Point(SECOND_LINE, 0), new Point(SECOND_LINE, height));
        drawLine(frame, new Point(THIRD_LINE, 0), new Point(THIRD_LINE, height));
    }

    public static double getDuration() {
        String str = spBreath.getString("duration", null);
        return Double.parseDouble(str);
    }

    public static boolean reachedReps() {
        int numOfReps = Integer.parseInt(spBreath.getString("numberOfrep", null));
        return numOfReps == greenAirTime.size();
    }

    public static int repsSuccess(int color) {
        int numOfReps = 0;
        ArrayList<Double> temp = color == 1 ? greenAirTime : color == 3 ? blueAirTime : color == 2 ? orangeAirTime : null; // gets the correct array list according to the color.
        try {
            numOfReps = temp.size();
        } catch (Exception ignored) {
        }
        return numOfReps;
    }

    public static int getDifficulty() {
        return Integer.parseInt(spBreath.getString("difficulty", null));
    }

    public static void showFeedback(String[] feedbackStrings) {
        Random random = new Random();

        int r = random.nextInt(feedbackStrings.length);
        remarksText.setText(feedbackStrings[r]);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        if (view == btnFeedback) {
            allExrDuration = (System.currentTimeMillis() - allExrDuration) / 1000;
            duration = ((double) allExrDuration);
            Intent intent = new Intent(this, Feedback.class);
            intent.putExtra("duration", duration);
            intent.putExtra("repsSuccess", repCounter);
            intent.putIntegerArrayListExtra("repDuration", repDuration);
            intent.putIntegerArrayListExtra("repMaxHeight", repMaxHeight);
            intent.putExtra("balldata", ballToUse);
            startActivity(intent);
            finish();
            startActivity(intent);
        }
    }

    private static void drawLine(Mat img, Point p1, Point p2) {
        Imgproc.line(img, p1, p2, new Scalar(0, 255, 0));
    }

    public static void onArrBlueChange() {
        double lastBlueHeight = blueHeight.get(blueHeight.size() - 1);
        float prec = (float) Math.abs((lastBlueHeight - initialY) / (initialY - 200));
        blueInRange = blueHeightSetting > (prec * 10) && prec != 0;
    }

    public static void onArrOrangeChange() {
        double lastOrangeHeight = orangeHeight.get(orangeHeight.size() - 1);
        float prec = (float) Math.abs((lastOrangeHeight - initialY) / (initialY - 200));
        orangeInRange = orangeHeightSetting > (prec * 10) && prec != 0;
    }

    private static ArrayList<Mat> toHSVImage(Mat frame) {
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);
        ArrayList<Mat> lab_list = new ArrayList<>(3);
        Core.split(hsvMat, lab_list);
        return lab_list;
    }

    private static void drawCircles(Mat frame) {
        Mat circles = new Mat();
        double[] c;
        Point center;
        Imgproc.HoughCircles(frame, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30, 70, 32.0, 40, 70);
        for (int i = 0; i < circles.width(); i++) {
            c = circles.get(0, i);
            center = new Point(Math.round(c[0]), Math.round(c[1]));
            Imgproc.circle(frame, center, (int) c[2], new Scalar(255, 0, 0), 5);
        }
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
        Point center = new Point(frame.width() / 2, frame.height() / 2); // get the center point.

        Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, 90, 1); // get rotation matrix.
        Imgproc.warpAffine(frame, resizedFrame, rotationMatrix, frame.size(), Imgproc.WARP_INVERSE_MAP); // we rotate the frame.
        frame = resizedFrame;


        final int FIRST_LINE = 10 * frame.width() / 30, SECOND_LINE = frame.width() / 2, THIRD_LINE = 19 * frame.width() / 30;

        if (isDone) {
            // if the timer is finished
            getFrameData(frame, FIRST_LINE, SECOND_LINE, THIRD_LINE);
        }

        drawHorizontalLines(frame, frame.height(), frame.width());
        drawVerticalLines(frame, FIRST_LINE, SECOND_LINE, THIRD_LINE);

        return frame;
    }

    public double getMaxHeight(int color) {
        double startingHeight = 0, maxHeight = 0;
        ArrayList<Double> temp = color == 1 ? greenHeight : color == 3 ? blueHeight : color == 2 ? orangeHeight : null;
        try {
            startingHeight = initialY;
        } catch (Exception ignored) {
        }


        for (int i = 0; i < temp.size(); i++) {
            double currHeight = temp.get(i);
            maxHeight = Math.max(Math.abs(currHeight - startingHeight), maxHeight);
        }
        return maxHeight;
    }

    public double getOverallTime(int color) {
        double overAllTime = 0;
        ArrayList<Double> temp = color == 1 ? greenAirTime : color == 3 ? blueAirTime : color == 2 ? orangeAirTime : null;
        for (int i = 0; i < temp.size(); i++) {
            overAllTime += temp.get(i);
        }
        return overAllTime;

    }

    /**
     * This function takes the first frame and gets the initial position of the balls. It returns the initial Y axis position.
     *
     * @param img the frame we analyze.
     * @return the initial Y axis position.
     */
    private void getFrameData(Mat img, int first_line, int second_line, int third_line) {
        if (allExrDuration == 0) {
            allExrDuration = System.currentTimeMillis();
//            screenFeedback(4);
        }

        Mat circles = new Mat();
        double[] c;
        Point center;
        final int H_CORE = 0, S_CORE = 1, V_CORE = 2;
        double[] rgb, rgb_min, hsvArr; // the circle's color.
        final double LINE_UPPER_PERC = 0.236, LINE_LOWER_PERC = 0.93;

        final int LINE_UPPER_BOUND = (int) (img.height() * LINE_UPPER_PERC), LINE_LOWER_BOUND = (int) (img.height() * LINE_LOWER_PERC);

        ArrayList<Mat> resultHSV = toHSVImage(img);
        Mat sMat = resultHSV.get(S_CORE);
        Imgproc.HoughCircles(sMat, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30, 70, 32.0, 40, 70);


        String orangeChecked = spBreath.getString("orange", null);
        detectBlue = false;
        detectGreen = false;
        detectOrange = false;
        requiredTime = getDuration();

        for (int i = circles.width() - 1; i >= 0; i--) {
            c = circles.get(0, i);
            center = new Point(Math.round(c[0]), Math.round(c[1]));
            int radius = (int) c[2];
            rgb = img.get((int) center.x, (int) center.y);
            rgb_min = img.get((int) center.x - radius + 10, (int) center.y);

            if (center.y > LINE_UPPER_BOUND && center.y < LINE_LOWER_BOUND) {
                if (!detectGreen && center.x > first_line - radius && center.x < first_line + radius) {
                    detectGreen = true;
                    Imgproc.circle(img, center, (int) c[2], new Scalar(255, 0, 0), 5);
                    greenHeight.add(center.y);
                } else if (!detectOrange && center.x > second_line - radius && center.x < second_line + radius) {
                    detectOrange = true;
                    Imgproc.circle(img, center, (int) c[2], new Scalar(255, 0, 0), 5);
                    orangeHeight.add(center.y);
                    long temp_timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
                    if (Math.abs(center.y - initialY) > radius && Math.abs(center.y - initialY) + radius >= ((orangeHeightSetting + 1) * (Math.abs(LINE_UPPER_BOUND - LINE_LOWER_BOUND) - 2 * radius) / 4.0)) {
                        if (orangeChecked.equals("true")) {
                            if (!isUp) {
                                screenFeedback(1);
                                isUp = true;
                                isRepEntirelyComplete = false;
                                timeBallInAir = System.currentTimeMillis();
                                breathAnimation();

                            } else if (!isRepEntirelyComplete && temp_timeBallInAir >= (long) (requiredTime * 10.0)) {
                                if (repCounter >= Integer.parseInt(repsNumTarget)) {
                                    screenFeedback(3);
                                } else {
                                    screenFeedback(2);
                                }

                                playSoundSuccess();
                                isRepEntirelyComplete = true;
                                goodReputations++;
                                orangeAirTime.add(1.0);
//                                repEnd();

                            }
                        }

                    } else if (orangeChecked.equals("true") && isUp && Math.abs(center.y - initialY) <= radius) {
                        timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
                        screenFeedback(-1);

                        isUp = false;
                    }

                    orangeInRange = true;
                } else if (!detectBlue && center.x > third_line - radius && center.x < third_line + radius) {
                    detectBlue = true;
                    Imgproc.circle(img, center, (int) c[2], new Scalar(255, 0, 0), 5);
                    if (initialY == 0)
                        initialY = (int) center.y;
                    blueHeight.add(center.y);
                    long temp_timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
                    if (Math.abs(center.y - initialY) > radius && Math.abs(center.y - initialY) + radius >= ((blueHeightSetting + 1) * (Math.abs(LINE_UPPER_BOUND - LINE_LOWER_BOUND) - 2 * radius) / 4.0)) {
                        if (orangeChecked.equals("false")) {
                            if (!isUp) {
                                screenFeedback(1);
                                isUp = true;
                                isRepEntirelyComplete = false;
                                timeBallInAir = System.currentTimeMillis();
                                breathAnimation();

                            } else if (!isRepEntirelyComplete && temp_timeBallInAir >= (long) (requiredTime * 10.0)) {
                                if (repCounter >= Integer.parseInt(repsNumTarget)) {
                                    screenFeedback(3);
                                } else {
                                    screenFeedback(2);
                                }
                                playSoundSuccess();
                                isRepEntirelyComplete = true;
                                goodReputations++;
                                blueAirTime.add(1.0);

                            }

                        }
                    } else if (orangeChecked.equals("false") && isUp && Math.abs(center.y - initialY) <= radius) {
                        timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
                        screenFeedback(-1);

                        isUp = false;
                    }

                    blueInRange = true;
                }
            }
        }
    }

    public void initialize() {
        /* IMAGE PROCESSING VARIABLES */
        greenHeight = new ArrayList<Double>();
        blueHeight = new ArrayList<Double>();
        orangeHeight = new ArrayList<Double>();

        greenAirTime = new ArrayList<Double>();
        blueAirTime = new ArrayList<Double>();
        orangeAirTime = new ArrayList<Double>();

        greenInAir = false;
        orangeInAir = false;
        blueInAir = false;

        rgbRange = new Scalar[3][2];
        hsvRange = new Scalar[3][2];

        ballArea = Double.MAX_VALUE;

        mPreviewRunning = false;

        isUp = false;


        isDone = false;
        greenBallFrames = 0;
        blueBallFrames = 0;
        orangeBallFrames = 0;

        initialY = 0;

        /* ANIMATION VARIABLES */
        started = false;
        hasanim = false;
        oldXscale = 1.0f;
        allExrDuration = 0;
        duration = 0.0;
        repCounter = 0;
        blueDuration = 0;
        orangeDuration = 0;
        isRepEnd = true;
        orangeInRange = false;
        blueInRange = false;
        repDuration = new ArrayList<>();
        repMaxHeight = new ArrayList<>();
        requiredTime = 1;
        timeBallInAir = 0;

        isRepEntirelyComplete = false;


    }

    public void screenFeedback(int choice) {
        /**
         * -1 = repEnd
         * 0 = feedbackDown,
         * 1 = feedbackUp,
         * 2 = feedbackAfterUp,
         * 3= feedbackFinish;
         */
        r = new Runnable() {
            @Override
            public void run() {
                if (choice == -1) {
                    if (timeBallInAir >= requiredTime * 10L) repCounter++;
                    Message msg = new Message();
                    msg.what = repCounter;
                    handler1.sendMessage(msg);
                    int ballDuration = ballToUse == 3 ? blueDuration : orangeDuration;
                    double ballMaxHeight = getMaxHeight(ballToUse);
                    float prec = (float) Math.abs((ballMaxHeight) / (initialY - 256));
                    int height = Math.min(((int) (prec * 100)), 100), normalized_height = 0;
                    if (height <= 100 && height >= 75) normalized_height = 100;
                    else if (height <= 75 && height >= 50) normalized_height = 75;
                    else if (height <= 50 && height >= 25) normalized_height = 50;
                    else if (height <= 25 && height >= 0) normalized_height = 25;

                    repDuration.add((int) timeBallInAir);
                    repMaxHeight.add(normalized_height);

                    blueHeight.clear();
                    orangeHeight.clear();
                    orangeDuration = 0;
                    blueDuration = 0;
                } else {
                    Message msg = new Message();
                    msg.what = -1;
                    msg.arg1 = choice;
                    handler1.sendMessage(msg);
                }
            }
        };
        t = new Thread(r);
        t.start();
    }

    /*
        Initialization of variables, properties and checking for permissions.
     */
    @SuppressLint("HandlerLeak")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        setContentView(R.layout.activity_exercise_page);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnFeedback = findViewById(R.id.btnFeedback);
        btnFeedback.setOnClickListener(this);
        cricleView = findViewById(R.id.cricleView);
        cricleView2 = findViewById(R.id.cricleView2);
        cricleView3 = findViewById(R.id.cricleView3);
        cricleView4 = findViewById(R.id.cricleView4);
        cricleView5 = findViewById(R.id.cricleView5);
        cricleView6 = findViewById(R.id.cricleView6);
        remarksText = findViewById(R.id.remarkstext);
        tvRepetition = findViewById(R.id.tvRepetition);

        feedbackDown = getResources().getStringArray(R.array.feedbackDown);
        feedbackUp = getResources().getStringArray(R.array.feedbackUp);
        feedbackAfterUp = getResources().getStringArray(R.array.feedbackAfterUp);
        feedbackFinish = getResources().getStringArray(R.array.feedbackFinish);
        feedbackStart = getResources().getStringArray(R.array.feedbackStart);
        String str1 = String.valueOf(repCounter);
        String str2 = "/";
        spBreath = getSharedPreferences("settingsBreath", 0);
        String orangeChecked = spBreath.getString("orange", null);
        if (orangeChecked.equals("true")) {
            repsNumTarget = spBreath.getString("numberOfrepOrange", null);
        } else {
            repsNumTarget = spBreath.getString("numberOfrepBlue", null);
        }
        tvRepetition.setText(str1 + str2 + repsNumTarget);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = findViewById(R.id.HelloOpenCvView);
        blueHeightSetting = Integer.parseInt(spBreath.getString("difficultyBlue", null));
        orangeHeightSetting = Integer.parseInt(spBreath.getString("difficultyOrange", null));
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                duration++;
//            }
//        }, 0, 1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(aa)
                    .build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }
        //phase 2 -load files to sp
        coin = sp.load(this, R.raw.sucssessound, 1);

        verifyPermissions();
//        String orangeChecked = spBreath.getString("orange", null);
        if (orangeChecked.equals("true")) {
            ballToUse = 2;
        } else
            ballToUse = 3;

        CountDownTimer count = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                isDone = true;
            }
        };
        count.start();

        startWaitingAnim();

        handler1 = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int a = msg.what;
                int arg = msg.arg1;
                if (a == -1) {
                    String[] arrfeedback = arg == 0 ? feedbackDown : arg == 1 ? feedbackUp : arg == 2 ? feedbackAfterUp : arg == 3 ? feedbackFinish : feedbackStart;
                    showFeedback(arrfeedback);
                } else {
                    String str1 = String.valueOf(a);
                    String str2 = "/";
                    String orangeChecked = spBreath.getString("orange", null);
                    if (orangeChecked.equals("true")) {
                        repsNumTarget = spBreath.getString("numberOfrepOrange", null);
                    } else {
                        repsNumTarget = spBreath.getString("numberOfrepBlue", null);
                    }
                    tvRepetition.setText(str1 + str2 + repsNumTarget);
                }
            }
        };
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