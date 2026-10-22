package dev.lonami.klooni;

public interface AdsController {
    void initializeAds();

    void showAds();

    /** True when the user must be offered a way to change their ad consent (UMP). */
    boolean isPrivacyOptionsRequired();

    void showPrivacyOptions();
}
