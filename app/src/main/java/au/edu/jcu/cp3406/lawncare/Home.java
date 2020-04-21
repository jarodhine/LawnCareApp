package au.edu.jcu.cp3406.lawncare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    Button scheduleDelivery;
    Button help;
    TextView deliveryDate;
    TextView pickupDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scheduleDelivery = findViewById(R.id.btnScheduleDelivery);
        help = findViewById(R.id.btnHelp);
        deliveryDate = findViewById(R.id.tvDeliveryDate);
        pickupDate = findViewById(R.id.tvPickupDate);

        scheduleDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
