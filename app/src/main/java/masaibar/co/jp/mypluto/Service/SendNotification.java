package masaibar.co.jp.mypluto.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import masaibar.co.jp.mypluto.MainActivity;

/**
 * Created by masaibar on 2015/01/03.
 */
public class SendNotification extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        sendNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendNotification() {
        //Intentの生成
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, 0);

        //Notificationインスタンスの生成
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //ステータスバーに表示する設定を行う
        builder.setSmallIcon(android.R.drawable.star_big_on);
        builder.setTicker("Do you need Pluto?");

        //通知領域に表示する設定を行う
        builder.setContentTitle("Plutoで家電を操作しますか？");
        builder.setContentText("こちらから開けます");

//        builder.addAction(R.drawable.ic_launcher, "開く", pendingIntent);
//        builder.addAction(R.drawable.ic_launcher, "あとで", pendingIntent);

        builder.setContentIntent(pendingIntent);
        builder.setWhen(System.currentTimeMillis());
        //通知エリアに残す
        builder.setAutoCancel(false);

        //通知の実行
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0 , builder.build());
    }
}
