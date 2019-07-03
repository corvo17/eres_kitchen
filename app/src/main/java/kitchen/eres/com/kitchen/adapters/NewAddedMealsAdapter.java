package kitchen.eres.com.kitchen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;

public class NewAddedMealsAdapter extends RecyclerView.Adapter<NewAddedMealsAdapter.MyViewHolder> {
private static final String TAG = "SingleItemsAdapter";
private Context mContext;
private ArrayList<SendAddedProduct> mData;
@SuppressLint("UseSparseArrays")
public NewAddedMealsAdapter(Context mContext, ArrayList<SendAddedProduct> mData) {
        this.mContext = mContext;
        this.mData = mData;
        }

@Override
public NewAddedMealsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.inner_item, parent, false);

        return new NewAddedMealsAdapter.MyViewHolder(view);
        }

@Override
public void onBindViewHolder(NewAddedMealsAdapter.MyViewHolder holder, int position) {
        holder.productPosition.setText(""+(position+1));

        holder.productName.setText(mData.get(position).getName());

//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.drawable.no_net)
//                .error(R.drawable.no_net)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .priority(Priority.HIGH);
//        Glide.with(mContext)
//                .load(mData.get(position).getImgUrl())
//                .apply(options)
//                .into(holder.korzinkaImg);


        }

@Override
public int getItemCount() {
        return mData.size();
        }

static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView productPosition;
    TextView productName;

    MyViewHolder(View itemView) {
        super(itemView);

        productPosition = itemView.findViewById(R.id.TVPosition);
        productName = itemView.findViewById(R.id.TVName);

    }
}


}