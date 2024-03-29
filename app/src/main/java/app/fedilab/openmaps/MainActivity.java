package app.fedilab.openmaps;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
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
    private PowerMenu powerSubMenu;
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
        main_webview.loadUrl(Helper.base_contrib_map);




        List<PowerMenuItem> distances = new ArrayList<>();
        distances.add(new PowerMenuItem(getString(R.string.trips), false));
        distances.add(new PowerMenuItem(getString(R.string.life_skills), false));
        distances.add(new PowerMenuItem(getString(R.string.hobbies), false));
        distances.add(new PowerMenuItem(getString(R.string.regional_maps), false));
        distances.add(new PowerMenuItem(getString(R.string.contributions), true));
        powerMenu = new PowerMenu.Builder(MainActivity.this)
                .addItemList(distances)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                .setTextGravity(Gravity.LEFT)
                .setShowBackground(false)
            //    .setHeight(1500)
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
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
           // powerMenu.showAtLocation(maps,width,(int)Helper.convertDpToPixel(metrics.widthPixels -40, MainActivity.this));
            powerMenu.showAsDropDown(maps,0,-750);
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
                    List<PowerMenuItem> distances = new ArrayList<>();
                    distances.add(new PowerMenuItem(getString(R.string.itinerary), false));
                    distances.add(new PowerMenuItem(getString(R.string.cycle_paths), false));
                    distances.add(new PowerMenuItem(getString(R.string.topographic), false));
                    distances.add(new PowerMenuItem(getString(R.string.free_parkings), false));
                    distances.add(new PowerMenuItem(getString(R.string.fuel), false));
                    powerSubMenu = new PowerMenu.Builder(MainActivity.this)
                        .setHeaderView(R.layout.layout_dialog_header_trips)
                        .setFooterView(R.layout.layout_dialog_footer)
                        .addItemList(distances)
                        .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                        .setWidth(700)
                        .setTextSize(15)
                        .setMenuRadius(10f)
                        .setMenuShadow(10f)
                        .setSelectedEffect(false)
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>(){
                            @Override
                            public void onItemClick(int position, PowerMenuItem item) {
                                String url = null;
                                switch (position) {
                                    case 1:
                                        url = Helper.direction_map;
                                        break;
                                    case 2:
                                        url = Helper.cyclo_map;
                                        break;
                                    case 3:
                                        url = Helper.topo_map;
                                        break;
                                    case 4:
                                        url = Helper.park_map;
                                        break;
                                    case 5:
                                        url = Helper.fuel_map;
                                        break;
                                }
                                if( url != null){
                                    main_webview.stopLoading();
                                    main_webview.loadUrl(url);
                                }
                                powerSubMenu.dismiss();
                                powerMenu.dismiss();
                            }
                        })
                        .build();
                    powerSubMenu.showAtCenter(main_webview);
                    break;
                case 1:
                    List<PowerMenuItem> lifeskills = new ArrayList<>();

                    lifeskills.add(new PowerMenuItem(getString(R.string.vegetarian_restaurants), false));
                    lifeskills.add(new PowerMenuItem(getString(R.string.accessible_places), false));
                    lifeskills.add(new PowerMenuItem(getString(R.string.beer), false));
                    lifeskills.add(new PowerMenuItem(getString(R.string.solar_panel), false));
                    lifeskills.add(new PowerMenuItem(getString(R.string.weather), false));

                    powerSubMenu = new PowerMenu.Builder(MainActivity.this)
                            .setHeaderView(R.layout.layout_dialog_header_life_skills)
                            .setFooterView(R.layout.layout_dialog_footer)
                            .addItemList(lifeskills)
                            .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                            .setWidth(700)
                            .setTextSize(15)
                            .setMenuRadius(10f)
                            .setMenuShadow(10f)
                            .setSelectedEffect(false)
                            .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>(){
                                @Override
                                public void onItemClick(int position, PowerMenuItem item) {
                                    String url = null;
                                    switch (position) {
                                        case 1:
                                            url = Helper.resto_map;
                                            break;
                                        case 2:
                                            url = Helper.wheel_map;
                                            break;
                                        case 3:
                                            url = Helper.beer_map;
                                            break;
                                        case 4:
                                            url = Helper.solar_map;
                                            break;
                                        case 5:
                                            url = Helper.weather_map;
                                            break;
                                    }
                                    if( url != null){
                                        main_webview.stopLoading();
                                        main_webview.loadUrl(url);
                                    }
                                    powerSubMenu.dismiss();
                                    powerMenu.dismiss();
                                }
                            })
                            .build();
                    powerSubMenu.showAtCenter(main_webview);
                    break;
                case 2:
                    List<PowerMenuItem> hobbies = new ArrayList<>();
                    hobbies.add(new PowerMenuItem(getString(R.string.ski_snow), false));
                    hobbies.add(new PowerMenuItem(getString(R.string.historic_places), false));
                    powerSubMenu = new PowerMenu.Builder(MainActivity.this)
                            .setHeaderView(R.layout.layout_dialog_header_hobbies)
                            .setFooterView(R.layout.layout_dialog_footer)
                            .addItemList(hobbies)
                            .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                            .setWidth(700)
                            .setTextSize(15)
                            .setMenuRadius(10f)
                            .setMenuShadow(10f)
                            .setSelectedEffect(false)
                            .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>(){
                                @Override
                                public void onItemClick(int position, PowerMenuItem item) {
                                    String url = null;
                                    switch (position) {
                                        case 1:
                                            url = Helper.snow_map;
                                            break;
                                        case 2:
                                            url = Helper.historic_map;
                                            break;
                                    }
                                    if( url != null){
                                        main_webview.stopLoading();
                                        main_webview.loadUrl(url);
                                    }
                                    powerSubMenu.dismiss();
                                    powerMenu.dismiss();
                                }
                            })
                            .build();
                    powerSubMenu.showAtCenter(main_webview);
                    break;
                case 3:
                    List<PowerMenuItem> regionals = new ArrayList<>();
                    regionals.add(new PowerMenuItem(getString(R.string.breton), false));
                    regionals.add(new PowerMenuItem(getString(R.string.occ_basq), false));
                    powerSubMenu = new PowerMenu.Builder(MainActivity.this)
                            .setHeaderView(R.layout.layout_dialog_header_regional_maps)
                            .setFooterView(R.layout.layout_dialog_footer)
                            .addItemList(regionals)
                            .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                            .setWidth(700)
                            .setTextSize(15)
                            .setMenuRadius(10f)
                            .setMenuShadow(10f)
                            .setSelectedEffect(false)
                            .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>(){
                                @Override
                                public void onItemClick(int position, PowerMenuItem item) {
                                    String url = null;
                                    switch (position) {
                                        case 1:
                                            url = Helper.breton_map;
                                            break;
                                        case 2:
                                            url = Helper.occ_map;
                                            break;
                                    }
                                    if( url != null){
                                        main_webview.stopLoading();
                                        main_webview.loadUrl(url);
                                    }
                                    powerSubMenu.dismiss();
                                    powerMenu.dismiss();
                                }
                            })
                            .build();
                    powerSubMenu.showAtCenter(main_webview);
                    break;
                case 4:
                    List<PowerMenuItem> contributions = new ArrayList<>();
                    contributions.add(new PowerMenuItem(getString(R.string.basic_map), true));
                    contributions.add(new PowerMenuItem(getString(R.string.thematic_maps), false));
                    contributions.add(new PowerMenuItem(getString(R.string.billboard_advertises), false));
                    contributions.add(new PowerMenuItem(getString(R.string.interior_buildings), false));
                    contributions.add(new PowerMenuItem(getString(R.string.then_and_now), false));

                    powerSubMenu = new PowerMenu.Builder(MainActivity.this)
                            .setHeaderView(R.layout.layout_dialog_header_contributions)
                            .setFooterView(R.layout.layout_dialog_footer)
                            .addItemList(contributions)
                            .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                            .setWidth(700)
                            .setTextSize(15)
                            .setMenuRadius(10f)
                            .setMenuShadow(10f)
                            .setSelectedEffect(false)
                            .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>(){
                                @Override
                                public void onItemClick(int position, PowerMenuItem item) {
                                    String url = null;
                                    switch (position) {
                                        case 1:
                                            url = Helper.base_contrib_map;
                                            break;
                                        case 2:
                                            url = Helper.theme_contrib_map;
                                            break;
                                        case 3:
                                            url = Helper.ads_warning_contrib_map;
                                            break;
                                        case 4:
                                            url = Helper.building_contrib_map;
                                            break;
                                        case 5:
                                            url = Helper.them_an_now_contrib_map;
                                            break;
                                    }

                                    if( url != null){
                                        main_webview.stopLoading();
                                        main_webview.loadUrl(url);
                                    }
                                    powerSubMenu.dismiss();
                                    powerMenu.dismiss();
                                }
                            })
                            .build();
                    powerSubMenu.showAtCenter(main_webview);
                    break;

            }
            View footerView = powerSubMenu.getFooterView();
            TextView close = footerView.findViewById(R.id.close_dialog);
            close.setOnClickListener(view -> {
                powerSubMenu.dismiss();
            });

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


    @Override
    public void onPause() {
        super.onPause();
        if (main_webview != null)
            main_webview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (main_webview != null)
            main_webview.onResume();
    }

    @Override
    public void onBackPressed() {
        if (main_webview.canGoBack()) {
            main_webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (main_webview != null)
            main_webview.destroy();
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
