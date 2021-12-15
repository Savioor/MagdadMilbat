import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;


import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Loading the OpenCV library.
        // Mat img = loadImage("src/Screenshot_2.png"); // Getting the image.
        VideoCapture vid = new VideoCapture("src/firstvid.mp4");
        Mat img = new Mat();
        JFrame jframe = new JFrame("Video"); // We create a new JFrame object.
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // We inform jframe what to do when we close the program.
        JLabel vidPanel = new JLabel(); // We create a new JLabel object.
        jframe.setContentPane(vidPanel); // We assign vidpanel to the jframe we created.
        jframe.setSize(2000, 4000); // We set the frame size to 2000x4000
        jframe.setVisible(true); // We make the jframe visible.

        while(vid.read(img)) {
            Mat procImg = prepareImage(img); // We prepare the image.

        /*
            We apply the HoughCircles algorithm, and get an array named circles that represents each circle we find.
            https://docs.opencv.org/4.x/dd/d1a/group__imgproc__feature.html#ga47849c3be0d0406ad3ca45db65a25d2d
         */
            Mat circles = new Mat(); // Will represent the circles we find.
            Imgproc.HoughCircles(procImg, circles, Imgproc.HOUGH_GRADIENT, 1.0, 80, 105.0, 28.0, 40, 100);
            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x); // Getting the circle, c is in the format of: {x, y, radius}.
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // Drawing the circle's center.
                Imgproc.circle(img, center, 1, new Scalar(0, 100, 100), 3, 8, 0);
                // Drawing the circle's outlines.
                int radius = (int) Math.round(c[2]);
                Imgproc.circle(img, center, radius, new Scalar(255, 0, 255), 3, 8, 0);
            }
            ImageIcon result = new ImageIcon(Mat2BufferedImage(img));
            vidPanel.setIcon(result);
            vidPanel.repaint();

            // showImage(img);
       }
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
        Imgproc.medianBlur(dest, dest, 3); // We blur the grayscale image using a kernel size of 7.
        return dest;
    }

    public static BufferedImage Mat2BufferedImage(Mat m) {
        //Method converts a Mat to a Buffered Image
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
