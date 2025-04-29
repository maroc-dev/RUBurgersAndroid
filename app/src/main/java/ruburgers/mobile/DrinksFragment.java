package ruburgers.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.GridLayoutManager;
import model.Beverage;
import model.Flavor;
import model.Size;

import java.util.ArrayList;
import java.util.List;

public class DrinksFragment extends Fragment {
    private Spinner sizeSpinner;
    private Spinner flavorSpinner;
    private EditText quantityEditText;
    private TextView priceTextView;
    private Button addToOrderButton;
    private Button viewOrderButton;

    private Size selectedSize;
    private Flavor selectedFlavor;
    private int quantity;
    private Beverage beverageItem;
    private DataManager dataManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drinks, container, false);

        dataManager = DataManager.getInstance();

        selectedSize = Size.MEDIUM;  // Default
        selectedFlavor = Flavor.COLA; // Default
        quantity = 1;

        initializeUIComponents(view);
        setupRecyclerView(view);
        setupListeners();
        updateDrinkAndPrice();

        return view;
    }
    private void initializeUIComponents(View view) {
        sizeSpinner = view.findViewById(R.id.drinkSizeSpinner);
        flavorSpinner = view.findViewById(R.id.drinkFlavorSpinner);
        quantityEditText = view.findViewById(R.id.drinkQuantityEditText);
        priceTextView = view.findViewById(R.id.drinkCurrentPrice);
        addToOrderButton = view.findViewById(R.id.drinkAddToOrderButton);
        viewOrderButton = view.findViewById(R.id.drinkViewOrderButton);

        quantityEditText.setText("1");

        // Size spinner setup
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Small", "Medium", "Large"}
        );
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        // Flavor spinner setup
        ArrayAdapter<String> flavorAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Cola", "Tea", "Juice", "Lemonade", "Water"}
        );
        flavorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flavorSpinner.setAdapter(flavorAdapter);
    }

    private List<Drink> createDrinkList() {
        List<Drink> drinks = new ArrayList<>();
        drinks.add(new Drink("Cola", R.drawable.cola_image));
        drinks.add(new Drink("Tea", R.drawable.tea_image));
        drinks.add(new Drink("Juice", R.drawable.juice_image));
        drinks.add(new Drink("Lemonade", R.drawable.lemonade_image));
        drinks.add(new Drink("Water", R.drawable.water_image));
        return drinks;
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.drinkRecyclerView);
        List<Drink> drinks = createDrinkList();
        DrinkAdapter adapter = new DrinkAdapter(drinks);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
    }



    private void setupListeners() {
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSize = Size.values()[position];
                updateDrinkAndPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        flavorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFlavor = Flavor.values()[position];
                updateDrinkAndPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        quantityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateQuantity();
                updateDrinkAndPrice();
            }
        });

        addToOrderButton.setOnClickListener(v -> addToOrder());
        viewOrderButton.setOnClickListener(v -> navigateToOrderView());
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

    private void updateDrinkAndPrice() {
        beverageItem = new Beverage(selectedSize, selectedFlavor, quantity);
        updatePriceDisplay();
    }

    private void updatePriceDisplay() {
        double price = beverageItem.price();
        priceTextView.setText(String.format("$%.2f", price));
    }

    private void addToOrder() {
        updateQuantity();
        updateDrinkAndPrice();
        dataManager.addOrderItem(beverageItem);
        Toast.makeText(getActivity(), "Drink added to order", Toast.LENGTH_SHORT).show();
        resetForm();
    }

    private void resetForm() {
        sizeSpinner.setSelection(1); // Medium
        flavorSpinner.setSelection(0); // Cola
        quantityEditText.setText("1");
        updateDrinkAndPrice();
    }

    private void navigateToOrderView() {
        OrderDialog orderDialog = new OrderDialog(getActivity());
        orderDialog.show();
    }
}
