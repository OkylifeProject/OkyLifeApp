package aplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import data.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rest.RequestCaller;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mordreth on 10/2/15.
 */
public class OkyLife extends Application {
    private RequestCaller masterCaller;
    private Account OkyLifeAccount;
    private User user;

    public OkyLife() {
        this.masterCaller = new RequestCaller();
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static double calculateCaloriesByDistance(double distance, double weight, double velocity) {
        double time = distance / velocity;
        return 0.029 * weight * 2.2 * time;
    }

    public static double calculateCaloriesByDistance(String effort, double distance, double weight) {
        double velocity = 0;
        if (effort.equals("Low")) {
            velocity = 90;
        }
        double time = distance / velocity;
        return 0.029 * weight * 2.2 * time;
    }

    public static boolean isJSON(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createAccountFirstTime(AccountManager accountManager, JSONObject jsonObject) {
        Bundle sessioninfo = new Bundle();
        try {
            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");
            String id = jsonObject.getString("id");
            String imageHash = "";

            Account account = new Account(email, "com.example.okylifeapp.app");
            sessioninfo.putString("imageHash", imageHash);
            sessioninfo.putString("email", email);
            sessioninfo.putString("id", id);
            accountManager.addAccountExplicitly(account, password, sessioninfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAccount(AccountManager accountManager, Account okyLifeAccount) {
        accountManager.removeAccount(okyLifeAccount, null, null);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] res, int length, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(res, 0, length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(res, 0, length, options);
    }

    public static Bitmap decodeSampledBitmapFromUri(Uri imageUri, ContentResolver resolver, int reqWidth, int reqHeight) {

        InputStream stream = null;
        try {
            stream = resolver.openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        BufferedInputStream buffer = new BufferedInputStream(stream);
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(buffer, null, options);

        try {
            buffer.reset();
        } catch (IOException e) {
            try {
                stream = resolver.openInputStream(imageUri);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            buffer = new BufferedInputStream(stream);
            e.printStackTrace();
        }
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(buffer, null, options);
    }

    //Tools and Other Algorithms

    /**
     * Segun formula de Harris-Benedict
     **/
    public static double calculateCaloriesHB(String sex, double age, double height, double weight, String effort) {
        double TMB = 0;
        double calories = 0;
        if (sex.equals("Male")) {
            TMB = 66.4730 + (13.7516 * weight) + (5.0033 * height) - (6.7550 * age);
        } else if (sex.equals("Female")) {
            TMB = 655.0955 + (9.5634 * weight) + (1.8449 * height) - (4.6756 * age);
        }
        Log.v("calories", String.valueOf(TMB));
        if (effort.equals("Low")) {
            calories = TMB * 1.375;
        } else if (effort.equals("High")) {
            calories = TMB * 1.725;
        } else if (effort.equals("Medium")) {
            calories = TMB * 1.55;
        } else if (effort.equals("VeryLow")) {
            calories = TMB * 1.2;
        } else if (effort.equals("VeryHigh")) {
            calories = TMB * 1.9;
        }
        return calories;
    }

    public RequestCaller getMasterCaller() {
        return masterCaller;
    }

    public void setMasterCaller(RequestCaller masterCaller) {
        this.masterCaller = masterCaller;
    }

    public Account getOkyLifeAccount() {
        return OkyLifeAccount;
    }

    public void setOkyLifeAccount(Account tasteThisAccount) {
        this.OkyLifeAccount = tasteThisAccount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

