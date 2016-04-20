package co.profapps.quitoseguro.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.profapps.quitoseguro.R;

public final class AppUtils {
    public static final LatLng QUITO_LAT_LNG = new LatLng(-0.2166667, -78.5166667);
    public static final String PLAY_STORE_URL =
            "https://play.google.com/store/apps/details?id=co.profapps.quitoseguro";

    private AppUtils() {}

    public static void showDialog(@NonNull Context context, String title,
                                  @NonNull String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static String getFormattedDate(@NonNull Date date) {
        return new SimpleDateFormat("EEEE MMMM d, yyyy", Locale.getDefault()).format(date);
    }

    public static void setAppBarColorOnRecentWindows(@NonNull Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
                    R.mipmap.ic_launcher);

            ActivityManager.TaskDescription taskDescription = null;
            taskDescription = new ActivityManager
                    .TaskDescription(activity.getTitle().toString(), bitmap, Color.rgb(17, 16, 29));

            activity.setTaskDescription(taskDescription);
        }
    }

    public static boolean appIsInstalled(@NonNull Context context, @NonNull String packageName) {
        PackageManager packageManager = context.getPackageManager();

        try {
            return packageManager.getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES) != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isValidLocation(@NonNull Location location) {
        return (location.getLatitude() < -0.0413704 &&  location.getLatitude() > -0.3610194) &&
                (location.getLongitude() < -78.4078789 && location.getLongitude() > -78.5881530);
    }

    public static boolean isValidLocation(@NonNull LatLng location) {
        return (location.latitude < -0.0413704 &&  location.latitude > -0.3610194) &&
                (location.longitude < -78.4078789 && location.longitude > -78.5881530);
    }
}
