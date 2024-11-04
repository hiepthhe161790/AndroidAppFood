package vn.edu.fpt.fooddelivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import vn.edu.fpt.fooddelivery.utils.SharedPrefManager;
import vn.edu.fpt.fooddelivery.model.Food;

import com.example.fooddelivery.databinding.FragmentFoodDetailsBinding;
import java.util.ArrayList;

public class FoodDetailsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFoodDetailsBinding binding = FragmentFoodDetailsBinding.inflate(inflater,container,false);
        binding.txtFoodName.setText(requireArguments().getString("title"));
        binding.txtFoodPrice.setText("$"+requireArguments().getString("price"));
        binding.txtDescription.setText(requireArguments().getString("description"));
        binding.imgFoodDetails.setImageResource(requireArguments().getInt("pic"));
        binding.txtCalories.setText(requireArguments().getInt("calories") + " kcal");
        binding.btnPlus.setOnClickListener(view -> {
            int num = Integer.parseInt(binding.txtNum.getText().toString());
            int newNum = (num+1);
            String n = String.valueOf(newNum);
            binding.txtNum.setText(n);
        });
        binding.btnMin.setOnClickListener(view -> {
            int num = Integer.parseInt(binding.txtNum.getText().toString());
            if (num != 1){
                int newNum = (num-1);
                String n = String.valueOf(newNum);
                binding.txtNum.setText(n);
            }
        });
        binding.btnAddToCart.setOnClickListener(view -> {
            int quantity = Integer.parseInt(binding.txtNum.getText().toString());
            Food foodItem = new Food(requireArguments().getString("title"),
                    requireArguments().getInt("pic"),
                    requireArguments().getString("description"),
                    Double.parseDouble(requireArguments().getString("price")),
                    requireArguments().getInt("calories"),
                    quantity,
                    requireArguments().getString("category"));

            SharedPrefManager sharedPrefManager = new SharedPrefManager(requireContext());
            ArrayList<Food> cartItems = sharedPrefManager.getCart();

            // Check if the item is already in the cart
            boolean isItemInCart = false;
            for (Food item : cartItems) {
                if (item.getTitle().equals(foodItem.getTitle())) {
                    isItemInCart = true;
                    item.setNumberInCart(item.getNumberInCart() + quantity); // Update quantity if already in cart
                    break;
                }
            }

            // If item is not in the cart, add it
            if (!isItemInCart) {
                cartItems.add(foodItem);
            }

            sharedPrefManager.saveCart(cartItems); // Save updated cart
            Toast.makeText(requireContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });
        return binding.getRoot();
    }
}