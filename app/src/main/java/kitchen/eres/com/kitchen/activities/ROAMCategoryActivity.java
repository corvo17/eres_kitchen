package kitchen.eres.com.kitchen.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.fragments.ROAMEditFragment;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;

public class ROAMCategoryActivity extends AppCompatActivity implements ROAMEditFragment.IBackPressed {
    private ConstraintLayout imgAdd;
    private ConstraintLayout imgEdit;
    private ConstraintLayout imgDash;
    private ConstraintLayout container;
    private Toolbar toolbar;
    private ArrayList<SendAddedProduct> editedProducts;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_roam);
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        editedProducts = new ArrayList<>();
        toolbar = findViewById(R.id.toolBar);
        imgAdd = findViewById(R.id.itemLeft);
        imgEdit = findViewById(R.id.itemMiddle);
        imgDash = findViewById(R.id.itemRIght);
        container = findViewById(R.id.container);
    }
    void makeInvisible(){
        imgAdd.setVisibility(View.INVISIBLE);
        imgEdit.setVisibility(View.INVISIBLE);
        imgDash.setVisibility(View.INVISIBLE);
    }


    public ArrayList<SendAddedProduct> getEditedProducts() {
        return editedProducts;
    }

    public void setEditedProducts(SendAddedProduct editedProduct) {
        editedProducts.add(editedProduct);
    }

    public void goAddingPage(View view) {
        startActivity(new Intent(this,ROAMActivity.class));
    }

    public void goEditPage(View view) {
        makeInvisible();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(container.getId(),new ROAMEditFragment(),null)
                .commit();
    }

    public void goDashboard(View view) {
    startActivity(new Intent(this, TrashActivity.class));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    public void backPressed(int i) {
        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        imgAdd.setVisibility(View.VISIBLE);
        imgEdit.setVisibility(View.VISIBLE);
        imgDash.setVisibility(View.VISIBLE);
    }
}
