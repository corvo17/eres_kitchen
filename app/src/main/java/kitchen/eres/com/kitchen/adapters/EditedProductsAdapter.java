package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;

public class EditedProductsAdapter extends RecyclerView.Adapter<EditedProductsAdapter.MyViewHolder> {


private Context mContext;
private ArrayList<SendAddedProduct> list;

public EditedProductsAdapter(Context mContext, ArrayList<SendAddedProduct> list) {
        this.mContext = mContext;
        this.list = list;
        }

@Override
public EditedProductsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.edited_product_item, parent, false);

        return new EditedProductsAdapter.MyViewHolder(view);
        }

@Override
public void onBindViewHolder(EditedProductsAdapter.MyViewHolder holder, final int position) {
        holder.TVName.setText(list.get(position).getName());

        }

@Override
public int getItemCount() {
        return list.size();
        }

static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView TVName;

    MyViewHolder(View itemView) {
        super(itemView);

       TVName = itemView.findViewById(R.id.TVName);

    }
}


}
