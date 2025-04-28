package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.*;
import javafx.stage.Stage;
import model.MenuItem;

/**
 * Controller class for the Burgers view.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Burgers {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Bread> burgerBreadOptions;

    @FXML
    private Button burgerOrder;

    @FXML
    private ComboBox<String> burgerPattyOptions;

    @FXML
    private TextField burgerQuantity;

    @FXML
    private CheckBox isBurgerAvacado;

    @FXML
    private CheckBox isBurgerCheese;

    @FXML
    private ToggleButton isBurgerCombo;

    @FXML
    private CheckBox isBurgerLettuce;

    @FXML
    private CheckBox isBurgerOnions;

    @FXML
    private CheckBox isBurgerTomatoe;

    @FXML
    private Label burgerPriceLabel;

    @FXML
    void burgerComboSelection(MouseEvent event) {
        try {
            // Create a burger from current selections
            Burger burger = createBurgerFromSelections();

            // Get the stage
            Stage stage = (Stage) burgerQuantity.getScene().getWindow();

            // Show combo selection popup with the burger toggle button
            new ComboSelectionPopup(burger, stage, isBurgerCombo, burgerPriceLabel);

            isBurgerCombo.setSelected(false);
            resetForm();

        } catch (IllegalArgumentException e) {
            // Show error for invalid selections
            showAlert("Selection Error", e.getMessage());
            isBurgerCombo.setSelected(false);
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
    void burgerOrder(MouseEvent event) {
        try {
            // Create burger from current selections
            Burger burger = createBurgerFromSelections();

            // Add to order
            OrderManager.getInstance().getCurrentOrder().addItem(burger);

            // Try to update the current order display if visible
            try {
                CurrentOrderManager.getInstance().updateCurrentOrderDisplay();
            } catch (Exception e) {
                // Display error in pop-up
                Alert updateError = new Alert(Alert.AlertType.WARNING);
                updateError.setTitle("Order Display Update");
                updateError.setHeaderText("The order was added but the display couldn't be updated");
                updateError.setContentText("Error details: " + e.getMessage());
                updateError.showAndWait();
            }

            // Show confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item Added");
            alert.setHeaderText(null);
            alert.setContentText("Burger added to order!\n\n" + burger.toString());
            alert.showAndWait();

            // Reset form
            resetForm();

        } catch (IllegalArgumentException e) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Order Error");
            alert.setHeaderText("Cannot add burger to order");
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

    private Burger createBurgerFromSelections() {
        // Validate selections
        if (burgerBreadOptions.getValue() == null) {
            throw new IllegalArgumentException("Please select a bread type");
        }

        if (burgerPattyOptions.getValue() == null) {
            throw new IllegalArgumentException("Please select a patty type");
        }

        // Get quantity
        int quantity;
        try {
            quantity = Integer.parseInt(burgerQuantity.getText());
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
        Bread bread = burgerBreadOptions.getValue();

        // Get patty option
        boolean doublePatty = burgerPattyOptions.getValue().equals("Double Patty");

        // Get add-ons
        ArrayList<AddOns> addOns = new ArrayList<>();
        if (isBurgerLettuce.isSelected()) {
            addOns.add(AddOns.LETTUCE);
        }
        if (isBurgerTomatoe.isSelected()) {
            addOns.add(AddOns.TOMATOES);
        }
        if (isBurgerOnions.isSelected()) {
            addOns.add(AddOns.ONIONS);
        }
        if (isBurgerAvacado.isSelected()) {
            addOns.add(AddOns.AVOCADOS);
        }
        if (isBurgerCheese.isSelected()) {
            addOns.add(AddOns.CHEESE);
        }

        // Create burger
        Burger burger = new Burger(bread, addOns, quantity, doublePatty);

        // Handle combo (This will be enhanced later)
        if (isBurgerCombo.isSelected()) {
            // For now, just note that it's a combo - will be replaced with actual combo logic
            //burger.setCombo(true);
        }

        return burger;
    }

    private void resetForm() {
        burgerBreadOptions.setValue(null);
        burgerPattyOptions.setValue(null);
        burgerQuantity.setText("1");
        isBurgerLettuce.setSelected(false);
        isBurgerTomatoe.setSelected(false);
        isBurgerOnions.setSelected(false);
        isBurgerAvacado.setSelected(false);
        isBurgerCheese.setSelected(false);
        isBurgerCombo.setSelected(false);
        updatePrice();
    }

    private void updatePrice() {
        try {
            // Create a temporary burger to calculate price
            Burger burger = createBurgerFromSelections();

            // Calculate price
            double price = burger.price();

            // Add combo price if selected
            if (isBurgerCombo.isSelected()) {
                price += 2.00 * burger.getQuantity(); // $2 extra per burger for combo
            }

            // Update price label
            burgerPriceLabel.setText(String.format("Price: $%.2f", price));
        } catch (Exception e) {
            // If any error occurs (like incomplete selections), show $0.00
            burgerPriceLabel.setText("Price: $0.00");
        }
    }

    @FXML
    void initialize() {
        try {
            // Validate FXML injection
            if (burgerBreadOptions == null) {
                showErrorAlert("Missing UI Component", "burgerBreadOptions ComboBox is missing");
                return;
            }
            if (burgerPattyOptions == null) {
                showErrorAlert("Missing UI Component", "burgerPattyOptions ComboBox is missing");
                return;
            }
            if (burgerQuantity == null) {
                showErrorAlert("Missing UI Component", "burgerQuantity TextField is missing");
                return;
            }
            if (burgerPriceLabel == null) {
                showErrorAlert("Missing UI Component", "burgerPriceLabel Label is missing");
                return;
            }

            // Set up bread options
            burgerBreadOptions.getItems().addAll(Bread.BRIOCHE, Bread.WHEAT, Bread.PRETZEL);

            // Set up patty options
            burgerPattyOptions.getItems().addAll("Single Patty", "Double Patty");

            // Set default quantity
            burgerQuantity.setText("1");

            // Set up event listeners for live price updates
            burgerBreadOptions.setOnAction(e -> updatePrice());
            burgerPattyOptions.setOnAction(e -> updatePrice());
            burgerQuantity.textProperty().addListener((obs, oldVal, newVal) -> updatePrice());

            // Setup checkboxes if they exist
            if (isBurgerLettuce != null) {
                isBurgerLettuce.setOnAction(e -> updatePrice());
            }
            if (isBurgerTomatoe != null) {
                isBurgerTomatoe.setOnAction(e -> updatePrice());
            }
            if (isBurgerOnions != null) {
                isBurgerOnions.setOnAction(e -> updatePrice());
            }
            if (isBurgerAvacado != null) {
                isBurgerAvacado.setOnAction(e -> updatePrice());
            }
            if (isBurgerCheese != null) {
                isBurgerCheese.setOnAction(e -> updatePrice());
            }
            if (isBurgerCombo != null) {
                isBurgerCombo.setOnAction(e -> updatePrice());
            }

            // Set initial price
            burgerPriceLabel.setText("Price: $0.00");

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Initialization Error", "Failed to initialize the burger ordering interface: " + e.getMessage());
        }
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("UI Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}