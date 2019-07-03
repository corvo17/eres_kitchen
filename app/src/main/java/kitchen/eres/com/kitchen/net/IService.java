package kitchen.eres.com.kitchen.net;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

import io.reactivex.Single;
import kitchen.eres.com.kitchen.model.Product;
import kitchen.eres.com.kitchen.net.Pojos.AccessToken;
import kitchen.eres.com.kitchen.net.Pojos.Category;
import kitchen.eres.com.kitchen.net.Pojos.GetMe;
import kitchen.eres.com.kitchen.net.Pojos.Unit;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;
import kitchen.eres.com.kitchen.net.Pojos.roam.Ingredient;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;
import kitchen.eres.com.kitchen.net.Pojos.roam.SingleProduct;
import kitchen.eres.com.kitchen.net.models.SendImg;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IService {

    @GET("api/orders/ordersforkitchen")
    Single<ArrayList<ResponseOrder>>  responceOrders();

//menu/products
    @GET("api/menu/products")
    Single<ArrayList <GetAllProducts> > getCategories();

    @PUT("api/orders/KitchenSave")
    Call<Void> sendReject(@Body ResponseOrder request, @Query("stateId") int value);

    @PUT("api/orders/kitchenSave")
    Call<Void> sendOk(@Body ResponseOrder request, @Query("stateId") int value);

    @PUT("api/menu/productexclude")
    Call<Void> addStopList(@Query("productId") int value);

    @PUT("api/menu/productinclude")
    Call<Void> removeStopList(@Query("productId") int value);

    @PUT("api/menu/productsinclude")
    Call<Void> removeAllStopList(@Body ArrayList<Long> removeStopList);


    @GET("api/menu/categories")
    Call<ArrayList<Category>> getProductCategories();

    @GET("api/images/all")
    Call<ArrayList<String>> getImgUrl();

    @GET("api/menu/ingredients")
    Call<ArrayList<Ingredient>> getIngredients();

    @PUT("api/menu/productsave")
    Call<Void> sendNewAddedProducts(@Body ArrayList<SendAddedProduct> products);

    @GET("api/menu/products")
    Call<ArrayList <GetAllProducts> > getEditableProducts();

    @GET("api/admin/getme")
    Call<GetMe> getMe(@Query("role") String kitechen);

    @GET("api/addmyphone")
    Call<Void> addMyPhone();

    @PUT("api/account/token")
    Call<AccessToken> getAccessToken(@Query("username") String userName , @Query("password") String password);


    @GET("api/menu/getunits")
    Call<ArrayList<Unit>> getUnits();

    @DELETE("api/menu/delete")
    Call<Void> deletePo(@Query("productId") long productId);

    @PUT("api/orders/KitchenPartialComplete")
    Call<AccessToken> sendPartlyComplete(@Query("orderItemId") long userName );
    @POST("api/files/saveimage")
    Call<Void> sendImgToServer(@Body SendImg sendImg,@Query("fileName") String imgName);


    @GET("api/menu/getalldeleted")
    Call<ArrayList<Product>> getAllDeleted();
    @DELETE("api/menu/clearTrash")
    Call<ArrayList<Product>> clearTrash();
    @GET("api/menu/restoreFromTrash")
    Call<Void> restoreFromTrash(@Query("products") ArrayList<Long> products);
}