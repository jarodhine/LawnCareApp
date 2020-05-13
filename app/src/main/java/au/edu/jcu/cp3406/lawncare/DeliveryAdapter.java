package au.edu.jcu.cp3406.lawncare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private ArrayList<DeliveryItem> mDeliveryList;

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        public TextView Time;
        public TextView Address;
        public TextView Type;

        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.tvTime);
            Address = itemView.findViewById(R.id.tvAddress);
            Type = itemView.findViewById(R.id.tvType);
        }
    }

    public DeliveryAdapter(ArrayList<DeliveryItem> deliveryList) {
        mDeliveryList = deliveryList;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item, parent, false);
        DeliveryViewHolder dvh = new DeliveryViewHolder(v);
        return  dvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        DeliveryItem currentItem = mDeliveryList.get(position);

        holder.Time.setText(currentItem.getTime());
        holder.Address.setText(currentItem.getAddress());
        holder.Type.setText(currentItem.getType());
    }

    @Override
    public int getItemCount() {
        return mDeliveryList.size();
    }
}
