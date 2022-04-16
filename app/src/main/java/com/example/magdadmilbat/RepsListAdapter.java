package com.example.magdadmilbat;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.MagdadMilbat.R;

import java.util.ArrayList;

public class RepsListAdapter  extends ArrayAdapter<Repetition> {
    private Context mContext;
    int mResource;

    public RepsListAdapter(Context context, int resource, ArrayList<Repetition> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the Reps information
        String repnum = String.valueOf(getItem(position).getNumber());
        String repduration = getItem(position).getRepduration();
        String maxheight = String.valueOf(getItem(position).getMaxHeight());

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView tvrepnum = convertView.findViewById(R.id.tvrepnum);
        TextView tvrepduration = convertView.findViewById(R.id.tvrepduration);
        TextView tvmaxheight = convertView.findViewById(R.id.tvmaxheight);

        tvrepnum.setText(repnum+"חזרה ");
        tvrepduration.setText(repduration);
        tvmaxheight.setText(maxheight);
        return convertView;
    }
}