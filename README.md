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
