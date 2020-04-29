package au.edu.jcu.cp3406.lawncare;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DeliveryActivity extends AppCompatActivity {

    Spinner devTime;
    Spinner devDay;
    Spinner picTime;
    Spinner picDay;

    Button verify;
    Button submit;

    String devTimeSelection;
    String devDaySelection;
    String picTimeSelection;
    String picDaySelection;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        db = new DatabaseHelper(this);

        final int userID = getIntent().getIntExtra("userID", 0);

        devTime = findViewById(R.id.spinDevTime);
        devDay = findViewById(R.id.spinDevDay);
        picTime = findViewById(R.id.spinPickTime);
        picDay = findViewById(R.id.spinPickDay);

        verify = findViewById(R.id.btnVerify);
        submit = findViewById(R.id.btnSubmit);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyDate(true);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyDate(false)) {
                    //TODO Submit to database and check no prior deliveries exist
                    if (db.checkExisting(userID)) {
                        db.addDelivery(userID, devTimeSelection.toString(), devDaySelection.toString(), picTimeSelection.toString(), picDaySelection.toString());
                        finish();
                    }
                }
            }
        });
    }

    public boolean verifyDate(boolean verbose) {
        getDate();

        //Compare database
        //Return false if dates are unavailable
        if (!verifyDay()) {
            Toast.makeText(getApplicationContext(), getString(R.string.day_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!verifyTime()) {
            Toast.makeText(getApplicationContext(), getString(R.string.time_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date devDate = formatter.getCalendar().getTime();
        Date picDate = formatter.getCalendar().getTime();

        try {
            devDate = formatter.parse(devDaySelection);
            picDate = formatter.parse(picDaySelection);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (picDate.before(devDate)) {
            Toast.makeText(getApplicationContext(), getString(R.string.date_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (picDate.equals(devDate)) {
            Toast.makeText(getApplicationContext(), getString(R.string.date_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (verbose) {
            Toast.makeText(getApplicationContext(), getString(R.string.date_available), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public boolean verifyDay() {
        //TODO check database
        return true;
    }

    public boolean verifyTime() {
        //TODO check database
        return true;
    }

    public void getDate() {
        //Get data from spinners
        devTimeSelection = devTime.getSelectedItem().toString();
        devDaySelection = devDay.getSelectedItem().toString();
        picTimeSelection = picTime.getSelectedItem().toString();
        picDaySelection = picDay.getSelectedItem().toString();

        LocalDate ld1 = LocalDate.now();
        LocalDate ld2 = LocalDate.now();
        LocalTime lt1 = LocalTime.now();
        LocalTime lt2 = LocalTime.now();

        switch (devDaySelection) {
            case "Monday":
                ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                break;
            case "Tuesday":
                ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
                break;
            case "Wednesday":
                ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
                break;
            case "Thursday":
                ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
                break;
            case "Friday":
                ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
                break;
        }

        switch (picDaySelection) {
            case "Monday":
                ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                break;
            case "Tuesday":
                ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
                break;
            case "Wednesday":
                ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
                break;
            case "Thursday":
                ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
                break;
            case "Friday":
                ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
                break;
        }

        switch (devTimeSelection) {
            case "10 AM":
                lt1 = LocalTime.parse("10:00");
                break;
            case "11 AM":
                lt1 = LocalTime.parse("11:00");
                break;
            case "12 PM":
                lt1 = LocalTime.parse("12:00");
                break;
            case "1 PM":
                lt1 = LocalTime.parse("13:00");
                break;
            case "2 PM":
                lt1 = LocalTime.parse("14:00");
                break;
            case "3 PM":
                lt1 = LocalTime.parse("15:00");
                break;
            case "4 PM":
                lt1 = LocalTime.parse("16:00");
                break;
            case "5 PM":
                lt1 = LocalTime.parse("17:00");
                break;
        }

        switch (picTimeSelection) {
            case "10 AM":
                lt2 = LocalTime.parse("10:00");
                break;
            case "11 AM":
                lt2 = LocalTime.parse("11:00");
                break;
            case "12 PM":
                lt2 = LocalTime.parse("12:00");
                break;
            case "1 PM":
                lt2 = LocalTime.parse("13:00");
                break;
            case "2 PM":
                lt2 = LocalTime.parse("14:00");
                break;
            case "3 PM":
                lt2 = LocalTime.parse("15:00");
                break;
            case "4 PM":
                lt2 = LocalTime.parse("16:00");
                break;
            case "5 PM":
                lt2 = LocalTime.parse("17:00");
                break;
        }

        devDaySelection = ld1.toString();
        picDaySelection = ld2.toString();
        devTimeSelection = lt1.toString();
        picTimeSelection = lt2.toString();
    }

}