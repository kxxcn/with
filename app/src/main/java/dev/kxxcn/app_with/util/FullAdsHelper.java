package dev.kxxcn.app_with.util;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import dev.kxxcn.app_with.BuildConfig;
import dev.kxxcn.app_with.R;

/**
 * Created by kxxcn on 2019-04-08.
 */
public class FullAdsHelper {

    private static InterstitialAd INSTANCE;

    public static synchronized InterstitialAd getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new InterstitialAd(context);
            if (BuildConfig.DEBUG) {
                INSTANCE.setAdUnitId(context.getString(R.string.ad_unit_test_id));
            } else {
                INSTANCE.setAdUnitId(context.getString(R.string.ad_unit_id));
            }
            setAds();
        }
        return INSTANCE;
    }

    private static void setAds() {
        if (!INSTANCE.isLoading() && !INSTANCE.isLoaded()) {
            AdRequest adRequest = new AdRequest
                    .Builder()
                    .build();
            INSTANCE.loadAd(adRequest);
            INSTANCE.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    INSTANCE.loadAd(adRequest);
                }
            });
        }
    }
}
