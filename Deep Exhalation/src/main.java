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

    static Scalar[][] rgbRange= new Scalar[3][2];
    static double ballArea;
    /* IMAGE PROCESSING VARIABLES */
    static ArrayList<Double> greenHeight = new ArrayList<Double>();
    static ArrayList<Double> blueHeight = new ArrayList<Double>();
    static ArrayList<Double> orangeHeight = new ArrayList<Double>();

    static ArrayList<Double> greenAirTime = new ArrayList<Double>();
    static ArrayList<Double> blueAirTime = new ArrayList<Double>();
    static ArrayList<Double> orangeAirTime = new ArrayList<Double>();


    static boolean greenInAir = false, orangeInAir = false, blueInAir = false;
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

        VideoCapture vid = new VideoCapture(filename);
        Mat img = new Mat();
        JFrame jframe = new JFrame("Video"); // We create a new JFrame object.
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // We inform jframe what to do when we close the program.
        JLabel vidPanel = new JLabel(); // We create a new JLabel object.
        jframe.setContentPane(vidPanel); // We assign vidpanel to the jframe we created.
        jframe.setSize(2000, 4000); // We set the frame size to 2000x4000
        jframe.setVisible(true); // We make the jframe visible.
        double fps = vid.get(Videoio.CAP_PROP_FPS);


        /* --------------------------------------------------------------------------------------------------- */;
        /*
        while(vid.read(img)) {
            if (numOfFrames == 0) {
                initialY = getFrameData(img);
            }

            findContoursAndDraw(img);

            ImageIcon result = new ImageIcon(Mat2BufferedImage(img));
            vidPanel.setIcon(result);
            vidPanel.repaint();
            numOfFrames++;
        }

         */


        img = loadImage("src/Screenshot_9.png");

        initialY = getFrameData(img);
        //findContoursAndDraw(img);
        showImage(img);



    }

    /**
     * This function takes the first frame and gets the initial position of the balls. It returns the initial Y axis position.
     * @param img the frame we analyze.
     * @return the initial Y axis position.
     */
    private static int getFrameData(Mat img) {
        Mat procImg = prepareImage(img);
        //procImg = procImg.submat(procImg.height() - 50, procImg.height(), 0, procImg.width());
        Mat circles = new Mat();
        int sensitivity = 22;

        Imgproc.HoughCircles(procImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30, 78.0, 28.0, img.width() / 20, img.width() / 6);
        if(circles.cols() == 0){
            return -1;
        }
        double[] c; // a circle.
        Point center; // the circle's center.
        double[] rgb; // the circle's color.
        try {
            for (int i = 0; i <= 2; i++) {
                c = circles.get(0, i);
                center = new Point(Math.round(c[0]), Math.round(c[1]));
                rgb = img.get((int) center.y, (int) center.x);
                Imgproc.circle(img, center, (int)c[2], new Scalar(0, 255, 0), 3, 8, 0);
                rgbRange[i][0] = new Scalar(rgb[0] - sensitivity, rgb[1] - sensitivity, rgb[2] - sensitivity); // we set the lower rgb bound of the ball.
                rgbRange[i][1] = new Scalar(rgb[0] + sensitivity, rgb[1] + sensitivity, rgb[2] + sensitivity); // we set the higher rgb bound of the ball.
                ballArea = Math.min(ballArea, Math.PI * c[2] * c[2]);
            }
        }catch(NullPointerException e){ return -1; }
        showImage(img);

        return (int)Math.round(circles.get(0, 0)[1]); // we return the initial Y --> This will be improved.
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

