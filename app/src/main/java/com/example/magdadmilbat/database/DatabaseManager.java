package com.example.magdadmilbat.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.magdadmilbat.Training;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(Context context) {
        super(context, "MyDatabase", null, 1); // 1 = version.
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTrainingTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table Training");
        onCreate(sqLiteDatabase);
    }

    private void createTrainingTable(SQLiteDatabase sqLiteDatabase)
    {
        String sql = "Create table Training (id integer primary key autoincrement, exerciseDescription text not null," +
                " trainingQuality integer not null, date text not null, time text not null)";
        sqLiteDatabase.execSQL(sql);
    }
    public void addTraining(Training training) {
        String sql = "insert into Training(date, time, exerciseDescription, trainingQuality) " +
                "values ('"+training.getDate() + "','" + training.getTime() + "','" + training.getExerciseDescription() + "',"+ training.getTrainingQuality()+ ")";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase(); // Open connection.
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close(); // Close connection.
    }

    private Training getTraining(Cursor cursor)
    {
        int dateI = cursor.getColumnIndex("date");
        int timeI = cursor.getColumnIndex("time");
        int exerciseDescriptionI = cursor.getColumnIndex("exerciseDescription");
        int trainingQualityI = cursor.getColumnIndex("trainingQuality");
        int trainingQuality = cursor.getInt(trainingQualityI);
        String exerciseDescription = cursor.getString(exerciseDescriptionI);
        String date = cursor.getString(dateI);
        String time = cursor.getString(timeI);

        return new Training(date, time, exerciseDescription, trainingQuality);

    }

    public ArrayList<Training> getAllTraining() {
        ArrayList<Training> trainingArrayList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Training", null);
        while(cursor.moveToNext()) {
            trainingArrayList.add(getTraining(cursor));
        }
        cursor.close();
        sqLiteDatabase.close(); // Close connection.
        return trainingArrayList;
    }
}
