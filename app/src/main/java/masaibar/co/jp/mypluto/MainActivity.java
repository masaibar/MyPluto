package masaibar.co.jp.mypluto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import masaibar.co.jp.mypluto.Service.SendNotification;


public class MainActivity extends ActionBarActivity {

    private WebView mWebView;
    private EditText editTextUrl;
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

    //リロードボタン押下時の処理
    public void onReloadPressed() {
        mWebView.reload();
    }

    //設定ボタン押下時の処理 設定画面に遷移させる
    public void onSettingPressed() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_reload:
                onReloadPressed();
                break;

            //メニューでチェックボックスを選択した時の挙動
            case R.id.always_show_notification:
                //チェックボックスの状態変更を行う
                item.setChecked(!item.isChecked());
                //反映後の状態を記憶する
                setNotificationState(item.isChecked());
                if (item.isChecked()) {
                    enableNotification();
                }
                break;

            case R.id.action_settings:
                onSettingPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
