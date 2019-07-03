package kitchen.eres.com.kitchen.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import kitchen.eres.com.kitchen.R;

public class ProfileActivity extends AppCompatActivity {
   private android.support.v7.widget.Toolbar toolbar;
   private ConstraintLayout itemMiddle;
   private ConstraintLayout itemLeft;
   private ConstraintLayout itemRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,StopListActivity.class));
            }
        });
        itemLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,ROAMCategoryActivity.class));
            }
        });
        itemRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                finish();
            }
        });
    }



    private void init() {
     toolbar = findViewById(R.id.toolBarProfile);
     itemMiddle = findViewById(R.id.itemMiddle);
     itemLeft = findViewById(R.id.itemLeft);
     itemRight = findViewById(R.id.itemRIght);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}
