package com.example.evil;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends Activity {
    public static final String TAG = "isEmulator";
    public static final String SIG = "Signature";
    public static final String ROOT = "Root";
    public static final int hashcode = -2024413744;

    public String name = "CheHuiZong";
    public String job = "Python Programmer";

    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int REQUEST_READ_SMS = 1;
    private Boolean read_phone_state = true;
    private Boolean read_sms = true;
    private final boolean root = false;

    static {
        System.loadLibrary("anti");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VpnCheck.vpncheck();

        //
        // 对抗
        //
        // 1. 混淆开启, 去除日志开启
        // 2. 对抗反编译工具 - (资源修改)
        // 3. 模拟器检测
        // 4. 对抗apk重打包开启(签名校验 - 不是安装时的签名校验，是App运行时的签名校验）
        // 5. so文件防调试
        // 6. 类/方法 - 空白混淆
        // 7. 字符串 - 混淆加密
        // 8. root检测
        //
        // 9. native层ollvm
        //

        Log.d("Che", "字符串测试: " + name + " - " + job);
        // Log.d("Che", "[Java - getMyString]:" + getMyString("China"));

        TestA.print();

        TextView tv = findViewById(R.id.textView);
        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(getMyString("China"));
            }
        });

        // Root检测
        try {
            getRootReports();
        } catch (Exception e) {
            return;
        }

        // 权限检查和模拟器检测
        try {
            checkPermissions();
        } catch (Exception e) {
            Log.d("Che", "请申请权限！");
            return;
        }

        if (read_phone_state && read_sms) {
            getEmulatorReports();
        }

        // Java层加入防止重打包技术
        Log.d(SIG, "getSignature: " + getSignature("com.example.evil"));
        if (getSignature("com.example.evil") != hashcode) {
            Toast.makeText(this, "app被重新打包了", Toast.LENGTH_LONG).show();
            // finish();
        }
    }

    public void getRootReports() {
        Log.d(ROOT, "checkDeviceDebuggable => " + AntiRoot.checkDeviceDebuggable());
        Log.d(ROOT, "checkSuperuserApk => " + AntiRoot.checkSuperuserApk());
        Log.d(ROOT, "checkRootPathSU => " + AntiRoot.checkRootPathSU());
        Log.d(ROOT, "checkRootWhichSU => " + AntiRoot.checkRootWhichSU());
        Log.d(ROOT, "checkBusybox => " + AntiRoot.checkBusybox());
        Log.d(ROOT, "checkAccessRootData => " + AntiRoot.checkAccessRootData());
        Log.d(ROOT, "checkGetRootAuth => " + AntiRoot.checkGetRootAuth());

        boolean isroot = AntiRoot.isDeviceRooted();
        if (isroot) {
            Log.d(ROOT, "手机已经root！");
        } else {
            Log.d(ROOT, "没root！");
        }
    }

    public void getEmulatorReports() {
        Log.d(TAG, "checkDeviceID => " + AntiEmulator.checkDeviceIDS(this));
        Log.d(TAG, "checkIMSIID => " + AntiEmulator.checkImsiIDS(this));
        Log.d(TAG, "checkPhoneNumber => " + AntiEmulator.checkPhoneNumber(this));
        Log.d(TAG, "checkEmulatorFiles => " + AntiEmulator.checkEmulatorFiles());
        Log.d(TAG, "checkEmulatorBuild => " + AntiEmulator.checkEmulatorBuild());
        Log.d(TAG, "checkOperatorNameAndroid => " + AntiEmulator.checkOperatorNameAndroid(this));
    }

    // 这里的代码是否可以再优化一下?
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermissions() {
        if (checkSelfPermission("android.permission.READ_PHONE_STATE")
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.READ_PHONE_STATE"}, 0);
        } else {
            read_phone_state = true;
        }

        if (checkSelfPermission("android.permission.READ_SMS")
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.READ_SMS"}, 1);
        } else {
            read_sms = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                if ((grantResults.length > 0)
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    read_phone_state = true;
                }
                break;
            }

            case REQUEST_READ_SMS: {
                if ((grantResults.length > 0)
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    read_sms = true;
                }
                break;
            }

            default:
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 获取签名证书的的hash值
    public int getSignature(String packageName) {
        PackageManager pm = this.getPackageManager();
        PackageInfo pi = null;
        int sig = 0;

        try {
            pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] s = pi.signatures;
            sig = s[0].hashCode();
        } catch (Exception e) {
            sig = 0;
            e.printStackTrace();
        }
        return sig;
    }

    public native String getMyString(String str);
}
