package vn.edu.fpt.fooddelivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddelivery.R;
import vn.edu.fpt.fooddelivery.adapter.CartAdapter;
import com.example.fooddelivery.databinding.FragmentCartBinding;
import vn.edu.fpt.fooddelivery.model.Food;
import vn.edu.fpt.fooddelivery.utils.SharedPrefManager;

import java.util.ArrayList;

public class CartFragment extends Fragment implements CartAdapter.CartUpdateListener {

    private FragmentCartBinding binding;
    private static final double DELIVERY_FEE = 0.0; // Fixed delivery fee
    private static final double TAX_RATE = 0.08; // 8% tax rate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        // Load cart data from SharedPreferences
        SharedPrefManager sharedPrefManager = new SharedPrefManager(requireContext());
        // Clear cart data when fragment is opened
//        sharedPrefManager.clearCart();
        ArrayList<Food> arrayFood = sharedPrefManager.getCart();

        // Set up the adapter
        CartAdapter adapter = new CartAdapter(requireContext(), arrayFood, this);
        binding.rvItemsInCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvItemsInCart.setAdapter(adapter);

        // Initialize totals when the cart is first loaded
        updateTotals(arrayFood);
        binding.btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeFragment(new CheckoutFragment());
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
        binding.txtTotalFee.setText(String.format("$%.2f", itemsTotal));
        binding.txtDelveryService.setText(String.format("$%.2f", DELIVERY_FEE));
        binding.txtTax.setText(String.format("$%.2f", tax));
        binding.textView20.setText(String.format("$%.2f", total));
    }
    public void swipeFragment(Fragment fragment) {
        // Use getParentFragmentManager() in a Fragment
        getParentFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.silde_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_Container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
