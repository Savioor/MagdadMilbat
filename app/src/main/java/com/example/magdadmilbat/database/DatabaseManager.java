package com.example.magdadmilbat.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.magdadmilbat.Training;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
public static final String TABLE_NAME = "Training";
    public static final String ID = "ID";
    public DatabaseManager(Context context) {
        super(context, "MyDatabase", null, 1); // 1 = version.
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTrainingTable(sqLiteDatabase);
    }

    //The function updates the table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table Training"); //Deletes the table
        onCreate(sqLiteDatabase); // call to "onCreate" function,that will recreate the table
    }

    private void createTrainingTable(SQLiteDatabase sqLiteDatabase)
    {
        String sql = "Create table Training (id integer primary key autoincrement, exerciseDescription text not null," +
                " trainingQuality integer not null, date text not null, time text not null, duration double not null, greenAirTime double not null, blueAirTime double not null, orangeAirTime double not null, greenMaxHeight double not null, blueMaxHeight double not null, orangeMaxHeight double not null, greenRepSuccess int not null, blueRepSuccess int not null, orangeRepSuccess int not null)";
        sqLiteDatabase.execSQL(sql);
    }

    /** This function adds the value of the training it receives to the training table **/
    public void addTraining(Training training) {
        String sql = "insert into Training(date, time, exerciseDescription, trainingQuality, duration, greenAirTime, blueAirTime, orangeAirTime, greenMaxHeight, blueMaxHeight, orangeMaxHeight, greenRepSuccess, blueRepSuccess, orangeRepSuccess) " +
                "values ('"+training.getDate() + "','" + training.getTime() + "','" + training.getExerciseDescription() + "','"+ training.getTrainingQuality() + "','"+ training.getDuration() + "','"+ training.getArrBalls()[0].getDurationInAir() + "','"+ training.getArrBalls()[1].getDurationInAir() + "' ,'"+ training.getArrBalls()[2].getDurationInAir() + "','" + training.getArrBalls()[0].getMaxHeight() + "','" + training.getArrBalls()[1].getMaxHeight() + "','" + training.getArrBalls()[2].getMaxHeight() + "','" + training.getArrBalls()[0].getRepetitionSuccess() + "','" + training.getArrBalls()[1].getRepetitionSuccess() + "'," + training.getArrBalls()[2].getRepetitionSuccess() + ")";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase(); // Open connection.
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close(); // Close connection.
    }

    public void deleteTraining(String rowId){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase(); // Open connection.
        String str_sql = "delete from " + TABLE_NAME + " where " + ID  + " = "  + rowId;
        sqLiteDatabase.execSQL(str_sql);
        sqLiteDatabase.close(); // Close connection.

    }


/**
    This method gets a cursor that holds a record (row) from the training table
    and creates for us a training object according to that record.
**/
    private Training getTraining(Cursor cursor)
    {
        int dateI = cursor.getColumnIndex("date");
        int timeI = cursor.getColumnIndex("time");
        int durationI = cursor.getColumnIndex("duration");
        int exerciseDescriptionI = cursor.getColumnIndex("exerciseDescription");
        int trainingQualityI = cursor.getColumnIndex("trainingQuality");
        int greenAirTimeI = cursor.getColumnIndex("greenAirTime");
        int blueAirTimeI = cursor.getColumnIndex("blueAirTime");
        int orangeAirTimeI = cursor.getColumnIndex("orangeAirTime");
        int greenMaxHeightI = cursor.getColumnIndex("greenMaxHeight");
        int blueMaxHeightI = cursor.getColumnIndex("blueMaxHeight");
        int orangeMaxHeightI = cursor.getColumnIndex("orangeMaxHeight");
        int greenRepSuccessI = cursor.getColumnIndex("greenRepSuccess");
        int blueRepSuccessI = cursor.getColumnIndex("blueRepSuccess");
        int orangeRepSuccessI = cursor.getColumnIndex("orangeRepSuccess");
        int trainingQuality = cursor.getInt(trainingQualityI);
        double duration = cursor.getDouble(durationI);
        String exerciseDescription = cursor.getString(exerciseDescriptionI);
        String date = cursor.getString(dateI);
        String time = cursor.getString(timeI);
        double greenAirTime = cursor.getDouble(greenAirTimeI);
        double blueAirTime = cursor.getDouble(blueAirTimeI);
        double orangeAirTime = cursor.getDouble(orangeAirTimeI);
        double greenMaxHeight = cursor.getDouble(greenMaxHeightI);
        double blueMaxHeight = cursor.getDouble(blueMaxHeightI);
        double orangeMaxHeight = cursor.getDouble(orangeMaxHeightI);
        int greenRepSuccess = cursor.getInt(greenRepSuccessI);
        int blueRepSuccess = cursor.getInt(blueRepSuccessI);
        int orangeRepSuccess = cursor.getInt(orangeRepSuccessI);
        return new Training(date, time, exerciseDescription, trainingQuality,duration, greenAirTime, blueAirTime, orangeAirTime, greenMaxHeight, blueMaxHeight, orangeMaxHeight, greenRepSuccess, blueRepSuccess, orangeRepSuccess);
    }

/**
    This function receives from the table all the training performed,
    and adds it to the array of training that we return at the end.
**/
    public ArrayList<Training> getAllTraining() {
        ArrayList<Training> trainingArrayList = new ArrayList<>(); //array of training

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Training", null);

        while(cursor.moveToNext()) { //Goes through all the records and adds them to the array
            trainingArrayList.add(getTraining(cursor));
        }
        cursor.close();
        sqLiteDatabase.close(); // Close connection.
        return trainingArrayList;
    }
}
