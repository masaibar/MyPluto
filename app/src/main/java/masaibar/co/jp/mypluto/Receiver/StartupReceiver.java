package masaibar.co.jp.mypluto.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import masaibar.co.jp.mypluto.Service.SendNotification;

public class StartupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //boot時であること 且つ 通知を出す設定になっていることを確認
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) && checkNotificationState(context)){
            context.startService(new Intent(context, SendNotification.class));
        }
    }

    public boolean checkNotificationState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyPlutoPreference", Context.MODE_PRIVATE);
        return preferences.getBoolean("notificationState", false);
    }
}
