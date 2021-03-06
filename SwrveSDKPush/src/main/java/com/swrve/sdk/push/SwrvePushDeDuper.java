package com.swrve.sdk.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swrve.sdk.SwrveHelper;
import com.swrve.sdk.SwrveLogger;
import com.swrve.sdk.SwrvePushConstants;

import java.util.LinkedList;

public class SwrvePushDeDuper {

    private final static String AMAZON_RECENT_PUSH_IDS = "recent_push_ids";
    private final static String AMAZON_PREFERENCES = "swrve_amazon_pref"; // TODO rename this
    protected final int DEFAULT_PUSH_ID_CACHE_SIZE = 16;

    private Context context;

    public SwrvePushDeDuper(Context context) {
        this.context = context;
    }

    /*
     * Check for duplicates. This is a necessary part of using ADM which might clone
     * a message as part of attempting to deliver it. We de-dupe by
     * checking against the tracking id and timestamp. (Multiple pushes with the same
     * tracking id are possible in some scenarios from Swrve).
     * Id is concatenation of tracking key and timestamp "$_p:$_s.t"
     */
    public boolean isDupe(Bundle msg) {

        boolean isDupe = false;

        Object rawId = msg.get(SwrvePushConstants.SWRVE_TRACKING_KEY);
        if (rawId == null) {
            rawId = msg.get(SwrvePushConstants.SWRVE_SILENT_TRACKING_KEY);
        }
        String msgId = (rawId != null) ? rawId.toString() : null;

        final String timestamp = msg.getString(SwrvePushConstants.TIMESTAMP_KEY);
        if (SwrveHelper.isNullOrEmpty(timestamp)) {
            SwrveLogger.e("Push notification: cannot dedupe as it's missing %s", SwrvePushConstants.TIMESTAMP_KEY);
            isDupe = true;
        } else {
            String curId = msgId + ":" + timestamp;
            LinkedList<String> recentIds = getRecentNotificationIdCache(context);
            if (recentIds.contains(curId)) {
                SwrveLogger.i("Push notification: but not processing because duplicate Id: %s", curId);
                isDupe =true;
            } else {
                // Try get de-dupe cache size
                int pushIdCacheSize = msg.getInt(SwrvePushConstants.PUSH_ID_CACHE_SIZE_KEY, DEFAULT_PUSH_ID_CACHE_SIZE);

                // No duplicate found. Update the cache.
                updateRecentNotificationIdCache(recentIds, curId, pushIdCacheSize, context);
            }
        }

        return isDupe;
    }

    protected LinkedList<String> getRecentNotificationIdCache(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AMAZON_PREFERENCES, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(AMAZON_RECENT_PUSH_IDS, "");
        Gson gson = new Gson();
        LinkedList<String> recentIds = gson.fromJson(jsonString, new TypeToken<LinkedList<String>>() {}.getType());
        recentIds = recentIds == null ? new LinkedList<String>() : recentIds;
        return recentIds;
    }

    protected void updateRecentNotificationIdCache(LinkedList<String> recentIds, String newId, int maxCacheSize, Context context) {
        //Update queue
        recentIds.add(newId);

        //This must be at least zero;
        maxCacheSize = Math.max(0, maxCacheSize);

        //Maintain cache size limit
        while (recentIds.size() > maxCacheSize) {
            recentIds.remove();
        }

        //Store latest queue to shared preferences
        Gson gson = new Gson();
        String recentNotificationsJson = gson.toJson(recentIds);
        SharedPreferences sharedPreferences = context.getSharedPreferences(AMAZON_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(AMAZON_RECENT_PUSH_IDS, recentNotificationsJson).apply();
    }
}
