package au.edu.jcu.cp3406.lawncare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        devTime = findViewById(R.id.spinDevTime);
        devDay = findViewById(R.id.spinDevDay);
        picTime = findViewById(R.id.spinPickTime);
        picDay = findViewById(R.id.spinPickDay);

        verify = findViewById(R.id.btnVerify);
        submit = findViewById(R.id.btnSubmit);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyDate();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyDate()) {
                    //Submit to database & return to home
                }
            }
        });
    }

    public boolean verifyDate() {
        getDate();
        //Compare database
        //Return false if dates are unavailable
        if (!verifyDayDeliveries()) {
            Toast.makeText(getApplicationContext(), getString(R.string.day_invalid_delivery), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!verifyDayPickups()) {
            Toast.makeText(getApplicationContext(), getString(R.string.day_invalid_pickup), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!verifyTimeDeliveries()) {
            Toast.makeText(getApplicationContext(), getString(R.string.time_invalid_delivery), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!verifyTimePickups()) {
            Toast.makeText(getApplicationContext(), getString(R.string.time_invalid_pickup), Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(getApplicationContext(), getString(R.string.date_avaialble), Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean verifyDayDeliveries() {
        return true;
    }

    public boolean verifyDayPickups() {
        return true;
    }

    public boolean verifyTimeDeliveries() {
        return true;
    }

    public boolean verifyTimePickups() {
        return true;
    }

    public void getDate() {
        //Get data from spinners
        devTimeSelection = devTime.getSelectedItem().toString();
        devDaySelection = devDay.getSelectedItem().toString();
        picTimeSelection = picTime.getSelectedItem().toString();
        picDaySelection = picDay.getSelectedItem().toString();
    }

}