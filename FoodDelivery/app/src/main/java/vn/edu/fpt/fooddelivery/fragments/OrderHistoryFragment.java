package vn.edu.fpt.fooddelivery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import java.util.ArrayList;
import java.util.List;
import com.example.fooddelivery.R;

import vn.edu.fpt.fooddelivery.adapter.OrderAdapter;
import vn.edu.fpt.fooddelivery.model.Order;
import vn.edu.fpt.fooddelivery.utils.DatabaseHelper;

public class OrderHistoryFragment extends Fragment implements OrderAdapter.OnOrderClickListener {

    private RecyclerView rvOrderHistory;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        rvOrderHistory = view.findViewById(R.id.rvOrderHistory);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        orderList = databaseHelper.getOrders();

        orderAdapter = new OrderAdapter(orderList, this);
        rvOrderHistory.setAdapter(orderAdapter);

        return view;
    }

    @Override
    public void onOrderClick(Order order) {
        // Tạo một instance của OrderDetailFragment
        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();

        // Gửi dữ liệu vào fragment thông qua Bundle
        Bundle bundle = new Bundle();
        bundle.putString("ORDER_ID", order.getOrderId());
        orderDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.setting_Container, orderDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
