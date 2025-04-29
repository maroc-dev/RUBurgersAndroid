package ruburgers.mobile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
/**
 * Main activity used to start up the program
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class MainActivity extends AppCompatActivity implements NavbarFragment.TabSelectListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            NavbarFragment navbarFragment = new NavbarFragment();
            navbarFragment.setTabSelectListener(this);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.navbar_container, navbarFragment)
                    .commit();

            loadFragment(0);
        }
    }

    @Override
    public void onTabSelected(int position) {
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
                selectedFragment = new BeveragesFragment();
                break;
            case 3: // Sides

                selectedFragment = new SidesFragment();
                break;
            case 4: // Order History

                selectedFragment = new OrderHistoryFragment();
                break;
            default:
                selectedFragment = new BurgersFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_container, selectedFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}