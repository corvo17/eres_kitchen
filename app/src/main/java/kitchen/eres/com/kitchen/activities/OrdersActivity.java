package kitchen.eres.com.kitchen.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.events.CategoryItemPressed;
import kitchen.eres.com.kitchen.events.EventRemoveItem;
import kitchen.eres.com.kitchen.fragments.AcceptedOrdersFragment;
import kitchen.eres.com.kitchen.fragments.RecievedOrdersFragment;
import kitchen.eres.com.kitchen.helper.MyDiffCallBack;
import kitchen.eres.com.kitchen.helper.OrderViewPagerAdapter;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;
import kitchen.eres.com.kitchen.server.MyHasChangeListiner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity implements OrderViewPagerAdapter.UpdateOrderCount,MyHasChangeListiner, App.IMyPin, RecievedOrdersFragment.InotifyOrderAccepted, AcceptedOrdersFragment.InotifyOrderComplete {
    private OrderViewPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private android.support.v7.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private Disposable disposable1;
    private Disposable disposable2;
    private App app;
    private ArrayList<GetAllProducts> products;
    private int ordersCount = 0;
    private int newAcceptedOrCount = 0;
    private ArrayList<ResponseOrder> newRecievedOrders;
    private TextView badge1;
    private TextView badge2;
    private int page;
    int getOrdersCount = 0;
    private static final String TAG = "OrdersActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

       try{
           App.getApp().setListiner(this);
           App.setInterface(this);

           EventBus.getDefault().register(this);
           init();
           setSupportActionBar(toolbar);
           if(pagerAdapter == null){
               getOrders(-1);
           }


           try {
               // Creates a file in the primary external storage space of the
               // current application.
               // If the file does not exists, it is created.
               File testFile = new File(this.getExternalFilesDir(null), "sardorLog.txt");
               if (!testFile.exists())
                   testFile.createNewFile();

               // Adds a line to the file
               BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
               writer.write("0_Exception in thread \"main\" java.lang.NullPointerException\n" +
                       "    1_at ThrowsExecp.main(File.java:8)" +
                       "Exception in thread \"main\" java.lang.NullPointerException\n" +
                       "    at ThrowsExecp.main(File.java:8)" +
                       "Exception in thread \"main\" java.lang.NullPointerException\n" +
                       "    at ThrowsExecp.main(File.java:8)" );
               writer.close();
               // Refresh the data so it can seen when the device is plugged in a
               // computer. You may have to unplug and replug the device to see the
               // latest changes. This is not necessary if the user should not modify
               // the files.
               MediaScannerConnection.scanFile(this,
                       new String[]{testFile.toString()},
                       null,
                       null);
           } catch (IOException e) {
//               Toast.makeText(this, "Excepion yo'kuuuuuuu" ,
//                       Toast.LENGTH_LONG).show();
           }
       }catch (Exception e){
          writeExternalFile(e);
       }
    }

    private void getOrders(int id){
        disposable1 = app.getManager().getAllProducts()
                .subscribe(new Consumer<ArrayList<GetAllProducts>>() {
                    @Override
                    public void accept(ArrayList<GetAllProducts> productss) throws Exception {
                        setProducts(id, productss);
                    }
                });
    }
    private void setProducts(int id, ArrayList<GetAllProducts> productss) {
        products = productss;
        disposable2 = app.getManager().getOrders()
                .subscribe((ArrayList<ResponseOrder> mResponses) -> {
                    setAdapterToRec(id,mResponses, products);
                });
    }
    public void setAdapterToRec(int id, ArrayList<ResponseOrder> mResponses, ArrayList<GetAllProducts> products) {
       if (pagerAdapter != null)
           page = viewPager.getCurrentItem();
       newRecievedOrders.clear();
       newRecievedOrders.addAll(mResponses);
       pagerAdapter = new OrderViewPagerAdapter(this,getSupportFragmentManager(),products,newRecievedOrders);
       RecievedOrdersFragment.notifyOrderAccepted(this);
       AcceptedOrdersFragment.notifyOrderAccepted(this);
       viewPager.setAdapter(pagerAdapter);
       tabLayout.setupWithViewPager(viewPager);
       if (id == 3){
           MediaPlayer ring= MediaPlayer.create(OrdersActivity.this,R.raw.notification_souund);
           ring.start();
           Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
           vibrator.vibrate(500);
       }
       // if (id == 1)viewPager.setVerticalScrollbarPosition(2);
       badge1.setText(ordersCount + "");
       badge2.setText(newAcceptedOrCount + "");
       if (page == 1)viewPager.setCurrentItem(1);
    }

    private void init() {
        viewPager = findViewById(R.id.pager);
        toolbar = findViewById(R.id.toolBar);
        tabLayout = findViewById(R.id.tablayout);
        app = (App) getApplication();
        newRecievedOrders = new ArrayList<>();
        badge1 = findViewById(R.id.badge1);
        badge2 = findViewById(R.id.badge2);
    }

    public void goProfile(View view) {startActivity(new Intent(this, ProfileActivity.class));}

    public View getTabView(int position) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item,null);
        TextView title = view.findViewById(R.id.title);
        TextView  orderCount = view.findViewById(R.id.badge);
        ViewGroup layout = view.findViewById(R.id.layout);

        layout.setBackgroundResource(R.color.eres_back);
        if (position == 0){
            title.setText("Hовые заказы");
            orderCount.setText("" + ordersCount);
        }
        else {
            title.setText("Принятые заказы");
            orderCount.setText(""+newAcceptedOrCount);
        }

        return view;
    }


    @Override
    public void noticeOrdersCount(int newOrdersCount, int acceptedOrdersCount) {

        ordersCount = newOrdersCount;
        newAcceptedOrCount = acceptedOrdersCount;
       // tabLayout.getTabAt(0).setCustomView(getTabView(0));
       // tabLayout.getTabAt(1).setCustomView(getTabView(1));
    }

    @Override
    public void onChange(int id) {
       if (!App.getApp().dontUpdate){
           getOrders(id);
       }
           // page = viewPager.getCurrentItem();
            //pagerAdapter = new OrderViewPagerAdapter(this,getSupportFragmentManager(),products,newRecievedOrders);
            //viewPager.setAdapter(pagerAdapter);
            //if (page == 1)viewPager.setCurrentItem(1);

    }
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void myEventBus(CategoryItemPressed products){
        //getOrders(100);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable1 != null )disposable1.dispose();
        if (disposable2 != null )disposable2.dispose();
        EventBus.getDefault().unregister(this);
        }

    @Override
    public void checkServer() {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_check_server,null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        TextView tVNo = view.findViewById(R.id.tVNo);
        TextView tVOk = view.findViewById(R.id.tVOK);
        dialog.show();
        tVNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        tVOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrdersActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void notifyOrderAccepted() {
        getOrders(7);
    }

    @Override
    public void refreshOrderAccepted() {
        app.getManager().getService().getImgUrl()
                .enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        Dialog dialog = new Dialog(OrdersActivity.this);
                        View view = LayoutInflater.from(OrdersActivity.this).inflate(R.layout.dialog_check_server,null);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(view);
                        TextView tVNo = view.findViewById(R.id.tVNo);
                        TextView tVOk = view.findViewById(R.id.tVOK);
                        dialog.show();
                        tVNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                        tVOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(OrdersActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }
                });
    }

    @Override
    public void refreshOrders() {
        app.getManager().getService().getImgUrl()
                .enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        Dialog dialog = new Dialog(OrdersActivity.this);
                        View view = LayoutInflater.from(OrdersActivity.this).inflate(R.layout.dialog_check_server,null);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(view);
                        TextView tVNo = view.findViewById(R.id.tVNo);
                        TextView tVOk = view.findViewById(R.id.tVOK);
                        dialog.show();
                        tVNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                        tVOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(OrdersActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }
                });
    }

    void writeExternalFile(Exception exc){
        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.
            File testFile = new File(this.getExternalFilesDir(null), "LOGGG.txt");
            if (!testFile.exists())
                testFile.createNewFile();

            // Adds a line to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write("This is a test file." + exc.getMessage());
            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(this,
                    new String[]{testFile.toString()},
                    null,
                    null);
        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
        }
    }
}
