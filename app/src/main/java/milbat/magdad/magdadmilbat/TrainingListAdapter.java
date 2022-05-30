package milbat.magdad.magdadmilbat;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.MagdadMilbat.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class TrainingListAdapter extends ArrayAdapter<Training> {
    private static final String TAG = "TrainingListAdapter";
    private final Context mContext;
    int mResource;

    public TrainingListAdapter(Context context, int resource, ArrayList<Training> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the Training information
        String trainingQuality = String.valueOf(getItem(position).getTrainingQuality());
        String time = getItem(position).getTime();
        String date = getItem(position).getDate();
        String target = String.valueOf(getItem(position).getTarget());

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView tvQuality = convertView.findViewById(R.id.textView1);
        TextView tvDate = convertView.findViewById(R.id.textView2);
//        TextView tvTime = convertView.findViewById(R.id.textView3);
        ProgressBar pb = convertView.findViewById(R.id.ProgressBar);
        LocalDate dateObj = LocalDate.parse(date);
        LocalDate dateObj2 = LocalDate.now();
        if (dateObj.isEqual(dateObj2)) {
            date = time;
        }

        tvQuality.setText(trainingQuality + "/" + target + " חזרות ");
        pb.setMax(Integer.parseInt(target));
        pb.setProgress(Integer.parseInt(trainingQuality));
        tvDate.setText(date);
//        tvTime.setText(time);
        return convertView;
    }
}
