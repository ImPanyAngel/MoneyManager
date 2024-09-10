package com.example.moneymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsCompat.Type;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.moneymanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean onHomeFragment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply WindowInsetsListener to the root view
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        binding.btnSwitchScreens.setOnClickListener(v -> {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
            assert navHostFragment != null;
            NavController navController = navHostFragment.getNavController();
            if (onHomeFragment) {
                binding.btnSwitchScreens.setText(R.string.title_home);
                navController.navigate(R.id.action_homeFragment_to_subscriptionFragment);
            } else {
                binding.btnSwitchScreens.setText(R.string.title_sub);
                navController.navigate(R.id.action_subscriptionFragment_to_homeFragment);
            }
            onHomeFragment = !onHomeFragment;
        });
    }
}
