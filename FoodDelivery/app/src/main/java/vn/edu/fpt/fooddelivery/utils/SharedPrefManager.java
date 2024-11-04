package vn.edu.fpt.fooddelivery.utils;

import android.content.Context;
import android.content.SharedPreferences;
import vn.edu.fpt.fooddelivery.model.Food;
import vn.edu.fpt.fooddelivery.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPrefManager {

    private static final String PREF_NAME = "cart_prefs";
    private static final String CART_KEY = "cart_items";
    private static final String USER_KEY = "user_data";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // Save cart items
    public void saveCart(ArrayList<Food> cartItems) {
        String json = gson.toJson(cartItems);
        editor.putString(CART_KEY, json);
        editor.apply();
    }

    // Retrieve cart items
    public ArrayList<Food> getCart() {
        String json = sharedPreferences.getString(CART_KEY, null);
        Type type = new TypeToken<ArrayList<Food>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    // Clear cart items
    public void clearCart() {
        editor.remove(CART_KEY);
        editor.apply();
    }
    // Save user information
    public void saveUser(User user) {
        String json = gson.toJson(user);
        editor.putString(USER_KEY, json);
        editor.apply();
    }

    // Retrieve user information
    public User getUser() {
        String json = sharedPreferences.getString(USER_KEY, null);
        Type type = new TypeToken<User>() {}.getType();
        return json != null ? gson.fromJson(json, type) : null;
    }
    public void clearProfile() {
        editor.remove(USER_KEY);
        editor.apply();
    }
}
