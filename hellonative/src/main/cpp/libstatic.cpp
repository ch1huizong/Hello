#include <jni.h>
#include <string>
#include <stdio.h>
#include <sys/syscall.h>
#include <unistd.h>
#include <android/log.h>
#include <pthread.h>
#include <queue>

#include "base/LogUtils.h"
#include "people/People.h"
#include "MyTest.h"
#include "base/FileUtils.h"
#include "inlineHook/inlineHook.h"
#include "include/substrate.h"
#include "dobby.h"


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_hellonative_MainActivity_stringFromJNI(JNIEnv *env, jclass clazz) {
    People people;
    return env->NewStringUTF(people.getString().c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_hellonative_MainActivity_Hello1(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello1";
    fun_test(6);
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_hellonative_MainActivity_Hello2(JNIEnv *env, jobject thiz, jint a) {
    return a;
}

// 验证ARM下函数调用时的参数传递顺序
int sum_three_and_mul(int a, int b, int c, int d, int e, int f) {
    int t1 = a + b + c;
    int t2 = d + e + f;
    int t3 = t1 * t2;
    return t3;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_hellonative_MainActivity_TestFunCall(JNIEnv *env, jobject thiz, jint a, jint b,
                                                      jint c, jint d, jint e, jint f) {
    LOGD("Sum by three units group and multipy!");
    jint result = sum_three_and_mul(a, b, c, d, e, f);

    return result;
}

int global_v1 = 1; // 已初始化全局
int global_v2; // 未初始化全局

#define PI 3.1415926

extern "C"
JNIEXPORT void JNICALL
Java_com_example_hellonative_MainActivity_TestCompile(JNIEnv *env, jobject thiz) {
    static int local_static_v3 = 3;
    static int local_static_v4;

    int local_v5 = 5;
    int local_v6;

    const float PII = 3.14; // 常量

    Point *myptr = point;
    int *p = &number;

    LOGD("myptr x: %d, y:%d", myptr->getX(), myptr->getY());
    LOGD("*p = %d", *p);

    clock_t t = clock();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_hellonative_MainActivity_MySyscall(JNIEnv *env, jobject thiz) {
    char *filename = "/sys/class/net/wlan0/address";
    std::string result = getFileText(filename, 1024);
    LOGD("Result is: %s", result.c_str());
}


long inline_add(long base) {
    long result = 0;
    /* 32位的内联汇编
    __asm__ __volatile__ (
        "MOV r0, %[base]\r\n"
        "ADD r0, r0\r\n"
        "MOV %[result], r0\r\n"

        :[result] "=r" (result)
        :[base] "r" (base)
    );
     */

    return result;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_hellonative_MainActivity_InlineAsm(JNIEnv *env, jclass clazz) {
    // do add
    long r = inline_add(100);
    LOGD("inline_add result: %d", r);
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_hellonative_MainActivity_toHooked(JNIEnv *env, jclass clazz, jstring name) {
    const char *name1 = env->GetStringUTFChars(name, 0);
    LOGD("Got name: %s", name1);
    env->ReleaseStringUTFChars(name, name1);

    return name;
}

jstring (*old_fun)(JNIEnv *env, jclass, jstring) = nullptr;

jstring new_fun(JNIEnv *env, jclass clazz, jstring name) {
    LOGD("new_fun replace success!");

    std::string new_name = "CheHuiZong";
    jstring new_name1 = env->NewStringUTF(new_name.c_str());

    return old_fun(env, clazz, new_name1);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_hellonative_MainActivity_startHook(JNIEnv *env, jclass clazz) {

    /* inlineHook 只有32位
    if (registerInlineHook((uint32_t)Java_com_example_hellonative_MainActivity_toHooked, (uint32_t) new_fun, (uint32_t **) &old_fun) != ELE7EN_OK) {
        return;
    }

    if (inlineHook((uint32_t)Java_com_example_hellonative_MainActivity_toHooked) != ELE7EN_OK) {
        return;
    }
     */

    // Substrate 总是出现错误
    //MSHookFunction((void*)Java_com_example_hellonative_MainActivity_toHooked, (void*)new_fun, (void**) &old_fun);

    if (DobbyHook((void *) Java_com_example_hellonative_MainActivity_toHooked, (void *) new_fun,
                  (void **) &old_fun) == RT_SUCCESS) {
        LOGD("DobbyHook success!");
    }

}

// 观察C代码转化成的ARM汇编代码
void if_code(int a) {
    if (a > 5) {
        printf("AAAAA");
    }
}

void ifelse_code(int a) {
    if (a > 5) {
        printf("AAAAA");
    } else if (a == 5) {
        printf("BBBBB");
    } else {
        printf("CCCCC");
    }
}

void switch_code(int a) {
    switch (a) {
        case 1:
            printf("AAAAA");
            break;
        case 2:
            printf("BBBBB");
            break;
        case 3:
            printf("CCCCC");
            break;
        case 4:
            printf("DDDDD");
            break;
        case 5:
            printf("EEEEE");
            return;
    }
}

void for_code(int a) {
    for (int i = 0; i < 5; ++i) {
        printf("AAAAA");
    }
}

void while_code(int a) {
    while (a > 0) {
        printf("AAAAA");
        a--;
    }
}

// 函数测试
void fun_test(int a) {
    if_code(a);
    ifelse_code(a);
    switch_code(a - 1);
    for_code(a);
    while_code(a);
}