package com.newmovies2020.streamingmoviehd;

import android.Manifest;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.sdk.InMobiSdk;
import com.ixidev.gdpr.GDPRChecker;
import com.newmovies2020.streamingmoviehd.newmovies2020_utl.ApiResources;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.newmovies2020.streamingmoviehd.newmovies2020_utl.MyAppClass.getContext;


public class SplashscreenActivity extends AppCompatActivity {

    private int SPLASH_TIME = 2000;

    private final String TAG = MainActivity.class.getSimpleName();
    InterstitialAd fanInterstitialAd;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAd;
    ProgressBar progressBar;
    InMobiInterstitial interstitialAd;
    LinearLayout progresly;
    Button button;
    private String fanInterid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_splashscreen);
        progresly=findViewById(R.id.llProgressBar);

        button=findViewById(R.id.startbut);
        progressBar=findViewById(R.id.progressbar1);


        getAdDetails(new ApiResources().getAdDetails());
        getStatusapp(new ApiResources().getInfoApp());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {
            }
        } else {
        }

        //inmobi
        JSONObject consentObject = new JSONObject();
        try {
            // Provide correct consent value to sdk which is obtained by User
            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true);
            // Provide 0 if GDPR is not applicable and 1 if applicable
            consentObject.put("gdpr", "1");
            // Provide user consent in IAB format
            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_IAB, "<<consent in IAB format>>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InMobiSdk.init(this, "1585170476353", consentObject);


        //Toast.makeText(SplashscreenActivity.this, "login:"+ isLogedIn(), Toast.LENGTH_SHORT).show();
//        Thread timer = new Thread() {
//            public void run() {
//                try {
//                    sleep(SPLASH_TIME);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (isLogedIn()) {
//                        startActivity(new Intent(SplashscreenActivity.this,MainActivity.class));
//                        finish();
//                    } else {
//                        if (!Constants.IS_LOGIN_MANDATORY) {
//                            Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            startActivity(intent);
//
//                        } else {
//                            startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
//                        }
//                        finish();
//                    }
//
//                }
//            }
//        };
//        timer.start();

    }

    public boolean isLogedIn() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        return preferences.getBoolean("status", false);

    }

    public void loadinter (String inter){

        fanInterstitialAd = new InterstitialAd(this,inter);

        fanInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

                progresly.setVisibility(View.GONE);
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                progresly.setVisibility(View.GONE);

                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");

                Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(Ad ad, AdError adError) {

                loadinteradmob(ApiResources.adMobInterstitialId);


                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                progresly.setVisibility(View.GONE);

                fanInterstitialAd.show();



                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad

            }

            @Override
            public void onAdClicked(Ad ad) {
                progresly.setVisibility(View.GONE);

                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        fanInterstitialAd.loadAd();
    }


    private void getAdDetails(String url){



        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject=response.getJSONObject("startapp");
                    ApiResources.startappid = jsonObject.getString("startappid");
                    ApiResources.startappstatus = jsonObject.getString("startappstatus");

                    StartAppSDK.init(getContext(), ApiResources.startappid, true);
                    StartAppSDK.setUserConsent (getContext(),
                            "pas",
                            System.currentTimeMillis(),
                            true);
                    StartAppAd.disableSplash();

                } catch (JSONException e) {
                    Log.e("json", "ERROR");


                    e.printStackTrace();
                }


                try {
                    JSONObject jsonObject=response.getJSONObject("admob");

                    ApiResources.admobstatus = jsonObject.getString("status");
                    ApiResources.adMobBannerId = jsonObject.getString("admob_banner_ads_id");
                    ApiResources.adMobInterstitialId = jsonObject.getString("admob_interstitial_ads_id");
                    ApiResources.adMobPublisherId = jsonObject.getString("admob_publisher_id");

//                    interadmob=jsonObject.getString("admob_interstitial_ads_id");

//                    Toast.makeText(getContext(),"coba"+interadmob,Toast.LENGTH_LONG).show();

                    new GDPRChecker()
                            .withContext(SplashscreenActivity.this)
                            .withPrivacyUrl(Config.TERMS_URL) // your privacy url
                            .withPublisherIds(ApiResources.adMobPublisherId) // your admob account Publisher id
                            .withTestMode("9424DF76F06983D1392E609FC074596C") // remove this on real project
                            .check();




                } catch (JSONException e) {
                    Log.e("json", "ERROR");

                    e.printStackTrace();
                }


                try {
                    JSONObject jsonObject=response.getJSONObject("fan");

                    ApiResources.fanadStatus = jsonObject.getString("status");
                    ApiResources.fanBannerid = jsonObject.getString("fan_banner");
                    fanInterid = jsonObject.getString("fan_inters");

//                    Toast.makeText(getContext(),ApiResources.fanadStatus+ApiResources.fanBannerid+ApiResources.fanInterid , Toast.LENGTH_LONG).show();
//
//
                    if (!fanInterid.equals("")){
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               loadinter(fanInterid);
                                progresly.setVisibility(View.VISIBLE);



                            }
                        });
                    }

//                    else{
//
//                        Intent intent = new Intent(getContext(),MainActivity.class);
//                        startActivity(intent);
//
//
//                    }











                } catch (JSONException e) {
                    Log.e("json", "ERROR");
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", String.valueOf(error));


            }
        });

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);





    }


    public void loadinteradmob(String inter){

        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        mInterstitialAd.setAdUnitId(inter);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                progresly.setVisibility(View.GONE);

                mInterstitialAd.show();

                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                loadinmobi();


//                final Handler   handler = new Handler();
//
//                final Runnable r = new Runnable() {
//                    public void run() {
//                        StartAppAd.showAd(getContext());
//
//                        layutprogressbar.setVisibility(View.GONE);
//
//                        Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);
//
//                        startActivity(intent);
//                    }
//                };
//
//                handler.postDelayed(r, 3000);



                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                progresly.setVisibility(View.GONE);

                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                progresly.setVisibility(View.GONE);


                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                progresly.setVisibility(View.GONE);

                Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);

                startActivity(intent);

                // Code to be executed when the interstitial ad is closed.
            }
        });
    }


    private void getStatusapp(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONObject jsonObject=response.getJSONObject("statusapp");

                    ApiResources.statusapp = jsonObject.getString("status");
                    String apk = jsonObject.getString("apk");

                    if (ApiResources.statusapp.equals("0")){

                        Intent intent=new Intent(getContext(), UpdateActivity.class);
                        intent.putExtra("apk",apk);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);


                        Toast.makeText(getApplicationContext(),"APPNOTFOUND" , Toast.LENGTH_LONG).show();



                    }

                    JSONObject jsonObject1=response.getJSONObject("notifapp");

                    ApiResources.statusnotif = jsonObject1.getString("statusnotif");
                    ApiResources.judulstatus = jsonObject1.getString("judulstatus");
                    ApiResources.pesan = jsonObject1.getString("pesan");
                    ApiResources.foto = jsonObject1.getString("foto");
                    ApiResources.icon = jsonObject1.getString("icon");
                    ApiResources.apknew = jsonObject1.getString("apk");





                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);


    }


    private void loadinmobi(){




        InterstitialAdEventListener mInterstitialAdEventListener = new InterstitialAdEventListener() {
            @Override
            public void onAdLoadSucceeded(InMobiInterstitial ad) {
                super.onAdLoadSucceeded(ad);
                interstitialAd.show();
            }

            @Override
            public void onAdLoadFailed(InMobiInterstitial ad, InMobiAdRequestStatus status) {
                super.onAdLoadFailed(ad, status);



                StartAppAd startAppAd = new StartAppAd(getContext());
                startAppAd.showAd(new AdDisplayListener() {
                    @Override
                    public void adHidden(com.startapp.android.publish.adsCommon.Ad ad) {
                        progresly.setVisibility(View.GONE);

                        Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);

                        startActivity(intent);

                    }

                    @Override
                    public void adDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {

                    }

                    @Override
                    public void adClicked(com.startapp.android.publish.adsCommon.Ad ad) {
                        progresly.setVisibility(View.GONE);

                        Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);

                        startActivity(intent);

                    }

                    @Override
                    public void adNotDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {

                        progresly.setVisibility(View.GONE);

                        Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);

                        startActivity(intent);

                    }
                })   ;
            }

            @Override
            public void onAdReceived(InMobiInterstitial ad) {
                super.onAdReceived(ad);
            }

            @Override
            public void onAdClicked(InMobiInterstitial ad, Map<Object, Object> params) {
                super.onAdClicked(ad, params);
                progresly.setVisibility(View.GONE);

            }

            @Override
            public void onAdWillDisplay(InMobiInterstitial ad) {
                super.onAdWillDisplay(ad);
                progresly.setVisibility(View.GONE);

            }

            @Override
            public void onAdDisplayed(InMobiInterstitial ad) {
                super.onAdDisplayed(ad);
            }

            @Override
            public void onAdDisplayFailed(InMobiInterstitial ad) {
                super.onAdDisplayFailed(ad);
            }

            @Override
            public void onAdDismissed(InMobiInterstitial ad) {
                super.onAdDismissed(ad);
                progresly.setVisibility(View.GONE);

                Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);

                startActivity(intent);
            }

            @Override
            public void onUserLeftApplication(InMobiInterstitial ad) {
                super.onUserLeftApplication(ad);
            }

            @Override
            public void onRewardsUnlocked(InMobiInterstitial ad, Map<Object, Object> rewards) {
                super.onRewardsUnlocked(ad, rewards);
            }

            @Override
            public void onRequestPayloadCreated(byte[] bytes) {
                super.onRequestPayloadCreated(bytes);
            }

            @Override
            public void onRequestPayloadCreationFailed(InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onRequestPayloadCreationFailed(inMobiAdRequestStatus);
            }
        };


         interstitialAd = new InMobiInterstitial(SplashscreenActivity.this, 1582481116106L, mInterstitialAdEventListener);

        interstitialAd.load();




    }


    /**
     * Listener for receiving notifications during the lifecycle of an interstitial.
     */
    public abstract class InterstitialAdEventListener extends com.inmobi.ads.listeners.InterstitialAdEventListener {
        /**
         * Called to indicate that an ad was loaded and it can now be shown. This will always be called
         * <strong>after</strong> the {@link #onAdReceived(InMobiInterstitial)} callback.
         *
         * @param ad Represents the {@link InMobiInterstitial} ad which was loaded
         */
        public void onAdLoadSucceeded(InMobiInterstitial ad) {}
        /**
         * Callback to signal that a request to fetch an ad (by calling
         * {@link InMobiInterstitial#load()} failed. The status code indicating the reason for failure
         * is available as a parameter. You should call {@link InMobiInterstitial#load()} again to
         * request a fresh ad.
         *
         * @param ad Represents the {@link InMobiInterstitial} ad which failed to load
         * @param status Represents the {@link InMobiAdRequestStatus} status containing error reason
         */
        public void onAdLoadFailed(InMobiInterstitial ad, InMobiAdRequestStatus status) {}
        /**
         * Called to indicate that an ad is available in response to a request for an ad (by calling
         * {@link InMobiInterstitial#load()}. <p class="note"><strong>Note</strong> This does not
         * indicate that the ad can be shown yet. Your code should show an ad <strong>after</strong> the
         * {@link #onAdLoadSucceeded(InMobiInterstitial)} method is called. Alternately, if you do not
         * want to handle this event, you must test if the ad is ready to be shown by checking the
         * result of calling the {@link InMobiInterstitial#isReady()} method.</p>
         *
         * @param ad Represents the {@link InMobiInterstitial} ad for which ad content was received
         */
        public void onAdReceived(InMobiInterstitial ad) {}
        /**
         * Called to indicate that an ad interaction was observed.
         *
         * @param ad Represents the {@link InMobiInterstitial} ad on which user clicked
         * @param params Represents the click parameters
         */
        public void onAdClicked(InMobiInterstitial ad, Map<Object, Object> params) {}
        /**
         * Called to indicate that the ad will be launching a fullscreen overlay.
         *
         * @param ad Represents the {@link InMobiInterstitial} ad which will display
         */
        public void onAdWillDisplay(InMobiInterstitial ad) {}
        /**
         * Called to indicate that the fullscreen overlay is now the topmost screen.
         *
         * @param ad Represents the {@link InMobiInterstitial} ad which is displayed
         */
        public void onAdDisplayed(InMobiInterstitial ad) {}
        /**
         * Called to indicate that a request to show an ad (by calling {@link InMobiInterstitial#show()}
         * failed. You should call {@link InMobiInterstitial#load()} to request for a fresh ad.
         *
         * @param ad Represents the {@link InMobiInterstitial} ad which failed to show
         */
        public void onAdDisplayFailed(InMobiInterstitial ad) {}
        /**
         * Called to indicate that the fullscreen overlay opened by the ad was closed.
         *
         * @param ad Represents the {@link InMobiInterstitial} ad which was dismissed
         */
        public void onAdDismissed(InMobiInterstitial ad) {}
        /**
         * Called to indicate that the user may leave the application on account of interacting with the ad.
         *
         * @param ad Represents the {@link InMobiInterstitial} ad
         */
        public void onUserLeftApplication(InMobiInterstitial ad) {}
        /**
         * Called to indicate that rewards have been unlocked.
         *
         * @param ad Represents the {@link InMobiInterstitial} ad for which rewards was unlocked
         * @param rewards Represents the rewards unlocked
         */
        public void onRewardsUnlocked(InMobiInterstitial ad, Map<Object, Object> rewards) {}
    }






}
