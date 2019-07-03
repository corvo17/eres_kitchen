package kitchen.eres.com.kitchen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.activities.ROAMActivity;
import kitchen.eres.com.kitchen.activities.ROAMCategoryActivity;
import kitchen.eres.com.kitchen.adapters.IngredientAdapter;
import kitchen.eres.com.kitchen.adapters.SelectedIngredientItemAdapter;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.roam.Ingredient;
import kitchen.eres.com.kitchen.net.Pojos.roam.ProdIngredient;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditIngredientFragment extends Fragment implements IngredientAdapter.MyInterface {
    private Context mContext;
    private App app;
    private RecyclerView ingredientsRV;
    private SearchView searchView;
    private ArrayList<Ingredient> ingredientsList;
    private IngredientAdapter adapter;
    private ArrayList<ProdIngredient> list;
    private SelectedIngredientItemAdapter adabterList;
    private RecyclerView recyclerView;

    private EditText eTWeight;
    private EditText eTName;
    private String selectedItem;
    private Button doneBtn;
    private AppCompatSpinner spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = container.getContext();
        return inflater.inflate(R.layout.fragment_add_ingredient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(mContext, "CorvoAttano EditMealsAdapter", Toast.LENGTH_SHORT).show();

        eTWeight = view.findViewById(R.id.eTWeight);
        eTName = view.findViewById(R.id.eTName);
        doneBtn = view.findViewById(R.id.btnAdd);
        spinner = view.findViewById(R.id.spinner17);
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

        ArrayList<String> units = new ArrayList<>();
        units.add("kg");
        units.add("gr");
        units.add("dona");
        units.add("litr");
        units.add("m.litr");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext,R.layout.spinner_item,units);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter2);
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

//        searchView = view.findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                for (Ingredient it : ingredientsList) {
//                    if (!it.getName().contains(newText)){
//                        ingredientsList.remove(it);
//                        adapter.notifyDataSetChanged();
//
//                    }
//                }
//                return false;
//            }
//        });


        ingredientsRV = view.findViewById(R.id.RVselectedItems);
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
                MyConfig.isChecked = true;
                removeFragment();

//                ImageView imageView =  ((ROAMCategoryActivity) mContext).findViewById(R.id.ingredientDone);
//                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_done));
            }
        });
    }

    private void setSelected(String s) {
        selectedItem = s;
    }

    void removeFragment(){
        FrameLayout container = ((ROAMCategoryActivity)mContext).findViewById(R.id.containerEdit);
        getActivity().getSupportFragmentManager().beginTransaction().replace(container.getId(),new EditProductFragment()).commit();
    }
    public void setAdapter(ArrayList<Ingredient> ingredients) {
        ingredientsList = ingredients;
        adapter = new IngredientAdapter(mContext,ingredientsList);
        adapter.setMyInterface(this);
        ingredientsRV.setAdapter(adapter);

        list = new ArrayList<>();
        SendAddedProduct product = app.getInstence();
        if (product.getProdIngredients() != null)
            list.addAll(product.getProdIngredients());
        recyclerView = ((ROAMCategoryActivity)mContext).findViewById(R.id.RVselectedItems);
        adabterList = new SelectedIngredientItemAdapter(mContext,list);
        recyclerView.setAdapter(adabterList);
    }

    @Override
    public void sendData(ProdIngredient ingredient) {
        list.add(ingredient);
        adabterList.notifyDataSetChanged();
    }
}
