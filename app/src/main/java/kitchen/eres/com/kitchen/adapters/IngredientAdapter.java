package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.App;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.activities.ROAMActivity;
import kitchen.eres.com.kitchen.net.Pojos.Unit;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;
import kitchen.eres.com.kitchen.net.Pojos.roam.Ingredient;
import kitchen.eres.com.kitchen.net.Pojos.roam.ProdIngredient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder> {
    private static final String TAG = "AcceptedItemsAdapter";
private MyInterface myInterface;

    public void setMyInterface(MyInterface myInterface) {
        this.myInterface = myInterface;
    }

    private Context mContext;
    private ArrayList<Ingredient> imgList;
    private String selectedItem;
    private App app;
    private ArrayAdapter<String> adapter;





    public IngredientAdapter(Context mContext, ArrayList<Ingredient> imgList) {
        this.imgList = imgList;
        this.mContext = mContext;
        App app = (App)mContext.getApplicationContext();
//        app.getManager().getService().getUnits()
//                .enqueue(new Callback<ArrayList<Unit>>() {
//                    @Override
//                    public void onResponse(Call<ArrayList<Unit>> call, Response<ArrayList<Unit>> response) {
//                        if (response.isSuccessful()){
//                            ArrayList<String> units = new ArrayList<>();
//                            for (Unit it : response.body()) {
//                                units.add(it.getName());
//                            }
//                            getUnits(units);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ArrayList<Unit>> call, Throwable t) {
//
//                    }
//                });
    }
//    void getUnits(ArrayList<String> units){
//        for (String it : units) {
//            myUnits.add(it);
//        }adapter.notifyDataSetChanged();
//    }


    @Override
    public IngredientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.single_ingridient_item, parent, false);

        return new IngredientAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.MyViewHolder holder, final int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_banked)
                .error(R.drawable.ic_banked)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(mContext)
                .load(MyConfig.BaseUrl+imgList.get(position).getImageUrl())
                .apply(options)
                .into(holder.imageView);

        ArrayList<String> unitsss = new ArrayList<>();
        unitsss.add("kg");
        unitsss.add("gr");
        unitsss.add("dona");
        unitsss.add("litr");
        Toast.makeText(mContext, "Litr Dona,.........", Toast.LENGTH_SHORT).show();
        unitsss.add("m.litr");
        adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item,unitsss);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSelected(adapterView.getSelectedItem().toString());
//                switch (i) {
//                    case 0:
//                        //holder.spinner.setBackgroundColor(Color.parseColor("#e6e7b4"));
//                        break;
//                    case 1:
//                        /holder.spinner.setBackgroundColor(Color.parseColor("#FFEDD0DA"));
//                        break;
//                    case 2:
//                        holder.price.setText("" + (0.85 * 200_000));
//                        holder.spinner.setBackgroundColor(Color.parseColor("#FFEDD0DA"));
//                        break;
//                    case 3:
//                        holder.price.setText("" + (0.8 * 200_000));
//                        holder.spinner.setBackgroundColor(Color.parseColor("#FFEDD0DA"));
//                        break;
//                    case 4:
//                        holder.price.setText("" + (0.75 * 200_000));
//                        holder.spinner.setBackgroundColor(Color.parseColor("#FFEDD0DA"));
//                        break;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setSelected(adapterView.getSelectedItem().toString());
            }
        });

    holder.doneImg.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int unit = 0;
            if (!holder.value.getText().toString().equals("")){
                String value = holder.value.getText().toString();
               switch (selectedItem){
                   case "kg": unit =1;break;
                   case "gr": unit =2;break;
                   case "dona": unit =3;break;
                   case "litr": unit =4;break;
                   case "m.litr": unit =5;break;

               }
                ProdIngredient prodIngredient = new ProdIngredient(0,0,imgList.get(position).getId(),Float.parseFloat(value),unit,imgList.get(position));

                myInterface.sendData(prodIngredient);
                notifyDataSetChanged();
            }
        }
    });
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public void setSelected(String selected) {
        selectedItem = selected;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;
        ImageView doneImg;
        EditText value;
        Spinner spinner;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

            doneImg = itemView.findViewById(R.id.imageView1);
            value = itemView.findViewById(R.id.ETValue);
            spinner = itemView.findViewById(R.id.spinner);

        }
    }

public interface MyInterface{
        void sendData(ProdIngredient ingredient);

}
}
