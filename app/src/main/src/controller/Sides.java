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
 * Controller class for the Sides view.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class Sides {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button appleSlicesButton;

    @FXML
    private GridPane beverageGrid; // Note: This should be renamed to sidesGrid in your FXML

    @FXML
    private Button chipsButton;

    @FXML
    private Button friesButton;

    @FXML
    private Button onionRingsButton;


    private Side selectedSide;
    private Size selectedSize;
    private int quantity = 1;
    private Popup sizeSelectionPopup;

    // Map to associate buttons with sides
    private Map<Button, Side> buttonSideMap = new HashMap<>();

    @FXML
    void initialize() {
        // Initialize the button-side map
        buttonSideMap.put(chipsButton, Side.CHIPS);
        buttonSideMap.put(friesButton, Side.FRIES);
        buttonSideMap.put(onionRingsButton, Side.ONION_RINGS);
        buttonSideMap.put(appleSlicesButton, Side.APPLE_SLICES);

        // Set up each button with an image and click event
        for (Map.Entry<Button, Side> entry : buttonSideMap.entrySet()) {
            Button button = entry.getKey();
            Side side = entry.getValue();

            // Set the image on the button
            setButtonImage(button, side);

            // Set the click event
            button.setOnAction(e -> showSizeSelection(side));
        }

    }

    private void setButtonImage(Button button, Side side) {
        try {
            // Convert side name to lowercase for path
            String sideName = side.name().toLowerCase();

            // Get resource from correct path
            String imagePath = "/resources/images/" + sideName + ".png";
            URL imageUrl = getClass().getResource(imagePath);

            if (imageUrl != null) {
                Image image = new Image(imageUrl.toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);
                imageView.setPreserveRatio(true);
                button.setGraphic(imageView);

                // Add tooltip with side name
                button.setTooltip(new Tooltip(side.name().replace("_", " ")));
            } else {
                // Try with underscores replaced by spaces
                String formattedName = side.name().toLowerCase().replace("_", " ");
                imagePath = "/resources/images/" + formattedName.replace(" ", "_") + ".png";
                imageUrl = getClass().getResource(imagePath);

                if (imageUrl != null) {
                    Image image = new Image(imageUrl.toExternalForm());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(80);
                    imageView.setFitWidth(80);
                    imageView.setPreserveRatio(true);
                    button.setGraphic(imageView);

                    // Add tooltip with side name
                    button.setTooltip(new Tooltip(side.name().replace("_", " ")));
                } else {
                    button.setText(side.name().replace("_", " "));
                }
            }
        } catch (Exception e) {
            // Log the error and fallback to text
            button.setText(side.name().replace("_", " "));
        }
    }

    private void showSizeSelection(Side side) {
        selectedSide = side;

        if (sizeSelectionPopup != null && sizeSelectionPopup.isShowing()) {
            sizeSelectionPopup.hide();
        }

        // Create popup content
        VBox popupContent = new VBox(10);
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 10;");
        popupContent.setPrefWidth(300);
        popupContent.setAlignment(Pos.CENTER);

        // Title
        Label titleLabel = new Label("Select Options for " + side.name().replace("_", " "));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        // Size selection
        Label sizeLabel = new Label("Size:");
        ToggleGroup sizeGroup = new ToggleGroup();
        RadioButton smallButton = new RadioButton("Small - $" + String.format("%.2f", side.getPriceBySize(Size.SMALL)));
        RadioButton mediumButton = new RadioButton("Medium - $" + String.format("%.2f", side.getPriceBySize(Size.MEDIUM)));
        RadioButton largeButton = new RadioButton("Large - $" + String.format("%.2f", side.getPriceBySize(Size.LARGE)));

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

        // Add to order button
        Button addButton = new Button("Add to Order");
        addButton.setDisable(true); // Initially disabled until size is selected

        // Cancel button
        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, cancelButton);

        // Event handlers
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

        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            quantity = newVal;
            updatePopupPrice(priceLabel, newVal);
        });

        addButton.setOnAction(e -> {
            try {
                // Create the side order
                SideOrder sideOrder = new SideOrder(selectedSide, selectedSize, quantity);

                // Add to order
                OrderManager.getInstance().getCurrentOrder().addItem(sideOrder);

                // Update current order display
                try {
                    CurrentOrderManager.getInstance().updateCurrentOrderDisplay();
                } catch (Exception ex) {
                    showAlert("Display Update Error",
                            "Side added to order but display couldn't update: " + ex.getMessage());
                }

                // Show confirmation
                showAlert("Side Added",
                        quantity + " x " + selectedSize.toString().toLowerCase() + " " +
                                selectedSide.name().replace("_", " ") + " added to order.");

                // Close popup and reset
                sizeSelectionPopup.hide();
                selectedSide = null;
                selectedSize = null;
                quantity = 1;

            } catch (Exception ex) {
                showAlert("Error", "Failed to add side: " + ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> sizeSelectionPopup.hide());

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

        // Create and show popup
        sizeSelectionPopup = new Popup();
        sizeSelectionPopup.getContent().add(popupContent);
        sizeSelectionPopup.setAutoHide(true);

        // Position and show popup
        Stage stage = (Stage) beverageGrid.getScene().getWindow();
        sizeSelectionPopup.show(stage,
                stage.getX() + stage.getWidth()/2 - popupContent.getPrefWidth()/2,
                stage.getY() + stage.getHeight()/2 - 150);
    }

    private void updatePopupPrice(Label priceLabel, int qty) {
        if (selectedSide != null && selectedSize != null) {
            SideOrder tempSideOrder = new SideOrder(selectedSide, selectedSize, qty);
            tempSideOrder.setQuantity(qty);

            double price = tempSideOrder.price() * qty;

            // Update the price displays
            priceLabel.setText(String.format("Total: $%.2f", price));
        } else {
            priceLabel.setText("Select a size to see price");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}