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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText mFirstNameInput;
    private TextView mFirstNameValidationLabel;
    private EditText mLastNameInput;
    private TextView mLastNameValidationLabel;
    private EditText mDOBInput;
    private TextView mDOBValidationLabel;
    private Button mCalculateAgeBtn;
    private static final String DATE_FORMAT = "M/d/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirstNameInput = findViewById(R.id.f_name_input);
        mFirstNameValidationLabel = findViewById(R.id.fname_validation_label);

        mLastNameInput = findViewById(R.id.l_name_input);
        mLastNameValidationLabel = findViewById(R.id.lname_validation_label);

        mDOBInput = findViewById(R.id.dob_input);
        mDOBValidationLabel = findViewById(R.id.dob_validation_label);

        mCalculateAgeBtn = findViewById(R.id.calculate_btn);

        mDOBInput.setInputType(InputType.TYPE_NULL); //stops the regular keyboard to load on click

        mDOBInput.setOnClickListener(new AgeCalculatorListener());
        mCalculateAgeBtn.setOnClickListener(new AgeCalculatorListener());
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
            String fName = mFirstNameInput.getText().toString();
            String lName = mLastNameInput.getText().toString();
            String dob = mDOBInput.getText().toString();
            if(isInputValid(fName, lName, dob)) {
                //hide all validation messages
                mFirstNameValidationLabel.setVisibility(View.GONE);
                mLastNameValidationLabel.setVisibility(View.GONE);
                mDOBValidationLabel.setVisibility(View.GONE);

                //calculate age
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

                LocalDate today = LocalDate.now(); // Today's date is 10th Jan 2022
                LocalDate birthday = LocalDate.parse(dob, formatter);

                Period p = Period.between(birthday, today);

                // Now access the values as below
                System.out.println(p.getDays());    //9
                System.out.println(p.getMonths());  //0
                System.out.println(p.getYears());   //42

                Toast.makeText(MainActivity.this, "You are " + p.getYears() + " years old!", Toast.LENGTH_LONG).show();
            }
        }

        //TODO: Refactor the logic
        private boolean isInputValid(String fName, String lName, String dob) {
            boolean valid = true;
            if(isBlank(fName)) {
                valid = false;
                //show fname validation message
                mFirstNameValidationLabel.setVisibility(View.VISIBLE);
            } else {
                //clear fname validation message
                mFirstNameValidationLabel.setVisibility(View.GONE);
            }
            if(isBlank(lName)) {
                valid = false;
                //show lname validation message
                mLastNameValidationLabel.setVisibility(View.VISIBLE);
            } else {
                //clear lname validation message
                mLastNameValidationLabel.setVisibility(View.GONE);
            }
            if(isBlank(dob)) {
                valid = false;
                //show lname validation message
                mDOBValidationLabel.setVisibility(View.VISIBLE);
            } else {
                //clear dob validation message
                mDOBValidationLabel.setVisibility(View.GONE);
            }
            return valid;
        }

        private boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }

        private void setSelectedDate(View view) {
            //close regular keyboard if open
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);

            //Current date - the date picker will be defaulted to this value
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