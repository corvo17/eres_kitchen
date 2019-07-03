package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;

public class StopListItemAdapterRight extends RecyclerView.Adapter<StopListItemAdapterRight.MyViewHolder> {


    private Context mContext;
    private ArrayList<GetAllProducts> list;

    public StopListItemAdapterRight(Context mContext, ArrayList<GetAllProducts> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public StopListItemAdapterRight.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.stop_item_left, parent, false);

        return new StopListItemAdapterRight.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StopListItemAdapterRight.MyViewHolder holder, final int position) {
        holder.categoryIMG.setImageResource(R.drawable.ic_left);
        holder.categoryTV.setText(list.get(position).getName());
        holder.categoryIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setExcluded(1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryIMG;
        TextView categoryTV;

        MyViewHolder(View itemView) {
            super(itemView);

            categoryIMG = itemView.findViewById(R.id.categoryIMG);
            categoryTV = itemView.findViewById(R.id.categoryTV);

        }
    }


}