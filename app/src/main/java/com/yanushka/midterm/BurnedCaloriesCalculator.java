package com.yanushka.midterm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.NumberFormat;


public class BurnedCaloriesCalculator extends Activity
implements OnSeekBarChangeListener {

    private EditText weightEditText;
    private EditText nameEditText;
    private SeekBar milesRanSeek;
    private Spinner feetSpinner;
    private Spinner inchesSpinner;
    private TextView milesRanTextView;
    private TextView caloriesBurnedTextView;
    private TextView bmiTextView;

    private String weight;
    private String name;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        weightEditText = (EditText) findViewById(R.id.weightEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        milesRanSeek = (SeekBar) findViewById(R.id.milesRanSeekBar);
        feetSpinner = (Spinner) findViewById(R.id.feetSpinner);
        inchesSpinner = (Spinner) findViewById(R.id.inchesSpinner);
        milesRanTextView = (TextView) findViewById(R.id.milesRanTextView);
        caloriesBurnedTextView = (TextView) findViewById(R.id.caloriesBurnedTextView);
        bmiTextView = (TextView) findViewById(R.id.bmiTextView);

        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource(this, R.array.feetArray, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> inchesAdapter = ArrayAdapter.createFromResource(this, R.array.inchesArray, android.R.layout.simple_spinner_item);

        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        feetSpinner.setAdapter(feetAdapter);
        inchesSpinner.setAdapter(inchesAdapter);

        feetSpinner.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        weightEditText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    calculateAndDisplay();
                }
                return false;
            }
        });
        milesRanSeek.setOnSeekBarChangeListener(this);


        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    public void calculateAndDisplay() {
        weight = weightEditText.getText().toString();
        name = nameEditText.getText().toString();

        int milesRan = milesRanSeek.getProgress();
        float weightCalc = Float.parseFloat(weight);
        float caloriesBurned = (float) (0.75 * weightCalc * milesRan);

        String feetString = (String) feetSpinner.getSelectedItem();
        String inchesString = (String) inchesSpinner.getSelectedItem();
        int feet = Integer.parseInt(feetString);
        int inches = Integer.parseInt(inchesString);

        int bmi = (int) ((weightCalc * 703) / ((12 * feet + inches) * (12 * feet + inches)));


        bmiTextView.setText(bmi);
        caloriesBurnedTextView.setText(Float.toString(caloriesBurned));

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        milesRanTextView.setText(progress + " mi");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        calculateAndDisplay();
    }

    @Override
    public void onPause() {
        Editor editor = savedValues.edit();
        editor.putString("weight", weight);
        editor.putInt("milesRan", milesRanSeek.getProgress());
        editor.putString("feet", String.valueOf(feetSpinner.getSelectedItemPosition()));
        editor.putString("inches", String.valueOf(inchesSpinner.getSelectedItemPosition()));
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();



        calculateAndDisplay();
    }

}
