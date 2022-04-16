package com.example.magdadmilbat;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import java.util.Timer;
import java.util.TimerTask;

public class ExercisePage extends Activity implements View.OnClickListener, JavaCameraView.CvCameraViewListener2 {
    Button btnBack, btnFeedback;
    private final int PERMISSIONS_READ_CAMERA = 1;
    private final int REQUEST_CODE = 2;
    private final int RANGE = 30;
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

    Mat procImg;
    Mat circles;
    boolean isDone = false;
    static double greenBallFrames = 0, blueBallFrames = 0, orangeBallFrames = 0;

    /* ANIMATION VARIABLES */
    boolean started = false;
    static boolean hasanim = false;
    static ValueAnimator anim;
    static ScaleAnimation animScale;
    static TranslateAnimation animTrans, animTrans2, animTrans3, animTrans4, animTrans5, animTrans6;
    static View cricleView, cricleView2, cricleView3, cricleView4, cricleView5, cricleView6;
    static TextView remarksText;
    static float oldXscale = 1.0f;
    static double duration = 0.0, framH, lastOrangeHeight;
    static int ballToUse, repCounter = 0, blueDuration = 0, orangeDuration = 0, blueHeightSetting, orangeHeightSetting, initialY = 0;
    static Thread t;
    static Runnable r;
    Handler handler1;
    static boolean isRepEnd = true, orangeInRange = false, blueInRange = false;
    static ArrayList<Integer> repDuration = new ArrayList<Integer>();
    static ArrayList<Integer> repMaxHeight = new ArrayList<Integer>();

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

    /**
     * This function takes the first frame and gets the initial position of the balls. It returns the initial Y axis position.
     *
     * @param img the frame we analyze.
     * @return the initial Y axis position.
     */
    private static void getFrameData(Mat img, int first_line, int second_line, int third_line) {
        Mat circles = new Mat();
        double[] c;
        Point center;
        final int H_CORE = 0, S_CORE = 1, V_CORE = 2;

        ArrayList<Mat> resultHSV = toHSVImage(img);
        Mat sMat = resultHSV.get(S_CORE);
        Imgproc.HoughCircles(sMat, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30, 70, 32.0, 40, 70);


        double[] rgb, rgb_min, hsvArr; // the circle's color.
        final int LINE_UPPER_BOUND = 550, LINE_LOWER_BOUND = 50;

        String orangeChecked = spBreath.getString("orange", null);


        for (int i = 0; i < circles.width(); i++) {
            c = circles.get(0, i);
            center = new Point(Math.round(c[0]), Math.round(c[1]));
            int radius = (int) c[2];
            rgb = img.get((int) center.x, (int) center.y);
            rgb_min = img.get((int) center.x - radius + 10, (int) center.y);

            if (i <= 2) {
                if (center.x > first_line - radius && center.x < first_line + radius) {
                    Imgproc.circle(img, center, (int) c[2], new Scalar(255, 0, 0), 5);

                    greenHeight.add(center.y);
                } else if (center.x > second_line - radius && center.x < second_line + radius) {
                    Imgproc.circle(img, center, (int) c[2], new Scalar(255, 0, 0), 5);
                    orangeHeight.add(center.y);
                    if (Math.abs(center.y - orangeHeight.get(0)) > radius && Math.abs(center.y - orangeHeight.get(0)) + radius >= (orangeHeightSetting * (Math.abs((LINE_UPPER_BOUND - 2 * radius) - LINE_LOWER_BOUND) / 10.0))) {
                        orangeAirTime.add(1.0); // TODO: make it a counter.
                        if (orangeChecked.equals("true")) {
                            r = new Runnable() {
                                @Override
                                public void run() {
                                    breathAnimation();
                                }
                            };
                            t = new Thread(r);
                            t.start();
                        }
                    }

                    orangeInRange = true;
                } else if (center.x > third_line - radius && center.x < third_line + radius) {
                    Imgproc.circle(img, center, (int) c[2], new Scalar(255, 0, 0), 5);
                    if (initialY == 0)
                        initialY = (int) center.y;
                    blueHeight.add(center.y);
                    if (Math.abs(center.y - blueHeight.get(0)) > radius && Math.abs(center.y - blueHeight.get(0)) + radius >= (blueHeightSetting * (Math.abs((LINE_UPPER_BOUND - 2 * radius) - LINE_LOWER_BOUND) / 10.0))) {
                        blueAirTime.add(1.0);
                        if (orangeChecked.equals("false")) {
                            r = new Runnable() {
                                @Override
                                public void run() {
                                    breathAnimation();
                                }
                            };
                            t = new Thread(r);
                            t.start();
                        }
                    }

                    blueInRange = true;
                }
            }
        }
    }

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

        isDone = false;
        greenBallFrames = 0;
        blueBallFrames = 0;
        orangeBallFrames = 0;

        initialY = 0;

        /* ANIMATION VARIABLES */
        started = false;
        hasanim = false;
        oldXscale = 1.0f;
        duration = 0.0;
        repCounter = 0;
        blueDuration = 0;
        orangeDuration = 0;
        isRepEnd = true;
        orangeInRange = false;
        blueInRange = false;
        repDuration = new ArrayList<>();
        repMaxHeight = new ArrayList<>();
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

    /**
     * this function draws horizontal lines on the frame.
     *
     * @param height height of the frame.
     * @param width  width of the frame.
     */
    public static void drawHorizontalLines(Mat frame, int height, int width) {
        final int LINE_UPPER_BOUND = 550, LINE_LOWER_BOUND = 50;
        drawLine(frame, new Point(0, height - LINE_UPPER_BOUND), new Point(width, height - LINE_UPPER_BOUND));
        drawLine(frame, new Point(0, height - LINE_LOWER_BOUND), new Point(width, height - LINE_LOWER_BOUND));
        drawLine(frame, new Point(0, 600), new Point(width, 600));
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

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        if (view == btnFeedback) {
            Intent intent = new Intent(this, Feedback.class);
            intent.putExtra("duration",duration);
            intent.putExtra("repsSuccess",repCounter);
            intent.putIntegerArrayListExtra("repDuration",repDuration);
            intent.putIntegerArrayListExtra("repMaxHeight",repMaxHeight);
            startActivity(intent);
            finish();
            startActivity(intent);
        }
    }

    public static boolean reachedReps() {
        int numOfReps = Integer.parseInt(spBreath.getString("numberOfrep", null));
        return numOfReps == greenAirTime.size();
    }

    public static int repsSuccess(int color) {
        int numOfReps = 0;
        ArrayList<Double> temp = color == 1 ? greenAirTime : color == 2 ? blueAirTime : color == 3 ? orangeAirTime : null; // gets the correct array list according to the color.
        numOfReps = temp.size();
        return numOfReps;
    }

    public static int getDifficulty() {
        return Integer.parseInt(spBreath.getString("difficulty", null));
    }

    public static int getDuration() {
        return Integer.parseInt(spBreath.getString("duration", null));
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
        //Imgproc.HoughCircles(frame, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30,100, 35.0, frame.width() / 20, frame.width() / 6);
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
        ArrayList<Double> temp = color == 1 ? greenHeight : color == 2 ? blueHeight : color == 3 ? orangeHeight : null;
        try {
            startingHeight = temp.get(0);
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
        ArrayList<Double> temp = color == 1 ? greenAirTime : color == 2 ? blueAirTime : color == 3 ? orangeAirTime : null;
        for (int i = 0; i < temp.size(); i++) {
            overAllTime += temp.get(i);
        }
        return overAllTime;

    }

    public void repEnd() {
        repCounter++;
//        tvRepetition.setText(String.valueOf(repCounter));
        int ballDuration = ballToUse == 2 ? blueDuration : orangeDuration;
        double ballMaxHeight = getMaxHeight(ballToUse);
        float prec = (float) Math.abs((ballMaxHeight - initialY) / (initialY - 200));
        repDuration.add(ballDuration);
        repMaxHeight.add((int) prec*10);
        blueHeight.clear();
        orangeHeight.clear();
        orangeDuration = 0;
        blueDuration = 0;
    }

    /*
        Initialization of variables, properties and checking for permissions.
     */
    @Override
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
        tvRepetition.setText(String.valueOf(repCounter));
        spBreath = getSharedPreferences("settingsBreath", 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = findViewById(R.id.HelloOpenCvView);
        blueHeightSetting = Integer.parseInt(spBreath.getString("difficultyBlue", null));
        orangeHeightSetting = Integer.parseInt(spBreath.getString("difficultyOrange", null));

        verifyPermissions();
        String orangeChecked = spBreath.getString("orange", null);
        if (orangeChecked.equals("true")) {
            ballToUse = 2;
        } else
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
            }
        }, 0, 1000);
        startWaitingAnim();

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