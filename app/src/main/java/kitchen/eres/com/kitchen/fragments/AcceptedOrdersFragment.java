package kitchen.eres.com.kitchen.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.adapters.AcceptedOrdersCategoryAdapter;
import kitchen.eres.com.kitchen.adapters.NewOrdersCategoryAdapter;
import kitchen.eres.com.kitchen.adapters.SingleAcceptedItemAdapter;
import kitchen.eres.com.kitchen.adapters.SingleItemAdapter2;
import kitchen.eres.com.kitchen.events.CategoryItemPressed;
import kitchen.eres.com.kitchen.helper.RecyclerItemClickListener;
import kitchen.eres.com.kitchen.net.IService;
import kitchen.eres.com.kitchen.net.NetworkManager;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.OrderItem;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kitchen.eres.com.kitchen.MyConfig.AcceptedOrderSelected;
import static kitchen.eres.com.kitchen.MyConfig.CHECK_CLICKABLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcceptedOrdersFragment extends Fragment {
    private ArrayList<ResponseOrder> acceptedOrders;
    private ArrayList<GetAllProducts> allProducts;

    private RecyclerView rVSingleOrderAccept;
    private AcceptedOrdersCategoryAdapter categoryAdapterAccept;
    private SingleAcceptedItemAdapter singleOrderAdapterAccept;
    //private ResponseOrder responseOrderAccept;
    ArrayList<OrderItem> orderItems;
    private TextView tVTableName;
    private TextView tVOrderComplete;
    private static final String TAG = "AcceptedOrdersFragment";
    private boolean isCompletable ;
    private static InotifyOrderComplete InnernotifyOrderAccepted;
    private SwipeRefreshLayout refreshLayout;

    public static AcceptedOrdersFragment newInstance(ArrayList<ResponseOrder> acceptedOrders, ArrayList<GetAllProducts> allProducts) {
        AcceptedOrdersFragment fragmentFirst = new AcceptedOrdersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("someInt", acceptedOrders);
        args.putParcelableArrayList("someTitle", allProducts);
        fragmentFirst.setArguments(args);
        return fragmentFirst;

    }
    public static void notifyOrderAccepted(InotifyOrderComplete notifyOrderAccepted){
        InnernotifyOrderAccepted = notifyOrderAccepted;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCompletable = true;
        acceptedOrders = getArguments().getParcelableArrayList("someInt");
        allProducts = getArguments().getParcelableArrayList("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_accepted_orders, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view111, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view111, savedInstanceState);
        if (acceptedOrders.size() >= 1 && AcceptedOrderSelected < acceptedOrders.size() ) {
            RecyclerView rVOrderListAccept = view111.findViewById(R.id.rVOrderListAccept);
            rVOrderListAccept.setHasFixedSize(true);
            rVSingleOrderAccept = view111.findViewById(R.id.rVSingleOrderAccept);
            tVTableName = view111.findViewById(R.id.tVTableNameAccept);
            tVOrderComplete = view111.findViewById(R.id.tVAcceptAccept);
            refreshLayout = view111.findViewById(R.id.refresh);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            // Stop animation (This will be after 3 seconds)
                            refreshLayout.setRefreshing(false);
                            InnernotifyOrderAccepted.refreshOrderAccepted();
                        }
                    }, 300);
                }
            });

            categoryAdapterAccept = new AcceptedOrdersCategoryAdapter(view111.getContext(), acceptedOrders);
            rVOrderListAccept.setAdapter(categoryAdapterAccept);
            Log.d(TAG, "onViewCreated:acceptedOrder size  " + acceptedOrders.size());

            tVTableName.setText(acceptedOrders.get(AcceptedOrderSelected).getWaiterName() +" - Cтол № " + acceptedOrders.get(AcceptedOrderSelected).getTableName());
            orderItems = (ArrayList<OrderItem>) acceptedOrders.get(AcceptedOrderSelected).getOrderItems();
            singleOrderAdapterAccept = new SingleAcceptedItemAdapter(view111.getContext(), orderItems, allProducts);
            rVSingleOrderAccept.setAdapter(singleOrderAdapterAccept);


            rVOrderListAccept.addOnItemTouchListener(new RecyclerItemClickListener(view111.getContext(), rVOrderListAccept, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    int oldItem = AcceptedOrderSelected;
                    AcceptedOrderSelected = position;
                    categoryAdapterAccept.notifyItemChanged(oldItem);
                    categoryAdapterAccept.notifyItemChanged(position);

                    tVTableName.setText(acceptedOrders.get(position).getWaiterName() +" - Cтол № " + acceptedOrders.get(position).getTableName());

                           // orderItems.clear();
//                            singleOrderAdapterAccept.notifyDataSetChanged();
//                            orderItems.addAll(acceptedOrders
//                                    .get(position).getOrderItems());
//                            singleOrderAdapterAccept.notifyDataSetChanged();

                            ArrayList<OrderItem> subOrders = (ArrayList<OrderItem>) acceptedOrders
                                    .get(position).getOrderItems();
                            singleOrderAdapterAccept = new SingleAcceptedItemAdapter(view.getContext(),subOrders,allProducts);
                            rVSingleOrderAccept.setAdapter(singleOrderAdapterAccept);
                   // EventBus.getDefault().post(new CategoryItemPressed());

                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
            tVOrderComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // mData.get(position).setStateId(10);;
                   if(acceptedOrders.size() >= 1){
                       for (OrderItem it : acceptedOrders.get(AcceptedOrderSelected).getOrderItems()) {
                           if (it.getStateId() == 4)isCompletable = false;
                       }
                   }
               if (acceptedOrders.size() >= 1 && isCompletable ){
                   isCompletable = false;
                   App app = (App) view.getContext().getApplicationContext();
                   NetworkManager manager = app.getManager();
                   IService service = manager.getService();
                   service.sendOk(acceptedOrders.get(AcceptedOrderSelected), 11).enqueue(new Callback<Void>() {
                       @Override
                       public void onResponse(Call<Void> call, Response<Void> response) {
                           isCompletable = true;
                           if (response.isSuccessful()) {
                               acceptedOrders.remove(AcceptedOrderSelected);
                               categoryAdapterAccept.notifyItemRemoved(AcceptedOrderSelected);

//                                for (ResponseOrder it : acceptedOrders) {
//                                    categoryAdapterAccept.notifyItemChanged(notifier);
//                                }
                               if (acceptedOrders.size() != 0) {
                                   if (acceptedOrders.size() <= MyConfig.AcceptedOrderSelected)
                                       MyConfig.AcceptedOrderSelected = (acceptedOrders.size() - 1);
                                   orderItems.clear();
                                   getOrderUp();
                                   orderItems.addAll(acceptedOrders.get(AcceptedOrderSelected).getOrderItems());
                                   singleOrderAdapterAccept.notifyDataSetChanged();
                                   tVTableName.setText(acceptedOrders.get(AcceptedOrderSelected).getWaiterName() +" - Cтол № " + acceptedOrders.get(MyConfig.AcceptedOrderSelected).getTableName());
                                   // categoryAdapterAccept.notifyDataSetChanged();
                                   categoryAdapterAccept.notifyItemChanged(AcceptedOrderSelected);
                               } else {
                                   tVTableName.setText("");
                                   orderItems.clear();
                                   singleOrderAdapterAccept.notifyDataSetChanged();
                                   //categoryAdapterAccept.notifyDataSetChanged();
                               }

                           } else {
                               Toast.makeText(view.getContext(), "Completion isn't sent :(", Toast.LENGTH_SHORT).show();
                           }
                       }

                       void  getOrderUp(){
                           InnernotifyOrderAccepted.notifyOrderAccepted();
//                           rVSingleOrderAccept.animate()
//                                   .translationY(-rVOrderListAccept.getHeight())
//                                   .alpha(1.0f)
//                                   .setDuration(700)
//                                   .setListener(new AnimatorListenerAdapter() {
//                                       @Override
//                                       public void onAnimationEnd(Animator animation) {
//                                           super.onAnimationEnd(animation);
//                                           rVSingleOrderAccept.setVisibility(View.INVISIBLE);
//                                           //tVTableName.setVisibility(View.INVISIBLE);
//                                           try {
//                                               Thread.sleep(300);
//                                           } catch (InterruptedException e) {
//                                               e.printStackTrace();
//                                           }
//                                           rVOrderListAccept.setVisibility(View.VISIBLE);
//                                       }
//                                   });
                       }
                       @Override
                       public void onFailure(Call<Void> call, Throwable t) {
                           isCompletable = true;
                           Toast.makeText(view.getContext(), "No Internet :(", Toast.LENGTH_SHORT).show();
                       }
                   });
               }

                }
            });

        }
    }
    public  interface  InotifyOrderComplete{
        void notifyOrderAccepted();
        void refreshOrderAccepted();
    }

}
