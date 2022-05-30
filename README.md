# Deep Exhalation - A Magdad Project

Deep Exhalation ('נשיפה עמוקה') is an application written in Java in collaboration with a non-profit organization in order to assist people with disabilities. The application consists of two main modules; image recognition and processing and application development (e.g. the backend and the frontend).

## Backend
The code in the backend is written using the OpenCV library for Android. Given the frames of the phone camera, the code processes them and finds the three colored balls and their position.

An example for an image it recieves may be:
[Will insert pictures]

### Explanation of the backend code
Firstly, we'll begin with our image processing varaibles:
```java
static ArrayList<Double> greenHeight = new ArrayList<Double>();
static ArrayList<Double> blueHeight = new ArrayList<Double>();
static ArrayList<Double> orangeHeight = new ArrayList<Double>();

static ArrayList<Double> greenAirTime = new ArrayList<Double>();
static ArrayList<Double> blueAirTime = new ArrayList<Double>();
static ArrayList<Double> orangeAirTime = new ArrayList<Double>();

static boolean greenInAir = false, orangeInAir = false, blueInAir = false;

static Scalar[][] rgbRange = new Scalar[3][2];
static Scalar[][] hsvRange = new Scalar[3][2];
``` 
greenHeight, blueHeight and orangeHeight are all to be used to store the height of each ball throughout the video. 

// WILL RESUME
