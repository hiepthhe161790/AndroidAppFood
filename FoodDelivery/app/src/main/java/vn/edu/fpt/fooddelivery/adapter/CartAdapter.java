package vn.edu.fpt.fooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fooddelivery.databinding.CartItemBinding;

import vn.edu.fpt.fooddelivery.model.Food;
import vn.edu.fpt.fooddelivery.utils.SharedPrefManager;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public Context context;
    public ArrayList<Food> data;
    private CartUpdateListener cartUpdateListener;

    // Define an interface for the callback
    public interface CartUpdateListener {
        void onCartUpdated(ArrayList<Food> updatedCart);
    }

    public CartAdapter(Context context, ArrayList<Food> data, CartUpdateListener listener) {
        this.context = context;
        this.data = data;
        this.cartUpdateListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CartViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Food foodItem = data.get(position);
        holder.binding.txtTitleCart.setText(foodItem.getTitle());
        holder.binding.picCart.setImageResource(foodItem.getPic());
        holder.binding.feeEachItem.setText(String.valueOf(foodItem.getCalo()));
        holder.binding.totalEachItem.setText(String.valueOf(foodItem.getFee() * foodItem.getNumberInCart()));
        holder.binding.numItems.setText(String.valueOf(foodItem.getNumberInCart()));

        holder.binding.plusBtnCart.setOnClickListener(v -> {
            foodItem.setNumberInCart(foodItem.getNumberInCart() + 1);
            notifyDataSetChanged();
            saveCartData();
            cartUpdateListener.onCartUpdated(data); // Notify the fragment about the update
        });

        holder.binding.minBtnCart.setOnClickListener(v -> {
            if (foodItem.getNumberInCart() > 1) {
                foodItem.setNumberInCart(foodItem.getNumberInCart() - 1);
            } else if (foodItem.getNumberInCart() == 1) {
                data.remove(position); // Remove item when quantity is zero
            }
            notifyDataSetChanged();
            saveCartData();
            cartUpdateListener.onCartUpdated(data); // Notify the fragment about the update
        });
    }

    // Save cart data to SharedPreferences
    private void saveCartData() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        sharedPrefManager.saveCart(data);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        public CartItemBinding binding;

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
