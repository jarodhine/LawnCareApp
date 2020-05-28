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

    private String currentDay;

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

        currentDay = LocalDate.now().toString();

        mAdapter.setOnItemClickListener(new DeliveryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                if (deliveryItems.get(position).getAddress().equals("No Deliveries or Pickups")) {
                }
                else {
                    //Prompt user for confirmation for deleting item
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this, R.style.AlertDialogCustom);

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
        //Retrieve all pickups and deliveries for current date
        deliveryItems = db.getList(LocalDate.now().toString());
    }

    public void removeItem(int position) {
        String time = deliveryItems.get(position).getTime();

        //Remove item from database
        db.removeDelivery(time, currentDay);

        //Update view
        mRecyclerView.removeViewAt(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, db.getDeliveryCount(currentDay));
        mAdapter.notifyDataSetChanged();
        mRecyclerView.invalidate();
        recreate();
    }

}
