package au.edu.jcu.cp3406.lawncare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import java.time.LocalDate;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private ArrayList<DeliveryItem> deliveryItems;

    private RecyclerView mRecyclerView;
    private DeliveryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = new DatabaseHelper(this);

        createDeliveryList();
        buildRecyclerView();

    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.rvDeliveries);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new DeliveryAdapter(deliveryItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new DeliveryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                if (deliveryItems.get(position).getAddress().equals("No Deliveries or Pickups")) {
                    return;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParent());

                    builder.setTitle("Confirmation");
                    builder.setMessage("Remove Item?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeItem(position);
                            dialog.dismiss();
                        }
                    });
                    
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    private void createDeliveryList() {
        deliveryItems = db.getList(LocalDate.now().toString());
    }

    public void removeItem(int position) {
        String time = deliveryItems.get(position).getTime();
        String address = deliveryItems.get(position).getAddress();
        db.removeDelivery(time, address);
    }

}
