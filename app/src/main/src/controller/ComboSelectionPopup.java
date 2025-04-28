package controller;

import java.net.URL;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.*;
import model.MenuItem;

/**
 * Controller class for the ComboSelectionPopup.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class ComboSelectionPopup {

    private Popup comboPopup;
    private Beverage selectedDrink;
    private Side selectedSide;
    private MenuItem baseItem; // The burger or sandwich being made into a combo
    private ToggleButton comboToggleButton; // The toggle button to reset if canceled

    public ComboSelectionPopup(MenuItem baseItem, Stage ownerStage, ToggleButton comboToggleButton, Label burgerPriceLabel) {
        this.baseItem = baseItem;
        this.comboToggleButton = comboToggleButton;

        // Create popup content
        VBox popupContent = new VBox(10);
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 15;");
        popupContent.setPrefWidth(400);
        popupContent.setAlignment(Pos.CENTER);

        // Title
        Label titleLabel = new Label("Create Your Combo");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");

        // Description
        Label descLabel = new Label("Combo includes a small side (chips or apple) \nand a medium drink (cola, tea, or juice)");
        descLabel.setStyle("-fx-font-size: 14;");

        // Side selection
        Label sideLabel = new Label("Select Side:");
        sideLabel.setStyle("-fx-font-weight: bold;");

        ToggleGroup sideGroup = new ToggleGroup();
        HBox sideBox = createSideOptions(sideGroup);

        // Drink selection
        Label drinkLabel = new Label("Select Drink:");
        drinkLabel.setStyle("-fx-font-weight: bold;");

        ToggleGroup drinkGroup = new ToggleGroup();
        HBox drinkBox = createDrinkOptions(drinkGroup);

        // Price display
        Label comboChargeLabel = new Label("Combo includes discounted pricing!");
        comboChargeLabel.setStyle("-fx-font-style: italic;");

        // Buttons
        Button addButton = new Button("Add Combo");
        addButton.setDisable(true); // Initially disabled

        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, cancelButton);

        // Logic for enabling add button when both selections are made
        sideGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            updateAddButton(addButton, sideGroup, drinkGroup);
        });

        drinkGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            updateAddButton(addButton, sideGroup, drinkGroup);
        });

        addButton.setOnAction(e -> {
            if (selectedSide != null && selectedDrink != null) {
                try {
                    // Get the quantity from the base item
                    int quantity = 1;
                    if (baseItem instanceof MenuItem) {
                        quantity = ((MenuItem)baseItem).getQuantity();
                    }

                    // Set the quantities on the drink
                    selectedDrink.setQuantity(quantity);

                    // Create the combo
                    Combo combo;
                    if (baseItem instanceof Burger) {
                        // For burgers, create a combo with burger-specific handling
                        combo = new Combo((Sandwich)baseItem, selectedDrink, selectedSide, quantity);
                    } else {
                        // For regular sandwiches
                        combo = new Combo((Sandwich)baseItem, selectedDrink, selectedSide, quantity);
                    }


                    if (OrderManager.getInstance().getCurrentOrder() == null) {
                        OrderManager.getInstance().setCurrentOrder(new Order());
                    }

                    // Add to order
                    OrderManager.getInstance().getCurrentOrder().addItem(combo);

                    // Update current order display
                    try {
                        CurrentOrderManager.getInstance().updateCurrentOrderDisplay();
                    } catch (Exception ex) {
                        showAlert("Display Update Error",
                                "Combo added to order but display couldn't update: " + ex.getMessage());
                    }

                    // Show confirmation
                    String itemType = (baseItem instanceof Burger) ? "Burger" : "Sandwich";
                    showAlert("Combo Added", quantity + "x " + itemType + " combo meal added to your order!");

                    // Close popup
                    comboPopup.hide();
                    burgerPriceLabel.setText("Price: $0.00");

                } catch (IllegalArgumentException ex) {
                    showAlert("Invalid Combo", ex.getMessage());
                    if (comboToggleButton != null) {
                        comboToggleButton.setSelected(false);
                    }
                    comboPopup.hide();
                    burgerPriceLabel.setText("Price: $0.00");

                }
            }
        });

        cancelButton.setOnAction(e -> {
            // Reset the toggle button if canceled
            if (comboToggleButton != null) {
                comboToggleButton.setSelected(false);
            }
            comboPopup.hide();
            burgerPriceLabel.setText("Price: $0.00");
        });

        // Add all components to the popup content
        popupContent.getChildren().addAll(
                titleLabel,
                descLabel,
                new Separator(),
                sideLabel,
                sideBox,
                new Separator(),
                drinkLabel,
                drinkBox,
                new Separator(),
                comboChargeLabel,
                buttonBox
        );

        // Create and configure popup
        comboPopup = new Popup();
        comboPopup.getContent().add(popupContent);
        comboPopup.setAutoHide(true);

        // Show popup centered on owner stage
        comboPopup.show(ownerStage,
                ownerStage.getX() + ownerStage.getWidth()/2 - popupContent.getPrefWidth()/2,
                ownerStage.getY() + ownerStage.getHeight()/2 - 200);

    }

    private HBox createSideOptions(ToggleGroup group) {
        HBox sideBox = new HBox(20);
        sideBox.setAlignment(Pos.CENTER);

        // Create option for chips - only allowed side options for combos
        VBox chipsOption = createSideOption(Side.CHIPS, group);

        // Create option for apple slices - only allowed side options for combos
        VBox appleOption = createSideOption(Side.APPLE_SLICES, group);

        sideBox.getChildren().addAll(chipsOption, appleOption);
        return sideBox;
    }

    private VBox createSideOption(Side side, ToggleGroup group) {
        VBox optionBox = new VBox(5);
        optionBox.setAlignment(Pos.CENTER);

        // Load image for side
        ImageView imageView = new ImageView();
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);

        try {
            String sideName = side.name().toLowerCase();
            URL imageUrl = getClass().getResource("/resources/images/" + sideName + ".png");
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toExternalForm());
                imageView.setImage(image);
            }
        } catch (Exception e) {
        }

        // Create radio button
        RadioButton radioButton = new RadioButton(side.name().replace("_", " "));
        radioButton.setToggleGroup(group);

        // Set action when selected
        radioButton.setOnAction(e -> {
            if (radioButton.isSelected()) {
                selectedSide = side;
            }
        });

        optionBox.getChildren().addAll(imageView, radioButton);
        return optionBox;
    }

    private HBox createDrinkOptions(ToggleGroup group) {
        HBox drinkBox = new HBox(20);
        drinkBox.setAlignment(Pos.CENTER);

        // Create option for cola - only allowed drink options for combos
        VBox colaOption = createDrinkOption(Flavor.COLA, group);

        // Create option for tea - only allowed drink options for combos
        VBox teaOption = createDrinkOption(Flavor.TEA, group);

        // Create option for juice - only allowed drink options for combos
        VBox juiceOption = createDrinkOption(Flavor.JUICE, group);

        drinkBox.getChildren().addAll(colaOption, teaOption, juiceOption);
        return drinkBox;
    }

    private VBox createDrinkOption(Flavor flavor, ToggleGroup group) {
        VBox optionBox = new VBox(5);
        optionBox.setAlignment(Pos.CENTER);

        // Load image for drink
        ImageView imageView = new ImageView();
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);

        try {
            String flavorName = flavor.toString().toLowerCase().replace(" ", "_");
            URL imageUrl = getClass().getResource("/resources/images/" + flavorName + ".png");
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toExternalForm());
                imageView.setImage(image);
            }
        } catch (Exception e) {
        }

        // Create radio button
        RadioButton radioButton = new RadioButton(flavor.toString());
        radioButton.setToggleGroup(group);

        // Set action when selected
        radioButton.setOnAction(e -> {
            if (radioButton.isSelected()) {
                // Always create MEDIUM size drinks for combos
                Beverage beverage = new Beverage(Size.MEDIUM, flavor, 1);
                selectedDrink = beverage;
            }
        });

        optionBox.getChildren().addAll(imageView, radioButton);
        return optionBox;
    }

    private void updateAddButton(Button addButton, ToggleGroup sideGroup, ToggleGroup drinkGroup) {
        // Enable the Add button only when both side and drink are selected
        addButton.setDisable(sideGroup.getSelectedToggle() == null || drinkGroup.getSelectedToggle() == null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}