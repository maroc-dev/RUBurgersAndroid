package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Order;
import model.OrderManager;

/**
 * Controller class for the OrderHistory.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class OrderHistory {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exportOrders;

    @FXML
    private ListView<String> listOrders;

    private final OrderManager orderManager = CurrentOrderManager.getInstance().getOrderManager();

    private final ObservableList<String> displayedOrders = FXCollections.observableArrayList();
    private List<Order> orderList;

    @FXML
    void exportOrders(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Orders to Text File");
        fileChooser.setInitialFileName("order_history.txt");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        Stage stage = (Stage) exportOrders.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                for (Order order : orderList) {
                    writer.write(order.toString() + "\n\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        assert exportOrders != null : "fx:id=\"exportOrders\" was not injected: check your FXML file 'order-history.fxml'.";
        assert listOrders != null : "fx:id=\"listOrders\" was not injected: check your FXML file 'order-history.fxml'.";

        populateOrderHistory();
        setupRightClickDelete();
    }

    private void populateOrderHistory() {
        orderList = orderManager.getOrderHistory();
        displayedOrders.clear();
        for (Order order : orderList) {
            displayedOrders.add(order.toString());
        }
        listOrders.setItems(displayedOrders);
    }

    private void setupRightClickDelete() {
        listOrders.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem deleteItem = new MenuItem("Remove Order");

                    deleteItem.setOnAction(e -> {
                        int index = cell.getIndex();
                        Order orderToRemove = orderList.get(index);
                        orderManager.removeFromOrderHistory(orderToRemove);
                        displayedOrders.remove(index);
                    });

                    contextMenu.getItems().add(deleteItem);
                    contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                }
            });

            return cell;
        });
    }
}
