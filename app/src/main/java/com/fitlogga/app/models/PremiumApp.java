package com.fitlogga.app.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.fitlogga.app.R;
import com.fitlogga.app.activities.MainActivity;

import java.util.Collections;
import java.util.List;

/**
 * This class is used to edit the device's premium status.
 * Premium Status costs money, and unlocks the full app.
 */
public class PremiumApp {

    private static BillingClient billingClient;

    /*
    There is no meaning behind this value. It's just to help prevent rooted users from opening
    up their shared-preferences folder, and easily enable PremiumApp through a boolean value.
    The value is completely made up to scare users.

    One note is, NEVER change this value, unless you have a way for users who already have premium
    enabled to migrate over to a different value/system that indicates premium status.
     */
    private static final String SECRET_PREMIUM_VALUE =
            "WARNING, ENSURE YOU KNOW WHAT YOU ARE DOING. EDITING SYSTEM FILES IS DISABLED BY " +
                    "DEFAULT, AND CAN SEVERELY DAMAGE YOUR DEVICE. THIS IS YOUR ONLY WARNING.";
    private static final String SECRET_PREMIUM_KEY = "enabled-string";

    private static SharedPreferences getSharedPreferences() {
        String prefName = "pa-shared-pref-file";
        Context context = ApplicationContext.getInstance();
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static void setEnabled(boolean enabled) {
        SharedPreferences pref = getSharedPreferences();

        SharedPreferences.Editor editor = pref.edit();
        if (enabled) {
            editor.putString(SECRET_PREMIUM_KEY, SECRET_PREMIUM_VALUE);
        }
        else {
            editor.putString(SECRET_PREMIUM_KEY, "");
        }
        editor.apply();
    }

    public static boolean isEnabled() {
        SharedPreferences pref = getSharedPreferences();
        String value = pref.getString(SECRET_PREMIUM_KEY, "");
        return SECRET_PREMIUM_VALUE.equals(value);
    }

    public static void popupPremiumAppDialog(Activity activity, String promptMessage) {
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_premium_app, null);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .show();

        TextView promptView = dialog.findViewById(R.id.tv_prompt_message);
        promptView.setText(promptMessage);

        Button purchaseButton = dialog.findViewById(R.id.btn_purchase);
        purchaseButton.setOnClickListener(buttonView -> startPaymentDialog(activity));

        Button cancelButton = dialog.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(buttonView -> dialog.dismiss());

    }

    public static void startPaymentDialog(Activity activity) {

        final String sku_id = "premium_fitness_bundle";
        List<String> sku_id_list = Collections.singletonList(sku_id);

        billingClient = BillingClient.newBuilder(activity)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

                        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                            return;
                        }

                        if (purchases == null) {
                            return;
                        }

                        executePurchase(activity, purchases.get(0));


                    }
                })
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                SkuDetailsParams detailsParams = SkuDetailsParams.newBuilder()
                        .setSkusList(sku_id_list)
                        .setType(BillingClient.SkuType.INAPP)
                        .build();

                billingClient.querySkuDetailsAsync(detailsParams, (billingResult1, skuDetailsList) -> {
                    SkuDetails skuDetails = skuDetailsList.get(0);

                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails)
                            .build();
                    billingClient.launchBillingFlow(activity, flowParams);


                });

            }

            @Override
            public void onBillingServiceDisconnected() {
                billingClient.endConnection();
            }
        });
    }

    private static void executePurchase(Activity activity, Purchase purchase) {

        AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder()
                .setDeveloperPayload(purchase.getDeveloperPayload())
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        billingClient.acknowledgePurchase(params, billingResult -> {

            if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                return;
            }

            PremiumApp.setEnabled(true);
            cachePurchaseDetails(purchase);

            // Essentially restart app
            activity.finish();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        });
    }

    private static void cachePurchaseDetails(Purchase purchase) {
        SharedPreferences pref = ApplicationContext.getInstance()
                .getSharedPreferences("purchases", Context.MODE_PRIVATE);

        String keyPrefix = "premiumapp";

        String purchaseTokenKey = keyPrefix + "_purchase_token";
        String purchaseToken = purchase.getPurchaseToken();

        String orderIdKey = keyPrefix + "_order_id";
        String orderId = purchase.getOrderId();

        pref.edit()
                .putString(purchaseTokenKey, purchaseToken)
                .putString(orderIdKey, orderId)
                .apply();
    }

}
