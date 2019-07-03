package kitchen.eres.com.kitchen.helper;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;

public class MyDiffCallBack extends DiffUtil.Callback {
    private ArrayList<ResponseOrder> oldOreders;
    private ArrayList<ResponseOrder> newOReders;

    public MyDiffCallBack(ArrayList<ResponseOrder> oldOreders, ArrayList<ResponseOrder> newOReders) {
        this.oldOreders = oldOreders;
        this.newOReders = newOReders;
    }

    @Override
    public int getOldListSize() {
        return oldOreders.size();
    }

    @Override
    public int getNewListSize() {
        return newOReders.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldOreders.get(oldItemPosition).getId() == newOReders.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldOreders.get(oldItemPosition).equals(newOReders.get(newItemPosition));
    }
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
