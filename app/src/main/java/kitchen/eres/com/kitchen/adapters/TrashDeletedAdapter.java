package kitchen.eres.com.kitchen.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.R;
import kitchen.eres.com.kitchen.model.Product;

public class TrashDeletedAdapter  extends RecyclerView.Adapter<TrashDeletedAdapter.MyViewHolder>
        {


    private Context mContext;
    private ArrayList<Product> items;
    private onMoveEvent onMove;

    public TrashDeletedAdapter(Context mContext, ArrayList<Product> _items,TrashDeletedAdapter.onMoveEvent onMove) {
        this.mContext = mContext;
        this.items = _items;
        this.onMove=onMove;

    }
    @Override
    public TrashDeletedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.stop_item_right, parent, false);

        return new TrashDeletedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrashDeletedAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(""+items.get(position).getName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onMove!=null)
                    onMove.onClickMoveBotton(items.get(position));
            }
        });
    }
    public interface onMoveEvent
    {
        void onClickMoveBotton(Product product);

    }
    @Override
    public int getItemCount() {
        if(items==null) return 0;
        return items.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ConstraintLayout button;
        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.categoryTV);
            button = itemView.findViewById(R.id.constOnClick);

        }
    }


}
