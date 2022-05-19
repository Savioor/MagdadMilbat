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

    /**
     * The function updates the table
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table Training"); //Deletes the table
        onCreate(sqLiteDatabase); // call to "onCreate" function,that will recreate the table
    }

    /**
     * this function create new table of Training
     */
    private void createTrainingTable(SQLiteDatabase sqLiteDatabase)
    {
        String sql = "Create table Training (id integer primary key autoincrement, exerciseDescription text not null," +
                " trainingQuality integer not null, date text not null, time text not null, duration double not null, repsDuration text not null, repsMaxHeight text not null, balldata integer not null, target integer not null, targetDuration double not null)";
        sqLiteDatabase.execSQL(sql);
    }

    /** This function adds the value of the training it receives to the training table **/
    public void addTraining(Training training) {
        String sql = "insert into Training(date, time, exerciseDescription, trainingQuality, duration, repsDuration, repsMaxHeight, balldata, target, targetDuration) " +
                "values ('"+training.getDate() + "','" + training.getTime() + "','" + training.getExerciseDescription() + "','"+ training.getTrainingQuality() + "','"+ training.getDuration() + "','"+training.getRepDuration()+"','" +training.getRepMaxHeight()+"','"+training.getBalldata()+"','"+training.getTarget()+"','"+training.getTargetDuration()+"')";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase(); // Open connection.
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close(); // Close connection.
    }

    /**
     * !!The function needs repair!!
     * this function delete specific Training from the table
     * According to the rowid specified
     * @param rowId the row id in table
     */
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
        int repsDurationI = cursor.getColumnIndex("repsDuration");
        int repsMaxHeightI = cursor.getColumnIndex("repsMaxHeight");
        int balldataI = cursor.getColumnIndex("balldata");
        int targetI = cursor.getColumnIndex("target");
        int targetDurationI = cursor.getColumnIndex("targetDuration");
        int trainingQuality = cursor.getInt(trainingQualityI);
        double duration = cursor.getDouble(durationI);
        String exerciseDescription = cursor.getString(exerciseDescriptionI);
        String date = cursor.getString(dateI);
        String time = cursor.getString(timeI);
        String repsDuration = cursor.getString(repsDurationI);
        String repsMaxHeight = cursor.getString(repsMaxHeightI);
        int balldata = cursor.getInt(balldataI);
        int target = cursor.getInt(targetI);
        double targetDuration = cursor.getInt(targetDurationI);
        return new Training(date, time, exerciseDescription, trainingQuality,duration,repsDuration,repsMaxHeight,balldata,target, targetDuration);
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
