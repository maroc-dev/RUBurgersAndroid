package ruburgers.mobile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import model.Beverage;
import model.Flavor;
import model.Size;

public class BeveragesFragment extends Fragment {

    private GridLayout beveragesGrid;
    private TextView currentBeveragePrice;
    private Button viewOrderButton;

    private DataManager dataManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beverages, container, false);

        dataManager = DataManager.getInstance();

        initializeUIComponents(view);
        setupBeverageButtons();

        return view;
    }

    private void initializeUIComponents(View view) {
        beveragesGrid = view.findViewById(R.id.beveragesGrid);
        currentBeveragePrice = view.findViewById(R.id.currentBeveragePrice);
        viewOrderButton = view.findViewById(R.id.viewOrderButton);

        currentBeveragePrice.setText("Price: $0.00");

        viewOrderButton.setOnClickListener(v -> {
            OrderDialog orderDialog = new OrderDialog(getActivity());
            orderDialog.show();
        });
    }

    private void setupBeverageButtons() {
        Flavor[] flavors = {
                Flavor.COLA, Flavor.LEMONADE,
                Flavor.ROOT_BEER, Flavor.TEA,
                Flavor.SPRITE, Flavor.CHERRY,
                Flavor.JUICE, Flavor.BLUE_RASPBERRY,
                Flavor.MANGO, Flavor.STRAWBERRY,
                Flavor.PEACH, Flavor.WATERMELON,
                Flavor.LIME, Flavor.VANILLA,
                Flavor.PINEAPPLE
        };

        int[] imageResources = {
                R.mipmap.cola, R.mipmap.lemonade,
                R.mipmap.root_beer, R.mipmap.tea,
                R.mipmap.sprite, R.mipmap.cherry,
                R.mipmap.juice, R.mipmap.blue_raspberry,
                R.mipmap.mango, R.mipmap.strawberry,
                R.mipmap.peach, R.mipmap.watermelon,
                R.mipmap.lime, R.mipmap.vanilla,
                R.mipmap.pineapple
        };

        beveragesGrid.setColumnCount(2);

        for (int i = 0; i < flavors.length; i++) {
            ImageButton button = new ImageButton(getActivity());
            button.setImageResource(imageResources[i]);
            button.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            button.setBackgroundResource(android.R.color.transparent);
            button.setAdjustViewBounds(true);
            button.setPadding(10, 10, 10, 10);

            button.setTag(flavors[i]);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(i % 2, 1, 1f); // Column and weight
            params.rowSpec = GridLayout.spec(i / 2, 1); // Row
            params.setMargins(8, 8, 8, 8);

            button.setLayoutParams(params);

            final Flavor flavor = flavors[i];
            button.setOnClickListener(v -> showBeverageOptionsDialog(flavor));

            beveragesGrid.addView(button);

            TextView label = new TextView(getActivity());
            label.setText(flavors[i].toString().replace("_", " "));
            label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            GridLayout.LayoutParams labelParams = new GridLayout.LayoutParams();
            labelParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
            labelParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            labelParams.columnSpec = GridLayout.spec(i % 2, 1, 1f);
            labelParams.rowSpec = GridLayout.spec(i / 2, 1);
            labelParams.setMargins(8, button.getHeight() + 4, 8, 8);

        }
    }

    private void showBeverageOptionsDialog(Flavor flavor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.beverage_options_dialogue, null);
        builder.setView(dialogView);

        // Get references to UI elements
        TextView flavorTitleTextView = dialogView.findViewById(R.id.flavorTitleTextView);
        flavorTitleTextView.setText(flavor.toString().replace("_", " "));

        RadioGroup sizeRadioGroup = dialogView.findViewById(R.id.sizeRadioGroup);
        RadioButton smallRadioButton = dialogView.findViewById(R.id.smallRadioButton);
        RadioButton mediumRadioButton = dialogView.findViewById(R.id.mediumRadioButton);
        RadioButton largeRadioButton = dialogView.findViewById(R.id.largeRadioButton);

        EditText quantityEditText = dialogView.findViewById(R.id.quantityEditText);
        quantityEditText.setText("1");

        TextView priceTextView = dialogView.findViewById(R.id.priceTextView);
        Button addToOrderButton = dialogView.findViewById(R.id.addToOrderButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        updatePrice(priceTextView, flavor, Size.SMALL, 1);

        sizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Size selectedSize;
            if (checkedId == R.id.mediumRadioButton) {
                selectedSize = Size.MEDIUM;
            } else if (checkedId == R.id.largeRadioButton) {
                selectedSize = Size.LARGE;
            } else {
                selectedSize = Size.SMALL;
            }

            int quantity = getQuantityFromEditText(quantityEditText);
            updatePrice(priceTextView, flavor, selectedSize, quantity);
        });

        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int quantity = getQuantityFromEditText(quantityEditText);

                Size selectedSize;
                if (mediumRadioButton.isChecked()) {
                    selectedSize = Size.MEDIUM;
                } else if (largeRadioButton.isChecked()) {
                    selectedSize = Size.LARGE;
                } else {
                    selectedSize = Size.SMALL;
                }

                updatePrice(priceTextView, flavor, selectedSize, quantity);
            }
        });

        AlertDialog dialog = builder.create();

        addToOrderButton.setOnClickListener(v -> {
            Size selectedSize;
            if (mediumRadioButton.isChecked()) {
                selectedSize = Size.MEDIUM;
            } else if (largeRadioButton.isChecked()) {
                selectedSize = Size.LARGE;
            } else {
                selectedSize = Size.SMALL;
            }

            int quantity = getQuantityFromEditText(quantityEditText);

            if (quantity <= 0) {
                Toast.makeText(getActivity(), "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            Beverage beverage = new Beverage(selectedSize, flavor, quantity);
            dataManager.addOrderItem(beverage);

            String sizeStr = selectedSize.toString().toLowerCase();
            String flavorStr = flavor.toString().replace("_", " ").toLowerCase();
            Toast.makeText(getActivity(),
                    "Added " + quantity + "x " + sizeStr + " " + flavorStr + " to your order",
                    Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Show dialog
        dialog.show();
    }

    private int getQuantityFromEditText(EditText quantityEditText) {
        try {
            String quantityStr = quantityEditText.getText().toString();
            if (quantityStr.isEmpty()) {
                return 1;
            }
            int quantity = Integer.parseInt(quantityStr);
            return quantity > 0 ? quantity : 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private void updatePrice(TextView priceTextView, Flavor flavor, Size size, int quantity) {
        if (quantity <= 0) quantity = 1; // Ensure minimum quantity of 1

        // Create a temporary beverage to calculate price
        Beverage tempBeverage = new Beverage(size, flavor, quantity);
        double totalPrice = tempBeverage.price();

        priceTextView.setText(String.format("Price: $%.2f", totalPrice));
    }
}