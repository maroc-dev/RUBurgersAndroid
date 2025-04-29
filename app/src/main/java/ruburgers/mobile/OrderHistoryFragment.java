package ruburgers.mobile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import model.MenuItem;
import model.Order;
import model.OrderManager;

/**
 * Fragment class for displaying the order history.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public class OrderHistoryFragment extends Fragment {

    private ListView orderHistoryListView;
    private TextView emptyHistoryTextView;
    private Button clearHistoryButton;

    private OrderManager orderManager;
    private ArrayList<Order> orderHistory;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> displayItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        orderManager = OrderManager.getInstance();
        orderHistory = orderManager.getOrderHistory();
        displayItems = new ArrayList<>();

        initializeUIComponents(view);
        setupListeners();
        updateOrderHistoryDisplay();

        return view;
    }

    private void initializeUIComponents(View view) {
        orderHistoryListView = view.findViewById(R.id.orderHistoryListView);
        emptyHistoryTextView = view.findViewById(R.id.emptyHistoryTextView);
        clearHistoryButton = view.findViewById(R.id.clearHistoryButton);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, displayItems);
        orderHistoryListView.setAdapter(adapter);
    }

    private void setupListeners() {
        orderHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < orderHistory.size()) {
                    showOrderDetailsDialog(position);
                }
            }
        });

        clearHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!orderHistory.isEmpty()) {
                    showClearHistoryConfirmation();
                } else {
                    Toast.makeText(getActivity(), "Order history is already empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateOrderHistoryDisplay() {
        displayItems.clear();

        if (orderHistory.isEmpty()) {
            emptyHistoryTextView.setVisibility(View.VISIBLE);
            orderHistoryListView.setVisibility(View.GONE);
            clearHistoryButton.setEnabled(false);
        } else {
            emptyHistoryTextView.setVisibility(View.GONE);
            orderHistoryListView.setVisibility(View.VISIBLE);
            clearHistoryButton.setEnabled(true);

            for (int i = 0; i < orderHistory.size(); i++) {
                Order order = orderHistory.get(i);
                displayItems.add("Order #" + order.getOrderNumber() +
                        String.format(" - Items: %d - Total: $%.2f",
                                order.getItems().size(), order.orderTotal()));
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void showOrderDetailsDialog(int position) {
        Order selectedOrder = orderHistory.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Order #" + selectedOrder.getOrderNumber());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.order_details_dialogue, null);
        builder.setView(dialogView);

        ListView itemsListView = dialogView.findViewById(R.id.orderItemsListView);
        TextView subtotalTextView = dialogView.findViewById(R.id.subtotalTextView);
        TextView taxTextView = dialogView.findViewById(R.id.taxTextView);
        TextView totalTextView = dialogView.findViewById(R.id.totalTextView);

        ArrayList<String> itemDetails = new ArrayList<>();
        for (MenuItem item : selectedOrder.getItems()) {
            itemDetails.add(item.toString() + String.format(" - $%.2f", item.price()));
        }

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, itemDetails);
        itemsListView.setAdapter(itemsAdapter);

        subtotalTextView.setText(String.format("Subtotal: $%.2f", selectedOrder.subtotal()));
        taxTextView.setText(String.format("Tax (6.625%%): $%.2f", selectedOrder.taxAmount()));
        totalTextView.setText(String.format("Total: $%.2f", selectedOrder.orderTotal()));

        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Remove Order", (dialog, which) -> {
            showRemoveOrderConfirmation(position);
        });

        builder.create().show();
    }

    private void showRemoveOrderConfirmation(int position) {
        Order orderToRemove = orderHistory.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Remove Order");
        builder.setMessage("Are you sure you want to remove Order #" +
                orderToRemove.getOrderNumber() + " from history?");

        builder.setPositiveButton("Remove", (dialog, which) -> {
            orderManager.removeFromOrderHistory(orderToRemove);
            updateOrderHistoryDisplay();
            Toast.makeText(getActivity(), "Order removed from history", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showClearHistoryConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Clear History");
        builder.setMessage("Are you sure you want to clear all order history? This action cannot be undone.");

        builder.setPositiveButton("Clear", (dialog, which) -> {
            ArrayList<Order> historyToRemove = new ArrayList<>(orderHistory);
            for (Order order : historyToRemove) {
                orderManager.removeFromOrderHistory(order);
            }
            updateOrderHistoryDisplay();
            Toast.makeText(getActivity(), "Order history cleared", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}