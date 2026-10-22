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

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class AndroidLauncher extends AndroidApplication implements AdsController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // Hide the system bars so the game is not drawn under them (Android 15+ edge-to-edge).
        config.useImmersiveMode = true;
        final AndroidShareChallenge shareChallenge = new AndroidShareChallenge(this);
        initialize(new Klooni(shareChallenge, this), config);

        // UMP consent first; ads are initialised and preloaded only when allowed.
        AdsConsent.gather(this, () -> InterstitialHelper.preload(this));
    }

    @Override
    public void initializeAds() {
        runOnUiThread(() -> InterstitialHelper.preload(this));
    }

    /** Called by the game on game over (a natural break point); capped to one ad per 60 s. */
    @Override
    public void showAds() {
        runOnUiThread(() -> InterstitialHelper.showIfReady(this, null));
    }

    @Override
    public boolean isPrivacyOptionsRequired() {
        return AdsConsent.isPrivacyOptionsRequired(this);
    }

    @Override
    public void showPrivacyOptions() {
        runOnUiThread(() -> AdsConsent.showPrivacyOptions(this));
    }
}
