package ruburgers.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import model.AddOns;
import model.Bread;
import model.Protein;
import model.Sandwich;

/**
 * Fragment class for the sandwich menu
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class SandwichesFragment extends Fragment {

    private Spinner breadSpinner;
    private Spinner proteinSpinner;
    private CheckBox lettuceCheckbox;
    private CheckBox tomatoCheckbox;
    private CheckBox onionsCheckbox;
    private CheckBox avocadoCheckbox;
    private CheckBox cheeseCheckbox;
    private EditText quantityEditText;
    private ToggleButton comboToggle;
    private TextView priceTextView;
    private Button viewOrderButton;
    private Button addToOrderButton;

    private DataManager dataManager;
    private ArrayList<AddOns> selectedAddOns;
    private Bread selectedBread;
    private Protein selectedProtein;
    private int quantity;
    private Sandwich sandwichItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sandwiches, container, false);

        dataManager = DataManager.getInstance();

        selectedAddOns = new ArrayList<>();
        selectedBread = Bread.BRIOCHE;
        selectedProtein = Protein.ROAST_BEEF;
        quantity = 1;

        initializeUIComponents(view);

        setupListeners();

        updateSandwichAndPrice();

        return view;
    }

    private void initializeUIComponents(View view) {
        breadSpinner = view.findViewById(R.id.sandwichBreadSpinner);
        proteinSpinner = view.findViewById(R.id.sandwichProteinSpinner);
        lettuceCheckbox = view.findViewById(R.id.lettuceSandwichCheckbox);
        tomatoCheckbox = view.findViewById(R.id.tomatoSandwichCheckbox);
        onionsCheckbox = view.findViewById(R.id.onionsSandwichCheckbox);
        avocadoCheckbox = view.findViewById(R.id.avacadoSandwichCheckbox);
        cheeseCheckbox = view.findViewById(R.id.cheeseSandwichCheckbox);
        quantityEditText = view.findViewById(R.id.sandwichQuantity);
        comboToggle = view.findViewById(R.id.sandwichComboMeal);
        priceTextView = view.findViewById(R.id.sandwichCurrentPrice);
        viewOrderButton = view.findViewById(R.id.sandwichViewOrder);
        addToOrderButton = view.findViewById(R.id.sandwichOrderButton);

        quantityEditText.setText("1");
    }

    private void setupListeners() {
        breadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String breadType = parent.getItemAtPosition(position).toString();
                updateBreadSelection(breadType);
                updateSandwichAndPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        proteinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String proteinType = parent.getItemAtPosition(position).toString();
                updateProteinSelection(proteinType);
                updateSandwichAndPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        View.OnClickListener checkboxListener = v -> {
            updateAddOns();
            updateSandwichAndPrice();
        };

        lettuceCheckbox.setOnClickListener(checkboxListener);
        tomatoCheckbox.setOnClickListener(checkboxListener);
        onionsCheckbox.setOnClickListener(checkboxListener);
        avocadoCheckbox.setOnClickListener(checkboxListener);
        cheeseCheckbox.setOnClickListener(checkboxListener);

        quantityEditText.setOnEditorActionListener((v, actionId, event) -> {
            updateQuantity();
            updateSandwichAndPrice();
            return false;
        });

        quantityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateQuantity();
                updateSandwichAndPrice();
            }
        });

        comboToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSandwichAndPrice();
        });

        viewOrderButton.setOnClickListener(v -> navigateToOrderView());
        addToOrderButton.setOnClickListener(v -> addToOrder());
    }

    private void updateBreadSelection(String breadType) {
        switch (breadType.toUpperCase()) {
            case "BRIOCHE":
                selectedBread = Bread.BRIOCHE;
                break;
            case "WHEAT":
            case "WHEAT BREAD":
                selectedBread = Bread.WHEAT;
                break;
            case "PRETZEL":
                selectedBread = Bread.PRETZEL;
                break;
            default:
                selectedBread = Bread.BRIOCHE;
                break;
        }
    }

    private void updateProteinSelection(String proteinType) {
        switch (proteinType.toUpperCase()) {
            case "ROAST BEEF":
                selectedProtein = Protein.ROAST_BEEF;
                break;
            case "SALMON":
                selectedProtein = Protein.SALMON;
                break;
            case "CHICKEN":
                selectedProtein = Protein.CHICKEN;
                break;
            default:
                selectedProtein = Protein.ROAST_BEEF;
                break;
        }
    }

    private void updateAddOns() {
        selectedAddOns.clear();

        if (lettuceCheckbox.isChecked()) selectedAddOns.add(AddOns.LETTUCE);
        if (tomatoCheckbox.isChecked()) selectedAddOns.add(AddOns.TOMATOES);
        if (onionsCheckbox.isChecked()) selectedAddOns.add(AddOns.ONIONS);
        if (avocadoCheckbox.isChecked()) selectedAddOns.add(AddOns.AVOCADOS);
        if (cheeseCheckbox.isChecked()) selectedAddOns.add(AddOns.CHEESE);
    }

    private void updateQuantity() {
        try {
            int inputQuantity = Integer.parseInt(quantityEditText.getText().toString());
            if (inputQuantity > 0) {
                quantity = inputQuantity;
            } else {
                quantity = 1;
                quantityEditText.setText("1");
                Toast.makeText(getActivity(), "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            quantity = 1;
            quantityEditText.setText("1");
            Toast.makeText(getActivity(), "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSandwichAndPrice() {
        sandwichItem = new Sandwich(selectedBread, selectedProtein, new ArrayList<>(selectedAddOns), quantity);

        updatePriceDisplay();
    }

    private void updatePriceDisplay() {
        double price = sandwichItem.price();

        priceTextView.setText(String.format("$%.2f", price));
    }

    private void addToOrder() {
        updateQuantity();

        updateAddOns();

        updateSandwichAndPrice();

        if (comboToggle.isChecked()) {
            navigateToComboSelection();
            resetForm();
            updatePriceDisplay();
        } else {
            dataManager.addOrderItem(sandwichItem);

            Toast.makeText(getActivity(), "Sandwich added to order", Toast.LENGTH_SHORT).show();

            resetForm();
            updatePriceDisplay();
        }
    }

    private void resetForm() {
        breadSpinner.setSelection(0);
        proteinSpinner.setSelection(0);
        lettuceCheckbox.setChecked(false);
        tomatoCheckbox.setChecked(false);
        onionsCheckbox.setChecked(false);
        avocadoCheckbox.setChecked(false);
        cheeseCheckbox.setChecked(false);
        quantityEditText.setText("1");
        comboToggle.setChecked(false);

        selectedAddOns.clear();
        selectedBread = Bread.BRIOCHE;
        selectedProtein = Protein.ROAST_BEEF;
        quantity = 1;

        updateSandwichAndPrice();
    }

    private void navigateToOrderView() {
        OrderDialog orderDialog = new OrderDialog(getActivity());
        orderDialog.show();
    }

    private void navigateToComboSelection() {
        updateSandwichAndPrice();

        ComboSelectionDialog comboDialog = new ComboSelectionDialog(
                getActivity(),
                sandwichItem,
                comboToggle,
                priceTextView);
        comboDialog.show();
    }
}