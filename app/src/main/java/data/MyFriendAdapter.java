package data;

import android.accounts.Account;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 11/20/15.
 */
public class MyFriendAdapter extends ArrayAdapter<User> implements AsyncResponse {
    // declaring our ArrayList of items
    private ArrayList<User> users;

    public MyFriendAdapter(Context context, int textViewResourceId, ArrayList<User> users) {
        super(context, textViewResourceId, users);
        this.users = users;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */

    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View view = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friend_row, null);
        }

		/*
         * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        final User user = users.get(position);

        if (user != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView nameText = (TextView) view.findViewById(R.id.friend_name);
            ImageButton profileImage = (ImageButton) view.findViewById(R.id.friend_image);
            Button addButton = (Button) view.findViewById(R.id.add_friend_btn);
            Button msnButton = (Button) view.findViewById(R.id.msn_btn);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (nameText != null) {
                nameText.setText(user.getFirstName());
            }
            if (profileImage != null) {
                Bitmap bitmap = null;
                if (user.getImageBytes() != null) {
                    byte[] imageBytes = Base64.decode(user.getImageBytes(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                } else {
                    bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_image);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                }
                profileImage.setImageBitmap(bitmap);
            }
            if (addButton != null) {
                if (user.isFriend()) {
                    addButton.setVisibility(View.GONE);
                } else {
                    final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    View.OnClickListener onClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Account okyLifeAccount = ((OkyLife) getContext().getApplicationContext()).getOkyLifeAccount();
                            params.add(new BasicNameValuePair("emailCurrent", okyLifeAccount.name));
                            params.add(new BasicNameValuePair("emailFriend", user.getEmail()));
                            addFriend(params);
                        }
                    };
                    addButton.setOnClickListener(onClickListener);
                }
            }
            if (msnButton != null) {

            }
        }

        // the view must be returned to our activity
        return view;

    }

    public void addFriend(ArrayList<NameValuePair> params) {
        ((OkyLife) getContext().getApplicationContext()).getMasterCaller().postData("User/addFriend", this, params);
    }

    @Override
    public void processFinish(String result) {
        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
    }
}