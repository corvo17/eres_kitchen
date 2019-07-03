package kitchen.eres.com.kitchen.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.OrderItem;

public class SingleItemAdapter2 extends RecyclerView.Adapter<SingleItemAdapter2.MyViewHolder> {
    private static final String TAG = "SingleItemsAdapter";
    private Context mContext;
    private  ArrayList<OrderItem>mData;
    private ArrayList<GetAllProducts> products;

    private INotifyCancel iNotifyCancel;

    public SingleItemAdapter2(Context mContext, ArrayList<OrderItem> mData, ArrayList<GetAllProducts> products) {
        this.mContext = mContext;
        this.mData = mData;
        this.products = products;
    }
    public void setContexToInterface(INotifyCancel iNotifyCancel){
        this.iNotifyCancel = iNotifyCancel;
    }

    @Override
    public SingleItemAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.inner_item2, parent, false);

        return new SingleItemAdapter2.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SingleItemAdapter2.MyViewHolder holder, final int position) {
        if (mData.get(position).getDescriptor() != null && mData.get(position).getDescriptor().length() >= 1)holder.tvWhichOne.setText(mData.get(position).getDescriptor());
        if (mData.get(position).getDescriptor2() != null && mData.get(position).getDescriptor().length() >= 1)holder.tvDescription.setText("( " + mData.get(position).getDescriptor2()+" )");
        holder.viiewCover.setVisibility(View.INVISIBLE);
        holder.TV.setText(""+mData.get(position).getCount());
        holder.TVPosition.setText(""+(position + 1));
        if (position % 2 != 1)holder.innerItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        for (GetAllProducts item : products ) {
            if (item.getId() == mData.get(position).getProductId()) {
                holder.TVName.setText(item.getName());
                break;
            }
        }
        holder.imgReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getApp().dontUpdate = true;
                mData.get(position).setFactCount(mData.get(position).getFactCount() - 1);
                //notifyDataSetChanged();
                holder.innerItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                holder.viiewCover.setVisibility(View.VISIBLE);
                //holder.imgReject.setVisibility(View.INVISIBLE);
                holder.imgReject.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_delete_2));
                iNotifyCancel.notifyCancel();
            }
        });

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

        TextView TV;
        TextView TVPosition;
        TextView TVName;
        ImageView imgReject;
        View innerItem;
        View viiewCover;
        TextView tvWhichOne;
        TextView tvDescription;

        MyViewHolder(View itemView) {
            super(itemView);

            TV = itemView.findViewById(R.id.TVCount);
            TVPosition = itemView.findViewById(R.id.TVPosition);
            TVName = itemView.findViewById(R.id.TVName);
            imgReject = itemView.findViewById(R.id.imgReject);
            innerItem = itemView.findViewById(R.id.innerItem);
            viiewCover = itemView.findViewById(R.id.viewCover);
            tvWhichOne = itemView.findViewById(R.id.tvWhichOne);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }

    public interface INotifyCancel{
        void notifyCancel();
    }
}