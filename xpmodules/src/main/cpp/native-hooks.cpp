//
// Created by che on 21-5-25.
//
#include <jni.h>
#include <string>
#include <sys/syscall.h>
#include <unistd.h>
#include <android/log.h>
#include <dlfcn.h>

#include "base/jvm.h"
#include "base/LogUtils.h"

#include "dobby.h"
#include "dlfcn_compat.h"


jstring (*source)(JNIEnv *env, jclass, jstring) = nullptr;

jstring myfun(JNIEnv *env, jclass clazz, jstring name) {
    LOGD("new_fun replace success!");

    std::string new_name = "America";
    jstring new_name1 = env->NewStringUTF(new_name.c_str());

    return source(env, clazz, new_name1);
}


// 这里出现问题了，我找不到原因啊!
void *findSoMethod(const char *soName, const char *method_name) {

    void *fd = dlopen(soName, RTLD_NOW);
    void *mptr = dlsym(fd, method_name);

    LOGD("Find fd: %p, mptr: %p", fd, mptr);
    return mptr;
}


//void hook_so(const char *soName, const char *method_name, void* new_fun, void**source) {
//    void *mptr = findSoMethod(soName, method_name);
//    if (DobbyHook((void *) mptr, (void *) new_fun, (void **) source) == RT_SUCCESS) {
//        LOGD("DobbyHook success!");
//    }
//}

extern "C"
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    setJvm(vm); // jvm全局

    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        LOGD("JNI_OnLoad failed!");
        return -1;
    }

//    hook_so("/data/app/com.example.evil-8b_4OQUOwpIObEQVYUkwWg==/lib/arm64/libanti.so",
//            "Java_com_example_evil_MainActivity_getMyString",
//            (void *) myfun,
//            &source);

    void *mptr = findSoMethod("/data/app/com.example.evil-Ir0o_9-p9XRFCyqD-6sR6g==/lib/arm64/libanti.so",
    "Java_com_example_evil_MainActivity_getMyString");

    if (DobbyHook((void *) mptr, (void *) myfun, (void **) source) == RT_SUCCESS) {
        LOGD("DobbyHook success!");
    } else {
        LOGD("DobbyHook failed!");
    }

    LOGD("JNI_OnLoad successed!");

    return JNI_VERSION_1_6;
}
