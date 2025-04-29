package ruburgers.mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private List<Drink> drinks;

    public DrinkAdapter(List<Drink> drinks) {
        this.drinks = drinks;
    }

    @Override
    public DrinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drink, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrinkViewHolder holder, int position) {
        Drink drink = drinks.get(position);
        holder.drinkNameTextView.setText(drink.getName());
        holder.drinkImageView.setImageResource(drink.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public static class DrinkViewHolder extends RecyclerView.ViewHolder {
        ImageView drinkImageView;
        TextView drinkNameTextView;

        public DrinkViewHolder(View itemView) {
            super(itemView);
            drinkImageView = itemView.findViewById(R.id.drinkImageView);
            drinkNameTextView = itemView.findViewById(R.id.drinkNameTextView);
        }
    }
}