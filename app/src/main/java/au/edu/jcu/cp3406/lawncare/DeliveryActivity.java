package au.edu.jcu.cp3406.lawncare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class DeliveryActivity extends AppCompatActivity {

    Spinner devTime;
    Spinner devDay;
    Spinner picTime;
    Spinner picDay;

    TextView deliveryDate;
    TextView pickupDate;

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

        deliveryDate = findViewById(R.id.tvDeliveryDate);
        pickupDate = findViewById(R.id.tvPickupDate);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyDate(true);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verify user input is correct
                if (verifyDate(false)) {
                    //Fail to add delivery if user has prior booking
                    if (db.checkExisting(userID)) {
                        db.addDelivery(userID, devTimeSelection, devDaySelection, picTimeSelection, picDaySelection);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.prior_delivery), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean verifyDate(boolean verbose) {
        getDate();
        updatePreview();

        //Compare to database
        //Return false if date or time is unavailable

        //Delivery day capacity check
        if (!db.checkDay(devDaySelection, "Delivery")) {
            Toast.makeText(getApplicationContext(), getString(R.string.dev_day_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        //Delivery time check
        if (!db.checkTime(devDaySelection,  devTimeSelection,"Delivery")) {
            Toast.makeText(getApplicationContext(), getString(R.string.dev_time_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        //Pickup day capacity check
        if (!db.checkDay(devDaySelection, "Pickup")) {
            Toast.makeText(getApplicationContext(), getString(R.string.pic_day_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        //Pickup time check
        if (!db.checkTime(devDaySelection, picTimeSelection, "Delivery")) {
            Toast.makeText(getApplicationContext(), getString(R.string.pic_time_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        //Set date format for easy sorting via database
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date devDate = formatter.getCalendar().getTime();
        Date picDate = formatter.getCalendar().getTime();

        //Update delivery date and pickup date with user selected values
        try {
            devDate = formatter.parse(devDaySelection);
            picDate = formatter.parse(picDaySelection);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Compare dates
        //Return false if check fails

        //Check pickup date is after delivery date
        if (picDate.before(devDate)) {
            Toast.makeText(getApplicationContext(), getString(R.string.date_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        //Check delivery and pickup are not on the same day
        if (picDate.equals(devDate)) {
            Toast.makeText(getApplicationContext(), getString(R.string.date_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (verbose) {
            Toast.makeText(getApplicationContext(), getString(R.string.date_available), Toast.LENGTH_SHORT).show();
        }

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

        String[] days = getResources().getStringArray(R.array.days);

        //Convert delivery day spinner selection to date format
        if (devDaySelection.equals(days[0])) {
            ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        if (devDaySelection.equals(days[1])) {
            ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        }

        if (devDaySelection.equals(days[2])) {
            ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        }

        if (devDaySelection.equals(days[3])) {
            ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
        }

        if (devDaySelection.equals(days[4])) {
            ld1 = ld1.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        }


        //Convert pickup day spinner selection to date format
        if (picDaySelection.equals(days[0])) {
            ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        if (picDaySelection.equals(days[1])) {
            ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        }

        if (picDaySelection.equals(days[2])) {
            ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        }

        if (picDaySelection.equals(days[3])) {
            ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
        }

        if (picDaySelection.equals(days[4])) {
            ld2 = ld2.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        }

        //Convert delivery time spinner selection to time format
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

        //Convert pickup time spinner selection to time format
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

    public void updatePreview() {
        deliveryDate.setText(String.format("%s %s", devDaySelection, devTimeSelection));
        pickupDate.setText(String.format("%s %s", picDaySelection, picTimeSelection));
    }

}