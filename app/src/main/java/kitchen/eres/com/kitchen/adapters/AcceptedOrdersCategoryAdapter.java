package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;

public class AcceptedOrdersCategoryAdapter extends RecyclerView.Adapter<AcceptedOrdersCategoryAdapter.MyViewHolder>  {
    private static final String TAG = "MainActivity";

    private Context mContext;
    private ArrayList<ResponseOrder> mData;

    public AcceptedOrdersCategoryAdapter(Context mContext, ArrayList<ResponseOrder>mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public AcceptedOrdersCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_order_list, parent, false);

        return new AcceptedOrdersCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AcceptedOrdersCategoryAdapter.MyViewHolder holder, int position) {
        holder.tableNumber.setText("Cтол №  " + mData.get(position).getTableName());
        holder.itemOrder.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        holder.tableNumber.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        holder.categorybadege.setText(""+mData.get(position).getOrderItems().size());
        if (position == MyConfig.AcceptedOrderSelected){
            holder.itemOrder.setBackgroundColor(mContext.getResources().getColor(R.color.eres_back));
            holder.tableNumber.setBackgroundColor(mContext.getResources().getColor(R.color.category_item_back));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tableNumber;
        ConstraintLayout itemOrder;
        TextView categorybadege;

        MyViewHolder(View itemView) {
            super(itemView);
            tableNumber = itemView.findViewById(R.id.categoryName);
            itemOrder = itemView.findViewById(R.id.itemOrders);
            categorybadege = itemView.findViewById(R.id.categoryBadge);
        }
    }

}