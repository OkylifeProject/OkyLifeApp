package Authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by mordreth on 10/4/15.
 */
public class AuthenticationService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new AccountAuthenticator(this).getIBinder();
    }
}