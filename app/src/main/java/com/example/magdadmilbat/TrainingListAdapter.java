package com.example.magdadmilbat;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        ProgressBar pb = convertView.findViewById(R.id.ProgressBar);

        tvQuality.setText(trainingQuality+"/10");
        pb.setProgress(Integer.parseInt(trainingQuality));
        tvDate.setText(date);
        tvTime.setText(time);
        return convertView;
    }
}
