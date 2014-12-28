package masaibar.co.jp.mypluto;

import android.annotation.SuppressLint;
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
}
