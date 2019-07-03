package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.events.EventRemoveItem;
import kitchen.eres.com.kitchen.events.EventUpdateEditProducts;
import kitchen.eres.com.kitchen.net.Pojos.roam.ProdIngredient;

public class SelectedIngredientItemAdapter extends RecyclerView.Adapter<SelectedIngredientItemAdapter.MyViewHolder> {
private static final String TAG = "AcceptedItemsAdapter";

private Context mContext;
private ArrayList<ProdIngredient> ingredients;

public SelectedIngredientItemAdapter(Context mContext, ArrayList<ProdIngredient> ingredients) {
        this.mContext = mContext;
        this.ingredients = ingredients;
    Toast.makeText(mContext, "Assalomu ALaykum", Toast.LENGTH_SHORT).show();
        }



@Override
public SelectedIngredientItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.selected_ingredient_item, parent, false);

        return new SelectedIngredientItemAdapter.MyViewHolder(view);
        }

@Override
public void onBindViewHolder(SelectedIngredientItemAdapter.MyViewHolder holder, final int position) {
    holder.imgDash.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyConfig.itemRemovePosition = position;
            EventBus.getDefault().post(new EventRemoveItem());
        }
    });
    String unit = "kg";
    holder.TVPosition.setText((position + 1)+"");
    holder.TV5.setText(ingredients.get(position).getIngredient().getName());
    holder.TV7.setText(ingredients.get(position).getCount()+"");
    switch (ingredients.get(position).getUnit()){
        case 0: unit = "Kilogramm"; break;
        case 1: unit = "Gramm"; break;
        case 2: unit = "Pieces"; break;
        case 3: unit = "Litr"; break;
        case 4: unit = "MilliLitr"; break;

    }
    holder.TV6.setText(unit);

        }


@Override
public int getItemCount() {
        return ingredients.size();
        }

static class MyViewHolder extends RecyclerView.ViewHolder {
    TextView TV5;
    TextView TV7;
    TextView TV6;
    TextView TVPosition;
    ImageView imgDash;



    MyViewHolder(View itemView) {
        super(itemView);
        TV5 = itemView.findViewById(R.id.textView5);
        TV7 = itemView.findViewById(R.id.textView7);
        TV6 = itemView.findViewById(R.id.textView6);
        TVPosition = itemView.findViewById(R.id.TVPosition);
        imgDash = itemView.findViewById(R.id.imgDash);

    }
}


}

