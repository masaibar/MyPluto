package masaibar.co.jp.mypluto.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import masaibar.co.jp.mypluto.Service.SendNotification;

/**
 * Created by masaibar on 2015/01/03.
 */
public class StartupReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            context.startService(new Intent(context, SendNotification.class));
        }
    }
}
