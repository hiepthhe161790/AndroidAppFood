package vn.edu.fpt.fooddelivery.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddelivery.R;
import vn.edu.fpt.fooddelivery.adapter.OrderItemAdapter;
import vn.edu.fpt.fooddelivery.model.Order;
import vn.edu.fpt.fooddelivery.model.OrderItem;
import vn.edu.fpt.fooddelivery.utils.DatabaseHelper;

import java.util.List;

public class OrderDetailFragment extends Fragment {

    private static final String ARG_ORDER_ID = "order_id";
    private TextView tvOrderID, tvOrderDate, tvOrderStatus, tvPaymentMethod, tvSubtotal, tvShippingFee, tvTotalAmount;
    private RecyclerView rvOrderItems;
    private DatabaseHelper databaseHelper;
    private String orderId;

    public static OrderDetailFragment newInstance(String orderId) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getString(ARG_ORDER_ID);
        }
        Log.d("OrderDetailFragment", "Order ID: " + orderId);  // Log the order ID for debugging
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        // Initialize UI components
        tvOrderID = view.findViewById(R.id.tvOrderID);
        tvOrderDate = view.findViewById(R.id.tvOrderDate);
        tvOrderStatus = view.findViewById(R.id.tvOrderStatus);
        tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod);
        tvSubtotal = view.findViewById(R.id.tvSubtotal);
        tvShippingFee = view.findViewById(R.id.tvShippingFee);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        rvOrderItems = view.findViewById(R.id.rvOrderItems);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(requireContext());

        // Load order details
        loadOrderDetails(orderId);

        return view;
    }

    private void loadOrderDetails(String orderId) {
        Order order = databaseHelper.getOrderDetails(orderId);
        if (order != null) {
            tvOrderID.setText("Order ID: " + order.getOrderId());
            tvOrderDate.setText("Order Date: " + order.getDate());
            tvOrderStatus.setText("Status: Delivered"); // Update based on your logic
            tvPaymentMethod.setText(order.getPaymentMethod());

            tvSubtotal.setText("$" + (order.getTotal() - 5)); // Example
            tvShippingFee.setText("$5"); // Example
            tvTotalAmount.setText("$" + order.getTotal());

            // Load order items
            List<OrderItem> orderItems = databaseHelper.getOrderItems(order.getOrderId());
            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(orderItems);
            rvOrderItems.setLayoutManager(new LinearLayoutManager(getContext()));
            rvOrderItems.setAdapter(orderItemAdapter);
        } else {
            // Handle case where order is null
            Log.e("OrderDetailFragment", "Order not found for ID: " + orderId);
        }
    }
}
