package au.edu.jcu.cp3406.lawncare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    Button scheduleDelivery;
    Button help;
    TextView deliveryDate;
    TextView pickupDate;

    DatabaseHelper db;

    int homeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);

        //Retrieve users ID and store in class variable
        final int userID = getIntent().getIntExtra("userID", 0);
        homeID = userID;

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

    @Override
    protected void onStart() {
        super.onStart();

        //Update preview
        deliveryDate.setText(String.format("%s%s", getString(R.string.next_delivery), db.getDelivery(homeID)));
        pickupDate.setText(String.format("%s%s", getString(R.string.next_pickup), db.getPickup(homeID)));
    }
}
