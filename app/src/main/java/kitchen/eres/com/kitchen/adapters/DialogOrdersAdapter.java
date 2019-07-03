package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.OrderItem;
import kitchen.eres.com.kitchen.net.Pojos.roam.ProdIngredient;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

public class DialogOrdersAdapter extends RecyclerView.Adapter<DialogOrdersAdapter.MyViewHolder> {
private static final String TAG = "AcceptedItemsAdapter";


private Context mContext;
private ArrayList<OrderItem> orderItems;
private String selectedItem;


public DialogOrdersAdapter(Context mContext, ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
        this.mContext = mContext;

        }


@Override
public DialogOrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.single_ingridient_item, parent, false);

        return new DialogOrdersAdapter.MyViewHolder(view);
        }

@Override
public void onBindViewHolder(DialogOrdersAdapter.MyViewHolder holder, final int position) {

        }

@Override
public int getItemCount() {
        return orderItems.size();
        }

public void setSelected(String selected) {
        selectedItem = selected;
        }



static class MyViewHolder extends RecyclerView.ViewHolder {
    AppCompatImageView imageView;
    ImageView doneImg;
    EditText value;
    Spinner spinner;

    MyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);

        doneImg = itemView.findViewById(R.id.imageView1);
        value = itemView.findViewById(R.id.ETValue);
        spinner = itemView.findViewById(R.id.spinner);

    }
}
}