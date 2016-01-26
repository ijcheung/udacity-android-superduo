package it.jaschke.alexandria.util;

import android.content.Context;
import android.widget.Toast;

public class Utilities {
    public static void showError(Context context, int resId){
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    private Utilities() {}
}