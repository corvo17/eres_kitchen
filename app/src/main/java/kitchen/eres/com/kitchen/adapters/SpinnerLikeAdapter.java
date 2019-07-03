package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.R;

public class SpinnerLikeAdapter extends RecyclerView.Adapter<SpinnerLikeAdapter.MyViewHolder> {


    private Context mContext;
    private ArrayList<String> list;
    private ISpinnerPressed iSpinnerPressed;
    public SpinnerLikeAdapter(Context mContext,ISpinnerPressed iSpinnerPressed, ArrayList<String> list) {
        this.mContext = mContext;
        this.list = list;
        this.iSpinnerPressed = iSpinnerPressed;
    }
    public SpinnerLikeAdapter(Context mContext, ArrayList<String> list) {
        this.mContext = mContext;
        this.list = list;
        iSpinnerPressed = (ISpinnerPressed) mContext;
    }

    @Override
    public SpinnerLikeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_recycler_like_spinner, parent, false);

        return new SpinnerLikeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpinnerLikeAdapter.MyViewHolder holder, final int position) {
        holder.tVName.setText(list.get(position));
        holder.imgX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {iSpinnerPressed.spinnerPressed(-1, position);
            }
        });
        holder.tVName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSpinnerPressed.spinnerPressed(1, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgX;
        TextView tVName;

        MyViewHolder(View itemView) {
            super(itemView);

            imgX = itemView.findViewById(R.id.imgX);
            tVName = itemView.findViewById(R.id.tvName);

        }
    }
public interface ISpinnerPressed{
        void spinnerPressed(int i, int position);
    }

}