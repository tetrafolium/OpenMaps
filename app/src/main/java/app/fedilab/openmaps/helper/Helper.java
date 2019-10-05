package app.fedilab.openmaps.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Helper {

    //URLs of maps
    // - Displacements
    public static String direction_map = "https://maps.openrouteservice.org";
    public static String cyclo_map = "https://www.cyclosm.org";
    public static String topo_map = "https://opentopomap.org";
    public static String park_map = "http://www.freeparking.world";
    public static String fuel_map = "https://openfuelmap.net";

    // - Life-skills
    public static String resto_map = "https://openvegemap.netlib.re";
    public static String wheel_map = "https://wheelmap.org";
    public static String beer_map = "https://openbeermap.github.io";
    public static String solar_map = "https://opensolarmap.org/";
    public static String weather_map = "https://openweathermap.org";

    // - Hobbies
    public static String breton_map = "https://kartenn.openstreetmap.bzh";
    public static String occ_map = "https://tile.openstreetmap.fr";

    // - Contributions
    public static String base_contrib_map = "https://www.openstreetmap.org";
    public static String theme_contrib_map = "https://www.mapcontrib.xyz";
    public static String ads_warning_contrib_map = "https://openadvertmap.pavie.info";
    public static String building_contrib_map = "https://openlevelup.net";
    public static String them_an_now_contrib_map = "https://mvexel.github.io/thenandnow/";



    @SuppressLint("SetJavaScriptEnabled")
    public static void initializeWebview(Context context, WebView webView) {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setGeolocationDatabasePath(context.getFilesDir().getPath() );
        webView.getSettings().setGeolocationEnabled(true);

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


}
