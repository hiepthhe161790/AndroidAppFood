package vn.edu.fpt.fooddelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.fooddelivery.R;

import vn.edu.fpt.fooddelivery.adapter.CategoryAdapter;
import vn.edu.fpt.fooddelivery.adapter.FoodAdapter;
import com.example.fooddelivery.databinding.ActivityMainBinding;
import vn.edu.fpt.fooddelivery.fragments.CartFragment;
import vn.edu.fpt.fooddelivery.model.Category;
import vn.edu.fpt.fooddelivery.model.Food;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Food> arrayFood;
    private FoodAdapter foodAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavView.setBackground(null);
        ArrayList<Category> arrayList = new ArrayList();
        arrayList.add(new Category("Pizza",R.drawable.cat_1));
        arrayList.add(new Category("Burger",R.drawable.cat_2));
        arrayList.add(new Category("Hotdog",R.drawable.cat_3));
        arrayList.add(new Category("Drink",R.drawable.cat_4));
        arrayList.add(new Category("Donut",R.drawable.cat_5));
        CategoryAdapter adapter = new CategoryAdapter(this, arrayList, this::filterFoodByCategory);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.rvCategories.setAdapter(adapter);

        arrayFood = new ArrayList<>();
        arrayFood.add(new Food("Fresh Juice Mix", R.drawable.bosottieuden, "Nutritional Value: varies, blend of fresh seasonal fruit juices", 69.000, 100, 100, "Drink"));
        arrayFood.add(new Food("Lunch Combo", R.drawable.gaolutmuoime, "Nutritional Value: varies, combo meal with selected dishes and sides", 40.000, 100, 100, "Combo"));
        arrayFood.add(new Food("Classic Sandwich", R.drawable.comlutcahoiapchao, "Nutritional Value: 470kcal, sandwich with choice of filling", 86.000, 100, 100, "Donut"));
        arrayFood.add(new Food("Chicken Burger", R.drawable.bosottieuden, "Nutritional Value: 400kcal, burger with grilled chicken and fresh vegetables", 69.000, 100, 100, "Burger"));
        arrayFood.add(new Food("Veggie Pizza", R.drawable.gaolutmuoime, "Nutritional Value: 350kcal, vegetarian pizza with seasonal vegetables", 40.000, 100, 100, "Pizza"));
        arrayFood.add(new Food("Pepperoni pizza",R.drawable.pop_1,"slices pepperoni,mozzerella cheese, fresh oregano, ground black pepper, pizza sauce", 86.000, 100, 100, "Pizza"));
        arrayFood.add(new Food("Cheese Burger",R.drawable.pop_2,"beef, Gouda Cheese, Special Sauce, Lettuce, tomato",69.000, 100, 100, "Burger"));
        arrayFood.add(new Food("Vegetable pizza",R.drawable.pop_3,"olive oil, Vegetable oil, pitted kalamata, cherry tomatoes, fresh oregano, basil", 86.000, 100, 100, "Pizza"));
        arrayFood.add(new Food("Salmon Pizza", R.drawable.comlutcahoiapchao, "Nutritional Value: 470kcal, pizza with grilled salmon and fresh herbs", 86.000, 100, 100, "Pizza"));
        arrayFood.add(new Food("Pepper Beef Burger", R.drawable.bosottieuden, "Nutritional Value: 400kcal, beef burger with black pepper sauce and fresh greens", 69.000, 100, 100, "Burger"));
        arrayFood.add(new Food("Vegetarian Hotdog", R.drawable.gaolutmuoime, "Nutritional Value: 350kcal, vegetarian hotdog with seasonal veggies and sesame salt", 40.000, 100, 100, "Hotdog"));
        arrayFood.add(new Food("Seasonal Fruit Smoothie", R.drawable.comlutcahoiapchao, "Nutritional Value: varies, refreshing smoothie made from seasonal fruits", 86.000, 100, 100, "Drink"));


        foodAdapter = new FoodAdapter(this,arrayFood);
        binding.rvPopular.setAdapter(foodAdapter);
        binding.rvPopular.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeFragment(new CartFragment());
            }
        });
        binding.bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        // This will refresh the current activity (MainActivity)
                        foodAdapter.updateData(arrayFood);
                        return true; // Optional: You can handle home action here
                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class)); // Replace ProfileActivity with your profile activity class
                        return true;
                    case R.id.support:
                        startActivity(new Intent(MainActivity.this, CalorieCalculatorActivity.class)); // Replace SupportActivity with your support activity class
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class)); // Replace SettingsActivity with your settings activity class
                        return true;
                }
                return false;
            }
        });
        // Search
        EditText searchEditText = binding.editTextTextPersonName;
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase();
                filterFoodByTitle(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed here
            }
        });
    }

    public void swipeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.silde_in
                ,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out)
                .replace(R.id.main_Container,fragment)
                .addToBackStack(null)
                .commit();
    }
    // Method to filter food by selected category
    public void filterFoodByCategory(String category) {
        ArrayList<Food> filteredFood = new ArrayList<>();
        for (Food food : arrayFood) {
            if (food.getCategory().equals(category)) {
                filteredFood.add(food);
            }
        }
         foodAdapter.updateData(filteredFood);
    }
    private void filterFoodByTitle(String title) {
        ArrayList<Food> filteredFood = new ArrayList<>();
        for (Food food : arrayFood) {
            if (food.getTitle().toLowerCase().contains(title)) {
                filteredFood.add(food);
            }
        }
        foodAdapter.updateData(filteredFood);
    }
}