package com.example.evil;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class AntiRoot {

    private static final String LOG_TAG = "root-check";

    //////////////////////////////////////////////////////////////
    //
    // root方法检测:
    //  1. Build标志, Superuser/BusyBox文件查找和命令测试
    //  2. su路径查找和命令执行（主要)
    //  3. 文件系统访问测试
    //
    //////////////////////////////////////////////////////////////

    public static boolean isDeviceRooted() {
//        if (checkDeviceDebuggable()) {
//            return true;
//        } // check buildTags
//
//        if (checkSuperuserApk()) {
//            return true;
//        } // Superuser.apk
//
//        if (checkRootPathSU()) {
//            return true;
//        } // find su in some path
//
//        if (checkRootWhichSU()) {
//            return true;
//        } // find su use 'which'
//
//        if (checkBusybox()) {
//            return true;
//        }
//
//        if (checkAccessRootData()) {
//            return true;
//        }

        return checkGetRootAuth(); // 终极大招，命令执行方式
    }

    public static boolean checkDeviceDebuggable() {
        String buildTags = android.os.Build.TAGS;

        if (buildTags != null && buildTags.contains("test-keys")) {
            Log.i(LOG_TAG, "buildTags=" + buildTags);
            return true;
        }

        return false;
    }

    public static boolean checkSuperuserApk() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                Log.i(LOG_TAG, "/system/app/Superuser.apk exist");
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    public static boolean checkRootPathSU() {
        File f = null;
        final String[] kSuSearchPaths = {
                "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"
        };

        try {
            for (String kSuSearchPath : kSuSearchPaths) {
                f = new File(kSuSearchPath + "su");
                if (f.exists()) {
                    Log.i(LOG_TAG, "find su in : " + kSuSearchPath);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean checkRootWhichSU() {
        String[] strCmd = new String[]{"which", "su"};
        ArrayList<String> execResult = executeCommand(strCmd);

        if (execResult != null) {
            Log.i(LOG_TAG, "execResult=" + execResult.toString());
            return true;
        } else {
            Log.i(LOG_TAG, "execResult=null");
            return false;
        }
    }

    // 最终大招
    public static synchronized boolean checkGetRootAuth() {
        Process process = null;
        DataOutputStream out = null;

        try {
            Log.i(LOG_TAG, "exec su");
            process = Runtime.getRuntime().exec("su");
            out = new DataOutputStream(process.getOutputStream());
            out.writeBytes("exit\n");
            out.flush();

            int exitValue = process.waitFor();
            Log.i(LOG_TAG, "exitValue=" + exitValue);

            return exitValue == 0;
        } catch (Exception e) {
            Log.i(LOG_TAG, "Unexpected error - Here is what I know: " + e.getMessage());
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized boolean checkBusybox() {
        try {
            Log.i(LOG_TAG, "exec busybox df");
            String[] strCmd = new String[]{"busybox", "df"};
            ArrayList<String> execResult = executeCommand(strCmd);

            if (execResult != null) {
                Log.i(LOG_TAG, "execResult=" + execResult.toString());
                return true;
            } else {
                Log.i(LOG_TAG, "execResult=null");
                return false;
            }
        } catch (Exception e) {
            Log.i(LOG_TAG, "Unexpected error - Here is what I know: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<String> executeCommand(String[] shellCmd) {
        String line = null;
        ArrayList<String> fullResponse = new ArrayList<String>();
        Process localProcess = null;

        try {
            Log.i(LOG_TAG, "to shell exec which for find su :");
            localProcess = Runtime.getRuntime().exec(shellCmd);
        } catch (Exception e) {
            return null;
        }

        BufferedWriter out =
                new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
        BufferedReader in =
                new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
        try {
            while ((line = in.readLine()) != null) {
                Log.i(LOG_TAG, "–> Line received: " + line);
                fullResponse.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(LOG_TAG, "–> Full response was: " + fullResponse);
        return fullResponse;
    }

    // 受限文件系统测试访问
    public static synchronized boolean checkAccessRootData() {
        try {
            Log.i(LOG_TAG, "to write /data");
            String fileContent = "test_ok";
            Boolean writeFlag = writeFile("/data/su_test", fileContent);
            if (writeFlag) {
                Log.i(LOG_TAG, "write ok");
            } else {
                Log.i(LOG_TAG, "write failed");
            }

            Log.i(LOG_TAG, "to read /data");
            String strRead = readFile("/data/su_test");
            Log.i(LOG_TAG, "strRead=" + strRead);
            return fileContent.equals(strRead);
        } catch (Exception e) {
            Log.i(LOG_TAG, "Unexpected error - Here is what I know: " + e.getMessage());
            return false;
        }
    }

    // 写文件
    public static Boolean writeFile(String fileName, String message) {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            byte[] bytes = message.getBytes();
            out.write(bytes);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 读文件
    public static String readFile(String fileName) {
        File file = new File(fileName);
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int len;
            while ((len = in.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }

            String result = new String(out.toByteArray());
            Log.i(LOG_TAG, result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
