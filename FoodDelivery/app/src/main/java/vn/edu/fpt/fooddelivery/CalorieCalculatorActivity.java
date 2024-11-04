package vn.edu.fpt.fooddelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;

public class CalorieCalculatorActivity extends AppCompatActivity {

    private EditText etWeight, etHeight, etAge,etCalorieIntake;
    private Spinner spActivityLevel;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_calculator);

        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etAge = findViewById(R.id.etAge);
        etCalorieIntake = findViewById(R.id.etCalorieIntake);
        spActivityLevel = findViewById(R.id.spActivityLevel);
        tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.btnCalculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTDEE();
            }
        });
    }

    private void calculateTDEE() {
        String weightStr = etWeight.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String calorieIntakeStr = etCalorieIntake.getText().toString().trim();

        if (weightStr.isEmpty() || heightStr.isEmpty() || ageStr.isEmpty() || calorieIntakeStr.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double height = Double.parseDouble(heightStr);
        int age = Integer.parseInt(ageStr);
        double calorieIntake = Double.parseDouble(calorieIntakeStr);

        // BMR calculation (Mifflin-St Jeor Equation)
        double bmr = 10 * weight + 6.25 * height - 5 * age + 5;  // For male
        // If the user is female, subtract 161 from BMR

        // Activity level multiplier
        String activityLevel = spActivityLevel.getSelectedItem().toString();
        double activityMultiplier = getActivityMultiplier(activityLevel);

        // TDEE Calculation
        double tdee = bmr * activityMultiplier;

        // Calculate calorie difference
        double calorieDifference = calorieIntake - tdee;

        // Format result message
        String resultMessage = String.format("Your Total Daily Energy Expenditure (TDEE) is: %.2f kcal\n", tdee);
        resultMessage += String.format("You consumed: %.2f kcal\n", calorieIntake);
        if (calorieDifference > 0) {
            resultMessage += String.format("You consumed %.2f kcal more than your TDEE.", calorieDifference);
        } else if (calorieDifference < 0) {
            resultMessage += String.format("You consumed %.2f kcal less than your TDEE.", Math.abs(calorieDifference));
        } else {
            resultMessage += "You consumed exactly your TDEE.";
        }

        // Show result in AlertDialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Calorie Calculation Result")
                .setMessage(resultMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    private double getActivityMultiplier(String activityLevel) {
        switch (activityLevel) {
            case "Sedentary (little to no exercise)":
                return 1.2;
            case "Lightly active (light exercise)":
                return 1.375;
            case "Moderately active (moderate exercise)":
                return 1.55;
            case "Very active (hard exercise)":
                return 1.725;
            case "Super active (hard exercise and physical job)":
                return 1.9;
            default:
                return 1.2;
        }
    }
}
