package com.example.magdadmilbat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.MagdadMilbat.R;

import java.util.ArrayList;

public class TrainingListAdapter  extends ArrayAdapter<Training> {
    private static final String TAG = "TrainingListAdapter";
    private Context mContext;
    int mResource;

    public TrainingListAdapter(Context context, int resource, ArrayList<Training> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the Training information
        String trainingQuality = String.valueOf(getItem(position).getTrainingQuality());
        String time = getItem(position).getTime();
        String date = getItem(position).getDate();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView tvQuality = convertView.findViewById(R.id.textView1);
        TextView tvDate = convertView.findViewById(R.id.textView2);
        TextView tvTime = convertView.findViewById(R.id.textView3);

        tvQuality.setText(trainingQuality);
        tvDate.setText(date);
        tvTime.setText(time);
        return convertView;
    }
}
