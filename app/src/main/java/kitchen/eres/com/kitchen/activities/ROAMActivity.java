package kitchen.eres.com.kitchen.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.adapters.AORMAdapter;
import kitchen.eres.com.kitchen.adapters.NewAddedMealsAdapter;
import kitchen.eres.com.kitchen.adapters.SpinnerLikeAdapter;
import kitchen.eres.com.kitchen.fragments.AddIngredientFragment;
import kitchen.eres.com.kitchen.helper.RecyclerItemClickListener;
import kitchen.eres.com.kitchen.net.NetworkManager;
import kitchen.eres.com.kitchen.net.Pojos.Category;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;
import kitchen.eres.com.kitchen.net.Pojos.roam.SpecialDesire;
import kitchen.eres.com.kitchen.net.models.SendImg;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ROAMActivity extends AppCompatActivity implements View.OnClickListener, SpinnerLikeAdapter.ISpinnerPressed {
    private RecyclerView recyclerView;
    private AORMAdapter adapter;
    private App app;
    private NetworkManager manager;
    private ArrayList<String> imgUrls;

    private String imgUrl = "";
    private Button addConfirm;

    private ConstraintLayout container;
    private ConstraintLayout container2;

    private AppCompatSpinner spinner;
    private int COUNTER = 0;
    private SendAddedProduct newAddedProduct;
    private String imgURL = null;
    private ArrayList<String> images;

    private RecyclerView RvAddedMeals;
    private NewAddedMealsAdapter newAddedMealsAdapter;

    private ProgressBar progressBar;
    private int resultCode;
    private Intent data;
    private TextView tVCount;
    private ImageView imgDown;
    private RecyclerView rvSpinner;
    SpinnerLikeAdapter spinnerLikeAdapter;
    ArrayList<String> spinnerLikeStrings;
    private  EditText desire;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roam);
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setAdapterOnClick();
        setAdapterRightSide();
    }

    private void setAdapterRightSide() {
        newAddedMealsAdapter = new NewAddedMealsAdapter(this,app.getAormList());
        RvAddedMeals.setAdapter(newAddedMealsAdapter);
    }

    private void setAdapterOnClick() {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imgUrl = imgUrls.get(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        addConfirm.setOnClickListener(this);
    }

    private void setAdapterToRecycler(ArrayList<String> list) {
        images = list;
        imgUrls.addAll(list);
        adapter = new AORMAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        app = (App) getApplication();
        RvAddedMeals = findViewById(R.id.RVAddedMeals);
        newAddedProduct = app.getInstence();
        spinner = findViewById(R.id.categorySpinner);
        container = findViewById(R.id.container);
        container2 = findViewById(R.id.container2);
        addConfirm =findViewById(R.id.button5);
        imgUrls = new ArrayList<>();
        recyclerView = findViewById(R.id.RVImg);
        recyclerView.setHasFixedSize(true);
        manager = app.getManager();
        tVCount = findViewById(R.id.tVCount);
        imgDown = findViewById(R.id.imgDown);
        toolbar = findViewById(R.id.toolbar3);
        workWithCustopSpinner();

        manager.getService()
                .getImgUrl().enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful())setAdapterToRecycler(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
        manager.getService()
                .getProductCategories().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if (response.isSuccessful())setSpinnerCategories(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

            }
        });

        addConfirm.setOnClickListener(this);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imgURL = images.get(position);
                MyConfig.chosenImgIndex = position;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        progressBar = findViewById(R.id.progressBar);
        progressBar = new android.widget.ProgressBar(
                this,
                null,
                android.R.attr.progressBarStyle);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.eres_back), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    private void workWithCustopSpinner() {
        spinnerLikeStrings = new ArrayList<>();
        spinnerLikeAdapter = new SpinnerLikeAdapter(this,spinnerLikeStrings);
        rvSpinner = findViewById(R.id.rvSpinner);
        rvSpinner.setAdapter(spinnerLikeAdapter);

    }



    private void  setSpinnerCategories(ArrayList<Category> categories){
        Toast.makeText(app, "" + categories.size(), Toast.LENGTH_SHORT).show();
        ArrayList<String> category = new ArrayList<>();
        category.add("Категория");
        for (Category item : categories) {
            category.add(item.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_2,category);
        adapter.setDropDownViewResource(R.layout.spinner_item_2);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    public void onClick(View view) {
            //  Toast.makeText(app, "Action Done", Toast.LENGTH_SHORT).show();
              EditText editText = findViewById(R.id.editText);
              EditText editText1 = findViewById(R.id.editText2);
              EditText eTPrice = findViewById(R.id.eTPrice);
        Toast.makeText(ROAMActivity.this, ""+(editText.getText().toString().length() >=2) + " "
                + (editText1.getText().toString().length() >= 2)
                + " "+(app.getInstence().getCategoryId()!=0), Toast.LENGTH_SHORT).show();

        if (editText.getText().toString().length() >=2
                      && editText1.getText().toString().length() >= 2
                      && app.getInstence().getCategoryId()!=0
                      && eTPrice.getText().toString().length() >= 2
                      && (imgURL.length() >= 2 || app.uniqueString.length() >= 2)){
                  ArrayList<SpecialDesire> specialDesires = new ArrayList<>();
                  for (String it : spinnerLikeStrings) {
                      SpecialDesire specialDesire = new SpecialDesire(0,it,newAddedProduct.getId());
                      specialDesires.add(specialDesire);
                  }
                  newAddedProduct.setSpecialDesires(specialDesires);

                  newAddedProduct.setPrice(Float.valueOf(eTPrice.getText().toString()));
                  newAddedProduct.setName(editText.getText().toString());
                  newAddedProduct.setDescription(editText1.getText().toString());
                  if (app.uniqueString.length() >= 2)
                      imgURL = app.uniqueString;
                  newAddedProduct.setImageUrl(imgURL);
                  app.setAormList(newAddedProduct);
                  app.setInstancesNull();

                  newAddedMealsAdapter.notifyDataSetChanged();
                  editText.setText("");
                  editText1.setText("");
                  desire.setText("");
                  eTPrice.setText("");
            spinnerLikeStrings.clear();
                  COUNTER = 0;
            tVCount.setText(COUNTER + "");
                  spinnerLikeAdapter.notifyDataSetChanged();
                  MyConfig.chosenImgIndex = -1;
                  adapter.notifyDataSetChanged();
                  ImageView imageView = findViewById(R.id.ingredientDone);
                  imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                  app.uniqueString = "";
              }else Toast.makeText(app, "Polyalarni to'ldiring !!!", Toast.LENGTH_SHORT).show();
    }

    public void addIngredient(View view) {
        container.setVisibility(View.GONE);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(container2.getId(),new AddIngredientFragment()).commit();

    }

    public void confirmAll(View view) {
        app.getManager()
                .getService()
                .sendNewAddedProducts(app
                .getAormList())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(app, "New Meals successfully added :)", Toast.LENGTH_SHORT).show();
                            app.clearData();
                            newAddedMealsAdapter.notifyDataSetChanged();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }

    public void addDesireToSpinner(View view) {
        desire = findViewById(R.id.editText5);
        if (!desire.getText().equals("")){

           spinnerLikeStrings.add(desire.getText().toString());
           spinnerLikeAdapter.notifyItemInserted(COUNTER);
           COUNTER++;
           tVCount.setText(COUNTER + "");
           desire.setText("");

       }

    }

    public void goAddingPag(View view) {
        startActivity(new Intent(this,ROAMActivity.class));
        finish();
    }

    public void goGallery(View view) {


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent,17);



    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
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
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
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
        images = list;
        imgUrls.addAll(list);
        adapter = new AORMAdapter(this,list);
        recyclerView.setAdapter(adapter);
        for (String it : list) {
            counter++;
            if (uuid.equals(it)){
                recyclerView.getLayoutManager().scrollToPosition(counter);}
                MyConfig.chosenImgIndex = counter;
            adapter.notifyDataSetChanged();
        }
    }

    public void showSpinner(View view) {
        rvSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void spinnerPressed(int i, int position) {
        if (i == 1){
            tVCount.setText(spinnerLikeStrings.get(position));
            rvSpinner.setVisibility(View.GONE);
        }else {
           if (position < spinnerLikeStrings.size()){
               spinnerLikeStrings.remove(position);
               spinnerLikeAdapter.notifyDataSetChanged();
               --COUNTER;
               tVCount.setText(COUNTER + "");
               rvSpinner.setVisibility(View.GONE);
           }
        }
    }

    class MyTask extends AsyncTask<Integer,Integer,Intent>{

        @Override
        protected Intent doInBackground(Integer... ints) {
            doItBackground();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Intent intent) {
            super.onPostExecute(intent);
            progressBar.setVisibility(View.GONE);
        }
    }
}
