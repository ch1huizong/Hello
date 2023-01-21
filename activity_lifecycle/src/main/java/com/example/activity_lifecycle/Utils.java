package com.example.activity_lifecycle;

import android.util.Log;

public class Utils {

    public static void MLog(String Tag, String msg) {
        if (msg.length() > 4000) {
            Log.d(Tag, "msg.length = " + msg.length());
            int chunkCount = msg.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= msg.length()) {
                    Log.d(Tag, "chunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i));
                } else {
                    Log.d(Tag, "chunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i, max));
                }
            }
        }
    }
}
