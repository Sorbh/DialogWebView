package in.unicodelabs.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import in.unicodelabs.view.utils.HexColorValidator;
import in.unicodelabs.view.utils.ThemeUtils;

/**
 * Created by saurabh on 3/3/18.
 */

public class DialogWebView extends Dialog {
    private final static String TAG = DialogWebView.class.getSimpleName();
    private Context context;
    private Params dialogParams;

    //Views
    private WebView mWebView;
    private ImageView backIv;
    private ImageView forwardIv;

    public DialogWebView(@NonNull Context context, Params params) {
        super(context);
        this.context = context;
        dialogParams = params;
    }

    public DialogWebView(@NonNull Context context, int themeResId, Params params) {
        super(context, themeResId);
        this.context = context;
        dialogParams = params;
    }

    protected DialogWebView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_webview_);

        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(null);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        //Check if owner activity is full screen or not, in case set the dialog with the same theme
        Activity owner = scanForActivity(context);
        if (owner != null) {

            boolean fullScreen = (owner.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
//        boolean forceNotFullScreen = (getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN) != 0;
//        boolean actionbarVisible = getActionBar().isShowing();

            if (fullScreen)
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        final Drawable close = context.getResources().getDrawable(R.drawable.ic_action_close);
//        close.setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//        toolbar.setNavigationIcon(close);
        toolbar.setNavigationIcon(R.drawable.ic_action_close);
        toolbar.setNavigationContentDescription("BACK");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle(TextUtils.isEmpty(dialogParams.title) ? dialogParams.url : dialogParams.title);
        toolbar.setSubtitle(TextUtils.isEmpty(dialogParams.subtitle) ? "" : dialogParams.subtitle);
        if (HexColorValidator.validate("" + dialogParams.titleBarColor)) {
            toolbar.setBackgroundColor(dialogParams.titleBarColor);
        } else {
            toolbar.setBackgroundColor(ThemeUtils.resolveAccentColor(context));
        }
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.white));
        toolbar.getNavigationIcon().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.pb);
        mWebView = (WebView) findViewById(R.id.web_view);
        backIv = (ImageView) findViewById(R.id.backIv);
        forwardIv = (ImageView) findViewById(R.id.forwardIv);
        ImageView publishIv = (ImageView) findViewById(R.id.publishIv);

        publishIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dialogParams.url));
                getContext().startActivity(browserIntent);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(final WebView view, String url, Bitmap favicon) {
                // Do something on page loading started
                // Visible the progressbar
                mProgressBar.setVisibility(View.VISIBLE);
//                    title.setText(view.getTitle());

                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (view.canGoBack()) {
                            backIv.setColorFilter(Color.parseColor("#000000"));
                            backIv.setOnClickListener(goBackListener);
                        } else {
                            backIv.setColorFilter(Color.parseColor("#d3d3d3"));
                            backIv.setOnClickListener(null);
                        }
                        if (view.canGoForward()) {
                            forwardIv.setColorFilter(Color.parseColor("#000000"));
                            forwardIv.setOnClickListener(goForwardListner);
                        } else {
                            forwardIv.setColorFilter(Color.parseColor("#d3d3d3"));
                            forwardIv.setOnClickListener(null);
                        }
                    }
                }, 1000);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Do something when page loading finished
//                Toast.makeText(context, "Page Loaded.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                // Update the progress bar with page loading progress
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    // Hide the progressbar
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mWebView.loadUrl(dialogParams.url);
        // Enable the javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
//        if (Build.VERSION.SDK_INT >= 19) {
//            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
//        else {
//            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

    }


    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mWebView.goBack();
        }
    };

    View.OnClickListener goForwardListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mWebView.goForward();
        }
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            dismiss();
        }
    }

    private Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    public static class Builder {
        private Context context;
        private String title = "";
        private String subTitle = "";
        private int themeResId = 0;
        private String url = "";
        private int titleBarColor = -1;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSubTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Builder setTheme(int themeResId) {
            this.themeResId = themeResId;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setTitleBarBackgroundColor(int color) {
            titleBarColor = color;
            return this;
        }


        public DialogWebView create() {
            Params params = new Params();
            params.title = title;
            params.subtitle = subTitle;
            params.url = url;
            params.titleBarColor = titleBarColor;

            final DialogWebView dialogWebView = new DialogWebView(context, themeResId, params);
            dialogWebView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            return dialogWebView;
        }

        public DialogWebView show() {
            DialogWebView dialogWebView = create();
            dialogWebView.show();
            return dialogWebView;
        }


    }

    public static class Params {
        String title = "";
        String subtitle = "";
        String url = "";
        int titleBarColor = -1;
    }
}
