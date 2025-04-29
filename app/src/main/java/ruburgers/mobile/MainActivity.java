package ruburgers.mobile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements NavbarFragment.TabSelectListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Create and add the navbar fragment
            NavbarFragment navbarFragment = new NavbarFragment();
            navbarFragment.setTabSelectListener(this);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.navbar_container, navbarFragment)
                    .commit();

            // Load the default content fragment (Burgers)
            loadFragment(0);
        }
    }

    @Override
    public void onTabSelected(int position) {
        // Load the appropriate fragment based on tab position
        loadFragment(position);
    }

    private void loadFragment(int position) {
        Fragment selectedFragment;

        switch (position) {
            case 0: // Burgers
                selectedFragment = new BurgersFragment();
                break;
            case 1: // Sandwiches
                selectedFragment = new SandwichesFragment();
                break;
            case 2: // Beverages
                selectedFragment = new BurgersFragment();

//                selectedFragment = new BeveragesFragment();
                break;
            case 3: // Sides
                selectedFragment = new BurgersFragment();

//                selectedFragment = new SidesFragment();
                break;
            case 4: // Order History
                selectedFragment = new BurgersFragment();

//                selectedFragment = new OrderHistoryFragment();
                break;
            default:
                selectedFragment = new BurgersFragment();
                break;
        }

        // Replace current content fragment with the selected one
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_container, selectedFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }



    public void navigateToComboSelection() {
//        // Replace current fragment with ComboSelectionFragment
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentContainer, new ComboSelectionFragment())
//                .addToBackStack(null)
//                .commit();
    }
}