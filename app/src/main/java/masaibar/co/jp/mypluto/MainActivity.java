package masaibar.co.jp.mypluto;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import masaibar.co.jp.mypluto.AsyncTask.AsyncHttpRequest;
import masaibar.co.jp.mypluto.Service.SendNotification;


public class MainActivity extends ActionBarActivity {

    private WebView mWebView;
    private EditText editTextUrl;
    private String mCookie;
    private static final String URL_PLUTO_LOGIN = "https://pluto.io/sp/login.html";
    private static final String URL_PLUTO = "https://pluto.io";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ソフトウェアキーボードの出現を抑制
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        editTextUrl = (EditText) findViewById((R.id.editText));
        mWebView = (WebView) findViewById(R.id.webView);

        //リンク先もWebViewで開くようにする
        mWebView.setWebViewClient(new WebViewClient());

        //JavaScriptの実行を許可
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.loadUrl(URL_PLUTO);

        //webViewの表示しているURLを表示
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                editTextUrl.setText(mWebView.getOriginalUrl());
                mWebView.loadUrl("javascript:android.setHtmltext(document.documentElement.outerHTML);");
                CookieManager cookieManager = CookieManager.getInstance();
                mCookie = cookieManager.getCookie(url);

                Log.d("masaibar debug", mCookie);
                execAsync(view);
            }
        });

        //未ログインだったらログイン画面に遷移させられないだろうか Todo
    }
    //戻るボタンの挙動をOverride
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem notificationSetting = menu.findItem(R.id.always_show_notification);
        notificationSetting.setChecked(getNotificationState());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_reload:
                onReloadPressed();
                break;

            case R.id.always_show_notification:
                onNotificationSettingPressed(item);
                break;

            case R.id.action_settings:
                onSettingPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Asyncの実行
    public void execAsync(View view) {
        AsyncHttpRequest task = new AsyncHttpRequest(this, mCookie);
        task.mActivity = this;
        task.execute(URL_PLUTO);
    }
    //リロードボタン押下時の処理
    public void onReloadPressed() {
        mWebView.reload();
    }

    //設定ボタン押下時の処理 設定画面に遷移させる
    public void onSettingPressed() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    //設定切り替えボタン押下時の処理
    public void onNotificationSettingPressed(MenuItem item) {
        //チェックボックスの状態変更を行う
        item.setChecked(!item.isChecked());
        //反映後の状態を記憶する
        setNotificationState(item.isChecked());
        //状態によって通知を出す、消す
        if (item.isChecked()) {
            enableNotification();
        } else {
            disableNotification();
        }
    }

    public boolean getNotificationState() {
        SharedPreferences preferences = getSharedPreferences("MyPlutoPreference", MODE_PRIVATE);
        return preferences.getBoolean("notificationState", false);
    }

    public void setNotificationState(Boolean state) {
        SharedPreferences preferences = getSharedPreferences("MyPlutoPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("notificationState", state);
        editor.commit();
    }

    public void enableNotification() {
        Intent intent = new Intent(this, SendNotification.class);
        startService(intent);
    }

    public void disableNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(MyPlutoNotificationIds.ALWAYS_NOTIFICATION);
    }
}
