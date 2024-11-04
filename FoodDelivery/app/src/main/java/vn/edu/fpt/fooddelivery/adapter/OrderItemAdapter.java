package vn.edu.fpt.fooddelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.fooddelivery.R;
import vn.edu.fpt.fooddelivery.model.OrderItem;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private final List<OrderItem> orderItems;

    public OrderItemAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvFee.setText("$" + item.getFee());
        holder.tvQuantity.setText("Quantity: " + item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvFee, tvQuantity;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvTitle = itemView.findViewById(R.id.tvOrderDetailTitle); // Make sure to match with your layout
//            tvFee = itemView.findViewById(R.id.tvFee); // Make sure to match with your layout
            tvQuantity = itemView.findViewById(R.id.numItems); // Make sure to match with your layout
        }
    }
}
