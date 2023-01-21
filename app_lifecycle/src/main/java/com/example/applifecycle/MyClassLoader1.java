package com.example.applifecycle;

import android.content.Context;

import java.io.File;

import dalvik.system.DexClassLoader;

public class MyClassLoader1 extends ClassLoader {
    private DexClassLoader m;

    public MyClassLoader1(Context context, String dexpath) {
        File optfile = context.getDir("opt_dex", 0);
        File libfile = context.getDir("lib_path", 0);

         m = new DexClassLoader(dexpath, optfile.getAbsolutePath(), libfile.getAbsolutePath(), context.getClassLoader());
    }

    public DexClassLoader getMyClassLoader() {
        return m;
    }
}
