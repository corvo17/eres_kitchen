package kitchen.eres.com.kitchen;

import android.app.Application;
import android.app.NotificationChannelGroup;
import android.content.res.Configuration;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TimeUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import kitchen.eres.com.kitchen.net.NetworkManager;
import kitchen.eres.com.kitchen.net.Pojos.roam.SendAddedProduct;
import kitchen.eres.com.kitchen.server.MyHasChangeListiner;
import kitchen.eres.com.kitchen.server.NotificationData;
import kitchen.eres.com.kitchen.server.NotificationType;
import kitchen.eres.com.kitchen.server.ObservableCollection;
import kitchen.eres.com.kitchen.server.WebServer;

public class App extends Application {
    private NetworkManager manager;
    private ArrayList<SendAddedProduct> aormList;
    private Configuration configuration;
    public boolean isImgAddedFromGallery;
    public Uri uri;
    public String uniqueString = "";
    private static final String TAG = "App";


    private MyHasChangeListiner listiner;
    private static App app;
    public static ObservableCollection<NotificationData> message;

    public void setListiner(MyHasChangeListiner listiner) {
        this.listiner = listiner;
    }

    public static WebServer webServer = null;
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public SendAddedProduct sendableProduct;
    public boolean dontUpdate = false;

    private static IMyPin myPing;
    private boolean isReachable ;

    @Override
    public void onCreate() {
        super.onCreate();

        webServer = new WebServer(10001);
        message = new ObservableCollection<>();
        try {
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isImgAddedFromGallery = false;

        updateData();
        aormList = new ArrayList<>();
        configuration = new Configuration(getResources().getConfiguration());

        // InetAddress.getByName(host).isReachable(timeOut)

//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                checkServer();
//            }
//        }, 0, 20000);
    }


    public static App getApp() {
        if (app == null) app = new App();
        return app;
    }

    public void loadLocalServer(Collection<? extends NotificationData> noteAll) {
        message.addAll(noteAll);
//        listiner.onChange(0);
        Log.d("TAG_R", "onCollectionChange: ");
        int size = noteAll.size();
        for (NotificationData note : noteAll) {


            int a = note.getNotificationTypeId();
            Log.d(TAG, "loadLocalServer: a =  " + a);
            if (a == NotificationType.OrderAcceptedInKitchen.ordinal() || a == NotificationType.OrderIsCanceled.ordinal() || a == NotificationType.TableIsNotServiced.ordinal() || a == NotificationType.OrderProblemsInKithen.ordinal() || a == NotificationType.OrderSendToKitchen.ordinal() || a == NotificationType.OrderRejectedInKithen.ordinal() || a== NotificationType.CompleteKitchen.ordinal()) {
                Log.d(TAG, "loadLocalServer: " + a);
                if (listiner != null)
                listiner.onChange(a);

            }
            message.remove(note);
//            }

        }


    }

    public void setLanguage(Locale locale) {
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        getApplicationContext().getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    public void setAormList(SendAddedProduct product) {
        SendAddedProduct sendAddedProduct = new SendAddedProduct(product.getId(),product.getName(),product.getCode(),product.getIsDish(),product.getPrice(),
                                            product.getDescription(), product.getImageUrl(), product.getExcluded(), product.getDepartmentId(),product.getCategoryId(),
                                            product.getConfirmed(), product.getSpecialDesires(), product.getProdIngredients());
        aormList.add(sendAddedProduct);
    }

    public ArrayList<SendAddedProduct> getAormList() {
        return aormList;
    }

    public void updateData() {
        manager = new NetworkManager(this);
    }

    public NetworkManager getManager() {
        return manager;
    }

    public void clearData() {
        aormList.clear();
    }

    public SendAddedProduct getInstence(){
        if (sendableProduct == null){
            sendableProduct = new SendAddedProduct(0,"",0,0,0,"","",0,0,0,false,null,null);
        }
        return sendableProduct;
    }

    public void setInstancesNull(){
        sendableProduct = new SendAddedProduct(0,"",0,0,0,"","",0,0,0,false,null,null);;
    }

    private boolean isServerReachable(){

       return isReachable;
    }
    void checkServer(){
        try {
            isReachable =  InetAddress.getByName("http://192.168.43.209").isReachable(10);
        } catch (IOException e) {
            isReachable =false;
            e.printStackTrace();
        }
        if (!isReachable){
            myPing.checkServer();
        }
    }

    public static void setInterface(IMyPin iMyPin){
        myPing = iMyPin;
    }

    public interface IMyPin{
        void checkServer();
    }

}
