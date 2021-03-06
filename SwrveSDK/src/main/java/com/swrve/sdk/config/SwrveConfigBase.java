package com.swrve.sdk.config;

import android.app.NotificationChannel;
import android.graphics.Color;

import com.swrve.sdk.SwrveAppStore;
import com.swrve.sdk.SwrveHelper;
import com.swrve.sdk.messaging.SwrveOrientation;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Configuration for the Swrve SDK.
 */
public abstract class SwrveConfigBase {

    private NotificationChannel defaultNotificationChannel;
    private String userId;
    private long maxSqliteDbSize = 1 * 1024 * 1024; // Maximum size of the internal SQLite database.
    private int maxEventsPerFlush = 50; // Maximum number of events to send per flush.
    private String dbName = "swrve.db";
    private SwrveStack selectedStack = SwrveStack.US;
    private URL eventsUrl = null;
    private URL defaultEventsUrl = null;
    private boolean useHttpsForEventsUrl = true;
    private URL contentUrl = null;
    private URL defaultContentUrl = null;
    private boolean useHttpsForContentUrl = true;
    private long newSessionInterval = 30000; // Session timeout time
    private String appVersion;
    private String appStore = SwrveAppStore.Google; // App Store where the app will be distributed.
    private String language; // App language. If null it defaults to the value returned by Locale.getDefault().
    private SwrveOrientation orientation = SwrveOrientation.Both;
    private boolean autoDownloadCampaignsAndResources = true;
    private int minSampleSize = 1; // Sample size to use when loading in-app message images. Has to be a power of two.
    private File cacheDir;
    private boolean sendQueuedEventsOnResume = true;
    private long autoShowMessagesMaxDelay = 5000; // Maximum delay for in-app messages to appear after initialization.
    private boolean loadCachedCampaignsAndResourcesOnUIThread = true; // Will load the campaign and resources cache on the UI thread.
    private int defaultBackgroundColor = Color.TRANSPARENT; // Default in-app background color used if none is specified in the template.
    private int httpTimeout = 60000; // HTTP timeout used when contacting the Swrve APIs, in milliseconds.
    private boolean hideToolbar = true; // Hide the toolbar when displaing in-app messages.
    private boolean androidIdLoggingEnabled; // Automatically log Android ID as "swrve.android_id".
    private boolean abTestDetailsEnabled; // Obtain information about the AB Tests a user is part of.
    private List<String> modelBlackList;

    /**
     * Create an instance of the SDK advance preferences.
     */
    public SwrveConfigBase() {
        modelBlackList = new ArrayList<String>();
        modelBlackList.add("Calypso AppCrawler");
    }

    /**
     * Set the default notification channel used to display notifications. This is required if you target Android O (API 26) or higher.
     * We recommend that the channel is created before setting it in our config. Our SDK will attempt to create it if it doesn't exist.
     *
     * @param defaultNotificationChannel Default notification channel
     */
    public void setDefaultNotificationChannel(NotificationChannel defaultNotificationChannel) {
        this.defaultNotificationChannel = defaultNotificationChannel;
    }

    /**
     * @return default channel used to display notifications. This is required if you target Android O (API 26) or higher.
     */
    public NotificationChannel getDefaultNotificationChannel() {
        return defaultNotificationChannel;
    }

    /**
     * @return Whether campaigns and resources will automatically be downloaded.
     */
    public boolean isAutoDownloadCampaignsAndResources() {
        return this.autoDownloadCampaignsAndResources;
    }

    /**
     * Download resources and in-app campaigns automatically.
     *
     * @param autoDownload Automatically download campaigns and resources.
     */
    public SwrveConfigBase setAutoDownloadCampaignsAndResources(boolean autoDownload) {
        this.autoDownloadCampaignsAndResources = autoDownload;
        return this;
    }

    /**
     * @return Orientation supported by the application.
     */
    public SwrveOrientation getOrientation() {
        return orientation;
    }

    /**
     * @param orientation Orientation supported by the application.
     */
    public SwrveConfigBase setOrientation(SwrveOrientation orientation) {
        this.orientation = orientation;
        return this;
    }

    /**
     * Set the locale of the app. If empty or null then
     * the default locale is used.
     *
     * @param locale Locale of the app.
     */
    public SwrveConfigBase setLanguage(Locale locale) {
        this.language = SwrveHelper.toLanguageTag(locale);
        return this;
    }

    /**
     * @return Language of the app.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Get the selected Stack
     * @return
     */
    public SwrveStack getSelectedStack() {
        return selectedStack;
    }

    /**
     * Set the stack prefix for the events and content url
     * @param stack The chosen stack of the app
     */
    public void setSelectedStack(SwrveStack stack){
        selectedStack = stack;
    }


    /**
     * Get the stack prefix for the events and content url
     * @return
     */
    private String getStackHostPrefix(){
        return (getSelectedStack() == SwrveStack.EU) ? "eu-" : "";
    }

    /**
     * @return Maximum byte size of the internal SQLite database.
     */
    public long getMaxSqliteDbSize() {
        return maxSqliteDbSize;
    }

    /**
     * Set the maximum byte size of the internal SQLite database.
     *
     * @param maxSqliteDbSize Maximum size in bytes.
     */
    public SwrveConfigBase setMaxSqliteDbSize(long maxSqliteDbSize) {
        this.maxSqliteDbSize = maxSqliteDbSize;
        return this;
    }

    /**
     * @return Maximum number of events per batch.
     */
    public int getMaxEventsPerFlush() {
        return maxEventsPerFlush;
    }

    /**
     * Set the maximum number of events per batch to the event server.
     *
     * @param maxEventsPerFlush Maximum number of events per batch.
     */
    public SwrveConfigBase setMaxEventsPerFlush(int maxEventsPerFlush) {
        this.maxEventsPerFlush = maxEventsPerFlush;
        return this;
    }

    /**
     * @return Name of the internal SQLite database.
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Override the name of the internal SQLite database.
     *
     * @param dbName Name of the internal SQLite database.
     */
    public SwrveConfigBase setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    /**
     * @return Location of the event server.
     */
    public URL getEventsUrl() {
        if (eventsUrl == null)
            return defaultEventsUrl;
        return eventsUrl;
    }

    /**
     * Set to override the default location of the server to which Swrve will send analytics events.
     * If your company has a special API end-point enabled, then you should specify it here.
     * You should only need to change this value if you are working with Swrve support on a specific support issue.
     *
     * @param eventsUrl Custom location of the event server.
     */
    public SwrveConfigBase setEventsUrl(URL eventsUrl) {
        this.eventsUrl = eventsUrl;
        return this;
    }

    /**
     * @return Whether to use HTTPS for events.
     */
    public boolean getUseHttpsForEventsUrl() {
        return useHttpsForEventsUrl;
    }

    /**
     * Enable HTTPS for event requests.
     *
     * @param useHttpsForEventsUrl Whether to use HTTPS for api.swrve.com.
     */
    public SwrveConfigBase setUseHttpsForEventsUrl(boolean useHttpsForEventsUrl) {
        this.useHttpsForEventsUrl = useHttpsForEventsUrl;
        return this;
    }

    /**
     * @return Location of the content server.
     */
    public URL getContentUrl() {
        if (contentUrl == null)
            return defaultContentUrl;
        return contentUrl;
    }

    /**
     * Set to override the default location of the server used to obtain resources and in-app campaigns.
     * If your company has a special API end-point enabled, then you should specify it here.
     * You should only need to change this value if you are working with Swrve support on a specific support issue.
     *
     * @param contentUrl Custom location of the content server.
     */
    public SwrveConfigBase setContentUrl(URL contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }

    /**
     * @return Whether to use HTTPS for resources and in-app campaigns.
     */
    public boolean getUseHttpsForContentUrl() {
        return useHttpsForContentUrl;
    }

    /**
     * Enable HTTPS for resources and in-app campaign requests.
     *
     * @param useHttpsForContentUrl Whether to use HTTPS for content.swrve.com.
     */
    public SwrveConfigBase setUseHttpsForContentUrl(boolean useHttpsForContentUrl) {
        this.useHttpsForContentUrl = useHttpsForContentUrl;
        return this;
    }

    /**
     * @return App version.
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * Set the app version. If empty or null then PackageInfo.versionName
     * will be used.
     *
     * @param appVersion Version of the app.
     */
    public SwrveConfigBase setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    /**
     * @return Session timeout time in milliseconds.
     */
    public long getNewSessionInterval() {
        return newSessionInterval;
    }

    /**
     * Set the session timeout time.
     * User activity after this time will be considered a new session.
     *
     * @param newSessionInterval session timeout in milliseconds.
     */
    public SwrveConfigBase setNewSessionInterval(long newSessionInterval) {
        this.newSessionInterval = newSessionInterval;
        return this;
    }

    /**
     * @return the App Store where the app will be distributed.
     */
    public String getAppStore() {
        return appStore;
    }

    /**
     * Set the App Store where the app will be distributed.
     *
     * @param appStore App Store where the app will be distributed.
     */
    public SwrveConfigBase setAppStore(String appStore) {
        this.appStore = appStore;
        return this;
    }

    /**
     * Generate default endpoints with the given app id. Used internally.
     *
     * @throws MalformedURLException
     */
    public void generateUrls(int appId) throws MalformedURLException {
        // If the prefix is non empty, prepend it with a .
        String prefix = getStackHostPrefix();
        // Build the URL
        defaultEventsUrl = new URL(getSchema(useHttpsForEventsUrl) + "://" + appId + "." +  prefix + "api.swrve.com");
        defaultContentUrl = new URL(getSchema(useHttpsForContentUrl) + "://" + appId + "." + prefix + "content.swrve.com");
    }

    private static String getSchema(boolean https) {
        return https? "https" : "http";
    }

    /**
     * @return Custom unique user id.
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Set a custom unique user id.
     *
     * @param userId Custom unique user id.
     */
    public SwrveConfigBase setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * The sample size used when loading in-app message images.
     *
     * @return sample size
     */
    public int getMinSampleSize() {
        return minSampleSize;
    }

    /**
     * Minimum sample size to use when loading in-app message images. Has to be a power
     * of two.
     *
     * @param sampleSize sample size
     */
    public void setMinSampleSize(int sampleSize) {
        this.minSampleSize = sampleSize;
    }

    /**
     * Cache folder used to save the in-app message images.
     *
     * @return cache folder
     */
    public File getCacheDir() {
        return cacheDir;
    }

    /**
     * Cache folder used to save the campaign images.
     * The default is Context.getCacheDir(). If permission to cacheDir is denied, then default is used.
     * @param cacheDir cache folder
     */
    public void setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
    }

    /**
     * @return Whether the SDK will send events onResume.
     */
    public boolean isSendQueuedEventsOnResume() {
        return sendQueuedEventsOnResume;
    }

    /**
     * Automatically send events onResume.
     *
     * @param sendQueuedEventsOnResume
     */
    public void setSendQueuedEventsOnResume(boolean sendQueuedEventsOnResume) {
        this.sendQueuedEventsOnResume = sendQueuedEventsOnResume;
    }

    /**
     * Maximum delay for in-app messages to appear after initialization.
     *
     * @return maximum delay in milliseconds
     */
    public long getAutoShowMessagesMaxDelay() {
        return autoShowMessagesMaxDelay;
    }

    /**
     * Maximum delay for in-app messages to appear after initialization.
     *
     * @param autoShowMessagesMaxDelay
     */
    public void setAutoShowMessagesMaxDelay(long autoShowMessagesMaxDelay) {
        this.autoShowMessagesMaxDelay = autoShowMessagesMaxDelay;
    }

    /**
     * Load campaigns and resources cache on the UI thread. Allows to get user resources and
     * campaigns on the early start.
     *
     * @return Whether the SDK will load this data on the UI thread.
     */
    public boolean isLoadCachedCampaignsAndResourcesOnUIThread() {
        return loadCachedCampaignsAndResourcesOnUIThread;
    }

    /**
     * Load campaigns and resources cache on the UI thread. Allows to get user resources and
     * campaigns on the early start.
     *
     * @param loadOnUIThread
     */
    public void setLoadCachedCampaignsAndResourcesOnUIThread(boolean loadOnUIThread) {
        this.loadCachedCampaignsAndResourcesOnUIThread = loadOnUIThread;
    }

    /**
     * The default in-app background color, if none is specified in the template.
     *
     * @return The default in-app background color.
     */
    public int getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    /**
     * Set the default in-app background color.
     *
     * @param defaultBackgroundColor
     */
    public void setDefaultBackgroundColor(int defaultBackgroundColor) {
        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    /**
     * Set the HTTP timeout.
     *
     * @param httpTimeout
     */
    public void setHttpTimeout(int httpTimeout) {
        this.httpTimeout = httpTimeout;
    }

    /**
     * The HTTP timeout used in the Swrve API calls.
     *
     * @return The timeout used in the HTTP calls
     */
    public int getHttpTimeout() {
        return httpTimeout;
    }

    /**
     * Hide the toolbar when displaying in-app messages.
     *
     * @param hideToolbar
     */
    public void setHideToolbar(boolean hideToolbar) {
        this.hideToolbar = hideToolbar;
    }

    /**
     * @return Whether the SDK will hide the toolbar when displaying in-app messages.
     */
    public boolean isHideToolbar() {
        return hideToolbar;
    }

    /**
     * @return if it will automatically log Android ID as "swrve.android_id".
     */
    public boolean isAndroidIdLoggingEnabled() {
        return androidIdLoggingEnabled;
    }

    /**
     * @param enabled to enable automatic logging of Android ID "swrve.android_id".
     */
    public void setAndroidIdLoggingEnabled(boolean enabled) {
        this.androidIdLoggingEnabled = enabled;
    }

    /**
     * @return if the SDK will request information about the AB Tests a user is part of.
     */
    public boolean isABTestDetailsEnabled() {
        return abTestDetailsEnabled;
    }

    /**
     * @param enabled to obtain information about the AB Tests a user is part of.
     */
    public void setABTestDetailsEnabled(boolean enabled) {
        this.abTestDetailsEnabled = enabled;
    }

    /**
     * @return list of Android models where the SDK won't run, for example the 'Calypso AppCrawler'.
     */
    public List<String> getModelBlackList() {
        return modelBlackList;
    }

    /**
     * @param modelBlackList list of Android models where the SDK won't run, for example 'Calypso AppCrawler'.
     */
    public void setModelBlackList(List<String> modelBlackList) {
        this.modelBlackList = modelBlackList;
    }
}
