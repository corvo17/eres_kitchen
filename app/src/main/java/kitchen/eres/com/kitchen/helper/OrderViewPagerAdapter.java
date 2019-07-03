package kitchen.eres.com.kitchen.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.fragments.AcceptedOrdersFragment;
import kitchen.eres.com.kitchen.fragments.RecievedOrdersFragment;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;

public  class OrderViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_ITEMS = 2;
    private ArrayList<GetAllProducts> allProducts;
    private ArrayList<ResponseOrder> allOrders;
    private ArrayList<ResponseOrder> newDataOrders;
    private ArrayList<ResponseOrder> newAcceptedOrders;
    public UpdateOrderCount updateOrderCount;

    public OrderViewPagerAdapter(Context context,FragmentManager fm, ArrayList<GetAllProducts> allProducts, ArrayList<ResponseOrder> allOrders) {
        super(fm);
        updateOrderCount = (UpdateOrderCount) context;
        this.allProducts = allProducts;
        this.allOrders = allOrders;
        newAcceptedOrders = new ArrayList<>();
        newDataOrders = new ArrayList<>();
        for (ResponseOrder item : allOrders) {
            if (item.getStateId() == 3) {
                newAcceptedOrders.add(item);

            } else {
                newDataOrders.add(item);
            }

        }
        updateOrderCount.noticeOrdersCount(newDataOrders.size(),newAcceptedOrders.size());
    }


    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RecievedOrdersFragment.newInstance(newDataOrders,allProducts);
            case 1:
                return AcceptedOrdersFragment.newInstance(newAcceptedOrders,allProducts);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface UpdateOrderCount{
         void noticeOrdersCount(int newOrdersCount, int acceptedOrdersCount);
    }
}