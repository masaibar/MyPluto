package masaibar.co.jp.mypluto;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    private WebView webView;
    private EditText editTextUrl;
//    private static final String URL_PLUTO = "https://pluto.io/sp/login.html";
    private static final String URL_PLUTO = "https://pluto.io";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ソフトウェアキーボードの出現を抑制
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        editTextUrl = (EditText) findViewById((R.id.editText));
        webView = (WebView) findViewById(R.id.webView);

        //リンク先もWebViewで開くようにする
        webView.setWebViewClient(new WebViewClient());
        //JavaScriptの実行を許可
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(URL_PLUTO);

        //webViewの表示しているURLを表示
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                editTextUrl.setText(webView.getOriginalUrl());
            }
        });

        sendNotification();
    }
    //戻るボタンの挙動をOverride
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

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

    //あとで切り離す
    public void sendNotification() {
        //Notificationインスタンスの生成
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //ステータスバーに表示する設定を行う
        builder.setSmallIcon(android.R.drawable.star_big_on);
        builder.setTicker("Do you need Pluto?");

        //通知領域に表示する設定を行う
        builder.setContentTitle("Plutoで家電を操作しますか？");
        builder.setContentText("こちらから開けます");

        //Intentの生成
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0 , intent, 0);
        builder.setContentIntent(contentIntent);
        builder.setWhen(System.currentTimeMillis());
        //通知エリアに残す
        builder.setAutoCancel(false);

        //通知の実行
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0 , builder.build());
    }
}
