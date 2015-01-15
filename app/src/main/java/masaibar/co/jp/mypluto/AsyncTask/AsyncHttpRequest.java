package masaibar.co.jp.mypluto.AsyncTask;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

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

    public AsyncHttpRequest(Activity activity, String cookie) {
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
//        TextView textView =  (TextView) owner.findViewById(R.id.textView);
//        textView.setText(mReceiveStr);
        String target = "THIS IS MAIN PAGE";

        boolean isLogin = mReceiveStr.contains(target);
        Log.d("masaibar debug", String.valueOf(isLogin));

        if (!isLogin) {
            WebView mWebView = (WebView) mActivity.findViewById(R.id.webView);
            mWebView.loadUrl(MainActivity.URL_PLUTO_LOGIN);
        }
    }
}

