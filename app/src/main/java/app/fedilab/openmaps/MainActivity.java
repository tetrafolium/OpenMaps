package app.fedilab.openmaps;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.fedilab.openmaps.helper.Helper;
import app.fedilab.openmaps.webview.OpenMapsWebChromeClient;
import app.fedilab.openmaps.webview.OpenMapsWebViewClient;

public class MainActivity extends AppCompatActivity {


    private PowerMenu powerMenu;
    private WebView main_webview;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static String TAG = "OpenMapsTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        main_webview = findViewById(R.id.main_webview);

        Helper.initializeWebview(MainActivity.this, main_webview);
        FrameLayout webview_container = findViewById(R.id.webview_container);
        final ViewGroup videoLayout = findViewById(R.id.videoLayout);
        OpenMapsWebChromeClient openMapsWebChromeClient = new OpenMapsWebChromeClient(MainActivity.this, main_webview, webview_container, videoLayout);
        main_webview.setWebChromeClient(openMapsWebChromeClient);
        main_webview.setWebViewClient(new OpenMapsWebViewClient(MainActivity.this));
        main_webview.loadUrl(Helper.fuel_map);

        List<PowerMenuItem> distances = new ArrayList<>();
        distances.add(new PowerMenuItem("Itinerary", false));
        distances.add(new PowerMenuItem("Cycle paths", false));
        distances.add(new PowerMenuItem("Topographic", false));
        distances.add(new PowerMenuItem("Free parkings", false));
        distances.add(new PowerMenuItem("Fuel", true));
        powerMenu = new PowerMenu.Builder(MainActivity.this)
                .addItemList(distances)
                .setDivider(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)))
                .addItem(new PowerMenuItem("Vegetarian restaurants", false))
                .addItem(new PowerMenuItem("Wheelchair Accessible Places", false))
                .addItem(new PowerMenuItem("Beer", false))
                .addItem(new PowerMenuItem("Solar panels", false))
                .addItem(new PowerMenuItem("Weather", false))
                .addItem(new PowerMenuItem("Breton", false))
                .addItem(new PowerMenuItem("Occitan et Basque", false))
                .addItem(new PowerMenuItem("Beer", false))
                .addItem(new PowerMenuItem("Basic card", false))
                .addItem(new PowerMenuItem("Thematic card", false))
                .addItem(new PowerMenuItem("Billboard advertises ", false))
                .addItem(new PowerMenuItem("Interior of buildings", false))
                .addItem(new PowerMenuItem("Then And Now", false))
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setTextGravity(Gravity.LEFT)
                .setShowBackground(false)
            //    .setHeight(1500)
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (Build.VERSION.SDK_INT >= 23) {
            permissionsAPI();
        }
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        final FloatingActionButton maps = findViewById(R.id.maps);
        maps.setOnClickListener(view -> {
           // powerMenu.showAtLocation(maps,width,(int)Helper.convertDpToPixel(metrics.widthPixels -40, getApplicationContext()));
            powerMenu.showAsDropDown(maps,0,-height);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            powerMenu.setSelectedPosition(position);
            switch (position){
                case 0:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.direction_map);
                    break;
                case 1:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.cyclo_map);
                    break;
                case 2:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.topo_map);
                    break;
                case 3:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.park_map);
                    break;
                case 4:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.fuel_map);
                    break;
                case 5:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.resto_map);
                    break;
                case 6:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.wheel_map);
                    break;
                case 7:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.beer_map);
                    break;
                case 8:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.solar_map);
                    break;
                case 9:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.weather_map);
                    break;


                case 10:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.breton_map);
                    break;
                case 11:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.occ_map);
                    break;


                case 12:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.base_contrib_map);
                    break;
                case 13:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.theme_contrib_map);
                    break;
                case 14:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.ads_warning_contrib_map);
                    break;
                case 15:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.building_contrib_map);
                    break;
                case 16:
                    main_webview.stopLoading();
                    main_webview.loadUrl(Helper.them_an_now_contrib_map);
                    break;

            }
            powerMenu.dismiss();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            // Check for ACCESS_FINE_LOCATION
            if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, R.string.all_granted, Toast.LENGTH_SHORT)
                        .show();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, R.string.permission_denied, Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void permissionsAPI() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add(getString(R.string.show_location));

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                // Need Rationale
                StringBuilder message = new StringBuilder(getString(R.string.access_needed) + permissionsNeeded.get(0));

                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message.append(", ").append(permissionsNeeded.get(i));

                showMessageOKCancel(message.toString(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
}
