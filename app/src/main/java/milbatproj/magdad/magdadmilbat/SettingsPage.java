package milbatproj.magdad.magdadmilbat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

public class SettingsPage extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    Button btnBack, btnSave;
    SeekBar sbLevel, sbRepetition;
    TextView tvLevelNumber, tvRepetitionNumber;
    EditText etDuration;
    CheckBox b1,b2;
    ScrollView sv;
    ImageView ivRight,ivLeft; //Arrows right, and left
    boolean blue = true,orange = false;
    boolean blueball = true , orangeball = false;
    SharedPreferences spBreath;
    //difficulty level
    int sbLevelNumberBlue = 1;
    int sbLevelNumberOrange = 1;
    // number of repetitions
    int sbRepNumberBlue = 1;
    int sbRepNumberOrange = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        sbLevel = findViewById(R.id.sbLevel);
        sv = findViewById(R.id.navballs);
        b1 = findViewById(R.id.blue);
        b2 = findViewById(R.id.orange);
        sbRepetition = findViewById(R.id.sbRepetition);
        tvLevelNumber = findViewById(R.id.tvLevelNumber);
        tvRepetitionNumber = findViewById(R.id.tvRepetitionNumber);
        etDuration = findViewById(R.id.etDuration);
        ivRight = findViewById(R.id.ivRight);
        ivLeft = findViewById(R.id.ivLeft);
        sv.setSmoothScrollingEnabled(true);
        //Set listeners
        b1.setOnCheckedChangeListener(this);
        b2.setOnCheckedChangeListener(this);
        ivRight.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        sbLevel.setOnSeekBarChangeListener(this);
        sbRepetition.setOnSeekBarChangeListener(this);
        // set the range of the slider (min & max)
        sbLevel.setMin(1);
        sbRepetition.setMin(1);
        sbLevel.setMax(3);
        sbRepetition.setMax(20);

        // Sets the SharedPreferences "settingsBreath"
        spBreath = getSharedPreferences("settingsBreath", 0);
        String numberOfrepOrange = spBreath.getString("numberOfrepOrange",null);
        String numberOfrepBlue = spBreath.getString("numberOfrepBlue",null);
        String difficultyOrange = spBreath.getString("difficultyOrange",null);
        String difficultyBlue = spBreath.getString("difficultyBlue",null);
        String duration = spBreath.getString("duration",null);
        String useOrange = spBreath.getString("orange",null);

        //If there are already saved settings,display and update the page
        if(numberOfrepBlue != null){
            sbRepNumberBlue = Integer.parseInt(numberOfrepBlue);
            sbRepetition.setProgress(sbRepNumberBlue);
            tvRepetitionNumber.setText(numberOfrepBlue);
        }
        if(difficultyBlue != null){
            tvLevelNumber.setText(difficultyBlue);
            sbLevelNumberBlue = Integer.parseInt(difficultyBlue);
            sbLevel.setProgress(sbLevelNumberBlue);
        }
        if(numberOfrepOrange != null){
            sbRepNumberOrange = Integer.parseInt(numberOfrepOrange);
        }
        if(difficultyOrange != null){
            sbLevelNumberOrange = Integer.parseInt(difficultyOrange);
        }

        if (useOrange != null) {
            orange = Boolean.valueOf(useOrange);
            b2.setChecked(orange);
        }
        if (duration != null)
            etDuration.setText(duration);
        else
            etDuration.setText("5.0");

        if (useOrange.equals("true")) {
            orangeball = true;
            blueball = false;
            sbRepetition.setEnabled(orange);
            sbLevel.setEnabled(orange);
            etDuration.setEnabled(orange);
            sv.post(new Runnable() {
                @Override
                public void run() {
                    sv.smoothScrollTo(0, b2.getTop());//Navigates to the orange ball
                }
            });
            sbLevel.setProgress(sbLevelNumberOrange);
            sbRepetition.setProgress(sbRepNumberOrange);
        }

    }

    /**
     * returns to the main page when click on the back button.
     * if btnSave clicked ,save the "settingsBreath" on SharedPreferences.
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View view) {

        if (view == btnBack)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        else if (view == btnSave)
        {
            SharedPreferences.Editor editor = spBreath.edit();
            String d = etDuration.getText().toString();
            editor.putString("numberOfrepBlue", String.valueOf(sbRepNumberBlue));
            editor.putString("difficultyBlue", String.valueOf(sbLevelNumberBlue));
            editor.putString("numberOfrepOrange", String.valueOf(sbRepNumberOrange));
            editor.putString("difficultyOrange", String.valueOf(sbLevelNumberOrange));
            editor.putString("orange", String.valueOf(orange));
            if(d.length() > 0){
                editor.putString("duration", d);
            }
            editor.putString("date", "1");
            editor.putString("hour", "1");
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        // navigate between the two balls
        if(view == ivRight){
            blueball = true;
            orangeball = false;
            sbRepetition.setEnabled(blue);
            sbLevel.setEnabled(blue);
            etDuration.setEnabled(blue);
            sv.smoothScrollTo( 0, b1.getTop()); //Navigates to the blue ball
            if(orange){
                sbRepNumberBlue = sbRepNumberOrange;
                sbLevelNumberBlue = sbLevelNumberOrange;
            }
            sbLevel.setProgress(sbLevelNumberBlue);
            sbRepetition.setProgress(sbRepNumberBlue);
        }

        if(view == ivLeft){
            orangeball = true;
            blueball = false;
            sbRepetition.setEnabled(orange);
            sbLevel.setEnabled(orange);
            etDuration.setEnabled(orange);
            sv.smoothScrollTo(0, b2.getTop());//Navigates to the orange ball
            sbLevel.setProgress(sbLevelNumberOrange);
            sbRepetition.setProgress(sbRepNumberOrange);
        }

    }

    /**
     * if slider level change, update the difficulty level setting
     * if slider Repetition change,update the "number Of Repetition" setting
     * @param seekBar the seekbar that Changed
     * @param i Updated value
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == sbLevel)
        {
            tvLevelNumber.setText(String.valueOf(i));
            if(orangeball){ // Checking which ball to refer to
                sbLevelNumberOrange = i;
            }else if(blueball){
                sbLevelNumberBlue = i;
            }
        }
        else if (seekBar == sbRepetition)
        {
            tvRepetitionNumber.setText(String.valueOf(i));
            if(orangeball){
                sbRepNumberOrange = i;
            }else if(blueball){
                sbRepNumberBlue = i;
            }
        }
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * this func run when checkBox change
     * @param buttonView the checkBox view
     * @param isChecked if the checkBox is Checked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // If b1 changes, update the values of the blue ball
        if(buttonView == b1){
            blue = isChecked;
            if(blueball){
                sbRepetition.setEnabled(blue);
                sbLevel.setEnabled(blue);
                etDuration.setEnabled(blue);
            }
        }
        // If b1 changes, update the values of the orange ball
        if(buttonView == b2){
            orange = isChecked;
            if(orangeball){
                sbRepetition.setEnabled(orange);
                sbLevel.setEnabled(orange);
                etDuration.setEnabled(orange);
            }
        }
    }
}