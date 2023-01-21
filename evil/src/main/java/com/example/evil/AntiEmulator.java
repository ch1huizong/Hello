package com.example.evil;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;

// 模拟器检测的一些条件
public class AntiEmulator {

    private static String[] known_device_ids = {
            "000000000000000",
    };

    private static String[] known_imsi_ids = {
            "310259999999998",
    };

    private static String[] known_numbers = {
            "15555255554", "15555215556",
    };


    private static String[] known_files = {
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props",
    };


    public static Boolean checkDeviceIDS(Context context) {
        TelephonyManager t = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String device_id = t.getDeviceId();
        Log.e("Che", "Device Id => " + device_id);

        for (String know_device_id : known_device_ids) {
            if (know_device_id.equalsIgnoreCase(device_id)) {
                Log.d("Che", "Found ids: 000000000000000");
                return true;
            }
        }

        Log.d("Che", "Not Found ids: 000000000000000");
        return false;
    }


    public static Boolean checkImsiIDS(Context context) {
        TelephonyManager t = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String imsi_id = t.getSubscriberId();
        Log.e("Che", "IMSI Id => " + imsi_id);

        for (String id : known_imsi_ids) {
            if (id.equalsIgnoreCase(imsi_id)) {
                Log.d("Che", "Found imsi id: 310260000000000!");
                return true;
            }
        }

        Log.d("Che", "Not Found imsi id: 310260000000000!");
        return false;
    }


    // 检查手机电话号码
    public static Boolean checkPhoneNumber(Context context) {
        TelephonyManager t = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String phone_number = t.getLine1Number();
        Log.e("Che", "Phone Number => " + phone_number);

        for (String number : known_numbers) {
            if (number.equalsIgnoreCase(phone_number)) {
                Log.d("Che", "Found PhoneNumber!");
                return true;
            }
        }

        Log.d("Che", "Not Found PhoneNumber!");
        return false;
    }


    // 检查模拟器上的文件
    public static Boolean checkEmulatorFiles() {
        for (int i = 0; i < known_files.length; i++) {
            String name = known_files[i];
            File file = new File(name);
            if (file.exists()) {
                Log.d("Che", "Found Emulator Files!");
                return true;
            }
        }

        Log.d("Che", "Not Found Emulator Files!");
        return false;
    }


    // 检测手机上的的一些硬件信息
    public static Boolean checkEmulatorBuild() {
        String BOARD = Build.BOARD;
        String BOOTLOADER = Build.BOOTLOADER;
        String BRAND = Build.BRAND;
        String DEVICE = Build.DEVICE;
        String HARDWARE = Build.HARDWARE;
        String MODEL = Build.MODEL;
        String PRODUCT = Build.PRODUCT;

        if (BOARD == "unknown" || BOOTLOADER == "unknown"
                || BRAND == "generic" || DEVICE == "generic"
                || MODEL == "sdk" || PRODUCT == "sdk"
                || HARDWARE == "goldfish") {
            Log.d("Che", "Found Emulator by Emulator Build!");
            return true;
        }

        Log.d("Che", "Not Found Emulator by Emulator Build!");
        return false;
    }


    // 检测手机运营商信息
    public static Boolean checkOperatorNameAndroid(Context context) {
        String op = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
        Log.e("Che", "Phone Operator => " + op);

        if (op.toLowerCase().equals("android")) {
            Log.d("Che", "Found Emulator by OperatorName!");
            return true;
        }

        Log.d("Che", "Not Found Emulator by OperatorName!");
        return false;
    }
}