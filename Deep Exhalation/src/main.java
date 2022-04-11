import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class main {
    private static final String filename = "src/thirdvid.mp4";

    static Scalar[][] hsvRange= new Scalar[3][2];
    static Scalar[][] rgbRange= new Scalar[3][2];
    static double ballArea;

    /* IMAGE PROCESSING VARIABLES */
    static ArrayList<Double> greenHeight = new ArrayList<Double>();
    static ArrayList<Double> blueHeight = new ArrayList<Double>();
    static ArrayList<Double> orangeHeight = new ArrayList<Double>();

    static ArrayList<Double> greenAirTime = new ArrayList<Double>();
    static ArrayList<Double> blueAirTime = new ArrayList<Double>();
    static ArrayList<Double> orangeAirTime = new ArrayList<Double>();


    static boolean greenInAir = false, orangeInAir = false, blueInAir = false, blueInRange = false;
    static final int orangeHeightSetting = 1, blueHeightSetting = 1;
    static double greenBallFrames = 0, blueBallFrames = 0, orangeBallFrames = 0;
    static boolean started = false;
    static int initialY = 0;

    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Loading the OpenCV library.
        int numOfFrames = 0;
        int RANGE = 30;
        Mat procImg;
        Mat circles;
        int firstBallFramesCounter = 0, secondBallFramesCounter = 0, thirdBallFramesCounter = 0, countOfBalls;
        Mat img = new Mat();

        img = loadImage("src/Screenshot_2.png");
        final int FIRST_LINE = 10 * img.width() / 30, SECOND_LINE = img.width() / 2, THIRD_LINE = 19 * img.width() / 30;

        drawHorizontalLines(img, img.height(), img.width());
        drawVerticalLines(img, FIRST_LINE, SECOND_LINE, THIRD_LINE);

        initialY = getFrameData(img, FIRST_LINE, SECOND_LINE, THIRD_LINE);

        showImage(img);
    }

    private static void drawLine(Mat img, Point p1, Point p2) {
        Imgproc.line(img, p1, p2, new Scalar(0, 255, 0));
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

    public static void processVideo(){
        VideoCapture vid = new VideoCapture(filename);
        Mat frame = new Mat();
        JFrame jframe = new JFrame("Video"); // We create a new JFrame object.
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // We inform jframe what to do when we close the program.
        JLabel vidPanel = new JLabel(); // We create a new JLabel object.
        jframe.setContentPane(vidPanel); // We assign vidpanel to the jframe we created.
        jframe.setSize(2000, 4000); // We set the frame size to 2000x4000
        jframe.setVisible(true); // We make the jframe visible.
        double fps = vid.get(Videoio.CAP_PROP_FPS);
        int initialY = 0;

        while(vid.read(frame)) {
            final int FIRST_LINE = 10 * frame.width() / 30, SECOND_LINE = frame.width() / 2, THIRD_LINE = 19 * frame.width() / 30;

            if (initialY == -1) {
                // if we have yet to find the balls.
                drawHorizontalLines(frame, frame.height(), frame.width());
                drawVerticalLines(frame, FIRST_LINE, SECOND_LINE, THIRD_LINE);
            }
            initialY = getFrameData(frame, FIRST_LINE, SECOND_LINE, THIRD_LINE);

            ImageIcon result = new ImageIcon(Mat2BufferedImage(frame));
            vidPanel.setIcon(result);
            vidPanel.repaint();
        }
        showImage(frame);
    }

    private static void toHSVImage(Mat frame){
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);
        ArrayList<Mat> lab_list = new ArrayList<>(3);
        Core.split(hsvMat,lab_list);
    }

    private static void drawLine(Mat img, Point p1, Point p2) {
        Imgproc.line(img, p1, p2, new Scalar(0, 255, 0));
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

    /**
     * This function takes the first frame and gets the initial position of the balls. It returns the initial Y axis position.
     * @param img the frame we analyze.
     * @return the initial Y axis position.
     */
    private static int getFrameData(Mat img, int first_line, int second_line, int third_line) {
        Mat procImg = prepareImage(img);
        Mat circles = new Mat();
        Mat hsvMat = new Mat();
        double[] rgb, rgb_min, hsvArr; // the circle's color.
        boolean orangeInRange = false;
        double[] c; // a circle.
        Point center; // the circle's center.
        final int LINE_UPPER_BOUND = 550, LINE_LOWER_BOUND = 50;

        Imgproc.cvtColor(img, hsvMat, Imgproc.COLOR_RGB2HSV);
        Scalar hsv;
        showImage(hsvMat);
        Imgproc.HoughCircles(procImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30, 95, 55.0, procImg.width() / 20, procImg.width() / 6);
        if (circles.width() == 0) {
            return -1;
        }

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

                    } else if (center.x > second_line - radius && center.x < second_line + radius) {
                        orangeHeight.add(center.y);
                        if (Math.abs(center.y - orangeHeight.get(0)) > radius && Math.abs(center.y - orangeHeight.get(0)) + radius >= (orangeHeightSetting * (Math.abs((LINE_UPPER_BOUND - 2 * radius) - LINE_LOWER_BOUND) / 10.0))) {
                            orangeAirTime.add(1.0); // TODO: make it a counter.
                        }

                        orangeInRange = true;
                        rgbRange[1][0] = new Scalar(rgb[0], rgb[1], rgb[2]);
                        rgbRange[1][1] = new Scalar(rgb_min[0], rgb_min[1], rgb_min[2]);
                        hsvRange[1][0] = new Scalar(hsvArr[0], hsvArr[1], hsvArr[2]);

                    } else if (center.x > third_line - radius && center.x < third_line + radius) {
                        blueHeight.add(center.y);
                        if (Math.abs(center.y - blueHeight.get(0)) > radius && Math.abs(center.y - blueHeight.get(0)) + radius >= (blueHeightSetting * (Math.abs((LINE_UPPER_BOUND - 2 * radius) - LINE_LOWER_BOUND) / 10.0))) {
                            blueAirTime.add(1.0);
                        }

                        blueInRange = true;

                        rgbRange[2][0] = new Scalar(rgb[0], rgb[1], rgb[2]);
                        rgbRange[2][1] = new Scalar(rgb_min[0], rgb_min[1], rgb_min[2]);
                        hsvRange[2][0] = new Scalar(hsvArr[0], hsvArr[1], hsvArr[2]);
                    }
                }
                ballArea = Math.min(ballArea, Math.PI * radius * radius);
            }
        } catch (NullPointerException e) {
            return -1;
        }
        //TODO: return something else when findContoursAndDraw will work
        return -1;
    }

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

    /**
     * This function loads the image from the path into a matrix.
     * @param imagePath the path of the image we load.
     * @return the image represented in a matrix.
     */
    public static Mat loadImage(String imagePath) {
        Imgcodecs imageCodecs = new Imgcodecs(); // We create an Imgcodecs Object.
        return imageCodecs.imread(imagePath); // We return a Mat Object representing the image.
    }

    /**
     * This function displays the image in a new window.
     * @param image the image to display.
     */
    public static void showImage(Mat image){
        HighGui.imshow("result", image);
        HighGui.waitKey();
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
     * This function converts a matrix to a buffered image.
     * @param m the matrix we convert.
     */
    public static BufferedImage Mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
}

