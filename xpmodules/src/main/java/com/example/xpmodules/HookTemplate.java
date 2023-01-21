package com.example.xpmodules;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.util.List;

public class HookTemplate implements IXposedHookLoadPackage {
    public Context mContext;
    public ClassLoader mLoader;

    public String packageName = "com.example.evil";
    public String className = "com.example.evil.MainActivity";
    public String methodName = "doLoad";

    public String targetSo = "libanti.so";
    public String injectedSo = "libnative-hooks.so";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(packageName)) {
            XposedBridge.log("Hook package --> " + packageName);
            try {
                HookAttach(lpparam);
            } catch (Throwable e) {
                Log.e("Che", "发现异常: " + e);
                e.printStackTrace();
            }
        }
    }

    private void HookAttach(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(
                Application.class,
                "attach",
                Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        mContext = (Context) param.args[0];
                        mLoader = mContext.getClassLoader();
                    }
                });

        XposedHelpers.findAndHookMethod(
                Runtime.class,
                "doLoad",
                String.class,
                ClassLoader.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String name = (String) param.args[0];

                        XposedBridge.log("Hook native lib --> " + name);
                        if (name.trim().contains(targetSo)) {
                            injectSo(param.args[1]);
                        }
                    }
                });
    }

    public void injectSo(Object ob) {
        String soPath = getSoPath();

        Log.d("Che", "so:" + soPath + "开始注入...");

        try {
            // 没成功啊？
            XposedHelpers.callMethod(Runtime.getRuntime(), "doLoad", soPath, ob);
            Log.d("Che", "so注入成功!");
        } catch (Throwable e) {
            Log.e("Che", e.getMessage());
            e.printStackTrace();
        }
    }


    public String getSoPath() {
        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> plist = pm.getInstalledPackages(0);

        if (plist.size() > 0) {
            for (PackageInfo pi : plist) {
                if (pi.applicationInfo.publicSourceDir.startsWith("/data/app/" + pi.packageName)) {
                    String path = pi.applicationInfo.publicSourceDir.replace("base.apk", "lib/arm64/");
                    injectedSo = path + injectedSo;

                    Log.d("Che", "获得so文件路径: " + injectedSo);
                    return injectedSo;
                }
            }
        }

        Log.e("Che", "没找到注入的so路径");

        return null;
    }
}
