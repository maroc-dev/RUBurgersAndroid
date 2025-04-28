package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.*;

/**
 * Controller class for the Beverages view.
 * This class handles the beverage selection and size selection process.
 * It updates the display of the current price and manages the beverage order.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Beverages {

    /**
     * The GridPane that holds the beverage selection buttons.
     */
    @FXML
    private GridPane beverageGrid;

    /**
     * The button for selecting the Blue Raspberry flavor.
     */
    @FXML
    private Button blueRaspberryButton;

    /**
     * The button for selecting the Cherry flavor.
     */
    @FXML
    private Button cherryButton;

    /**
     * The button for selecting the Cola flavor.
     */
    @FXML
    private Button colaButton;

    /**
     * The label that displays the current price of the selected beverage.
     */
    @FXML
    private Label currentbBeveragePrice;

    /**
     * The button for selecting the Juice flavor.
     */
    @FXML
    private Button juiceButton;

    /**
     * The button for selecting the Lemonade flavor.
     */
    @FXML
    private Button lemonadeButton;

    /**
     * The button for selecting the Lime flavor.
     */
    @FXML
    private Button limeButton;

    /**
     * The button for selecting the Mango flavor.
     */
    @FXML
    private Button mangoButton;

    /**
     * The button for selecting the Peach flavor.
     */
    @FXML
    private Button peachButton;

    /**
     * The button for selecting the Pineapple flavor.
     */
    @FXML
    private Button pineappleButton;

    /**
     * The button for selecting the Root Beer flavor.
     */
    @FXML
    private Button rootBeerButton;

    /**
     * The button for selecting the Sprite flavor.
     */
    @FXML
    private Button spriteButton;

    /**
     * The button for selecting the Strawberry flavor.
     */
    @FXML
    private Button strawberryButton;

    /**
     * The button for selecting the Tea flavor.
     */
    @FXML
    private Button teaButton;

    /**
     * The button for selecting the Vanilla flavor.
     */
    @FXML
    private Button vanillaButton;

    /**
     * The button for selecting the Watermelon flavor.
     */
    @FXML
    private Button watermelonButton;

    /**
     * The selected flavor for the current beverage.
     */
    private Flavor selectedFlavor;

    /**
     * The selected size for the current beverage.
     */
    private Size selectedSize;

    /**
     * The quantity of the selected beverage.
     */
    private int quantity = 1;

    /**
     * The popup that allows the user to select the size and quantity for the beverage.
     */
    private Popup sizeSelectionPopup;

    /**
     * A map that associates buttons with their corresponding flavors.
     */
    private Map<Button, Flavor> buttonFlavorMap = new HashMap<>();

    /**
     * Initializes the beverage selection screen, setting up the buttons and their associated flavors.
     * It also sets up event listeners for each button to show the size selection popup.
     */
    @FXML
    void initialize() {
        // Initialize the button-flavor map
        buttonFlavorMap.put(colaButton, Flavor.COLA);
        buttonFlavorMap.put(lemonadeButton, Flavor.LEMONADE);
        buttonFlavorMap.put(rootBeerButton, Flavor.ROOT_BEER);
        buttonFlavorMap.put(teaButton, Flavor.TEA);
        buttonFlavorMap.put(spriteButton, Flavor.SPRITE);
        buttonFlavorMap.put(cherryButton, Flavor.CHERRY);
        buttonFlavorMap.put(juiceButton, Flavor.JUICE);
        buttonFlavorMap.put(blueRaspberryButton, Flavor.BLUE_RASPBERRY);
        buttonFlavorMap.put(mangoButton, Flavor.MANGO);
        buttonFlavorMap.put(strawberryButton, Flavor.STRAWBERRY);
        buttonFlavorMap.put(peachButton, Flavor.PEACH);
        buttonFlavorMap.put(watermelonButton, Flavor.WATERMELON);
        buttonFlavorMap.put(limeButton, Flavor.LIME);
        buttonFlavorMap.put(vanillaButton, Flavor.VANILLA);
        buttonFlavorMap.put(pineappleButton, Flavor.PINEAPPLE);

        // Set button images and event handlers
        for (Map.Entry<Button, Flavor> entry : buttonFlavorMap.entrySet()) {
            Button button = entry.getKey();
            Flavor flavor = entry.getValue();

            // Set the image on the button
            setButtonImage(button, flavor);

            // Set the click event
            button.setOnAction(e -> showSizeSelection(flavor));
        }

        // Initialize price display
        currentbBeveragePrice.setText("Price: $0.00");
    }

    /**
     * Sets the image for the given button based on the flavor.
     *
     * @param button The button to set the image for.
     * @param flavor The flavor associated with the button.
     */
    private void setButtonImage(Button button, Flavor flavor) {
        try {
            // Convert flavor name to lowercase and replace spaces with underscores
            String flavorName = flavor.toString().toLowerCase().replace(" ", "_");

            String imagePath = "/resources/images/" + flavorName + ".png";
            URL imageUrl = getClass().getResource(imagePath);

            if (imageUrl != null) {
                Image image = new Image(imageUrl.toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);
                imageView.setPreserveRatio(true);
                button.setGraphic(imageView);

                // Add tooltip with flavor name
                button.setTooltip(new Tooltip(flavor.toString()));
            }
        } catch (Exception e) {
            button.setText(flavor.toString());
        }
    }

    /**
     * Displays a popup that allows the user to select the size and quantity of the selected beverage.
     *
     * @param flavor The selected flavor for the beverage.
     */
    private void showSizeSelection(Flavor flavor) {
        selectedFlavor = flavor;

        // Hide any existing popups
        if (sizeSelectionPopup != null && sizeSelectionPopup.isShowing()) {
            sizeSelectionPopup.hide();
        }

        // Create popup content
        VBox popupContent = new VBox(10);
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 10;");
        popupContent.setPrefWidth(300);
        popupContent.setAlignment(Pos.CENTER);

        // Title
        Label titleLabel = new Label("Select Options for " + flavor.toString());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        // Size selection
        Label sizeLabel = new Label("Size:");
        ToggleGroup sizeGroup = new ToggleGroup();
        RadioButton smallButton = new RadioButton("Small - $1.99");
        RadioButton mediumButton = new RadioButton("Medium - $2.49");
        RadioButton largeButton = new RadioButton("Large - $2.99");

        smallButton.setToggleGroup(sizeGroup);
        mediumButton.setToggleGroup(sizeGroup);
        largeButton.setToggleGroup(sizeGroup);

        HBox sizeBox = new HBox(10);
        sizeBox.setAlignment(Pos.CENTER);
        sizeBox.getChildren().addAll(smallButton, mediumButton, largeButton);

        // Quantity selection
        Label quantityLabel = new Label("Quantity:");
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 99, 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(80);

        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER);
        quantityBox.getChildren().addAll(quantityLabel, quantitySpinner);

        // Price display
        Label priceLabel = new Label("Select a size to see price");
        priceLabel.setStyle("-fx-font-weight: bold;");

        Button addButton = new Button("Add to Order");
        addButton.setDisable(true); // Initially disabled until size is selected

        // Cancel button
        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, cancelButton);

        // Event handlers for size selection and quantity change
        sizeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == smallButton) {
                selectedSize = Size.SMALL;
            } else if (newVal == mediumButton) {
                selectedSize = Size.MEDIUM;
            } else if (newVal == largeButton) {
                selectedSize = Size.LARGE;
            }

            updatePopupPrice(priceLabel, quantitySpinner.getValue());
            addButton.setDisable(false);
        });

        // Event handler for quantity change
        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            quantity = newVal;
            updatePopupPrice(priceLabel, newVal);
        });

        // Event handler for adding to order
        addButton.setOnAction(e -> {
            try {
                Beverage beverage = new Beverage(selectedSize, selectedFlavor, quantity);
                OrderManager.getInstance().getCurrentOrder().addItem(beverage);
                try {
                    CurrentOrderManager.getInstance().updateCurrentOrderDisplay();
                } catch (Exception ex) {
                    showAlert("Display Update Error",
                            "Beverage added to order but display couldn't update: " + ex.getMessage());
                }
                showAlert("Beverage Added",
                        quantity + " x " + selectedSize.toString().toLowerCase() + " " +
                                selectedFlavor.toString() + " added to order.");
                sizeSelectionPopup.hide();
                selectedFlavor = null;
                selectedSize = null;
                quantity = 1;
                currentbBeveragePrice.setText("Price: $0.00");

            } catch (Exception ex) {
                showAlert("Error", "Failed to add beverage: " + ex.getMessage());
            }
        });

        // Event handler for cancel button
        cancelButton.setOnAction(e -> sizeSelectionPopup.hide());

        // Add components to the popup content
        popupContent.getChildren().addAll(
                titleLabel,
                new Separator(),
                sizeLabel,
                sizeBox,
                new Separator(),
                quantityBox,
                new Separator(),
                priceLabel,
                buttonBox
        );

        // Create and show the popup
        sizeSelectionPopup = new Popup();
        sizeSelectionPopup.getContent().add(popupContent);
        sizeSelectionPopup.setAutoHide(true);
        Stage stage = (Stage) beverageGrid.getScene().getWindow();
        sizeSelectionPopup.show(stage,
                stage.getX() + stage.getWidth()/2 - popupContent.getPrefWidth()/2,
                stage.getY() + stage.getHeight()/2 - 150);
    }

    /**
     * Updates the displayed price based on the selected size and quantity.
     *
     * @param priceLabel The label displaying the price.
     * @param qty The selected quantity.
     */
    private void updatePopupPrice(Label priceLabel, int qty) {
        if (selectedFlavor != null && selectedSize != null) {
            Beverage tempBeverage = new Beverage(selectedSize, selectedFlavor, qty);
            double price = tempBeverage.price();
            priceLabel.setText(String.format("Total: $%.2f", price));
            currentbBeveragePrice.setText(String.format("Price: $%.2f", price));
        } else {
            priceLabel.setText("Select a size to see price");
        }
    }

    /**
     * Displays an alert with the specified title and message.
     *
     * @param title The title of the alert.
     * @param message The message to display in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
