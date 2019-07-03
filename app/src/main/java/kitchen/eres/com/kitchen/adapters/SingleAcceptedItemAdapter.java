package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.net.IService;
import kitchen.eres.com.kitchen.net.NetworkManager;
import kitchen.eres.com.kitchen.net.Pojos.AccessToken;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.OrderItem;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kitchen.eres.com.kitchen.MyConfig.CHECK_CLICKABLE;

public class SingleAcceptedItemAdapter extends RecyclerView.Adapter<SingleAcceptedItemAdapter.MyViewHolder> {


    private Context mContext;
    private ArrayList<OrderItem> acceptedOrders;
    private ArrayList<GetAllProducts> products;
   // private ResponseOrder responseOrder;
    public SingleAcceptedItemAdapter(Context mContext, ArrayList<OrderItem> orderItems, ArrayList<GetAllProducts> products) {
        this.mContext = mContext;
        this.acceptedOrders = orderItems;
        this.products = products;
    }

    @Override
    public SingleAcceptedItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.single_accepted_item, parent, false);

        return new SingleAcceptedItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SingleAcceptedItemAdapter.MyViewHolder holder, final int position) {
        holder.TVPosition.setText((position+1)+"");

        holder.imgSingleDone.setImageResource(R.drawable.ic_done_first);
        for (GetAllProducts item : products ) {
            if (item.getId() == acceptedOrders.get(position).getProductId())
                holder.TVName.setText(item.getName());
        }
        holder.TVCount.setText(acceptedOrders.get(position).getCount()+"");
        if (acceptedOrders.get(position).getDescriptor().length() >= 1)
        holder.TVDescribtion.setText(acceptedOrders.get(position).getDescriptor());

       if(acceptedOrders.get(position).getStateId() != 4){
           holder.imgSingleDone.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (acceptedOrders.size() > position) {
                       if (acceptedOrders.get(position).getState() != 2){
                           holder.imgSingleDone.setImageResource(R.drawable.ic_done_second);
                           acceptedOrders.get(position).setState(2);
                       } else {
                           CHECK_CLICKABLE = false;
                           holder.imgSingleDone.setImageResource(R.drawable.ic_done_third);
                           holder.vieCover.setVisibility(View.VISIBLE);
                           acceptedOrders.get(position).setState(3);
                           acceptedOrders.get(position).setStateId(11);
                           App app = (App) mContext.getApplicationContext();
                           NetworkManager manager = app.getManager();
                           IService service = manager.getService();
                           service.sendPartlyComplete(acceptedOrders.get(position).getId())
                                   .enqueue(new Callback<AccessToken>() {
                                       @Override
                                       public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                                           if (response.isSuccessful()){
                                               Toast.makeText(app, "Partly COMPLETED :)", Toast.LENGTH_SHORT).show();
                                           }else {
                                               Toast.makeText(app, "Code not 200 :(", Toast.LENGTH_SHORT).show();
                                           }
                                           CHECK_CLICKABLE = false;
                                       }

                                       @Override
                                       public void onFailure(Call<AccessToken> call, Throwable t) {
                                           CHECK_CLICKABLE = false;
                                       }
                                   });

                       }
                   }
               }
           });
       }else {
           holder.imgSingleDone.setImageResource(R.drawable.ic_done_third);
           holder.vieCover.setVisibility(View.VISIBLE);
       }
    }

    @Override
    public int getItemCount() {
        return acceptedOrders.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TVPosition;
        TextView TVName;
        TextView TVCount;
        TextView TVDescribtion;
        TextView TVWhichOne;
        ImageView imgSingleDone;
        View vieCover;


        MyViewHolder(View itemView) {
            super(itemView);

            TVPosition = itemView.findViewById(R.id.TVPosition);
            TVName = itemView.findViewById(R.id.TVName);
            TVCount = itemView.findViewById(R.id.TVCount);
            TVDescribtion = itemView.findViewById(R.id.TVDescrption);
            TVWhichOne = itemView.findViewById(R.id.TVWhichOne);
            imgSingleDone = itemView.findViewById(R.id.imgSingleDone);
            vieCover = itemView.findViewById(R.id.viewCover);
        }
    }

}
