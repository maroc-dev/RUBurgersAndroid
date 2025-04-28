package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import model.Order;
import model.OrderManager;
import model.SummaryItem;

/**
 * Controller class for the CurrentOrder.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class CurrentOrder {

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button checkOut;

    @FXML
    private Button removeButton;

    @FXML
    private ListView<Object> orderList;

    @FXML
    void checkOut() {
        Order order = OrderManager.getInstance().getCurrentOrder();
        if (order.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty Order");
            alert.setHeaderText(null);
            alert.setContentText("There are no items in the order.");
            alert.showAndWait();
            return;
        }



        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Placed");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been placed successfully!");
        OrderManager.getInstance().addToOrderHistory(order);
        alert.showAndWait();


        clearOrder();
        // Update UI
        updateOrderDisplay();

    }

    @FXML
    void removeSelectedItem() {
        Object selectedItem = orderList.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an item to remove.");
            alert.showAndWait();
            return;
        }

        // Only remove if it's a menu item (not summary or separator)
        if (selectedItem instanceof model.MenuItem) {
            Order order = OrderManager.getInstance().getCurrentOrder();
            order.removeItem((model.MenuItem) selectedItem);

            // Update display
            updateOrderDisplay();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Selection");
            alert.setHeaderText(null);
            alert.setContentText("You can only remove menu items from the order.");
            alert.showAndWait();
        }
    }

    @FXML
    void clearOrder() {
        OrderManager.getInstance().setCurrentOrder(null);

        updateOrderDisplay();
    }

    public void updateOrderDisplay() {
        Order order = OrderManager.getInstance().getCurrentOrder();

        // Clear the list
        orderList.getItems().clear();
        try {
            // Add all order items
            orderList.getItems().addAll(order.getItems());
        } catch (Exception e) {
            return;
        }


        // Only add summary if there are items
        if (!order.getItems().isEmpty()) {
            // Calculate totals
            double subtotal = order.subtotal();
            double tax = order.taxAmount();
            double total = order.orderTotal();

            // Add a spacer
            orderList.getItems().add("----------------------------------------");

            // Add totals as special items
            orderList.getItems().add(new SummaryItem("Subtotal", subtotal));
            orderList.getItems().add(new SummaryItem("Tax (6.625%)", tax));
            orderList.getItems().add(new SummaryItem("Total", total));
        }
    }

    @FXML
    void initialize() {
        assert checkOut != null : "fx:id=\"checkOut\" was not injected: check your FXML file 'current-order.fxml'.";
        assert orderList != null : "fx:id=\"orderList\" was not injected: check your FXML file 'current-order.fxml'.";
        assert removeButton != null : "fx:id=\"removeButton\" was not injected: check your FXML file 'current-order.fxml'.";

        // Set up the cell factory to handle different types of items
        orderList.setCellFactory(listView -> new TextFieldListCell<>(new StringConverter<Object>() {
            @Override
            public String toString(Object item) {
                if (item instanceof model.MenuItem) {
                    return ((model.MenuItem) item).toString();
                } else if (item instanceof SummaryItem summaryItem) {
                    return String.format("%s: $%.2f", summaryItem.getLabel(), summaryItem.getAmount());
                } else {
                    return item.toString();
                }
            }

            @Override
            public Object fromString(String string) {
                // This is only needed for editable cells, which we don't need
                return string;
            }
        }));

        // Enable selection
        orderList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Add context menu for right-click removal
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeMenuItem = new MenuItem("Remove Item");
        removeMenuItem.setOnAction(e -> removeSelectedItem());
        contextMenu.getItems().add(removeMenuItem);

        orderList.setContextMenu(contextMenu);

        // Register this controller with the manager
        CurrentOrderManager.getInstance().setCurrentOrderController(this);

        // Set up the initial order display
        updateOrderDisplay();
    }
}