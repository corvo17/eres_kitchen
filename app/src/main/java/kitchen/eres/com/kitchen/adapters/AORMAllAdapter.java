package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.net.Pojos.roam.SingleProduct;

public class AORMAllAdapter extends RecyclerView.Adapter<AORMAllAdapter.MyViewHolder> {
    private static final String TAG = "AcceptedItemsAdapter";

    private Context mContext;
    private ArrayList<SingleProduct> imgList;




    public AORMAllAdapter(Context mContext, ArrayList<SingleProduct> imgList) {
        this.imgList = imgList;
        this.mContext = mContext;
    }


    @Override
    public AORMAllAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.img_item, parent, false);

        return new AORMAllAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AORMAllAdapter.MyViewHolder holder, final int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_banked)
                .error(R.drawable.ic_banked)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(mContext)
                .load(MyConfig.BaseUrl+imgList.get(position).getImageUrl())
                .apply(options)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);


        }
    }


}