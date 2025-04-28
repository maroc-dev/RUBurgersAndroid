package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.layout.FlowPane;
import model.OrderManager;

/**
 * Controller class for the MainController.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab beverageTab;

    @FXML
    private Tab burgerTab;

    @FXML
    private FlowPane currentOrderPane;

    @FXML
    private Tab orderHistoryTab;

    @FXML
    private Tab sandwichTab;

    @FXML
    private Tab sidesTab;

    private Parent burgerView;
    private Parent sandwichView;
    private Parent beverageView;
    private Parent sidesView;
    private Parent orderHistoryView;
    private Parent currentOrderView;
    private CurrentOrder currentOrderController;

    @FXML
    void beverageTabSelected(Event event) {
        if (beverageTab.isSelected()) {
            loadView(beverageTab, "beverages.fxml", beverageView);
        }
    }

    @FXML
    void burgerTabSelected(Event event) {
        if (burgerTab.isSelected()) {
            loadView(burgerTab, "burgers.fxml", burgerView);
        }
    }

    @FXML
    void orderHistorySelected(Event event) {
        if (orderHistoryTab.isSelected()) {
            loadView(orderHistoryTab, "order-history.fxml", orderHistoryView);
        }
    }

    @FXML
    void sandwichTabSelected(Event event) {
        if (sandwichTab.isSelected()) {
            loadView(sandwichTab, "sandwiches.fxml", sandwichView);
        }
    }

    @FXML
    void sidesTabSelected(Event event) {
        if (sidesTab.isSelected()) {
            loadView(sidesTab, "sides.fxml", sidesView);
        }
    }

    private void loadView(Tab tab, String fxmlFile, Parent viewCache) {
        try {
            if (viewCache == null) {
                // Load the view if it's not already loaded
                URL resource = getClass().getClassLoader().getResource(fxmlFile);
                if (resource == null) {
                    return;
                }

                FXMLLoader loader = new FXMLLoader(resource);
                viewCache = loader.load();
            }

            // Set the tab content to the loaded view
            tab.setContent(viewCache);

        } catch (IOException e) {
            e.printStackTrace();

            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading View");
            alert.setHeaderText("Failed to load " + fxmlFile);
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void initialize() {
        try {
            // Initialize order manager if not already initialized
            OrderManager.getInstance();

            // Validate FXML injection
            assert beverageTab != null : "fx:id=\"beverageTab\" was not injected: check your FXML file 'view.fxml'.";
            assert burgerTab != null : "fx:id=\"burgerTab\" was not injected: check your FXML file 'view.fxml'.";
            assert currentOrderPane != null : "fx:id=\"currentOrderPane\" was not injected: check your FXML file 'view.fxml'.";
            assert orderHistoryTab != null : "fx:id=\"orderHistoryTab\" was not injected: check your FXML file 'view.fxml'.";
            assert sandwichTab != null : "fx:id=\"sandwichTab\" was not injected: check your FXML file 'view.fxml'.";
            assert sidesTab != null : "fx:id=\"sidesTab\" was not injected: check your FXML file 'view.fxml'.";

            // Load current order pane
            try {
                URL resource = getClass().getClassLoader().getResource("current-order.fxml");
                if (resource != null) {
                    FXMLLoader loader = new FXMLLoader(resource);
                    currentOrderView = loader.load();
                    currentOrderController = loader.getController();

                    // Register the current order controller with the manager
                    CurrentOrderManager.getInstance().setCurrentOrderController(currentOrderController);

                    // Add it to the current order pane
                    currentOrderPane.getChildren().add(currentOrderView);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Loading Current Order");
                alert.setHeaderText("Failed to load current order view");
                alert.setContentText("Error details: " + e.getMessage());
                alert.showAndWait();
            }

            // Load the first tab (burger tab) by default
            try {
                URL resource = getClass().getClassLoader().getResource("burgers.fxml");
                if (resource != null) {
                    FXMLLoader loader = new FXMLLoader(resource);
                    burgerView = loader.load();
                    burgerTab.setContent(burgerView);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Loading Burger View");
                alert.setHeaderText("Failed to load burger view");
                alert.setContentText("Error details: " + e.getMessage());
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Initialization Error");
            alert.setHeaderText("Failed to initialize the application");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // This allows manually updating the current order pane from other controllers if needed
    public void updateCurrentOrder() {
        if (currentOrderController != null) {
            currentOrderController.updateOrderDisplay();
        }
    }
}