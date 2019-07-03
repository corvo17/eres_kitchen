package kitchen.eres.com.kitchen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.net.Pojos.Category;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;

import static kitchen.eres.com.kitchen.MyConfig.row_index;

public class TopCategoryAdapter extends RecyclerView.Adapter<TopCategoryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<GetAllProducts> list;
    private ArrayList<GetAllProducts> products;
    private ArrayList<Category> categories;

    public TopCategoryAdapter(Context mContext, ArrayList<GetAllProducts> list, ArrayList<GetAllProducts> products,ArrayList<Category> categories) {
        this.list = list;
        this.mContext = mContext;
        this.products = products;
        this.categories = categories;
    }

    @Override
    public TopCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.stop_list_item, parent, false);

        return new TopCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TopCategoryAdapter.MyViewHolder holder, final int position) {

        holder. categoryTV.setText(categories.get(position).getName());
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                RecyclerView recyclerLeft = ((StopListActivity)mContext).findViewById(R.id.stopListLeft);
//                RecyclerView recyclerRight = ((StopListActivity)mContext).findViewById(R.id.stopListRight);
//                ArrayList<GetAllProducts> selectedProducts = new ArrayList<>();
//                ArrayList<GetAllProducts> rightItems = new ArrayList<>();
//                ArrayList<GetAllProducts> leftItems = new ArrayList<>();
//                for (GetAllProducts item : products) {
//                    if (item.getCategoryId() == position+1)
//                        selectedProducts.add(item);
//                }
//                for (GetAllProducts item : selectedProducts) {
//                    if (item.getExcluded() == 0){
//                        leftItems.add(item);
//                    }else rightItems.add(item);
//                }
//
//
//                StopListItemAdapterLeft adapterLeft = new StopListItemAdapterLeft(mContext,leftItems);
//                StopListItemAdapterRight adapterRight = new StopListItemAdapterRight(mContext,rightItems);
//                recyclerLeft.setAdapter(adapterLeft);
//                recyclerRight.setAdapter(adapterRight);
//
//
//                holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPressed));
//                row_index = position;
//                notifyDataSetChanged();
//            }
//        });
        if(row_index==position){
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPressed));
        }
        else
        {
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.eres_back));
        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTV;
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);

            categoryTV = itemView.findViewById(R.id.categoryTV);
            cardView = itemView.findViewById(R.id.topCategoryItem);

        }
    }


}