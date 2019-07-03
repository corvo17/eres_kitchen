package kitchen.eres.com.kitchen.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kitchen.eres.com.kitchen.MyConfig;
import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.ordersPojo.ResponseOrder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    private IService service;
    private SharedPreferences sharedPreferences;


    public NetworkManager(Context context ) {
        sharedPreferences = context.getSharedPreferences("ACCES_TOKEN",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Interceptor interceptor = chain -> {
            Request newRequest = chain.request().newBuilder().addHeader("token", MyConfig.IME).build();
            return chain.proceed(newRequest);
        };
        Interceptor interceptor2 = chain -> {
            Request newRequest = chain.request().newBuilder().addHeader("Authorization", MyConfig.TOKEN).build();
            return chain.proceed(newRequest);
        };

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.MILLISECONDS)
                .writeTimeout(1000, TimeUnit.MILLISECONDS)
                .readTimeout(1000, TimeUnit.MILLISECONDS)
                //.addInterceptor(logging)
                //.addInterceptor(new ChuckInterceptor(context))
                .addInterceptor(interceptor)
                .addInterceptor(interceptor2)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(MyConfig.BaseUrl)
                .build();
        service = retrofit.create(IService.class);

    }


    public Single<ArrayList<GetAllProducts>> getAllProducts(){
           return getService().getCategories()
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread());

    }
    public  Single<ArrayList<ResponseOrder>> getOrders(){
        return getService()
                .responceOrders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public IService getService() {
        return service;
    }

}
