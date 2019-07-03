package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;

import static kitchen.eres.com.kitchen.MyConfig.chosenImgIndex;
import static kitchen.eres.com.kitchen.MyConfig.isEditImg;
import static kitchen.eres.com.kitchen.MyConfig.row_index;


public class AORMAdapter extends RecyclerView.Adapter<AORMAdapter.MyViewHolder> {
    private static final String TAG = "AcceptedItemsAdapter";

    private Context mContext;
    private ArrayList<String> imgList;
    private App app;




    public AORMAdapter(Context mContext, ArrayList<String> imgList) {
        this.imgList = imgList;
        this.mContext = mContext;
        app = (App)mContext.getApplicationContext();
    }


    @Override
    public AORMAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.img_item, parent, false);

        return new AORMAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AORMAdapter.MyViewHolder holder, final int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_banked)
                .error(R.drawable.ic_banked)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        if (app.isImgAddedFromGallery){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),app.uri);
                holder.imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Glide.with(mContext)
                    .load(MyConfig.BaseUrl+imgList.get(position))
                    .apply(options)
                    .into(holder.imageView);
        }

        if(chosenImgIndex==position){
            holder.chosenImg.setVisibility(View.VISIBLE);
            isEditImg = false;
        }
        else
        {
            holder.chosenImg.setVisibility(View.GONE);
        }
        if (isEditImg && position == 0)holder.chosenImg.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;
        View chosenImg;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            chosenImg = itemView.findViewById(R.id.imgCover);

        }
    }


}