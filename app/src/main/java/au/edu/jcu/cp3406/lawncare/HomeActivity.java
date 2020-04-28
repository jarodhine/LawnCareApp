package au.edu.jcu.cp3406.lawncare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    Button scheduleDelivery;
    Button help;
    TextView deliveryDate;
    TextView pickupDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final int userID = getIntent().getIntExtra("userID", 0);

        scheduleDelivery = findViewById(R.id.btnScheduleDelivery);
        help = findViewById(R.id.btnHelp);
        deliveryDate = findViewById(R.id.tvDeliveryDate);
        pickupDate = findViewById(R.id.tvPickupDate);

        scheduleDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DeliveryActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
    }

}
