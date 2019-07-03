package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.helper.MyDiffCallBack;
import kitchen.eres.com.kitchen.net.IService;
import kitchen.eres.com.kitchen.net.NetworkManager;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kitchen.eres.com.kitchen.MyConfig.CHECK_CLICKABLE;
import static kitchen.eres.com.kitchen.MyConfig.scrollPosition;

public class NewOrdersCategoryAdapter extends RecyclerView.Adapter<NewOrdersCategoryAdapter.MyViewHolder>  {
    private static final String TAG = "MainActivity";


    private Context mContext;
    private ArrayList<ResponseOrder> mData;

    public NewOrdersCategoryAdapter(Context mContext, ArrayList<ResponseOrder>mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public NewOrdersCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_order_list, parent, false);

        return new NewOrdersCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewOrdersCategoryAdapter.MyViewHolder holder,  int position) {
        holder.tableNumber.setText("Cтол №  " + mData.get(position).getTableName());
        holder.itemOrder.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        holder.tableNumber.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        holder.categoryBadge.setText(""+mData.get(position).getOrderItems().size());
        if (position == MyConfig.OrderSelected){
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
        TextView categoryBadge;

        MyViewHolder(View itemView) {
            super(itemView);
            tableNumber = itemView.findViewById(R.id.categoryName);
            itemOrder = itemView.findViewById(R.id.itemOrders);
            categoryBadge = itemView.findViewById(R.id.categoryBadge);
        }
    }

}