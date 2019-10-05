package app.fedilab.openmaps.webview;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import app.fedilab.openmaps.R;

public class OpenMapsWebViewClient extends WebViewClient {

    private Activity activity;
    private final int PAGE_STARTED = 0x1;
    private int webViewPreviousState;
    private CoordinatorLayout rootView;
    private Dialog loadingDialog;

    public OpenMapsWebViewClient(Activity activity){
        this.activity = activity;
        rootView = activity.findViewById(R.id.main_layout);
        loadingDialog = new Dialog(activity);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // When user clicks a hyperlink, load in the existing WebView
        view.loadUrl(url);
        return true;
    }



    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        webViewPreviousState = PAGE_STARTED;

        if (loadingDialog == null || !loadingDialog.isShowing())
            loadingDialog = ProgressDialog.show(activity, "",
                    activity.getString(R.string.loading_wait), true, true,
                    new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // do something
                        }
                    });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request,
                                WebResourceError error) {


        if (isConnected()) {
            if( error.getDescription() != null && error.getDescription().length() > 0 && !error.getDescription().toString().trim().equals("Not Found")) {
                final Snackbar snackBar = Snackbar.make(rootView, activity.getString(R.string.error, error.getDescription()), Snackbar.LENGTH_INDEFINITE);
                snackBar.setAction(activity.getString(R.string.reload), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.loadUrl("javascript:window.location.reload( true )");
                    }
                });
                snackBar.show();
            }
        } else {
            final Snackbar snackBar = Snackbar.make(rootView, activity.getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE);
            snackBar.setAction(activity.getString(R.string.enable_data), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
                    view.loadUrl("javascript:window.location.reload( true )");
                    snackBar.dismiss();
                }
            });
            snackBar.show();
        }

        super.onReceivedError(view, request, error);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedHttpError(WebView view,
                                    WebResourceRequest request, WebResourceResponse errorResponse) {

        if (isConnected()) {
            if( errorResponse.getReasonPhrase() != null && errorResponse.getReasonPhrase().length() > 0 && !errorResponse.getReasonPhrase().trim().equals("Not Found")) {
                final Snackbar snackBar = Snackbar.make(rootView, activity.getString(R.string.error,  errorResponse.getReasonPhrase()) , Snackbar.LENGTH_INDEFINITE);

                snackBar.setAction(activity.getString(R.string.reload), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.loadUrl("javascript:window.location.reload( true )");
                    }
                });
                snackBar.show();
            }
        } else {
            final Snackbar snackBar = Snackbar.make(rootView, activity.getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE);
            snackBar.setAction(activity.getString(R.string.enable_data), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
                    view.loadUrl("javascript:window.location.reload( true )");
                    snackBar.dismiss();
                }
            });
            snackBar.show();
        }
        super.onReceivedHttpError(view, request, errorResponse);
    }


    /**
     * Check if there is any connectivity
     *
     * @return is Device Connected
     */
    private boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        }

        return false;

    }

    @Override
    public void onPageFinished(WebView view, String url) {

        if (null != loadingDialog) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
