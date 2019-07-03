package kitchen.eres.com.kitchen.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.activities.ROAMActivity;
import kitchen.eres.com.kitchen.adapters.IngredientAdapter;
import kitchen.eres.com.kitchen.adapters.SelectedIngredientItemAdapter;
import kitchen.eres.com.kitchen.events.EventRemoveItem;
import kitchen.eres.com.kitchen.events.EventUpdateEditProducts;
import kitchen.eres.com.kitchen.net.Pojos.roam.Ingredient;
import kitchen.eres.com.kitchen.net.Pojos.roam.ProdIngredient;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddIngredientFragment extends Fragment implements IngredientAdapter.MyInterface {
private Context mContext;
private App app;
//private RecyclerView ingredientsRV;
private ArrayList<Ingredient> ingredientsList;
    private IngredientAdapter adapter;
    private ArrayList<ProdIngredient> list;
    private SelectedIngredientItemAdapter adabterList;
    private RecyclerView recyclerView;
    private AppCompatSpinner spinner;

    private EditText eTWeight;
    private EditText eTName;
    private String selectedItem;
    private Button doneBtn;

    public AddIngredientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = container.getContext();
        EventBus.getDefault().register(this);
        return inflater.inflate(R.layout.fragment_add_ingredient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eTWeight = view.findViewById(R.id.eTWeight);
        eTName = view.findViewById(R.id.eTName);
        doneBtn = view.findViewById(R.id.btnAdd);
       // ingredientsRV = view.findViewById(R.id.RVImg);
        app = (App) mContext.getApplicationContext();
        app.getManager()
                .getService()
                .getIngredients()
                .enqueue(new Callback<ArrayList<Ingredient>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Ingredient>> call, Response<ArrayList<Ingredient>> response) {
                        setAdapter(response.body());
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Ingredient>> call, Throwable t) {

                    }
                });

        Button button = view.findViewById(R.id.button9);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendAddedProduct newProduct = app.getInstence();
                newProduct.setProdIngredients(list);

                removeFragment();
                ConstraintLayout constraintLayout = ((ROAMActivity) mContext).findViewById(R.id.container);
                constraintLayout.setVisibility(View.VISIBLE);
                ImageView imageView =  ((ROAMActivity) mContext).findViewById(R.id.ingredientDone);
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_done));


            }
        });

        spinner = view.findViewById(R.id.spinner17);
        ArrayList<String> units = new ArrayList<>();
        units.add("kg");
        units.add("gr");
        units.add("dona");
        units.add("litr");
        units.add("m.litr");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.spinner_item,units);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    setSelected(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setSelected(adapterView.getSelectedItem().toString());
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int unit = 0;
                if (!eTWeight.getText().toString().equals("")){
                    String value = eTWeight.getText().toString();
                    switch (selectedItem){
                        case "kg": unit =0;break;
                        case "gr": unit =1;break;
                        case "dona": unit =2;break;
                        case "litr": unit =3;break;
                        case "m.litr": unit =4;break;
                        default:

                    }
                    ProdIngredient prodIngredient = new ProdIngredient(0,0,0,Float.parseFloat(value),unit,new Ingredient(0,eTName.getText().toString(),""));
                    list.add(prodIngredient);
                    adabterList.notifyDataSetChanged();
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void myEventBus(EventRemoveItem products){
        list.remove(MyConfig.itemRemovePosition);
        adabterList.notifyDataSetChanged();
    }
    void removeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
    public void setAdapter(ArrayList<Ingredient> ingredients) {
        ingredientsList = ingredients;
        adapter = new IngredientAdapter(mContext,ingredientsList);
        adapter.setMyInterface(this);
        //ingredientsRV.setAdapter(adapter);

        list = new ArrayList<>();
        SendAddedProduct product = app.getInstence();
        if (product.getProdIngredients() != null){
            list.addAll(product.getProdIngredients());
        }
        recyclerView = ((ROAMActivity)mContext).findViewById(R.id.RVselectedItems);
        adabterList = new SelectedIngredientItemAdapter(mContext,list);
        recyclerView.setAdapter(adabterList);
    }

    @Override
    public void sendData(ProdIngredient ingredient) {

    }
    public void setSelected(String selected) {
        selectedItem = selected;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
