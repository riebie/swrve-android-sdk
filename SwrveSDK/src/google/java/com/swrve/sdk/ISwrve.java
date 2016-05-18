package com.swrve.sdk;

import android.content.Intent;

import com.swrve.sdk.config.SwrveConfig;
import com.swrve.sdk.gcm.ISwrvePushNotificationListener;
import com.swrve.sdk.gcm.ISwrveSilentPushNotificationListener;

/**
 * Swrve Google SDK interface.
 */
public interface ISwrve extends ISwrveBase<ISwrve, SwrveConfig> {

    void setPushNotificationListener(ISwrvePushNotificationListener pushNotificationListener);

    void setSilentPushNotificationListener(ISwrveSilentPushNotificationListener silentPushNotificationListener);

    void iapPlay(String productId, double productPrice, String currency, String purchaseData, String dataSignature);

    void iapPlay(String productId, double productPrice, String currency, SwrveIAPRewards rewards, String purchaseData, String dataSignature);

    void processIntent(Intent intent);

    void onTokenRefreshed();
}
