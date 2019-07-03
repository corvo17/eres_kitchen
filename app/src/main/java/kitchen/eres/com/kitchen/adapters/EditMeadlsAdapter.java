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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.activities.ROAMActivity;
import kitchen.eres.com.kitchen.activities.ROAMCategoryActivity;
import kitchen.eres.com.kitchen.fragments.EditProductFragment;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kitchen.eres.com.kitchen.MyConfig.deletePosition;
import static kitchen.eres.com.kitchen.MyConfig.editableProductId;

public class EditMeadlsAdapter  extends RecyclerView.Adapter<EditMeadlsAdapter.MyViewHolder> {


    private Context mContext;
    private ArrayList<GetAllProducts> list;

    public EditMeadlsAdapter(Context mContext, ArrayList<GetAllProducts> list) {
        this.mContext = mContext;
        this.list = list;
       // Toast.makeText(mContext, "CorvoAttano EditMealsAdapter", Toast.LENGTH_SHORT).show();
    }

    @Override
    public EditMeadlsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.edit_meal_item, parent, false);

        return new EditMeadlsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditMeadlsAdapter.MyViewHolder holder, final int position) {
        holder.categoryTV.setText(list.get(position).getName());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout containerLinear = ((ROAMCategoryActivity)mContext).findViewById(R.id.containerLinear);
                containerLinear.setVisibility(View.GONE);
                FrameLayout container = ((ROAMCategoryActivity)mContext).findViewById(R.id.containerEdit);
                FragmentManager manager = ((ROAMCategoryActivity) mContext).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction
                        .replace(container.getId(),new EditProductFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                editableProductId = list.get(position).getId();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePosition = position;
                App app  = (App)mContext.getApplicationContext();
                app.getManager().getService()
                        .deletePo(list.get(position).getId())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(mContext, "Product DELETED :)", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                list.remove(deletePosition);
               // notifyDataSetChanged();
               // notifyItemRemoved(deletePosition);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTV;
        ImageView btnEdit;
        ImageView btnDelete;

        MyViewHolder(View itemView) {
            super(itemView);

            categoryTV = itemView.findViewById(R.id.categoryTV);
            btnEdit = itemView.findViewById(R.id.button11);
            btnDelete = itemView.findViewById(R.id.button10);

        }
    }


}

