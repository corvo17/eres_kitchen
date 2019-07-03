package kitchen.eres.com.kitchen.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.adapters.TrashDeletedAdapter;
import kitchen.eres.com.kitchen.adapters.TrashRecoverydAdapter;
import kitchen.eres.com.kitchen.helper.SpacesItemDecoration;
import kitchen.eres.com.kitchen.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrashActivity extends AppCompatActivity {

    private App app=null;
    private ArrayList<Product> deletedProducts=new ArrayList<>();
    private ArrayList<Product> recoveryProducts=new ArrayList<>();
    private RecyclerView deleteRV;
    private RecyclerView recoveryRV;
    private ArrayList<Product> deletedLis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        deleteRV=(RecyclerView)findViewById(R.id.deletedRV);
        //deleteRV.addItemDecoration(new SpacesItemDecoration(10));
        deleteRV.setLayoutManager(new LinearLayoutManager(this));
        recoveryRV=(RecyclerView)findViewById(R.id.recoveryRV);
        //recoveryRV.addItemDecoration(new SpacesItemDecoration(10));
        recoveryRV.setLayoutManager(new LinearLayoutManager(this));

        app = (App)this.getApplication();
        findViewById(R.id.backButton)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((Button)findViewById(R.id.clear_trash_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.getManager().getService().clearTrash().enqueue(new Callback<ArrayList<Product>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                        Init();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                        Toast.makeText(TrashActivity.this, "error on clear trash", Toast.LENGTH_LONG).show();
                        Init();
                    }
                });
            }
        });
        ((Button)findViewById(R.id.clear_restore_items_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (recoveryProducts.size()>0)
                {
                    Product product=recoveryProducts.get(0);
                    recoveryProducts.remove(product);
                    deletedProducts.add(product);
                }
                deleteRV.getAdapter().notifyDataSetChanged();
                recoveryRV.getAdapter().notifyDataSetChanged();

            }
        });
        ((Button)findViewById(R.id.restore_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recoveryProducts.size()==0)
                    return;
                ArrayList<Long> items=new ArrayList<Long>();
                for (Product product:recoveryProducts) {
                    items.add(product.getId());
                }
                app.getManager().getService().restoreFromTrash(items).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200)
                            Init();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(TrashActivity.this, "error on restored process", Toast.LENGTH_LONG).show();
                    }
                });
                while (recoveryProducts.size()>0)
                {
                    Product product=recoveryProducts.get(0);
                    recoveryProducts.remove(product);
                    deletedProducts.add(product);
                }
                deleteRV.getAdapter().notifyDataSetChanged();
                recoveryRV.getAdapter().notifyDataSetChanged();

            }
        });

        Init();
    }
    private void Init()
    {
        deletedProducts.clear();
        recoveryProducts.clear();
        deleteRV.setAdapter(new TrashDeletedAdapter(TrashActivity.this, deletedProducts, product -> {
            //Toast.makeText(TrashActivity.this, "move to right", Toast.LENGTH_LONG).show();
            recoveryProducts.add(product);
            deletedProducts.remove(product);
            deleteRV.getAdapter().notifyDataSetChanged();
            recoveryRV.getAdapter().notifyDataSetChanged();

        }));
        recoveryRV.setAdapter(new TrashRecoverydAdapter(TrashActivity.this, recoveryProducts, new TrashRecoverydAdapter.onMoveEvent() {
            @Override
            public void onClickMoveBotton(Product product) {
                //Toast.makeText(TrashActivity.this, "move to left", Toast.LENGTH_LONG).show();
                recoveryProducts.remove(product);
                deletedProducts.add(product);
                deleteRV.getAdapter().notifyDataSetChanged();
                recoveryRV.getAdapter().notifyDataSetChanged();

            }
        }));

                app.getManager().getService().getAllDeleted().enqueue(new Callback<ArrayList<Product>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                        if (response.code() == 200) {
                            TrashActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    deletedProducts.clear();
                                    for (Product pr : response.body()) {
                                        deletedProducts.add(pr);
                                    }
                                    deleteRV.getAdapter().notifyDataSetChanged();
                                }
                            });


                        } else
                            Toast.makeText(TrashActivity.this, "Error load deleted items", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                        Toast.makeText(TrashActivity.this, "Error load deleted items", Toast.LENGTH_LONG).show();
                    }
                });

    }
}
