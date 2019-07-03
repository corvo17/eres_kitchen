package kitchen.eres.com.kitchen.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.activities.ROAMActivity;
import kitchen.eres.com.kitchen.activities.ROAMCategoryActivity;
import kitchen.eres.com.kitchen.adapters.AORMAdapter;
import kitchen.eres.com.kitchen.adapters.SpinnerLikeAdapter;
import kitchen.eres.com.kitchen.events.EventUpdateEditProducts;
import kitchen.eres.com.kitchen.helper.RecyclerItemClickListener;
import kitchen.eres.com.kitchen.net.NetworkManager;
import kitchen.eres.com.kitchen.net.Pojos.Category;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.roam.ProdIngredient;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;
import kitchen.eres.com.kitchen.net.Pojos.roam.SpecialDesire;
import kitchen.eres.com.kitchen.net.models.SendImg;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;
import static kitchen.eres.com.kitchen.MyConfig.editableProductId;
import static kitchen.eres.com.kitchen.MyConfig.isChecked;
import static kitchen.eres.com.kitchen.MyConfig.isEditImg;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProductFragment extends Fragment implements SpinnerLikeAdapter.ISpinnerPressed {

   private GetAllProducts product;
   private int resultCode;
   private Intent data;
    private static final String TAG = "EditProductFragment";
    private ArrayList<Category> mCategories;

    private Context mContext;
    private AppCompatSpinner spinnerCategory;
    private RecyclerView rvSpinner;
    private SpinnerLikeAdapter spinnerLikeAdapter;
    private TextView tVCount;
    private ImageView imgDown;
   // private Spinner spinnerDesire;
    private App app;
    private ArrayList<String> desires;
    private ArrayList<String> category;
    //private ArrayAdapter<String> adapterSpinner;
    private ArrayAdapter<String> adapter;
    private int COUNTER = 0;
    private EditText ETAddDesire;
    private ImageView imgAdd;
    private EditText ETName;
    private EditText ETDescription;
    private CompositeDisposable disposables;

    private ConstraintLayout addIngredient;
    private RecyclerView RVImgs;
    private ArrayList<String> listImgs;
    private  AORMAdapter adapterImg;
    private Button btnEdited;
    private  ArrayList<SendAddedProduct> needToSave;
    private EditText etPrice;
    private LinearLayout linearGallery;
    private String imgUrl;

    public EditProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();
                if(isChecked){
                    ImageView imageView =  view.findViewById(R.id.ingredientDone);
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_done));
                    isChecked = false;
                }
                rvSpinner = view.findViewById(R.id.rvSpinner);
        spinnerCategory = view.findViewById(R.id.categorySpinner);
        ETName = view.findViewById(R.id.ETName);
        ETDescription = view.findViewById(R.id.editText2);
        imgDown = view.findViewById(R.id.imgDown);
        etPrice = view.findViewById(R.id.eTPrice);
        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvSpinner.setVisibility(View.VISIBLE);
            }
        });
        app = (App)mContext.getApplicationContext();
        tVCount = view.findViewById(R.id.tVCount);
        app.getManager().getService()
                .getProductCategories()
                .enqueue(new Callback<ArrayList<Category>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            setSpinnerCategories(response.body());
                            app.getManager().getService()
                                    .getEditableProducts()
                                    .enqueue(new Callback<ArrayList<GetAllProducts>>() {
                                        @Override
                                        public void onResponse(Call<ArrayList<GetAllProducts>> call, Response<ArrayList<GetAllProducts>> response) {
                                            if (response.isSuccessful())getAllProductAndChoose(response.body());
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<GetAllProducts>> call, Throwable t) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                    }
                });
        ETAddDesire = view.findViewById(R.id.editText5);
        imgAdd = view.findViewById(R.id.imageView5);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ETAddDesire.getText().toString().equals("")){
                    desires.add(ETAddDesire.getText().toString());
                    ETAddDesire.setText("");
                    ++COUNTER;
                    spinnerLikeAdapter.notifyDataSetChanged();
                    ETAddDesire.setText("");
                }
            }
        });
        setSpinnerDesires();
        addIngredient = view.findViewById(R.id.editText3);
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout container = ((ROAMCategoryActivity)mContext).findViewById(R.id.containerEdit);
                FragmentTransaction transaction = ((ROAMCategoryActivity)mContext).getSupportFragmentManager().beginTransaction();
                transaction.replace(container.getId(),new EditIngredientFragment());
                transaction.commit();
            }
        });

        RVImgs = view.findViewById(R.id.RVImg);
        RVImgs.addOnItemTouchListener(new RecyclerItemClickListener(mContext, RVImgs, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imgUrl = listImgs.get(position);
                MyConfig.chosenImgIndex = position;
                adapterImg.notifyDataSetChanged();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        app.getManager().getService().getImgUrl().enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful())setAdapterToRVImg(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
        btnEdited = view.findViewById(R.id.button5);

        needToSave = new ArrayList<>();
        btnEdited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendAddedProduct sendAddedProduct = app.getInstence();

                if (ETName.getText().toString().length() >=2
                        && ETDescription.getText().toString().length() >= 2
                        && app.getInstence().getCategoryId()!=0){
                   // if (desires.size() > 0)desires.remove(0);
                    ArrayList<SpecialDesire> specialDesires = new ArrayList<>();
                    for (String it : desires) {
                        SpecialDesire specialDesire = new SpecialDesire(0,it,product.getId());
                        specialDesires.add(specialDesire);

                    }
                    sendAddedProduct.setImageUrl(imgUrl);
                    sendAddedProduct.setPrice(Float.parseFloat(etPrice.getText().toString()));
                    sendAddedProduct.setSpecialDesires(specialDesires);
                    sendAddedProduct.setName(ETName.getText().toString());
                    sendAddedProduct.setDescription(ETDescription.getText().toString());
                    needToSave.add(app.getInstence());
                    ((ROAMCategoryActivity)mContext).setEditedProducts(app.getInstence());

                    app.getManager().getService()
                            .sendNewAddedProducts(needToSave)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()){
                                        Toast.makeText(mContext, "Product successfully added :)", Toast.LENGTH_SHORT).show();
                                        removeIt();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                    //app.setEditedProducts(app.getInstence());
                    EventBus.getDefault().post(new EventUpdateEditProducts());
                    app.setInstancesNull();
                }else Toast.makeText(app, "Polyalarni to'ldiring !!!", Toast.LENGTH_SHORT).show();

            }
        });

        linearGallery = view.findViewById(R.id.linearGallery);
        linearGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goGallery();
            }
        });
    }

    public void goGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent,17);

    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        this.resultCode = resultCode;
        this.data = data;
        MyTask myTask = new MyTask();
        myTask.execute();
    }

    private void doItBackground() {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();
                UUID uuid = UUID.randomUUID();
                app.uniqueString = "images/" + uuid + ".png";
                final InputStream imageStream = mContext.getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap myBitmap = Bitmap.createScaledBitmap(selectedImage,100,100,false);


                // Bitmap bm = BitmapFactory.decodeFile(imagePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                byte[] byteArrayImage = baos.toByteArray();

                String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                app.getManager().getService()
                        .sendImgToServer(new SendImg(encodedImage),uuid + ".png")
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()){
                                    reloadImgs(uuid + "");
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void reloadImgs(String uuid) {
        NetworkManager manager = app.getManager();
        manager.getService()
                .getImgUrl().enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful())setAdapterToRecycler(response.body(),("images/"+uuid + ".png"));
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
    }
    private void setAdapterToRecycler(ArrayList<String> list, String uuid) {
        int counter = -1;
        listImgs = list;
        listImgs.addAll(list);
        adapterImg = new AORMAdapter(mContext,listImgs);
        RVImgs.setAdapter(adapterImg);
        for (String it : list) {
            counter++;
            if (uuid.equals(it)){
                RVImgs.getLayoutManager().scrollToPosition(counter);}
            MyConfig.chosenImgIndex = counter;
            adapterImg.notifyDataSetChanged();
        }
    }

    private void setAdapterToRVImg(ArrayList<String> body) {
        listImgs = new ArrayList<>();
        listImgs.addAll(body);
        adapterImg = new AORMAdapter(mContext,listImgs);
        RVImgs.setAdapter(adapterImg);
    }

    private void removeIt() {
        FragmentTransaction transaction = ((ROAMCategoryActivity)mContext).getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        LinearLayout linearLayout = ((ROAMCategoryActivity)mContext).findViewById(R.id.containerLinear);
        linearLayout.setVisibility(View.VISIBLE);
        transaction.commit();
    }

    private void getAllProductAndChoose(ArrayList<GetAllProducts> products) {

        for (GetAllProducts item : products ) {
            int sanachi = -1;
            if (editableProductId == item.getId()){
                product = item;
//                listImgs.add(0,product.getImageUrl());
                isEditImg = true;
                imgUrl = product.getImageUrl();
            //    adapterImg.notifyItemInserted(0);
                for (String img : desires){
                    sanachi ++ ;
                    //Toast.makeText(mContext, ""+product.getImageUrl(), Toast.LENGTH_SHORT).show();
                    if (product.getImageUrl().equals(img)){
                        RVImgs.setVerticalScrollbarPosition(sanachi);
                    }
                }

            }
        }SendAddedProduct product2 = app.getInstence();
        product2.setId(product.getId());
        product2.setImageUrl(product.getImageUrl());
        product2.setProdIngredients(product.getProdIngredients());


        HashMap<Integer,String> map = new HashMap<>();
        for (Category item : mCategories) {
            map.put(item.getId(),item.getName());
        }
        String name = "";
        String description = "";
        int categoryId = 0;
        ArrayList<SpecialDesire> desiresss = new ArrayList<>();
        for (GetAllProducts item : products ) {
            if (item.getId() == editableProductId){
                name = item.getName();
                description = item.getDescription();
                categoryId = item.getCategoryId();
                desiresss = item.getSpecialDesires();
                etPrice.setText("" + item.getPrice());
            }
        }
        ETName.setText(name);
        ETDescription.setText(description);
        category.add(0,map.get(categoryId));
        adapter.notifyDataSetChanged();
        desires.clear();
        COUNTER = desiresss.size();
        for (SpecialDesire it : desiresss ) {
            desires.add(it.getName());
        }
        tVCount.setText("" + desires.size());
        spinnerLikeAdapter.notifyDataSetChanged();
    }

    private void setSpinnerDesires() {
        desires = new ArrayList<>();
        spinnerLikeAdapter = new SpinnerLikeAdapter(mContext, this::spinnerPressed,desires);
        tVCount.setText("" + COUNTER);
        rvSpinner.setAdapter(spinnerLikeAdapter);
    }

    private void  setSpinnerCategories(ArrayList<Category> categories){
        mCategories = categories;
        Toast.makeText(app, "" + categories.size(), Toast.LENGTH_SHORT).show();
        category = new ArrayList<>();
        for (Category item : categories) {
            category.add(item.getName());
        }
        adapter = new ArrayAdapter<>(mContext, R.layout.spinner_item_2, category);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                for (Category item : categories ) {
                    if (item.getName().equals(category.get(i))){
                        SendAddedProduct product = app.getInstence();
                        product.setCategoryId(item.getId());
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public void spinnerPressed(int i, int position) {
        if (i == 1){
            tVCount.setText(desires.get(position));
            rvSpinner.setVisibility(View.GONE);
        }else {
            if (position < desires.size()){
                desires.remove(position);
                spinnerLikeAdapter.notifyDataSetChanged();
                --COUNTER;
                tVCount.setText(COUNTER + "");
                rvSpinner.setVisibility(View.GONE);
            }
        }
    }

    class MyTask extends AsyncTask<Integer,Integer,Intent> {

        @Override
        protected Intent doInBackground(Integer... ints) {
            doItBackground();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Intent intent) {
            super.onPostExecute(intent);
        }
    }
}
