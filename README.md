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

static Scalar[][]rgbRange=new Scalar[3][2];
static Scalar[][]hsvRange=new Scalar[3][2];

``` 

greenHeight, blueHeight and orangeHeight are all to be used to store the height of each ball
throughout the video.

### Explanation of the history database

### Explanation of the feedback page

### Explanation of the setting page

### Known software bug

1. Sometimes when the algorithm does not detect the balls or detects incorrect balls the app
   collapses. We wrapped the function getFrameData with try and catch, but it does not always work.
2. When we change the database (add or remove a parameter) the history page collapses, and we need
   to do cash cleaning for the app.
3. If the patient closes the exercise page immediately (before the ******)

### Directions for the future

1. The details about the training are saved locally with SQLITE, but it will be helpful to add an
   option to send this data to the speech therapists (through gmail for example, or Sheba's server)
2. At the moment, if the patient does not reach the height goal the app doesn't recognizes it as a
   success and doesn't show it in the history page. On the other hand if the patient doesn't reach
   the time goal it does. Adding the option to see half-successful intervals can increase the
   self-confidence of the patients and also help the speech therapists

// WILL RESUME
