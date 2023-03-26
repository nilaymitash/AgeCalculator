package com.example.agecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText mFirstNameInput;
    private EditText mLastNameInput;
    private EditText mDOBInput;
    private TextView mTestResultLabel;
    private Button mCalculateAgeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirstNameInput = findViewById(R.id.first_name_input);
        mLastNameInput = findViewById(R.id.last_name_input);
        mDOBInput = findViewById(R.id.dob_input);
        mCalculateAgeBtn = findViewById(R.id.calculate_btn);
        mDOBInput.setInputType(InputType.TYPE_NULL); //stops the regular keyboard to load on click
        mDOBInput.setOnClickListener(new AgeCalculatorListener());
        mCalculateAgeBtn.setOnClickListener(new AgeCalculatorListener());

        mTestResultLabel = findViewById(R.id.test_result_label);
    }

    public class AgeCalculatorListener implements View.OnClickListener {

        private DatePickerDialog datePickerDialog;

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dob_input: setSelectedDate(view); break;
                case R.id.calculate_btn: calculateAge(); break;
            }
        }

        private void calculateAge() {
            mTestResultLabel.setText(
                    mFirstNameInput.getText() + ";" + mLastNameInput.getText() + ";" + mDOBInput.getText()
            );
        }

        private void setSelectedDate(View view) {
            //close regular keyboard if open
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);

            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                    mDOBInput.setText((selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear);
                }
            };

            datePickerDialog = new DatePickerDialog(MainActivity.this, onDateSetListener, year, month, day);
            datePickerDialog.show();
        }
    }
}