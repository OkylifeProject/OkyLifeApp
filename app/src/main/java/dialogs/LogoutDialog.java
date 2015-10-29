package dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by mordreth on 10/29/15.
 */
public class LogoutDialog extends DialogFragment {
    AlertPositiveLogoutListener alertPositiveLogoutListener;

    DialogInterface.OnClickListener logoutListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            alertPositiveLogoutListener.onPositiveLogoutClick(true);
        }
    };

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            alertPositiveLogoutListener = (AlertPositiveLogoutListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement AlertPositiveLogoutListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /** Creating a Builder for the alert window **/
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        /** Setting a title for the window */

        /** Setting a label for the alert dialog**/
        builder.setMessage("Are you sure you want to logout?");

        /** Setting a positive button and its listener **/
        builder.setPositiveButton("Yes", logoutListener);

        /** Creating the alert dialog window using the builder class */
        AlertDialog alertDialog = builder.create();

        /** Return the alert dialog window */
        return alertDialog;
    }

    public interface AlertPositiveLogoutListener {
        public void onPositiveLogoutClick(boolean logout);
    }
}
