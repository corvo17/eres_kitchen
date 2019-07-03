package kitchen.eres.com.kitchen.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.activities.ROAMCategoryActivity;
import kitchen.eres.com.kitchen.adapters.EditMeadlsAdapter;
import kitchen.eres.com.kitchen.adapters.EditedProductsAdapter;
import kitchen.eres.com.kitchen.adapters.StopListItemAdapterLeft;
import kitchen.eres.com.kitchen.adapters.StopListItemAdapterRight;
import kitchen.eres.com.kitchen.adapters.TopCategoryAdapter;
import kitchen.eres.com.kitchen.events.EventUpdateEditProducts;
import kitchen.eres.com.kitchen.helper.RecyclerItemClickListener;
import kitchen.eres.com.kitchen.net.Pojos.Category;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static kitchen.eres.com.kitchen.MyConfig.row_index;

/**
 * A simple {@link Fragment} subclass.
 */
public class ROAMEditFragment extends Fragment {
    Context mContext;
    private Toolbar toolbar;
    private RecyclerView recyclerLeft;
    private RecyclerView recyclerCategories;
    private EditMeadlsAdapter adapterLeft;
    private TopCategoryAdapter adapter;
    private App app;
    private ArrayList<GetAllProducts> allProducts;
    private Disposable disposable;
    private ArrayList<Category> productCategories;
    private EditedProductsAdapter editedProductsAdapter;
    private RecyclerView rvEditedProducts;
    private ImageView imgBack;
    private IBackPressed iBackPressed;
    private Button buttonConfirm;


    public ROAMEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EventBus.getDefault().register(this);
        return inflater.inflate(R.layout.fragment_roamedit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();
        initViews(view);
        getDataAndSet();
    }

    private void initViews( View vIew) {

        imgBack = vIew.findViewById(R.id.backPressed);
        app = (App)((ROAMCategoryActivity) mContext).getApplication();
        toolbar = ((ROAMCategoryActivity) mContext).findViewById(R.id.toolbar2);
        ((ROAMCategoryActivity) mContext).setSupportActionBar(toolbar);
        recyclerLeft = ((ROAMCategoryActivity) mContext).findViewById(R.id.editProducts);
        recyclerCategories = ((ROAMCategoryActivity) mContext).findViewById(R.id.recycler_top_buttons);
        ArrayList<SendAddedProduct> list  = ((ROAMCategoryActivity) mContext).getEditedProducts();
        editedProductsAdapter = new EditedProductsAdapter(mContext, list);
        rvEditedProducts = ((ROAMCategoryActivity) mContext).findViewById(R.id.editedProducts);
        rvEditedProducts.setAdapter(editedProductsAdapter);
        buttonConfirm = vIew.findViewById(R.id.button5);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iBackPressed = (IBackPressed) mContext;
                iBackPressed.backPressed(1);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iBackPressed = (IBackPressed) mContext;
                iBackPressed.backPressed(1);
            }
        });

    }

    private void getDataAndSet() {
        allProducts = new ArrayList<>();
        productCategories = new ArrayList<>();

        app.getManager().getService()
                .getProductCategories()
                .enqueue(new Callback<ArrayList<Category>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                        if (response.isSuccessful()){
                            productCategories = response.body();
                            disposable = app.getManager().getAllProducts()
                                    .subscribe(new Consumer<ArrayList<GetAllProducts>>() {
                                        @Override
                                        public void accept(ArrayList<GetAllProducts> products) throws Exception {
                                            allProducts = products;
                                            loadProducts(products);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                    }
                });

    }

    void loadProducts(ArrayList<GetAllProducts> mProducts){
        for (GetAllProducts product : mProducts ) {
            adapterLeft = new EditMeadlsAdapter(mContext,mProducts);
            recyclerLeft.setAdapter(adapterLeft);

            adapter = new TopCategoryAdapter(mContext,mProducts,allProducts,productCategories);
            recyclerCategories.setAdapter(adapter);
            recyclerCategories.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerCategories, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    categoriyItemSelected(view,position);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));

        }
    }

    private void categoriyItemSelected(View view, int position) {

        ArrayList<GetAllProducts> selectedProducts = new ArrayList<>();
        for (GetAllProducts item : allProducts) {
            if (item.getCategoryId() == position+1)
                selectedProducts.add(item);
        }
        adapterLeft = new EditMeadlsAdapter(mContext,selectedProducts);
        adapterLeft.notifyDataSetChanged();
       // adapterRight.notifyDataSetChanged();
        recyclerLeft.setAdapter(adapterLeft);
        //recyclerRight.setAdapter(adapterRight);
        row_index = position;
        adapter.notifyDataSetChanged();

    }
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void myEventBus(EventUpdateEditProducts products){
        editedProductsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               iBackPressed.backPressed(1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (disposable != null)disposable.dispose();
        EventBus.getDefault().unregister(this);
    }
    public  interface  IBackPressed{
        void backPressed(int t);
    }
}
