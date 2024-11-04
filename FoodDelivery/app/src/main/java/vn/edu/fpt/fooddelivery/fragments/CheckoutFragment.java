package vn.edu.fpt.fooddelivery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.fooddelivery.R;
import vn.edu.fpt.fooddelivery.adapter.CartAdapter;
import com.example.fooddelivery.databinding.FragmentCheckoutBinding;
import vn.edu.fpt.fooddelivery.model.Food;
import vn.edu.fpt.fooddelivery.model.User;
import vn.edu.fpt.fooddelivery.utils.DatabaseHelper;
import vn.edu.fpt.fooddelivery.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutFragment extends Fragment implements CartAdapter.CartUpdateListener {

    private FragmentCheckoutBinding binding;
    private static final double DELIVERY_FEE = 0.0; // Fixed delivery fee
    private static final double TAX_RATE = 0.08; // 8% tax rate

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);

        // Load user and cart data from SharedPreferences
        SharedPrefManager sharedPrefManager = new SharedPrefManager(requireContext());
        User user = sharedPrefManager.getUser();
        ArrayList<Food> cartItems = sharedPrefManager.getCart();

        // Populate user data in the UI
        if (user != null) {
            binding.tvUserName.setText(user.getName());
            binding.tvUserPhone.setText(user.getPhone());
            // Assuming the first address is used as the shipping address
            if (!user.getAddresses().isEmpty()) {
                binding.tvUserAddress.setText(user.getAddresses().get(0).getAddressLine());
            }
        }

        // Set up the RecyclerView for cart items
        CartAdapter adapter = new CartAdapter(requireContext(), cartItems, this);
        binding.rvCheckoutItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCheckoutItems.setAdapter(adapter);

        // Initialize totals when the cart is first loaded
        updateTotals(cartItems);
        binding.radioGroupPayment.check(R.id.rbCreditCard);
        // Lắng nghe sự kiện thay đổi phương thức thanh toán
        binding.radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPayPal) {
                // Hiển thị thông báo khi chọn "Credit Card"
                Toast.makeText(requireContext(), "This feature is under development. Please use Cash.", Toast.LENGTH_SHORT).show();
                // Reset về "Cash"
                binding.radioGroupPayment.check(R.id.rbCreditCard);
            }
        });
        // Handle payment confirmation
        binding.btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.tvUserName.getText().toString();
                String userAddress = binding.tvUserAddress.getText().toString();
                String userPhone = binding.tvUserPhone.getText().toString();
                String note = binding.etNote.getText().toString();
                String paymentMethod = "Tiền mặt"; // This could be dynamically selected by the user in a real app

                if (!cartItems.isEmpty()) {
                    saveData(userName, userAddress, userPhone, cartItems, note, paymentMethod);
                } else {
                    Toast.makeText(requireContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCartUpdated(ArrayList<Food> updatedCart) {
        // Update totals whenever the cart changes
        updateTotals(updatedCart);
    }

    // Method to calculate and update totals
    private void updateTotals(ArrayList<Food> cartItems) {
        double itemsTotal = 0.0;

        // Calculate items total (subtotal)
        for (Food item : cartItems) {
            itemsTotal += item.getFee() * item.getNumberInCart();
        }

        // Calculate tax
        double tax = itemsTotal * TAX_RATE;

        // Calculate total (items total + delivery fee + tax)
        double total = itemsTotal + DELIVERY_FEE + tax;

        // Update UI with the calculated values
        binding.tvSubtotal.setText(String.format("$%.2f", itemsTotal));
        binding.tvShippingFee.setText(String.format("$%.2f", DELIVERY_FEE));
        binding.tvTotalAmount.setText(String.format("$%.2f", total));
    }

    // Method to save order data to the server using a web service
    private void saveData(String userName, String userAddress, String userPhone, ArrayList<Food> cartItems, String note, String paymentMethod) {
        // Create a list of simplified product objects
        ArrayList<Map<String, Object>> simplifiedProducts = new ArrayList<>();

        for (Food item : cartItems) {
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("title", item.getTitle());
            productMap.put("fee", item.getFee());
            productMap.put("numberInCart", item.getNumberInCart());
            simplifiedProducts.add(productMap);
        }

        // Convert simplifiedProducts to a JSON string
        String productsJson = new Gson().toJson(simplifiedProducts);

        // Base URL for the web service
        String url = "https://script.google.com/macros/s/AKfycbzFsq-CKwx5eXLybXl58Z99f_fImPY8AwDanWcZe_iJnSu9ZsH3UO4CYzmvCg2EQbgI/exec";

        // Create a POST request with parameters
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle success response
                        Toast.makeText(requireContext(), "Order confirmed: " + response, Toast.LENGTH_SHORT).show();
                        // Save order data to SQLite
                        DatabaseHelper db = new DatabaseHelper(requireContext());
                        double totalAmount = calculateTotal(cartItems); // Assuming you have a method to get the final amount

                        db.insertOrder(
                                userName,
                                userAddress,
                                userPhone,
                                paymentMethod,
                                note,
                                totalAmount,
                                cartItems
                        );

                        // Optionally, clear cart items in Shared Preferences after saving order
                        SharedPrefManager sharedPrefManager = new SharedPrefManager(requireContext());
                        sharedPrefManager.clearCart();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName); // Pass the user's name
                params.put("userAddress", userAddress); // Pass the user's address
                params.put("userPhone", userPhone); // Pass the user's phone number
                params.put("products", productsJson); // Add simplified products data
                params.put("paymentMethod", paymentMethod); // Pass the payment method
                params.put("note", note); // Add any note the user inputs

                return params;
            }
        };

        // Create a RequestQueue and add the request
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(stringRequest);
    }
    private double calculateTotal(ArrayList<Food> cartItems) {
        double subtotal = 0.0;
        for (Food item : cartItems) {
            subtotal += item.getFee() * item.getNumberInCart();
        }
        return subtotal + DELIVERY_FEE + (subtotal * TAX_RATE);
    }
}
