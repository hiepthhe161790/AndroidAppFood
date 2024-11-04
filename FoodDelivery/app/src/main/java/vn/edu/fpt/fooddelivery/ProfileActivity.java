package vn.edu.fpt.fooddelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;

import vn.edu.fpt.fooddelivery.model.User;
import vn.edu.fpt.fooddelivery.model.Address;
import vn.edu.fpt.fooddelivery.utils.SharedPrefManager;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private Button buttonAddHome, buttonAddWork, buttonAddOther, buttonSavePersonalInfo, buttonSetHomeDefault, buttonSetWorkDefault, buttonSetOtherDefault;
    private TextView addressHomeText, addressWorkText, addressOtherText;
    private EditText editTextName, editTextEmail, editTextPhone;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        addressHomeText = findViewById(R.id.addressHomeText);
        addressWorkText = findViewById(R.id.addressWorkText);
        addressOtherText = findViewById(R.id.addressOtherText);
        buttonAddHome = findViewById(R.id.buttonAddHome);
        buttonAddWork = findViewById(R.id.buttonAddWork);
        buttonAddOther = findViewById(R.id.buttonAddOther);
        buttonSavePersonalInfo = findViewById(R.id.buttonSavePersonalInfo);
        buttonSetHomeDefault = findViewById(R.id.buttonSetHomeDefault);
        buttonSetWorkDefault = findViewById(R.id.buttonSetWorkDefault);
        buttonSetOtherDefault = findViewById(R.id.buttonSetOtherDefault);
        TextView textViewDefaultAddress = findViewById(R.id.tvDefaultAddress);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        user = sharedPrefManager.getUser();  // Load user data

        if (user != null) {
            populateUserData(user);
            enableAddressButtons(true);

            if (!user.getAddresses().isEmpty()) {
                Address defaultAddress = user.getAddresses().get(0); // Lấy địa chỉ đầu tiên
                textViewDefaultAddress.setText("Default Address: " + defaultAddress.getAddressLine());
            } else {
                textViewDefaultAddress.setText("No default address available");
            }
        } else {
            enableAddressButtons(false); // Initially hide the address buttons
        }

        // Handle save personal info button
        buttonSavePersonalInfo.setOnClickListener(v -> savePersonalInfo());

        // Handle address default button clicks
        buttonSetHomeDefault.setOnClickListener(v -> setDefaultAddress("home"));
        buttonSetWorkDefault.setOnClickListener(v -> setDefaultAddress("work"));
        buttonSetOtherDefault.setOnClickListener(v -> setDefaultAddress("other"));

        // Handle button clicks to add/edit addresses
        buttonAddHome.setOnClickListener(v -> showAddressInputDialog("home"));
        buttonAddWork.setOnClickListener(v -> showAddressInputDialog("work"));
        buttonAddOther.setOnClickListener(v -> showAddressInputDialog("other"));
    }

    private void enableAddressButtons(boolean enable) {
        buttonAddHome.setVisibility(enable ? View.VISIBLE : View.GONE);
        buttonAddWork.setVisibility(enable ? View.VISIBLE : View.GONE);
        buttonAddOther.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void savePersonalInfo() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user == null) {
            user = new User(name, email, phone, new ArrayList<>());
        } else {
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
        }

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.saveUser(user);

        Toast.makeText(this, "Personal Information Saved", Toast.LENGTH_SHORT).show();
        enableAddressButtons(true);
    }

    private void setDefaultAddress(String addressType) {
        Address selectedAddress = getAddressByType(addressType);
        if (selectedAddress != null) {
            // Chuyển đổi danh sách từ List sang ArrayList để có thể thao tác
            ArrayList<Address> addresses = new ArrayList<>(user.getAddresses());

            // Xóa địa chỉ được chọn khỏi danh sách
            addresses.remove(selectedAddress);
            // Đưa địa chỉ được chọn lên vị trí đầu tiên
            addresses.add(0, selectedAddress);

            // Cập nhật danh sách địa chỉ trong đối tượng user
            user.setAddresses(addresses);  // Quan trọng: cập nhật lại danh sách địa chỉ của user

            // Lưu lại thông tin người dùng sau khi cập nhật
            SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
            sharedPrefManager.saveUser(user);

            Toast.makeText(this, addressType + " address set as default.", Toast.LENGTH_SHORT).show();

            // Cập nhật UI với địa chỉ mặc định mới
            TextView textViewDefaultAddress = findViewById(R.id.tvDefaultAddress);
            textViewDefaultAddress.setText("Default Address: " + selectedAddress.getAddressLine());
        } else {
            Toast.makeText(this, "No " + addressType + " address found.", Toast.LENGTH_SHORT).show();
        }
    }


    private Address getAddressByType(String addressType) {
        for (Address address : user.getAddresses()) {
            if (address.getAddressType().equals(addressType)) {
                return address;
            }
        }
        return null;
    }

    private void showAddressInputDialog(String addressType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Address for " + addressType);

        final EditText input = new EditText(this);
        input.setHint("Enter Address");
        builder.setView(input);

        Address existingAddress = getAddressByType(addressType);
        if (existingAddress != null) {
            input.setText(existingAddress.getAddressLine());
        }

        builder.setPositiveButton("OK", (dialog, which) -> {
            String address = input.getText().toString();
            if (!address.isEmpty()) {
                addOrUpdateAddress(addressType, address);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addOrUpdateAddress(String addressType, String addressLine) {
        Address existingAddress = getAddressByType(addressType);

        if (existingAddress != null) {
            existingAddress.setAddressLine(addressLine);
        } else {
            Address newAddress = new Address(addressLine, addressType);
            user.getAddresses().add(newAddress);
        }

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.saveUser(user);

        updateAddressUI(addressType, addressLine);
    }

    private void updateAddressUI(String addressType, String addressLine) {
        if (addressType.equals("home")) {
            addressHomeText.setText("Home: " + addressLine);
            addressHomeText.setVisibility(View.VISIBLE);
            buttonAddHome.setText("Edit Home Address");
        } else if (addressType.equals("work")) {
            addressWorkText.setText("Work: " + addressLine);
            addressWorkText.setVisibility(View.VISIBLE);
            buttonAddWork.setText("Edit Work Address");
        } else if (addressType.equals("other")) {
            addressOtherText.setText("Other: " + addressLine);
            addressOtherText.setVisibility(View.VISIBLE);
            buttonAddOther.setText("Edit Other Address");
        }
    }

    private void populateUserData(User user) {
        editTextName.setText(user.getName());
        editTextEmail.setText(user.getEmail());
        editTextPhone.setText(user.getPhone());

        for (Address address : user.getAddresses()) {
            updateAddressUI(address.getAddressType(), address.getAddressLine());
        }
    }
}
