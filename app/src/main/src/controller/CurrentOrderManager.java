package controller;

import model.OrderManager;

/**
 * Controller class for the CurrentOrderManager.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class CurrentOrderManager {
    private static CurrentOrderManager instance;
    private CurrentOrder currentOrderController;
    private OrderManager orderManager = OrderManager.getInstance();

    private CurrentOrderManager() {}

    public static CurrentOrderManager getInstance() {
        if (instance == null) {
            instance = new CurrentOrderManager();
        }
        return instance;
    }

    public void setCurrentOrderController(CurrentOrder controller) {
        this.currentOrderController = controller;
    }

    public void updateCurrentOrderDisplay() {
        if (currentOrderController != null) {
            currentOrderController.updateOrderDisplay();
        }
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

}