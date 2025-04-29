package ruburgers.mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import model.Beverage;
import model.Burger;
import model.Combo;
import model.Flavor;
import model.MenuItem;
import model.Sandwich;
import model.Side;
import model.Size;

public class ComboSelectionDialog {
    private Context context;
    private DataManager dataManager;
    private AlertDialog dialog;
    private Burger burgerItem;
    private ToggleButton comboToggle;
    private TextView priceTextView;

    private Side selectedSide;
    private Beverage selectedDrink;

    public ComboSelectionDialog(Context context, Burger burgerItem, ToggleButton comboToggle, TextView priceTextView) {
        this.context = context;
        this.dataManager = DataManager.getInstance();
        this.burgerItem = burgerItem;
        this.comboToggle = comboToggle;
        this.priceTextView = priceTextView;
    }


    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.combo_popup, null);
        builder.setView(dialogView);

        // Get references to UI elements
        TextView descriptionText = dialogView.findViewById(R.id.comboDescriptionText);
        descriptionText.setText("Combo includes a small side (chips or apple) and a medium drink (cola, tea, or juice)");

        // Side selection
        RadioGroup sideGroup = dialogView.findViewById(R.id.sideRadioGroup);
        RadioButton chipsRadioButton = dialogView.findViewById(R.id.chipsRadioButton);
        RadioButton appleRadioButton = dialogView.findViewById(R.id.appleRadioButton);

        // Drink selection
        RadioGroup drinkGroup = dialogView.findViewById(R.id.drinkRadioGroup);
        RadioButton colaRadioButton = dialogView.findViewById(R.id.colaRadioButton);
        RadioButton teaRadioButton = dialogView.findViewById(R.id.teaRadioButton);
        RadioButton juiceRadioButton = dialogView.findViewById(R.id.juiceRadioButton);

        // Buttons
        Button addButton = dialogView.findViewById(R.id.addComboButton);
        addButton.setEnabled(false);

        Button cancelButton = dialogView.findViewById(R.id.cancelComboButton);

        sideGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipsRadioButton) {
                selectedSide = Side.CHIPS;
            } else if (checkedId == R.id.appleRadioButton) {
                selectedSide = Side.APPLE_SLICES;
            }

            enableAddButtonIfReady(addButton, sideGroup, drinkGroup);
        });

        drinkGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.colaRadioButton) {
                selectedDrink = new Beverage(Size.MEDIUM, Flavor.COLA, 1);
            } else if (checkedId == R.id.teaRadioButton) {
                selectedDrink = new Beverage(Size.MEDIUM, Flavor.TEA, 1);
            } else if (checkedId == R.id.juiceRadioButton) {
                selectedDrink = new Beverage(Size.MEDIUM, Flavor.JUICE, 1);
            }

            // Enable add button if both side and drink are selected
            enableAddButtonIfReady(addButton, sideGroup, drinkGroup);
        });

        addButton.setOnClickListener(v -> {
            if (selectedSide != null && selectedDrink != null) {
                try {
                    int quantity = burgerItem.getQuantity();

                    selectedDrink.setQuantity(quantity);

                    Combo combo = new Combo(burgerItem, selectedDrink, selectedSide, quantity);

                    dataManager.addOrderItem(combo);

                    Toast.makeText(context, quantity + "x Burger combo added to order",
                            Toast.LENGTH_SHORT).show();

                    dialog.dismiss();

                    if (priceTextView != null) {
                        priceTextView.setText("$0.00");
                    }

                } catch (IllegalArgumentException e) {
                    Toast.makeText(context, "Error: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    if (comboToggle != null) {
                        comboToggle.setChecked(false);
                    }
                    dialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(v -> {
            // Reset toggle button if canceled
            if (comboToggle != null) {
                comboToggle.setChecked(false);
            }
            dialog.dismiss();
        });

        // Create and show dialog
        dialog = builder.create();
        dialog.setCancelable(false); // Prevent dismiss on outside click
        dialog.show();
    }

    private void enableAddButtonIfReady(Button addButton, RadioGroup sideGroup, RadioGroup drinkGroup) {
        // Enable add button only when both selections are made
        boolean sideSelected = sideGroup.getCheckedRadioButtonId() != -1;
        boolean drinkSelected = drinkGroup.getCheckedRadioButtonId() != -1;
        addButton.setEnabled(sideSelected && drinkSelected);
    }
}