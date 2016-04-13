package co.profapps.quitoseguro.firebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

public final class FirebaseHelper {
    private static final String APP_URL = "https://quito-seguro.firebaseio.com";
    private static boolean initialized = false;

    private FirebaseHelper() {}

    public static void initialize(@NonNull Context context) {
        Firebase.setAndroidContext(context);
        initialized = true;
    }

    public static Firebase getAppRef() {
        if (!initialized) {
            throw new IllegalStateException("Firebase is not initialized.");
        }

        return new Firebase(APP_URL);
    }

    public static Firebase getReportsDataRef() {
        if (!initialized) {
            throw new IllegalStateException("Firebase is not initialized.");
        }

        return new Firebase(String.format("%s/reports", APP_URL));
    }

    public static Firebase getTipsDataRef() {
        if (!initialized) {
            throw new IllegalStateException("Firebase is not initialized.");
        }

        return new Firebase(String.format("%s/tips", APP_URL));
    }

    public static Query getReportsQueryRef(String offenseFilter) {
        if (!initialized) {
            throw new IllegalStateException("Firebase is not initialized.");
        }

        if (offenseFilter != null) {
            return getReportsDataRef().orderByChild("offense").equalTo(offenseFilter);
        } else {
            return getReportsDataRef().orderByChild("offense");
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
