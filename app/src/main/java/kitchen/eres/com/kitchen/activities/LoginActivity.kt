package kitchen.eres.com.kitchen.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.*
import kitchen.eres.com.kitchen.App
import kitchen.eres.com.kitchen.MyConfig
import kitchen.eres.com.kitchen.MyConfig.*
import kitchen.eres.com.kitchen.R
import retrofit2.Call
import retrofit2.Response
import java.util.*
import android.telephony.TelephonyManager
import android.util.Log
import kitchen.eres.com.kitchen.net.Pojos.AccessToken
import kitchen.eres.com.kitchen.net.Pojos.GetMe
import kotlin.collections.ArrayList
import kitchen.eres.com.kitchen.R.id.editText
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import com.andrognito.pinlockview.PinLockView
import io.reactivex.internal.util.HalfSerializer.onComplete


class LoginActivity : AppCompatActivity() {
    private lateinit var app : App
    private lateinit var spinner: Spinner

    private var saveLang: SharedPreferences? = null
    private lateinit var editor: Editor
    private var lang: String? = null

    private lateinit var config: Configuration
    private lateinit var linearLayout: LinearLayout

    private lateinit var sharedPreferences: SharedPreferences
    private var selectedItem :String = ""
    private lateinit var  units: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var names :ArrayList<String> ;

    private lateinit var pinLockView : PinLockView
    private lateinit var indicatorDots : IndicatorDots

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }


        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_PHONE_STATE),
                        1)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


        val IMEINumber = tm.deviceId
        IME = IMEINumber
        setContentView(R.layout.activity_login)
        initFields()
        getMe()
        setLang()
       // myClick()
    }


    private fun goNextActivity() {
        startActivity(Intent(this, OrdersActivity::class.java))
        finish()
    }



    private fun getMe() {
        app.manager.service.getMe("Kitchen").enqueue(object : retrofit2.Callback<GetMe> {
            override fun onResponse(call: Call<GetMe>, response: Response<GetMe>) {
                if (response.isSuccessful){
                    names = ArrayList()
                    for (it in response.body()?.users!!) {
                        names.add(it.name)
                    }
                    setSpinner(names)
                    Toast.makeText(app, "Successfully registrated !!!", Toast.LENGTH_SHORT).show()
                }else{
                    app.manager.service.addMyPhone().enqueue(object : retrofit2.Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful){
                                Toast.makeText(app, "Successfully added your phone !!!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(app, "Error cannpt added your phone", Toast.LENGTH_SHORT).show()

                        }
                    })
                }
            }

            override fun onFailure(call: Call<GetMe>, t: Throwable) {
                Toast.makeText(app, "Error occured onFailure ", Toast.LENGTH_SHORT).show()
                app.manager.service.addMyPhone().enqueue(object : retrofit2.Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful){
                            Toast.makeText(app, "Successfully added your phone !!!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(app, "Error cannpt added your phone", Toast.LENGTH_SHORT).show()

                    }
                })
            }
        })
    }

    private fun setSpinner(users: ArrayList<String>?){
        users?.forEach {
            units?.add(it)
        }
        adapter.notifyDataSetChanged()
        Toast.makeText(app, "Xmmmm" + users?.size, Toast.LENGTH_SHORT).show()
    }

    fun setSelected(selected: String) {
        selectedItem = selected
    }

    private fun setLang() {
            app.setLanguage(Locale("ru"))
    }

    private fun initFields() {
        app = applicationContext as App
        saveLang = getSharedPreferences("SAVE_LANG", Context.MODE_PRIVATE)

        config = Configuration(resources.configuration)
        editor = saveLang!!.edit()

        pinLockView = findViewById(R.id.pin_lock_view)
        indicatorDots = findViewById(R.id.indicator_dots)
        val mPinLockListener = object : PinLockListener {
            override fun onComplete(pin: String) {
                var password = pin

                if ( password.length >= 1) {
                    sharedPreferences = getSharedPreferences("ACCES_TOKEN", Context.MODE_PRIVATE)
                    var editor = sharedPreferences.edit()
                    app.manager.service.getAccessToken(selectedItem,password.toString()).enqueue(object : retrofit2.Callback<AccessToken> {
                        override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) =
                                if (response.isSuccessful){
                                    Toast.makeText(app, "Successfully added your phone !!!", Toast.LENGTH_SHORT).show()
                                    editor.putString("TOKEN",("bearer " + response.body()?.access_token))
                                    MyConfig.TOKEN = "bearer " + response.body()?.access_token;
                                    editor.apply()
                                    goNextActivity()
                                }else{

                                    val shake = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.shake)
                                    indicatorDots.startAnimation(shake)

                                    var vibe =  this@LoginActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                    vibe.vibrate(500);
//                                    pin.setText("")
//                                    ETPassword.setError("Wrong password")
                                }

                        override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                            Toast.makeText(app, "Error cannpt added your phone", Toast.LENGTH_SHORT).show()

                        }
                    })

                }
            }

            override fun onEmpty() {
            }

            override fun onPinChange(pinLength: Int, intermediatePin: String) {
            }
        }

        pinLockView.setPinLockListener(mPinLockListener)
        pinLockView.attachIndicatorDots(indicatorDots)

        units = ArrayList()
        spinner = findViewById(R.id.EDLogin)
        units.add("users")

        adapter = ArrayAdapter(this, R.layout.item_spinner_users, units)
        adapter.setDropDownViewResource(R.layout.item_spinner_users)
        spinner.setAdapter(adapter)
        editor.putBoolean("first",true)
       if(saveLang!!.getBoolean("first",true )){
           spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
               override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                   setSelected(adapterView.selectedItem.toString())
               }

               override fun onNothingSelected(adapterView: AdapterView<*>) {
                   setSelected(adapterView.selectedItem.toString())
               }
           })
       }
    }

     fun myClick() {

        editor.putBoolean("first",false)

                config.setLocale(Locale("ru"))
                editor.putString("LANG", "ru")
                editor.apply()
                application.resources.updateConfiguration(config, resources.displayMetrics)
                LANG = 2
                recreate()
                linearLayout.visibility = View.GONE


    }
    override fun onBackPressed() {
        super.onBackPressed()
//        startActivity(Intent(this, LoginActivity::class.java))
//        finish()
    }
}
