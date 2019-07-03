package kitchen.eres.com.kitchen.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.adapters.StopListItemAdapterLeft;
import kitchen.eres.com.kitchen.adapters.StopListItemAdapterRight;
import kitchen.eres.com.kitchen.adapters.TopCategoryAdapter;
import kitchen.eres.com.kitchen.helper.RecyclerItemClickListener;
import kitchen.eres.com.kitchen.net.Pojos.Category;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kitchen.eres.com.kitchen.MyConfig.row_index;

public class StopListActivity extends AppCompatActivity  {
    private Toolbar toolbar;
    private RecyclerView recyclerLeft;
    private RecyclerView recyclerRight;
    private RecyclerView recyclerCategories;
    private static final String TAG = "StopListActivity";
    private StopListItemAdapterLeft adapterLeft;
    private StopListItemAdapterRight adapterRight;
    private ArrayList<GetAllProducts> allProducts1;
    private ArrayList<GetAllProducts> allProducts2;
    private TopCategoryAdapter adapter;
    private App app;
    private ArrayList<GetAllProducts> allProducts;
    private Disposable disposable;
    private ArrayList<Category> productCategories;

    private EditText etSearch;
    private ArrayList<GetAllProducts> searchableProducts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);

        init();
        getDataAndSet();

    }
    private void getAllProducts(){
        disposable = app.getManager().getAllProducts()
                .subscribe(new Consumer<ArrayList<GetAllProducts>>() {
                    @Override
                    public void accept(ArrayList<GetAllProducts> products) throws Exception {
                        allProducts = products;
                        loadProducts(products);
                    }
                });
    }

    private void getDataAndSet() {
        allProducts = new ArrayList<>();
        allProducts1 = new ArrayList<>();
        productCategories = new ArrayList<>();

        allProducts2 = new ArrayList<>();
        app.getManager().getService()
                .getProductCategories()
                .enqueue(new Callback<ArrayList<Category>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                        if (response.isSuccessful()){
                            productCategories = response.body();
                            getAllProducts();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                    }
                });

    }

       void loadProducts(ArrayList<GetAllProducts> mProducts){
           for (GetAllProducts product : mProducts ) {
               if (product.getExcluded() == 0 )
                   allProducts1.add(product);
               if (product.getExcluded() == 1 )
                   allProducts2.add(product);

               adapterLeft = new StopListItemAdapterLeft(this,allProducts1);
               recyclerLeft.setAdapter(adapterLeft);

               adapterRight = new StopListItemAdapterRight(this,allProducts2);
               recyclerRight.setAdapter(adapterRight);


               adapter = new TopCategoryAdapter(this,mProducts,allProducts,productCategories);
               recyclerCategories.setAdapter(adapter);
               recyclerCategories.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerCategories, new RecyclerItemClickListener.OnItemClickListener() {
                   @Override
                   public void onItemClick(View view, int position) {
                       categoriyItemSelected(view,position);
                   }

                   @Override
                   public void onLongItemClick(View view, int position) {

                   }
               }));

               recyclerLeft.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerLeft, new RecyclerItemClickListener.OnItemClickListener() {
                   @Override
                   public void onItemClick(View view, int position) {

                       onStopItemClicked(position);
                   }

                   @Override
                   public void onLongItemClick(View view, int position) {

                   }
               }));
               recyclerRight.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerLeft, new RecyclerItemClickListener.OnItemClickListener() {
                   @Override
                   public void onItemClick(View view, int position) {
                       onStopItemClickedRe(position);
                   }

                   @Override
                   public void onLongItemClick(View view, int position) {

                   }
               }));

           }
       }

    private void categoriyItemSelected(View view, int position) {
        CardView cardView1 = (CardView) view;
        TextView textView = view.findViewById(R.id.categoryTV);
        textView.setText("Corvo");

                ArrayList<GetAllProducts> selectedProducts = new ArrayList<>();
                allProducts1.clear();
               // allProducts2.clear();
                for (GetAllProducts item : allProducts) {
                    if (item.getCategoryId() == position+1)
                        selectedProducts.add(item);
                }
                for (GetAllProducts item : selectedProducts) {
                    if (item.getExcluded() == 0){
                        allProducts1.add(item);
                    }//else allProducts2.add(item);
                }
                adapterLeft.notifyDataSetChanged();
               // adapterRight.notifyDataSetChanged();
                recyclerLeft.setAdapter(adapterLeft);
               // recyclerRight.setAdapter(adapterRight);
                cardView1.setCardBackgroundColor(getBaseContext().getResources().getColor(R.color.colorPressed));
                row_index = position;
                adapter.notifyDataSetChanged();

    }

    void onStopItemClicked (int position){
        allProducts1.get(position).setExcluded(1);
        for (GetAllProducts item : allProducts) {
            if (item.getId() == allProducts1.get(position).getId())item.setExcluded(1);
        }

         app
                .getManager()
                .getService()
                .addStopList(allProducts1.get(position).getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful())Toast.makeText(app, "added STOP list :)", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(app, "couldn;t add STOP list :(", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
        allProducts2.add(allProducts1.get(position));
        allProducts1.remove(position);

        adapterLeft.notifyItemRemoved(position);
        adapterRight.notifyDataSetChanged();

       // getDataAndSet();
    }
    void onStopItemClickedRe (int position){
        allProducts2.get(position).setExcluded(0);
        for (GetAllProducts item : allProducts) {
            if (item.getId() == allProducts2.get(position).getId())item.setExcluded(0);
        }

         app
                .getManager()
                .getService()
                .removeStopList(allProducts2.get(position).getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful())Toast.makeText(app, "removed STOP list :)", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(app, "can't remove STOP list :(", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
        allProducts1.add(allProducts2.get(position));
        allProducts2.remove(position);

        adapterRight.notifyItemRemoved(position);
        adapterLeft.notifyDataSetChanged();
        // getDataAndSet();
    }

    private void init() {
        app = (App)this.getApplication();
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerLeft = findViewById(R.id.stopListLeft);
        recyclerRight = findViewById(R.id.stopListRight);
        recyclerCategories = findViewById(R.id.recycler_top_buttons);
        etSearch = findViewById(R.id.eTSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                allProducts1.clear();
                allProducts2.clear();
                searchableProducts = new ArrayList<>();
                for (GetAllProducts item : allProducts) {
                    if (item.getExcluded() == 0){
                        searchableProducts.add(item);
                    }else allProducts2.add(item);
                }
                allProducts1.clear();
                for (GetAllProducts it : searchableProducts) {
                    if (it.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        allProducts1.add(it);
                    }
                }

                adapterLeft.notifyDataSetChanged();
                adapterRight.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null)disposable.dispose();
    }

    public void deleteAll(View view) {
        ArrayList<Long> longArrayList = new ArrayList<>();
        for (GetAllProducts product : allProducts2){
            longArrayList.add((long) product.getId());
        }app.getManager()
                .getService()
                .removeAllStopList(longArrayList)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(app, "Removed all items :)", Toast.LENGTH_SHORT).show();
                            allProducts.remove(allProducts2);
                            for (GetAllProducts it : allProducts2) {
                                it.setExcluded(0);
                            }allProducts.addAll(allProducts2);
                            allProducts1.addAll(allProducts2);
                            adapterLeft.notifyDataSetChanged();
                            allProducts2.clear();
                            adapterRight.notifyDataSetChanged();
                        }
                        else Toast.makeText(app, "cannot removed :(", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
