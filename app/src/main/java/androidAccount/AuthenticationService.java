package androidAccount;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Camilo on 08/06/2015.
 */
public class AuthenticationService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new AccountAuthenticator(this).getIBinder();
    }
}
