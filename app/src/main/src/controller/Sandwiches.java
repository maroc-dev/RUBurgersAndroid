package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.AddOns;
import model.Bread;
import model.OrderManager;
import model.Protein;
import model.Sandwich;

/**
 * Controller class for the Sandwiches view.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Sandwiches {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Protein> sandwichProteinOptions;

    @FXML
    private CheckBox isSandwichAvacado;

    @FXML
    private CheckBox isSandwichCheese;

    @FXML
    private ToggleButton isSandwichCombo;

    @FXML
    private CheckBox isSandwichLettuce;

    @FXML
    private CheckBox isSandwichOnions;

    @FXML
    private CheckBox isSandwichTomatoe;

    @FXML
    private ComboBox<Bread> sandwichBreadOptions;

    @FXML
    private Button sandwichOrder;

    @FXML
    private Label sandwichPriceLabel;

    @FXML
    private TextField sandwichQuantity;

    @FXML
    void sandwichComboSelection(MouseEvent event) {
        try {
            Sandwich sandwich = createSandwichFromSelections();

            Stage stage = (Stage) sandwichQuantity.getScene().getWindow();

            new ComboSelectionPopup(sandwich, stage, isSandwichCombo, sandwichPriceLabel);

            isSandwichCombo.setSelected(false);
            resetForm();

        } catch (IllegalArgumentException e) {
            // Show error for invalid selections
            showAlert("Selection Error", e.getMessage());
            isSandwichCombo.setSelected(false);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void sandwichOrder(MouseEvent event) {
        try {
            // Create sandwich from current selections
            Sandwich sandwich = createSandwichFromSelections();

            // Add to order
            OrderManager.getInstance().getCurrentOrder().addItem(sandwich);

            // Try to update the current order display if visible
            try {
                CurrentOrderManager.getInstance().updateCurrentOrderDisplay();
            } catch (Exception e) {
                // Display error in pop-up
                Alert updateError = new Alert(Alert.AlertType.WARNING);
                updateError.setTitle("Order Display Update");
                updateError.setHeaderText("The sandwich was added but the display couldn't be updated");
                updateError.setContentText("Error details: " + e.getMessage());
                updateError.showAndWait();
            }

            // Show confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item Added");
            alert.setHeaderText(null);
            alert.setContentText("Sandwich added to order!\n\n" + sandwich.toString());
            alert.showAndWait();

            // Reset form
            resetForm();

        } catch (IllegalArgumentException e) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Order Error");
            alert.setHeaderText("Cannot add sandwich to order");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            // Catch any other unexpected errors
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unexpected Error");
            alert.setHeaderText("An unexpected error occurred");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private Sandwich createSandwichFromSelections() {
        // Validate selections
        if (sandwichBreadOptions.getValue() == null) {
            throw new IllegalArgumentException("Please select a bread type");
        }

        if (sandwichProteinOptions.getValue() == null) {
            throw new IllegalArgumentException("Please select a protein type");
        }

        // Get quantity
        int quantity;
        try {
            quantity = Integer.parseInt(sandwichQuantity.getText());
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be at least 1");
            }
            if (quantity > 99) {
                throw new IllegalArgumentException("Quantity cannot exceed 99");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid quantity number");
        }

        // Get bread
        Bread bread = sandwichBreadOptions.getValue();

        // Get protein
        Protein protein = sandwichProteinOptions.getValue();

        // Get add-ons
        ArrayList<AddOns> addOns = new ArrayList<>();
        if (isSandwichLettuce.isSelected()) {
            addOns.add(AddOns.LETTUCE);
        }
        if (isSandwichTomatoe.isSelected()) {
            addOns.add(AddOns.TOMATOES);
        }
        if (isSandwichOnions.isSelected()) {
            addOns.add(AddOns.ONIONS);
        }
        if (isSandwichAvacado.isSelected()) {
            addOns.add(AddOns.AVOCADOS);
        }
        if (isSandwichCheese.isSelected()) {
            addOns.add(AddOns.CHEESE);
        }

        // Create sandwich
        Sandwich sandwich = new Sandwich(bread, protein, addOns, quantity);

        return sandwich;
    }

    private void resetForm() {
        sandwichBreadOptions.setValue(null);
        sandwichProteinOptions.setValue(null);
        sandwichQuantity.setText("1");
        isSandwichLettuce.setSelected(false);
        isSandwichTomatoe.setSelected(false);
        isSandwichOnions.setSelected(false);
        isSandwichAvacado.setSelected(false);
        isSandwichCheese.setSelected(false);
        isSandwichCombo.setSelected(false);
        updatePrice();
    }

    private void updatePrice() {
        try {
            // Create a temporary sandwich to calculate price
            Sandwich sandwich = createSandwichFromSelections();

            // Calculate price
            double price = sandwich.price();

            // Add combo price if selected
            if (isSandwichCombo.isSelected()) {
                price += 2.00 * sandwich.getQuantity(); // $2 extra per sandwich for combo
            }

            // Update price label
            sandwichPriceLabel.setText(String.format("Price: $%.2f", price));
        } catch (Exception e) {
            // If any error occurs (like incomplete selections), show $0.00
            sandwichPriceLabel.setText("Price: $0.00");
        }
    }

    @FXML
    void initialize() {
        try {
            // Fix the ComboBox reference - adjust the fx:id in FXML or fix the field name here
            assert sandwichProteinOptions != null : "fx:id=\"sandwichProteinOptions\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert isSandwichAvacado != null : "fx:id=\"isSandwichAvacado\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert isSandwichCheese != null : "fx:id=\"isSandwichCheese\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert isSandwichCombo != null : "fx:id=\"isSandwichCombo\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert isSandwichLettuce != null : "fx:id=\"isSandwichLettuce\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert isSandwichOnions != null : "fx:id=\"isSandwichOnions\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert isSandwichTomatoe != null : "fx:id=\"isSandwichTomatoe\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert sandwichBreadOptions != null : "fx:id=\"sandwichBreadOptions\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert sandwichOrder != null : "fx:id=\"sandwichOrder\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert sandwichPriceLabel != null : "fx:id=\"sandwichPriceLabel\" was not injected: check your FXML file 'sandwiches.fxml'.";
            assert sandwichQuantity != null : "fx:id=\"sandwichQuantity\" was not injected: check your FXML file 'sandwiches.fxml'.";

            sandwichBreadOptions.getItems().addAll(Bread.BRIOCHE, Bread.WHEAT, Bread.PRETZEL,
                    Bread.BAGEL, Bread.SOURDOUGH);

            // Set up protein options
            sandwichProteinOptions.getItems().addAll(Protein.ROAST_BEEF, Protein.SALMON, Protein.CHICKEN);

            // Set default quantity
            sandwichQuantity.setText("1");

            // Set up event listeners for live price updates
            sandwichBreadOptions.setOnAction(e -> updatePrice());
            sandwichProteinOptions.setOnAction(e -> updatePrice());
            sandwichQuantity.textProperty().addListener((obs, oldVal, newVal) -> updatePrice());

            isSandwichLettuce.setOnAction(e -> updatePrice());
            isSandwichTomatoe.setOnAction(e -> updatePrice());
            isSandwichOnions.setOnAction(e -> updatePrice());
            isSandwichAvacado.setOnAction(e -> updatePrice());
            isSandwichCheese.setOnAction(e -> updatePrice());
            isSandwichCombo.setOnAction(e -> updatePrice());

            // Set initial price
            sandwichPriceLabel.setText("Price: $0.00");

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Initialization Error");
            alert.setHeaderText("Failed to initialize the sandwich ordering interface");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}