package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.OrderItem;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kitchen.eres.com.kitchen.MyConfig.CHECK_CLICKABLE;

public class AcceptedItemsAdapter extends RecyclerView.Adapter<AcceptedItemsAdapter.MyViewHolder> {
    private static final String TAG = "AcceptedItemsAdapter";

    private Context mContext;
    private ArrayList<ResponseOrder> acceptedOrders;
    private ArrayList<GetAllProducts> products;

    private Thread thread;
    private int COUNTER ;
    private App app;
    private ArrayList<ResponseOrder> newData;
    private NetworkManager manager;
    private IMoveChangesToAcceptedOrders changedOrders;

    public AcceptedItemsAdapter(Context mContext, ArrayList<ResponseOrder> acceptedOrders, ArrayList<GetAllProducts> products) {
        COUNTER = 2;
        changedOrders = (IMoveChangesToAcceptedOrders) mContext;
        newData = new ArrayList<>();
        this.mContext = mContext;
        this.acceptedOrders = acceptedOrders;
        this.products = products;
        app = (App) this.mContext.getApplicationContext();
        loadNewData();
    }

    private void loadNewData() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    updateData();
                }
            }
        });
        thread.start();
    }

    public void updateData(){
        if (COUNTER == 2){
            manager = app.getManager();
            Disposable disposable1 = manager.getOrders()
                    .subscribe((ArrayList<ResponseOrder> responseOrders) -> {
                        for (ResponseOrder it : responseOrders) {
                            if (it.getStateId() == 3)
                                newData.add(it);
                        }
                        if (newData.size() != acceptedOrders.size()){
                            updateList(newData);
                            newData.clear();
                        }
                        else {
                            for (int i = 0; i < newData.size(); i++) {
                                if ( !(newData.get(i).toString().equals(acceptedOrders.get(i).toString()))){
                                    updateList(newData);
                                    newData.clear();

                                }
                            }
                            newData.clear();
                        }


                    });

        }
    }
    private void updateList(ArrayList<ResponseOrder> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallBack(acceptedOrders, newList));
        diffResult.dispatchUpdatesTo(this);
        acceptedOrders.clear();
        acceptedOrders.addAll(newList);
    }

    @Override
    public AcceptedItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.accepted_item, parent, false);

        return new AcceptedItemsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AcceptedItemsAdapter.MyViewHolder holder, final int position) {

          holder.tVTableName.setText(mContext.getResources().getString(R.string.orderNumber) + acceptedOrders.get(position).getTableName());
         
          SingleAcceptedItemAdapter adapter = new SingleAcceptedItemAdapter(mContext, (ArrayList<OrderItem>) acceptedOrders.get(position).getOrderItems(),products);
          holder.recyclerView.setLayoutManager(holder.manager);
          holder.recyclerView.setAdapter(adapter);
          holder.btnOk.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  COUNTER = 1;
                  if ((position + 1) <= acceptedOrders.size() && CHECK_CLICKABLE){
                      CHECK_CLICKABLE = false;
                  // mData.get(position).setStateId(10);;
                  App app = (App) mContext.getApplicationContext();
                  NetworkManager manager = app.getManager();
                  IService service = manager.getService();
                  service.sendOk(acceptedOrders.get(position), 11).enqueue(new Callback<Void>() {
                      @Override
                      public void onResponse(Call<Void> call, Response<Void> response) {
                          if (response.isSuccessful()){
                              Toast.makeText(mContext, "Order Completed :)", Toast.LENGTH_SHORT).show();
                              removeItem(position);
                              COUNTER = 2;
                              CHECK_CLICKABLE = true;
                          }
                          else{ Toast.makeText(mContext, "Completion isn't sent :(", Toast.LENGTH_SHORT).show();
                              removeItem(position);}
                              CHECK_CLICKABLE = true;
                      }

                      @Override
                      public void onFailure(Call<Void> call, Throwable t) {
                          Toast.makeText(mContext, "No Internet :(", Toast.LENGTH_SHORT).show();
                          CHECK_CLICKABLE = true;
                      }
                  });

              }}
          });



    }
    void removeItem(int position){
        acceptedOrders.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return acceptedOrders.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        RecyclerView.LayoutManager manager;
        Button btnOk;
        TextView tVTableName;


        MyViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.singleItemRecycler);
            manager = new LinearLayoutManager(itemView.getContext());
            btnOk = itemView.findViewById(R.id.button);
            tVTableName = itemView.findViewById(R.id.TVOrderNumber);
        }
    }

    public interface IMoveChangesToAcceptedOrders{
        public void moveDataToAcceptedOrders(ArrayList<ResponseOrder> orders);
    }
}
