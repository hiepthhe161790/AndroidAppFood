package vn.edu.fpt.fooddelivery;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.example.fooddelivery.R;

import vn.edu.fpt.fooddelivery.fragments.OrderHistoryFragment;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Button for viewing Order History
        Button btnOrderHistory = findViewById(R.id.btnOrderHistory);
        btnOrderHistory.setOnClickListener(view -> showOrderHistoryFragment());
    }
    // Method to display the OrderHistoryFragment
    private void showOrderHistoryFragment() {
        OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, orderHistoryFragment); // Replaces the content with OrderHistoryFragment
        transaction.addToBackStack(null); // Allows the user to navigate back to the settings screen
        transaction.commit();
    }
}
