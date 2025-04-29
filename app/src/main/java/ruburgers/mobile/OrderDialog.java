package ruburgers.mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.MenuItem;
import model.Order;

public class OrderDialog {
    private Context context;
    private DataManager dataManager;
    private AlertDialog dialog;
    private TextView subtotalText;
    private TextView taxText;
    private TextView totalText;
    private ArrayList<MenuItem> orderItems;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> displayItems;

    public OrderDialog(Context context) {
        this.context = context;
        this.dataManager = DataManager.getInstance();
        this.orderItems = dataManager.getCurrentOrder().getItems();
        this.displayItems = new ArrayList<>();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Current Order");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.orderdialog, null);
        builder.setView(dialogView);

        ListView orderListView = dialogView.findViewById(R.id.orderListView);
        subtotalText = dialogView.findViewById(R.id.subtotalText);
        taxText = dialogView.findViewById(R.id.taxText);
        totalText = dialogView.findViewById(R.id.totalText);

        updateOrderDisplay();

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, displayItems);
        orderListView.setAdapter(adapter);

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showRemoveItemDialog(position);
            }
        });

        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear Order", (dialog, which) -> {
            showClearOrderConfirmation();
        });

        dialog = builder.create();
        dialog.show();
    }

    private void updateOrderDisplay() {
        Order order = dataManager.getCurrentOrder();
        orderItems = order.getItems();
        displayItems.clear();

        for (int i = 0; i < orderItems.size(); i++) {
            MenuItem item = orderItems.get(i);
            displayItems.add((i + 1) + ". " + item.toString());
        }

        if (displayItems.isEmpty()) {
            displayItems.add("No items in order");
        }

        subtotalText.setText(String.format("Subtotal: $%.2f", order.subtotal()));
        taxText.setText(String.format("Tax (6.625%%): $%.2f", order.taxAmount()));
        totalText.setText(String.format("Total: $%.2f", order.orderTotal()));

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void showRemoveItemDialog(int position) {
        if (orderItems.isEmpty() || position >= orderItems.size()) {
            return;
        }

        MenuItem itemToRemove = orderItems.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Remove Item");
        builder.setMessage("Do you want to remove this item from your order?\n\n" + itemToRemove.toString());

        builder.setPositiveButton("Remove", (dialog, which) -> {
            dataManager.removeOrderItem(itemToRemove);
            updateOrderDisplay();
            Toast.makeText(context, "Item removed from order", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showClearOrderConfirmation() {
        if (orderItems.isEmpty()) {
            Toast.makeText(context, "Order is already empty", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Clear Order");
        builder.setMessage("Are you sure you want to clear your entire order?");

        builder.setPositiveButton("Clear", (dialog, which) -> {
            dataManager.clearOrder();
            this.dialog.dismiss();
            show();
            Toast.makeText(context, "Order cleared", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}