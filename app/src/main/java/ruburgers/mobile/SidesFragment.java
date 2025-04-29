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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import model.Side;
import model.SideOrder;
import model.Size;

/**
 * Fragment class for the sides menu
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class SidesFragment extends Fragment {

    private ImageButton chipsButton;
    private ImageButton friesButton;
    private ImageButton onionRingsButton;
    private ImageButton appleSlicesButton;
    private Button viewOrderButton;

    private DataManager dataManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sides, container, false);

        dataManager = DataManager.getInstance();

        initializeUIComponents(view);
        setupListeners();

        return view;
    }

    private void initializeUIComponents(View view) {
        chipsButton = view.findViewById(R.id.chipsButton);
        friesButton = view.findViewById(R.id.friesButton);
        onionRingsButton = view.findViewById(R.id.onionRingsButton);
        appleSlicesButton = view.findViewById(R.id.appleSlicesButton);
        viewOrderButton = view.findViewById(R.id.viewOrderButton);
    }

    private void setupListeners() {
        chipsButton.setOnClickListener(v -> showSideOptionsDialog(Side.CHIPS));
        friesButton.setOnClickListener(v -> showSideOptionsDialog(Side.FRIES));
        onionRingsButton.setOnClickListener(v -> showSideOptionsDialog(Side.ONION_RINGS));
        appleSlicesButton.setOnClickListener(v -> showSideOptionsDialog(Side.APPLE_SLICES));
        viewOrderButton.setOnClickListener(v -> showOrderDialog());
    }

    private void showOrderDialog() {
        OrderDialog orderDialog = new OrderDialog(getActivity());
        orderDialog.show();
    }

    private void showSideOptionsDialog(Side sideType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.sides_popup, null);
        builder.setView(dialogView);

        // Set the title
        TextView titleTextView = dialogView.findViewById(R.id.sideOptionsTitleTextView);
        titleTextView.setText(sideType.name().replace("_", " "));

        // Get UI components
        RadioGroup sizeRadioGroup = dialogView.findViewById(R.id.sizeRadioGroup);
        RadioButton smallRadioButton = dialogView.findViewById(R.id.smallRadioButton);
        RadioButton mediumRadioButton = dialogView.findViewById(R.id.mediumRadioButton);
        RadioButton largeRadioButton = dialogView.findViewById(R.id.largeRadioButton);

        EditText quantityEditText = dialogView.findViewById(R.id.quantityEditText);

        TextView priceTextView = dialogView.findViewById(R.id.priceTextView);
        Button addToOrderButton = dialogView.findViewById(R.id.addToOrderButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        // Set initial values
        quantityEditText.setText("1");
        smallRadioButton.setChecked(true);
        updatePriceDisplay(priceTextView, sideType, Size.SMALL, 1);

        // Set up listeners
        sizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Size newSize;
            if (checkedId == R.id.mediumRadioButton) {
                newSize = Size.MEDIUM;
            } else if (checkedId == R.id.largeRadioButton) {
                newSize = Size.LARGE;
            } else {
                newSize = Size.SMALL;
            }

            int quantity = getQuantityFromEditText(quantityEditText);
            updatePriceDisplay(priceTextView, sideType, newSize, quantity);
        });

        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int quantity = getQuantityFromEditText(quantityEditText);

                Size currentSize;
                if (mediumRadioButton.isChecked()) {
                    currentSize = Size.MEDIUM;
                } else if (largeRadioButton.isChecked()) {
                    currentSize = Size.LARGE;
                } else {
                    currentSize = Size.SMALL;
                }

                updatePriceDisplay(priceTextView, sideType, currentSize, quantity);
            }
        });

        AlertDialog dialog = builder.create();

        addToOrderButton.setOnClickListener(v -> {
            Size finalSize;
            if (mediumRadioButton.isChecked()) {
                finalSize = Size.MEDIUM;
            } else if (largeRadioButton.isChecked()) {
                finalSize = Size.LARGE;
            } else {
                finalSize = Size.SMALL;
            }

            int quantity = getQuantityFromEditText(quantityEditText);

            if (quantity <= 0) {
                Toast.makeText(getActivity(), "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                SideOrder sideOrder = new SideOrder(sideType, finalSize, quantity);

                sideOrder.setQuantity(quantity);

                dataManager.addOrderItem(sideOrder);

                String sizeStr = finalSize.toString().toLowerCase();
                String sideStr = sideType.name().replace("_", " ").toLowerCase();
                Toast.makeText(getActivity(),
                        "Added " + quantity + "x " + sizeStr + " " + sideStr + " to your order",
                        Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getActivity(),
                        "Error adding to order: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Show dialog
        dialog.show();
    }

    private int getQuantityFromEditText(EditText quantityEditText) {
        try {
            String quantityStr = quantityEditText.getText().toString();
            if (quantityStr.isEmpty()) {
                return 1; // Default to 1 if empty
            }
            int quantity = Integer.parseInt(quantityStr);
            return quantity > 0 ? quantity : 1; // Ensure minimum of 1
        } catch (NumberFormatException e) {
            return 1; // Default to 1 if invalid
        }
    }

    private void updatePriceDisplay(TextView priceTextView, Side sideType, Size size, int quantity) {
        if (quantity <= 0) quantity = 1; // Ensure minimum quantity of 1
        double unitPrice = sideType.getPriceBySize(size);
        double totalPrice = unitPrice * quantity;
        priceTextView.setText(String.format("Price: $%.2f", totalPrice));
    }
}