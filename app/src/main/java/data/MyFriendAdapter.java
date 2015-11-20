package data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.okylifeapp.app.R;

import java.util.ArrayList;

/**
 * Created by mordreth on 11/20/15.
 */
public class MyFriendAdapter extends ArrayAdapter<User> {
    // declaring our ArrayList of items
    private ArrayList<User> friends;

    public MyFriendAdapter(Context context, int textViewResourceId, ArrayList<User> friends) {
        super(context, textViewResourceId, friends);
        this.friends = friends;
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
        User friend = friends.get(position);

        if (friend != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView nameText = (TextView) view.findViewById(R.id.friend_name);
            ImageButton profileImage = (ImageButton) view.findViewById(R.id.friend_image);
            Button addButton = (Button) view.findViewById(R.id.add_friend_btn);
            Button msnButton = (Button) view.findViewById(R.id.msn_btn);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (nameText != null) {
                nameText.setText(friend.getFirstName());
            }
            if (profileImage != null) {
                Bitmap bitmap = null;
                if (friend.getImageBytes() != null) {
                    byte[] imageBytes = Base64.decode(friend.getImageBytes(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                } else {
                    bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_image);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                }
                profileImage.setImageBitmap(bitmap);
            }
            if (addButton != null) {

            }
            if (msnButton != null) {

            }
        }

        // the view must be returned to our activity
        return view;

    }

}