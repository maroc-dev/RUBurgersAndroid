package ruburgers.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;

/**
 * Navigation bar fragment for the app.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class NavbarFragment extends Fragment {
    private TabSelectListener tabSelectListener;

    public interface TabSelectListener {
        void onTabSelected(int position);
    }

    public void setTabSelectListener(TabSelectListener listener) {
        this.tabSelectListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navbar, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabSelectListener != null) {
                    tabSelectListener.onTabSelected(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Nothing to do
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Nothing to do
            }
        });

        return view;
    }
}