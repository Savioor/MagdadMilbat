# Deep Exhalation - A Magdad Project

Deep Exhalation ('נשיפה עמוקה') is an application written in Java in collaboration with a non-profit
organization in order to assist people with disabilities. The application consists of two main
modules; image recognition and processing and application development (e.g. the backend and the
frontend).

## Backend

The code in the backend is written using the OpenCV library for Android. Given the frames of the
phone camera, the code processes them and finds the three colored balls and their position.

An example for an image it recieves may be:
[Will insert pictures]

### Explanation of the balls detection algorithm

Firstly, we'll begin with our image processing varaibles:

```java
static ArrayList<Double> greenHeight=new ArrayList<Double>();
static ArrayList<Double> blueHeight=new ArrayList<Double>();
static ArrayList<Double> orangeHeight=new ArrayList<Double>();

static ArrayList<Double> greenAirTime=new ArrayList<Double>();
static ArrayList<Double> blueAirTime=new ArrayList<Double>();
static ArrayList<Double> orangeAirTime=new ArrayList<Double>();

static boolean greenInAir=false,orangeInAir=false,blueInAir=false;

``` 

greenHeight, blueHeight and orangeHeight are all to be used to store the height of each ball
throughout the video. 

greenAirTime, blueAirTime, and orangeAirTime are arraylists that hold the times of each ball in the air. 

greenInAir, orangeInAir and blueInAir are boolean variables that hold infomration about the ball's current position.

Going forward, every time a frame is being read from the camera, the onCameraFrame function is called, the function is as follows:
```java
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


    final int FIRST_LINE = 10 * frame.width() / 30, SECOND_LINE = 14 * frame.width() / 30, THIRD_LINE = 18 * frame.width() / 30;

    if (isDone) {
        // if the timer is finished
        getFrameData(frame, FIRST_LINE, SECOND_LINE, THIRD_LINE);
    }

    drawHorizontalLines(frame, frame.height(), frame.width());
    drawVerticalLines(frame, FIRST_LINE, SECOND_LINE, THIRD_LINE);

    return frame;
}
 ```

This is the function that executes all of the processing functions, and so, we shall go over it and understand it.

Firsly, we get the frame by writing the line
```java 
 Mat frame = inputFrame.rgba();
 ```
 Using .rgba() gives us the frame in it's rgb form.
 
 After we get the frame, we rotate it as the frame comes rotated as input, we rotate the frame as follows:
 
 ```java
 Mat resizedFrame = new Mat();
 Point center = new Point(frame.width() / 2, frame.height() / 2); // get the center point.

 Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, 90, 1); // get rotation matrix.
 Imgproc.warpAffine(frame, resizedFrame, rotationMatrix, frame.size(), Imgproc.WARP_INVERSE_MAP); // we rotate the frame.
 frame = resizedFrame;
 ```
 
 Now, after our frame is resized, rotated and ready to go, we begin the processing.
 There's something important about the next part of the function, as seen, there's an if statement checking whether a variable called "isDone" is true, the variable isDone is used to determine whether 10 seconds had passed, only after 10 seconds passed we begin taking our data and processing the images. While 10 seconds have still not passed, we only draw lines using the variables we have created here:
 ```java
  final int FIRST_LINE = 10 * frame.width() / 30, SECOND_LINE = 14 * frame.width() / 30, THIRD_LINE = 18 * frame.width() / 30;
  ```
 The rest of the function, if so, is this:
 ```
if (isDone) {
    // if the timer is finished
    getFrameData(frame, FIRST_LINE, SECOND_LINE, THIRD_LINE);
}

drawHorizontalLines(frame, frame.height(), frame.width());
drawVerticalLines(frame, FIRST_LINE, SECOND_LINE, THIRD_LINE);

return frame;
 ```
 And the return of the frame is for the frame to be shown on screen.
 
 As you can see, there are calls for three different functions; getFrameData(), drawHorizontalLines(), and drawVerticalLines(). The former two needn't be discussed, as their implementation is quite straightforward, but we will discuss getFrameData(), which is our biggest function.
 
 The function starts off with declarations of variables and initializations, these are straightforward and have meaningful names or uses so they shall not be explained.
 ```java
Mat circles = new Mat();
double[] c;
Point center;
final int H_CORE = 0, S_CORE = 1, V_CORE = 2;
final double LINE_UPPER_PERC = 0.28, LINE_LOWER_PERC = 0.93;
final int BLUE = 0, ORANGE = 1, GREEN = 2;
Ball[] balls = new Ball[3];
for (int i = 0; i < 3; i++) {
    balls[i] = new Ball(0, new Point(0, 0));
}

final int LINE_UPPER_BOUND = (int) (img.height() * LINE_UPPER_PERC), LINE_LOWER_BOUND = (int) (img.height() * LINE_LOWER_PERC);
 ```
 
 Afterwards, we take the RGB and turn it into an HSV image using toHSVImage(), get the S core of the frame, and apply the HoughCircles algorithm to our frame, like so:
 ```java
ArrayList<Mat> resultHSV = toHSVImage(img);
Mat sMat = resultHSV.get(S_CORE);
Imgproc.HoughCircles(sMat, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30, 70, 32.0, 40, 70);
 ```
 Afterwards, we use a for loop and get the most accurate balls using their locations like so:
 ```java
for (int i = circles.width() - 1; i >= 0; i--) {
c = circles.get(0, i);
center = new Point(Math.round(c[0]), Math.round(c[1]));
int radius = (int) c[2];

if (center.y > LINE_UPPER_BOUND && center.y < LINE_LOWER_BOUND) {
    if (center.x > first_line - radius && center.x < first_line + radius) {
        if (balls[GREEN].getRadius() == 0 || balls[GREEN].getCenter().y > center.y) {
            balls[GREEN] = new Ball(radius, center);
        }
    } else if (center.x > second_line - radius && center.x < second_line + radius) {
        if (balls[ORANGE].getRadius() == 0 || balls[ORANGE].getCenter().y > center.y) {
            balls[ORANGE] = new Ball(radius, center);
        }
    } else if (center.x > third_line - radius && center.x < third_line + radius) {
        if (balls[BLUE].getRadius() == 0 || balls[BLUE].getCenter().y > center.y) {
            balls[BLUE] = new Ball(radius, center);
        }
    }
 }
}
```
After we get the most accurate balls and their positions, we iterate over each of the balls, add their current height, check whether a reputation has been performed and play animations:
```java
int radius = balls[ORANGE].getRadius();
center = balls[ORANGE].getCenter();
Imgproc.circle(img, center, radius, new Scalar(255, 0, 0), 5);

if (orangeChecked.equals("true") && radius > 0) {
    if (initialY == 0)
        initialY = (int) center.y;
    orangeHeight.add(center.y);
    long temp_timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
    if (Math.abs(center.y - initialY) > radius && Math.abs(center.y - initialY) + radius >= ((orangeHeightSetting + 1) * (Math.abs(LINE_UPPER_BOUND - LINE_LOWER_BOUND) - 2 * radius) / 4.0)) {
        if (!isUp) {
            screenFeedback(1);
            isUp = true;
            isRepEntirelyComplete = false;
            timeBallInAir = System.currentTimeMillis();
        } else if (!isRepEntirelyComplete && temp_timeBallInAir >= (long) (requiredTime * 10.0)) {
            if (repCounter >= Integer.parseInt(repsNumTarget)) {
                if (requiredTime > 0)
                    screenFeedback(3);
            } else {
                screenFeedback(2);
            }
            playSoundSuccess();
            breathAnimation();
            isRepEntirelyComplete = true;
            goodReputations++;
            orangeAirTime.add(1.0);
        }

    } else if (isUp && Math.abs(center.y - initialY) <= radius) {
        timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
        screenFeedback(-1);
        if (repCounter + 1 >= Integer.parseInt(repsNumTarget)) {
            screenFeedback(3);
        } else {
            screenFeedback(5);
        }

        isUp = false;
    }
    orangeInRange = true;

}
/* Handle blue ball */
radius = balls[BLUE].getRadius();
center = balls[BLUE].getCenter();
Imgproc.circle(img, center, radius, new Scalar(255, 0, 0), 5);

if (radius > 0 && orangeChecked.equals("false")) {
    if (initialY == 0)
        initialY = (int) center.y;
    blueHeight.add(center.y);
    long temp_timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
    if (Math.abs(center.y - initialY) > radius && Math.abs(center.y - initialY) + radius >= ((blueHeightSetting + 1) * (Math.abs(LINE_UPPER_BOUND - LINE_LOWER_BOUND) - 2 * radius) / 4.0)) {
        if (!isUp) {
            screenFeedback(1);
            isUp = true;
            isRepEntirelyComplete = false;
            timeBallInAir = System.currentTimeMillis();

        } else if (!isRepEntirelyComplete && temp_timeBallInAir >= (long) (requiredTime * 10.0)) {
            if (repCounter >= Integer.parseInt(repsNumTarget)) {
                screenFeedback(3);
            } else {
                screenFeedback(2);
            }
            playSoundSuccess();
            breathAnimation();

            isRepEntirelyComplete = true;
            goodReputations++;
            blueAirTime.add(1.0);
        }

    } else if (isUp && Math.abs(center.y - initialY) <= radius) {
        timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
        screenFeedback(-1);
        if (repCounter + 1 >= Integer.parseInt(repsNumTarget)) {
            screenFeedback(3);
        } else {
            screenFeedback(5);
        }

    }

    blueInRange = true;
}
} catch (Exception e) {
System.out.println("Catch exeption");
}
```
And the whole function:
```java
/**
* This function takes the first frame and gets the initial position of the balls. It returns the initial Y axis position.
*
* @param img the frame we analyze.
* @return the initial Y axis position.
*/
private void getFrameData(Mat img, int first_line, int second_line, int third_line) {
   try {
       if (allExrDuration == 0) {
           allExrDuration = System.currentTimeMillis();
           screenFeedback(4);
       }

       Mat circles = new Mat();
       double[] c;
       Point center;
       final int H_CORE = 0, S_CORE = 1, V_CORE = 2;
       final double LINE_UPPER_PERC = 0.28, LINE_LOWER_PERC = 0.93;
       final int BLUE = 0, ORANGE = 1, GREEN = 2;
       Ball[] balls = new Ball[3];
       for (int i = 0; i < 3; i++) {
           balls[i] = new Ball(0, new Point(0, 0));
       }

       final int LINE_UPPER_BOUND = (int) (img.height() * LINE_UPPER_PERC), LINE_LOWER_BOUND = (int) (img.height() * LINE_LOWER_PERC);

       ArrayList<Mat> resultHSV = toHSVImage(img);
       Mat sMat = resultHSV.get(S_CORE);
       Imgproc.HoughCircles(sMat, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 30, 70, 32.0, 40, 70);


       String orangeChecked = spBreath.getString("orange", null);
       requiredTime = getDuration();

       for (int i = circles.width() - 1; i >= 0; i--) {
           c = circles.get(0, i);
           center = new Point(Math.round(c[0]), Math.round(c[1]));
           int radius = (int) c[2];

           if (center.y > LINE_UPPER_BOUND && center.y < LINE_LOWER_BOUND) {
               if (center.x > first_line - radius && center.x < first_line + radius) {
                   if (balls[GREEN].getRadius() == 0 || balls[GREEN].getCenter().y > center.y) {
                       balls[GREEN] = new Ball(radius, center);
                   }
               } else if (center.x > second_line - radius && center.x < second_line + radius) {
                   if (balls[ORANGE].getRadius() == 0 || balls[ORANGE].getCenter().y > center.y) {
                       balls[ORANGE] = new Ball(radius, center);
                   }
               } else if (center.x > third_line - radius && center.x < third_line + radius) {
                   if (balls[BLUE].getRadius() == 0 || balls[BLUE].getCenter().y > center.y) {
                       balls[BLUE] = new Ball(radius, center);
                   }
               }
           }
       }

       /* Handle orange ball */
       int radius = balls[ORANGE].getRadius();
       center = balls[ORANGE].getCenter();
       Imgproc.circle(img, center, radius, new Scalar(255, 0, 0), 5);

       if (orangeChecked.equals("true") && radius > 0) {
           if (initialY == 0)
               initialY = (int) center.y;
           orangeHeight.add(center.y);
           long temp_timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
           if (Math.abs(center.y - initialY) > radius && Math.abs(center.y - initialY) + radius >= ((orangeHeightSetting + 1) * (Math.abs(LINE_UPPER_BOUND - LINE_LOWER_BOUND) - 2 * radius) / 4.0)) {
               if (!isUp) {
                   screenFeedback(1);
                   isUp = true;
                   isRepEntirelyComplete = false;
                   timeBallInAir = System.currentTimeMillis();
               } else if (!isRepEntirelyComplete && temp_timeBallInAir >= (long) (requiredTime * 10.0)) {
                   if (repCounter >= Integer.parseInt(repsNumTarget)) {
                       if (requiredTime > 0)
                           screenFeedback(3);
                   } else {
                       screenFeedback(2);
                   }
                   playSoundSuccess();
                   breathAnimation();
                   isRepEntirelyComplete = true;
                   goodReputations++;
                   orangeAirTime.add(1.0);
               }

           } else if (isUp && Math.abs(center.y - initialY) <= radius) {
               timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
               screenFeedback(-1);
               if (repCounter + 1 >= Integer.parseInt(repsNumTarget)) {
                   screenFeedback(3);
               } else {
                   screenFeedback(5);
               }

               isUp = false;
           }
           orangeInRange = true;

       }
       /* Handle blue ball */
       radius = balls[BLUE].getRadius();
       center = balls[BLUE].getCenter();
       Imgproc.circle(img, center, radius, new Scalar(255, 0, 0), 5);

       if (radius > 0 && orangeChecked.equals("false")) {
           if (initialY == 0)
               initialY = (int) center.y;
           blueHeight.add(center.y);
           long temp_timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
           if (Math.abs(center.y - initialY) > radius && Math.abs(center.y - initialY) + radius >= ((blueHeightSetting + 1) * (Math.abs(LINE_UPPER_BOUND - LINE_LOWER_BOUND) - 2 * radius) / 4.0)) {
               if (!isUp) {
                   screenFeedback(1);
                   isUp = true;
                   isRepEntirelyComplete = false;
                   timeBallInAir = System.currentTimeMillis();

               } else if (!isRepEntirelyComplete && temp_timeBallInAir >= (long) (requiredTime * 10.0)) {
                   if (repCounter >= Integer.parseInt(repsNumTarget)) {
                       screenFeedback(3);
                   } else {
                       screenFeedback(2);
                   }
                   playSoundSuccess();
                   breathAnimation();

                   isRepEntirelyComplete = true;
                   goodReputations++;
                   blueAirTime.add(1.0);
               }

           } else if (isUp && Math.abs(center.y - initialY) <= radius) {
               timeBallInAir = (System.currentTimeMillis() - timeBallInAir) / 100;
               screenFeedback(-1);
               if (repCounter + 1 >= Integer.parseInt(repsNumTarget)) {
                   screenFeedback(3);
               } else {
                   screenFeedback(5);
               }

           }

           blueInRange = true;
       }
   } catch (Exception e) {
       System.out.println("Catch exeption");
   }
}
```
### Explanation of the history database
On this page we define the SQLITE DATABASE, which keeps our training history in the table.
#### There are a number of things you should know about the functions
>##### createTrainingTable()
>in this function we create new table of Training, with statements for all the parameters we need
<br />

>##### addTraining()
>This function inserts into a table a training represented by the Training class.
<br />

>##### onUpgrade()
>This function is called, when SQLite detects a change in the structure of the table, when new parameters are added to the table.
>within this function we update the new table and delete the old one.
>
>##### Two things you should know about this function:
>1. Currently, this function delete the all old data
>2. If we want this function to be called, we need to notify SQLite that we have made changes, by updating the variable DB_VERSION to a new version.

### Explanation of the feedback page
This page is intended to provide users feedback, close to the training they have completed.
the training they did will be saved in the database.
#### An explanation of our variables:
> This variables save the data of repitition in the exercise, 
> Each index in our ArrayList contains a value of repetition in training
>```java
>static ArrayList<Integer> repMaxHeight = new ArrayList<>();
>static ArrayList<Integer> repDuration = new ArrayList<>();
>```
> repMaxHeight - Maximum height of repetition, 
> repDuration - The total duration of repetition
      
<br/>
   
>SharPreference variable, gives access to balls preferences
>```java
>static SharedPreferences spBreath;
>```
   
<br/>
   
>The number of successful repetitions counted in training
>```java
>int repsSuccess;
>```

#### An explanation of the important functions on this page:
>  ##### format2db()
>  This function is designed to make it easier for us to save repetitions data, and to convert our repetitions data from repMaxHeight,repDuration to a string format, 
> The string is kept under one column in the table.
   
<br/>
   
> ##### onClick()
> By clicking back to the home screen, we save the data in the database by creating a Training object and sending it all the parameters we have collected and are
> needed to build the object, then We  open a reference to SQLite and call the addTraining() function that adds the training to the database.
   
  <br/>
   
##### onCreate()
This function has a number of lines of code that are worth understanding.
>Gets the data we transferred from the exercise page   
>```java
>   repDuration = intent.getExtras().getIntegerArrayList("repDuration");
>   repMaxHeight = intent.getExtras().getIntegerArrayList("repMaxHeight");
>   repsSuccess = intent.getExtras().getInt("repsSuccess");
>   duration = intent.getExtras().getDouble("duration");
>   balldata = intent.getExtras().getInt("balldata");
>```
   
   <br/>
   
> Checks which ball to refer to, depending on user preferences
>```java
> String targetBall = balldata == 3 ? "numberOfrepBlue" : balldata == 2 ? "numberOfrepOrange" :null;
> targetrep = Integer.parseInt(spBreath.getString(targetBall,null));
>```  
   
   <br/>
   
   > The confetti displayed to the user is used in an external library (called DanielMartinus/Konfetti)
   > https://github.com/DanielMartinus/Konfetti
   >```java
>   EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
>       Party party = new PartyFactory(emitterConfig)
>               .angle(270)
>               .spread(90)
>               .setSpeedBetween(1f, 5f)
>               .timeToLive(2000L)
>               .position(0.0, 0.0, 1.0, 0.0)
>               .build();
>       konfettiView.start(party);
>  ```
   
### Explanation of the setting page

### Known software bug

1. Sometimes when the algorithm does not detect the balls or detects incorrect balls the app
   collapses. We wrapped the function getFrameData with try and catch, but it does not always work.
2. When we change the database (add or remove a parameter) the history page collapses, and we need
   to do cash cleaning for the app.
3. If the patient closes the exercise page immediately (before the red circles appears) the time of
   training in the history is incorrect.

### Directions for the future

1. The details about the training are saved locally with SQLITE, but it will be helpful to add an
   option to send this data to the speech therapists (through gmail for example, or Sheba's server)
2. At the moment, if the patient does not reach the height goal the app doesn't recognizes it as a
   success and doesn't show it in the history page. On the other hand if the patient doesn't reach
   the time goal it does. Adding the option to see half-successful intervals can increase the
   self-confidence of the patients and also help the speech therapists.

// WILL RESUME
