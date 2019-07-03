package kitchen.eres.com.kitchen.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.adapters.NewOrdersCategoryAdapter;
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

import static kitchen.eres.com.kitchen.MyConfig.CHECK_CLICKABLE;
import static kitchen.eres.com.kitchen.MyConfig.OrderSelected;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecievedOrdersFragment extends Fragment implements SingleItemAdapter2.INotifyCancel {
    private ArrayList<ResponseOrder> newOrders;
    private ArrayList<GetAllProducts> allProducts;
    private RecyclerView rVOrderList;
    private RecyclerView rVSingleOrder;
    private NewOrdersCategoryAdapter categoryAdapter;
    private SingleItemAdapter2 singleOrderAdapter;
    private ArrayList<OrderItem> orderItems;
    private TextView tVTableName;
    private boolean isAcceptable;
    private App app;

    private TextView tVAccept;
    private TextView tVCancel;
    private static InotifyOrderAccepted InnernotifyOrderAccepted;
    private SwipeRefreshLayout refreshLayout;

    public static RecievedOrdersFragment newInstance(ArrayList<ResponseOrder> newOrders , ArrayList<GetAllProducts> allProducts) {
        RecievedOrdersFragment fragmentFirst = new RecievedOrdersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("someInt", newOrders);
        args.putParcelableArrayList("someTitle", allProducts);
        fragmentFirst.setArguments(args);
        return fragmentFirst;

    }

    public static void notifyOrderAccepted(InotifyOrderAccepted notifyOrderAccepted){
        InnernotifyOrderAccepted = notifyOrderAccepted;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAcceptable = true;
        newOrders = getArguments().getParcelableArrayList("someInt");
        allProducts = getArguments().getParcelableArrayList("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_recieved_orders, container, false);

   View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_recieved_orders,container,false);
   return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = (App)view.getContext().getApplicationContext();
        Dialog dialog = new Dialog(view.getContext());
        View view2 = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_confirm,null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view2);
        TextView tVNo = view2.findViewById(R.id.tVNo);
        TextView tVOk = view2.findViewById(R.id.tVOK);
        refreshLayout = view.findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        refreshLayout.setRefreshing(false);
                        InnernotifyOrderAccepted.refreshOrders();
                    }
                }, 300);
            }
        });
        tVNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getApp().dontUpdate = false;
                dialog.cancel();
            }
        });
        tVOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getApp().dontUpdate =false;
                dialog.cancel();
                if (MyConfig.OrderSelected < newOrders.size())
                    rejectOrder();
            }
        });



        if(newOrders.size() >= 1){
            rVOrderList = view.findViewById(R.id.rVOrderList);
            rVOrderList.setHasFixedSize(true);
            rVSingleOrder = view.findViewById(R.id.rVSingleOrder);
            tVTableName = view.findViewById(R.id.tVTableName);
            tVAccept = view.findViewById(R.id.tVAccept);
            tVCancel = view.findViewById(R.id.tVCancel);

            categoryAdapter = new NewOrdersCategoryAdapter(view.getContext(),newOrders);

            if (newOrders.size() >=1 && MyConfig.OrderSelected < newOrders.size()){
                tVTableName.setText(newOrders.get(MyConfig.OrderSelected).getWaiterName()+" - Cтол № " + newOrders.get(MyConfig.OrderSelected).getTableName());
                orderItems = (ArrayList<OrderItem>) newOrders.get(MyConfig.OrderSelected).getOrderItems();
                singleOrderAdapter = new SingleItemAdapter2(view.getContext(),orderItems,allProducts);
                singleOrderAdapter.setContexToInterface(this);
                rVSingleOrder.setAdapter(singleOrderAdapter);
            }

            rVOrderList.setAdapter(categoryAdapter);

            rVOrderList.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(), rVOrderList, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int oldItem = MyConfig.OrderSelected;
                    MyConfig.OrderSelected = position;
                    categoryAdapter.notifyItemChanged(oldItem);
                    Toast.makeText(view.getContext(), " Size = "+newOrders.get(0).getOrderItems().size(), Toast.LENGTH_SHORT).show();
                    tVTableName.setText(newOrders.get(MyConfig.OrderSelected).getWaiterName()+" - Cтол № " + newOrders.get(MyConfig.OrderSelected).getTableName());

//                    orderItems.clear();
//                    orderItems.addAll(newOrders.get(MyConfig.OrderSelected).getOrderItems());
                    ArrayList<OrderItem> subOrders = (ArrayList<OrderItem>) newOrders.get(position).getOrderItems();
                    singleOrderAdapter = new SingleItemAdapter2(view.getContext(),subOrders,allProducts);
                    singleOrderAdapter.setContexToInterface(RecievedOrdersFragment.this);
                    rVSingleOrder.setAdapter(singleOrderAdapter);
                    categoryAdapter.notifyItemChanged(position);
                    //EventBus.getDefault().post(new CategoryItemPressed());
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));

            tVAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    App.getApp().dontUpdate = false;
                   if (newOrders.size() >=1 && isAcceptable){
                       isAcceptable = false;
                       App app = (App)view.getContext().getApplicationContext();
                       NetworkManager manager = app.getManager();
                       IService service = manager.getService();

                       service.sendOk(newOrders.get(MyConfig.OrderSelected),7).enqueue(new Callback<Void>() {
                           @Override
                           public void onResponse(Call<Void> call, Response<Void> response) {
                               isAcceptable = true;
                               if (response.isSuccessful()){
                                   newOrders.remove(MyConfig.OrderSelected);
                                   categoryAdapter.notifyItemRemoved(OrderSelected);


                                   if (newOrders.size() != 0){
                                       if (newOrders.size() <= MyConfig.OrderSelected)MyConfig.OrderSelected = newOrders.size() -1;
                                       tVTableName.setText(newOrders.get(MyConfig.OrderSelected).getWaiterName()+" - Cтол № "+ newOrders.get(MyConfig.OrderSelected).getTableName());
                                       orderItems.clear();
                                       getOrderUp();
                                       orderItems.addAll(newOrders.get(MyConfig.OrderSelected).getOrderItems());
                                       singleOrderAdapter.notifyDataSetChanged();
                                       categoryAdapter.notifyItemChanged(OrderSelected);
                                   }else {
                                       tVTableName.setText("");
                                       orderItems.clear();
                                       singleOrderAdapter.notifyDataSetChanged();
                                   }
                               }
                               else Toast.makeText(view.getContext(), "Acception isn't sent :(", Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void onFailure(Call<Void> call, Throwable t) {
                               isAcceptable = true;
                               Toast.makeText(view.getContext(), "No Internet :(", Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
//                SingleItemsAdapter adapter = new SingleItemsAdapter(mContext,list2,products);
//                holder.itemRecycler.setLayoutManager(holder.manager);
//                holder.itemRecycler.setAdapter(adapter);

                }
            });
            tVCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.setContentView(view2);
                    dialog.show();


                }
            });

        }

    }

    void rejectOrder(){
        NetworkManager manager = app.getManager();
        IService service = manager.getService();
        int checkCount = 0;
        for (OrderItem it : orderItems) {
            if (it.getFactCount() == it.getCount())checkCount++;
        }
        if (orderItems.size() == checkCount){
            for (OrderItem it : orderItems) {
                it.setFactCount(it.getFactCount()-1);
            }
        }
        service.sendReject(newOrders.get(MyConfig.OrderSelected),5).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    newOrders.remove(MyConfig.OrderSelected);

                    if (newOrders.size() != 0){
                        if (newOrders.size() <= MyConfig.OrderSelected)MyConfig.OrderSelected = newOrders.size() -1;
                        tVTableName.setText(newOrders.get(MyConfig.OrderSelected).getWaiterName()+" - Cтол № " + newOrders.get(MyConfig.OrderSelected).getTableName());
                        orderItems.clear();
                        getOrderUp();
                        orderItems.addAll((ArrayList<OrderItem>) newOrders.get(MyConfig.OrderSelected).getOrderItems());
                        singleOrderAdapter.notifyDataSetChanged();
                        categoryAdapter.notifyDataSetChanged();
                    }else {
                        tVTableName.setText("");
                        orderItems.clear();
                        singleOrderAdapter.notifyDataSetChanged();
                        categoryAdapter.notifyDataSetChanged();

                    }

                }else {
                    Toast.makeText(app, "REJECTON not sent :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    void  getOrderUp(){
        InnernotifyOrderAccepted.notifyOrderAccepted();
//        rVSingleOrder.animate()
//                .translationY(-rVSingleOrder.getHeight())
//                .alpha(1.0f)
//                .setDuration(200)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        rVSingleOrder.setVisibility(View.INVISIBLE);
//                        InnernotifyOrderAccepted.notifyOrderAccepted();
////                        rVSingleOrder.setVisibility(View.VISIBLE);
////                        tVTableName.setVisibility(View.INVISIBLE);
//                       // loadNewData();
//                    }
//                });
    }

    private void loadNewData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               makeRvVIsible();
            }
        });
        thread.start();
    }

    private void makeRvVIsible() {
        rVSingleOrder.setVisibility(View.VISIBLE);
    }

    private void setTvZoomInOutAnimation(final TextView textView)
    {
        // TODO Auto-generated method stub
        Toast.makeText(app, "Bosildi ", Toast.LENGTH_SHORT).show();
        final float startSize = 28;
        final float endSize = 20;
        final int animationDuration = 1200; // Animation duration in ms

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                float animatedValue = (Float) valueAnimator.getAnimatedValue();
                textView.setTextSize(animatedValue);
            }
        });

        //animator.setRepeatCount(ValueAnimator.INFINITE);  // Use this line for infinite animations
        animator.setRepeatCount(0);
        animator.start();
    }

    @Override
    public void notifyCancel() {
           setTvZoomInOutAnimation(tVCancel);
           tVCancel.setText("Reject the selected");
    }


    public  interface  InotifyOrderAccepted{
        void notifyOrderAccepted();
        void refreshOrders();
    }
}
