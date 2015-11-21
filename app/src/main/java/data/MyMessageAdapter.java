package data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import aplication.OkyLife;
import com.example.okylifeapp.app.R;
import org.apache.http.NameValuePair;
import rest.AsyncResponse;

import java.util.ArrayList;

/**
 * Created by mordreth on 11/20/15.
 */
public class MyMessageAdapter extends ArrayAdapter<Message> implements AsyncResponse {
    // declaring our ArrayList of items
    private ArrayList<Message> messages;

    public MyMessageAdapter(Context context, int textViewResourceId, ArrayList<Message> messages) {
        super(context, textViewResourceId, messages);
        this.messages = messages;
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
            view = inflater.inflate(R.layout.message_row, null);
        }

		/*
         * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        final Message message = this.messages.get(position);

        if (message != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView remitentText = (TextView) view.findViewById(R.id.remitent_name);
            TextView subjectText = (TextView) view.findViewById(R.id.subject_name);
            Button msnDeleteButton = (Button) view.findViewById(R.id.delete_msn_btn);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (remitentText != null) {
                remitentText.setText(message.getRemitentEmail());
            }
            if (subjectText != null) {
                subjectText.setText(message.getSubject());
            }
            if (msnDeleteButton != null) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Hola", Toast.LENGTH_LONG).show();
                    }
                };
                msnDeleteButton.setOnClickListener(onClickListener);
            }
        }
        // the view must be returned to our activity
        return view;
    }

    public void addFriend(ArrayList<NameValuePair> params) {
        ((OkyLife) getContext().getApplicationContext()).getMasterCaller().postData("Messages/addFriend", this, params);
    }

    @Override
    public void processFinish(String result) {
        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
    }
}