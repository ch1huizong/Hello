#include <jni.h>
#include <string>
#include <iostream>
#include <android/log.h>


//__attribute__ ((visibility ("hidden")))
extern "C"
JNIEXPORT
jint cal_sum_five(jint a, jint b, jint c, jint d, jint e) {
    return a + b + c + d + e;
}

__attribute__ ((visibility ("hidden")))
jint hello3(JNIEnv *env, jobject obj, jobject ob, jint a, jint b) { // 验证文件内函数跳转
    jint c = 33, d = 44, e = 55;
    jint result = cal_sum_five(a, b, c, d, e);
    return result;
}

__attribute__ ((visibility ("hidden")))
jint acc(JNIEnv *env, jobject thiz, jint a) {
    int result = 0;
    for (int i = 1; i <= a; i++) {
        result += i;
    }
    return result;
}


////////////////////////////////////////////////////////////
//
// 动态注册部分
//

static const char *className = "com/example/hellonative/MainActivity";

// 找到的java类中方法和本地方法的对应表
static JNINativeMethod methods[] = {
        {"Dyn_Hello3",  "(Lcom/example/hellonative/MainActivity;II)I", (void *) hello3},
        {"Dyn_Acc",     "(I)I",                                        (void *) acc},
};

// 我的注册帮助方法
static int
MyRegisterNatives(JNIEnv *env, const char *className, JNINativeMethod *methods, int nMethods) {
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, methods, nMethods) < -1) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

// 加载注册
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        __android_log_print(4, "Che", "This jni version is not supported!");
        return JNI_ERR;
    }

    if (MyRegisterNatives(env, className, methods, sizeof(methods) / sizeof(methods[0])) <
        0) {
        __android_log_print(4, "Che", "Unable to register native methods!");
        return JNI_ERR;
    }

    __android_log_print(4, "Che", "So file <dynamic-lib> loaded success");
    return JNI_VERSION_1_6;
}