/*
    1010! Klooni, a free customizable puzzle game for Android and Desktop
    Copyright (C) 2017-2019  Lonami Exo @ lonami.dev

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.lonami.klooni;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


import java.lang.reflect.Method;

import dev.lonami.klooni.actors.MoneyBuyBand;
import dev.lonami.klooni.actors.ShopCard;

public class AndroidLauncher extends AndroidApplication implements AdsController{
    private InterstitialAd mInterstitialAd = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIXME: Hack to allow us use the old way to share files
        // https://stackoverflow.com/a/42437379/
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        final AndroidShareChallenge shareChallenge = new AndroidShareChallenge(this);
        initialize(new Klooni(shareChallenge, this), config);
        loadInterstitial();
        Timers.timer().start();

    }

    private void loadInterstitial() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersitial_id));
        initializeAds();
    }


    @Override
    public void initializeAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void showAds() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            loadInterstitial();
                            Timers.timer().start();
                            Klooni.TIMER_FINISHED=false;
                            super.onAdClosed();
                        }
                    });
                }
            }
        });

    }



}
