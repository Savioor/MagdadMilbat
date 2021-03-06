package com.example.magdadmilbat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.MagdadMilbat.R;
import java.util.List;

public class DetailsAdapter extends ArrayAdapter<Details> {

    Context context;
    List<Details> data;

    public DetailsAdapter(Context context, List<Details> data) {
        super(context, 0, 0, data);

        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.activity_exercise_details, parent, false);

        TextView tvExercise = (TextView) view.findViewById(R.id.tvExercise);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
        TextView tvRepetition = (TextView) view.findViewById(R.id.tvRepetition);
        TextView tvLevel = (TextView) view.findViewById(R.id.tvLevel);
        TextView tvDuration = (TextView) view.findViewById(R.id.tvDuration);

        Details temp = data.get(position);
        tvExercise.setText(temp.getExercise());
        tvDate.setText(temp.getDate());
        tvTime.setText(temp.getTime());
        tvRepetition.setText(temp.getRepetition());
        tvLevel.setText(temp.getLevel());
        tvDuration.setText(temp.getDuration());

        return view;
    }
}