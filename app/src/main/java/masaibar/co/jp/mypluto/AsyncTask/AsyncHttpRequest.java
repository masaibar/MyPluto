package masaibar.co.jp.mypluto.AsyncTask;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import masaibar.co.jp.mypluto.MainActivity;
import masaibar.co.jp.mypluto.R;

public class AsyncHttpRequest extends AsyncTask<String, Void, String> {
    public Activity mActivity;
    private String mCookie;
    private String mReceiveStr;
    private Context mContext;

    public AsyncHttpRequest(Context context, Activity activity, String cookie) {
        mContext = context;
        mActivity = activity;
        mCookie = cookie;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            HttpGet httpGet = new HttpGet(params[0]);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpGet.setHeader("Connection", "Keep-Alive");
            httpGet.setHeader("Cookie", mCookie);

            HttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
                throw new Exception("");
            } else {
                mReceiveStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String target = "THIS IS MAIN PAGE";

        boolean isLogin = mReceiveStr.contains(target);

        if (!isLogin) {
            Toast.makeText(mContext, "ログイン画面にリダイレクトします", Toast.LENGTH_SHORT).show();
            WebView mWebView = (WebView) mActivity.findViewById(R.id.webView);
            mWebView.loadUrl(MainActivity.URL_PLUTO_LOGIN);
        }
    }
}

