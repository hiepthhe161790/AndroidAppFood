package vn.edu.fpt.fooddelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.fooddelivery.R;
import vn.edu.fpt.fooddelivery.model.Order;
import vn.edu.fpt.fooddelivery.model.OrderItem;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnOrderClickListener onOrderClickListener;

    public OrderAdapter(List<Order> orderList, OnOrderClickListener onOrderClickListener) {
        this.orderList = orderList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Format items list without square brackets or object references
        StringBuilder itemsText = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsText.append(item.getTitle()) // Assuming OrderItem has a getTitle() method
                    .append(" x")
                    .append(item.getQuantity())
                    .append(", ");
        }

        // Remove the trailing comma and space
        if (itemsText.length() > 0) {
            itemsText.setLength(itemsText.length() - 2);
        }

        holder.tvOrderId.setText("Order ID: " + order.getOrderId());
        holder.tvOrderDate.setText("Date: " + order.getDate());
        holder.tvOrderItems.setText("Items: " + itemsText.toString());
        holder.tvOrderTotal.setText("Total: $" + String.format("%.2f", order.getTotal()));

        // Set click listener for the "View Details" button
        holder.btnViewDetails.setOnClickListener(v -> {
            if (onOrderClickListener != null) {
                onOrderClickListener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderItems, tvOrderTotal;
        ImageButton btnViewDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderItems = itemView.findViewById(R.id.tvOrderItems);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }

    // Define an interface for item clicks
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }
}
