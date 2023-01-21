//
// Created by che on 21-5-25.
//
#include <jni.h>
#include <string>
#include <stdio.h>
#include <sys/syscall.h>
#include <unistd.h>
#include <android/log.h>
#include <pthread.h>
#include <queue>

#include "people/People.h"
#include "base/jvm.h"
#include "base/LogUtils.h"

static jclass threadClazz;
static jmethodID threadMethod;
static jobject threadObject;

void *threadCallBack(void *);


// JNI基础数据类型的转换
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_jni_MainActivity_callNativeInt(JNIEnv *env, jclass clazz, jint a) {
    LOGD("Java int value is %d", a);
    jint result = (jint) 100 * a;
    return result;
}

extern "C"
JNIEXPORT jbyte JNICALL
Java_com_example_jni_MainActivity_callNativeByte(JNIEnv *env, jclass clazz, jbyte a) {
    LOGD("Java byte value is %d", a);
    jbyte b = a + (jbyte) 10;
    return b;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_jni_MainActivity_callNativeBool(JNIEnv *env, jclass clazz, jboolean a) {
    LOGD("Java boolean value is %d", a);
    jboolean b = (jboolean) !a;
    return b;
}

extern "C"
JNIEXPORT jchar JNICALL
Java_com_example_jni_MainActivity_callNativeChar(JNIEnv *env, jclass clazz, jchar a) {
    LOGD("Java char value is %c", a);
    jchar c = a + (jchar) 3;
    return c;
}


////////////////////////////////////////////////////////////
// 字符串操作
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_jni_MainActivity_callNativeString(JNIEnv *env, jclass clazz, jstring jstr) {
    const char *s = env->GetStringUTFChars(jstr, 0); // 获得C风格的字符串
    LOGD("--Got java string value is %s", s);

    env->ReleaseStringUTFChars(jstr, s); // 记得释放，防止内存泄漏
    return env->NewStringUTF("This is a C style string");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_stringMethod(JNIEnv *env, jclass clazz, jstring jstr) {
    const char *s = env->GetStringUTFChars(jstr, 0);
    char buff[128];

    int len = env->GetStringLength(jstr); // jni中提供的字符串操作函数
    LOGD("Got java string length is %d", len);

    env->GetStringUTFRegion(jstr, 0, len, buff); // 字符串切片
    buff[len] = '\0';
    LOGD("jstring is %s", buff);

    env->ReleaseStringUTFChars(jstr, s);
}

// 对数组的操作: toAdd
// env->GetIntArrayElements会获取数组指针， 然后可以a[i]索引访问了
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_jni_MainActivity_callNativeStringArray(JNIEnv *env, jclass clazz, jobjectArray arr) {
    int len = env->GetArrayLength(arr); // 获取数组长度
    LOGD("len is %d", len);

    auto first = static_cast<jstring>(env->GetObjectArrayElement(arr, 0)); // 取值

    const char *str = env->GetStringUTFChars(first, 0);
    LOGD("first string is %s", str); // 打印C风格字符串
    env->ReleaseStringUTFChars(first, str);


    return env->NewStringUTF(str);
}


////////////////////////////////////////////////////////////
// jni静态字段和实例字段
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_accessInstanceField(JNIEnv *env, jclass clazz, jobject a) { // jobject a是Animal类
    jclass cls = env->GetObjectClass(a);
    jfieldID fid = env->GetFieldID(cls, "name", "Ljava/lang/String;");
    jstring str = env->NewStringUTF("New Animal Name");
    env->SetObjectField(a, fid, str); // 修改实例字段
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_accessStaticField(JNIEnv *env, jclass clazz, jobject a) {
    jclass cls = env->GetObjectClass(a);
    jfieldID fid = env->GetStaticFieldID(cls, "num", "I");
    int num = env->GetStaticIntField(cls, fid);
    env->SetStaticIntField(cls, fid, ++num); // 修改类字段
}


////////////////////////////////////////////////////////////
// 静态方法和实例方法
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_accessInstanceMethod(JNIEnv *env, jclass clazz, jobject a) {
    jclass cls = env->GetObjectClass(a);
    jmethodID mid = env->GetMethodID(cls, "callInstanceMethod", "(I)V");
    env->CallVoidMethod(a, mid, 2222);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_accessStaticMethod(JNIEnv *env, jclass clazz, jobject a) {
    jclass cls = env->GetObjectClass(a);
    jmethodID mid = env->GetStaticMethodID(cls, "callStaticMethod", "(Ljava/lang/String;)Ljava/lang/String;");
    jstring str = env->NewStringUTF("C++ String");
    env->CallStaticObjectMethod(cls, mid, str);

    // 重载方法的调用
    mid = env->GetStaticMethodID(cls, "callStaticMethod", "([Ljava/lang/String;I)Ljava/lang/String;");

    //构造参数
    jclass strClass = env->FindClass("java/lang/String");
    int size = 2;
    jobjectArray strArray = env->NewObjectArray(size, strClass, nullptr);

    jstring item;
    for (int i = 0; i < size; ++i) {
        item = env->NewStringUTF("string in native");
        env->SetObjectArrayElement(strArray, i, item); // !这里对数组赋值
    }
    env->CallStaticObjectMethod(cls, mid, strArray, size);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_nativeCallBack(JNIEnv *env, jclass clazz, jobject c) {
    LOGD("nativeCallBack");

    jclass cls = env->GetObjectClass(c);
    jmethodID mid = env->GetMethodID(cls, "callback", "()V");
    env->CallVoidMethod(c, mid);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_nativeThreadCallBack(JNIEnv *env, jclass clazz, jobject c) {
    threadObject = env->NewGlobalRef(c);
    threadClazz = env->GetObjectClass(c);
    threadMethod = env->GetMethodID(threadClazz, "callback", "()V");

    pthread_t handle;
    pthread_create(&handle, nullptr, threadCallBack, nullptr); // 创建线程,这个方法在新线程里运行
}

// 子线程中回调java的方法，注意env不能跨线程, 所以关键是获取env变量
void *threadCallBack(void *) {
    JavaVM *gvm = getJvm();
    JNIEnv *env = nullptr;

    if (gvm->AttachCurrentThread(&env, nullptr) == 0) {
        env->CallVoidMethod(threadObject, threadMethod);
        gvm->DetachCurrentThread();
    }

    return nullptr;
}

//  创建对象
extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_jni_MainActivity_invokeAnimalConstructor(JNIEnv *env, jclass clazz) {
    jclass cls = env->FindClass("com/example/jni/Animal");
    jmethodID mid = env->GetMethodID(cls, "<init>", "(Ljava/lang/String;)V");
    jstring str = env->NewStringUTF("Dog1");
    jobject a = env->NewObject(cls, mid, str); // 创建对象，方法一

    return a;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_jni_MainActivity_allocObjectConstructor(JNIEnv *env, jclass clazz) {
    jclass cls = env->FindClass("com/example/jni/Animal");
    jmethodID mid = env->GetMethodID(cls, "<init>", "(Ljava/lang/String;)V");
    jstring str = env->NewStringUTF("Dog2");

    jobject a = env->AllocObject(cls); // 创建对象，未初始化，方法二
    env->CallNonvirtualVoidMethod(a, cls, mid, str); // 初始化对象

    return a;
}


////////////////////////////////////////////////////////////
// 局部/全局/弱引用/异常处理
// "引用"其实就是变量的生存期
extern "C"
JNIEXPORT jstring JNICALL // 局部引用
Java_com_example_jni_MainActivity_errorCacheLocalRef(JNIEnv *env, jclass clazz) {
    jclass localRefs = env->FindClass("java/lang/String");
    jmethodID mid = env->GetMethodID(localRefs, "<init>", "(Ljava/lang/String;)V");
    jstring str = env->NewStringUTF("Locals");

    // 避免局部引用过多, 维护的局部引用表过大，局部需要提前释放
    for (int i = 0; i < 1000; ++i) {
        jclass cls = env->FindClass("java/lang/String");
        env->DeleteLocalRef(cls); // 局部引用管理问题:1. 自动释放 2. delete 3. jni Api管理(push/pop)
    }

    return static_cast<jstring>(env->NewObject(localRefs, mid, str));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_jni_MainActivity_cacheWithGlobalRef(JNIEnv *env, jclass clazz) {
    static jclass stringClass = nullptr;  // 全局引用，用作缓冲

    if (stringClass == nullptr) {
        jclass cls = env->FindClass("java/lang/String");
        stringClass = static_cast<jclass>(env->NewGlobalRef(cls)); // 不能NewLocalRef
        env->DeleteLocalRef(cls);
    } else {
        LOGD("use cached");
    }

    jmethodID mid = env->GetMethodID(stringClass, "<init>", "(Ljava/lang/String;)V");
    jstring str = env->NewStringUTF("Globals");

    return static_cast<jstring>(env->NewObject(stringClass, mid, str));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_useWeakGlobalRef(JNIEnv *env, jclass clazz) {
    static jclass stringClass = nullptr;

    if (stringClass == nullptr) {
        jclass cls = env->FindClass("java/lang/String");
        stringClass = static_cast<jclass>(env->NewWeakGlobalRef(cls)); // 1. 可以跨函数、跨线程 2. 不能阻止gc回收
        env->DeleteLocalRef(cls);
    } else {
        LOGD("use weak cached");
    }

    jmethodID mid = env->GetMethodID(stringClass, "<init>", "(Ljava/lang/String;)V");
    jboolean isGc = env->IsSameObject(stringClass, nullptr);

    // Others
}

// java中本身方法有问题，native来捕获并解决
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_nativeInvokeJavaException(JNIEnv *env, jclass clazz, jobject m) {
    jclass cls = env->GetObjectClass(m);
    jmethodID mid = env->GetMethodID(cls, "operation", "()I");
    env->CallIntMethod(m, mid);

    // 保存错误, native的处理手段
    jthrowable e = env->ExceptionOccurred();
    if (e) {
        env->ExceptionDescribe();
        env->ExceptionClear();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_nativeThrowException(JNIEnv *env, jclass clazz) {
    jclass cls = env->FindClass("java/lang/IllegalArgumentException");
    env->ThrowNew(cls, "C++ throw exception"); // 自己抛出异常，让jvm去处理
}


////////////////////////////////////////////////////////////
// jni中线程的创建、销毁、等待和同步（都在一个进程里)

void *printThreadHello(void *) {
    LOGD("hello thread");
    //pthread_exit(0);
    return nullptr;  // 必须有返回语句
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_createNativeThread(JNIEnv *env, jclass clazz) {
    pthread_t handle;
    int result = pthread_create(&handle, nullptr, printThreadHello, nullptr);
    if (result == 0) {
        LOGD("create thread success");
    } else {
        LOGD("create thread failed");
    }
}

struct ThreadRunArgs {
    int id;
    int result;
};

void *printThreadArgs(void *arg) {
    ThreadRunArgs *args = static_cast<ThreadRunArgs *>(arg);
    LOGD("thread id is %d", args->id);
    LOGD("thread result is %d", args->result);
    return nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_createNativeThreadWithArgs(JNIEnv *env, jclass clazz) {
    pthread_t handle;
    ThreadRunArgs *args = new ThreadRunArgs; // 创建结构体指针
    args->id = 2;
    args->result = 100;

    int result = pthread_create(&handle, nullptr, printThreadArgs, args);
    if (result == 0) {
        LOGD("create thread success");
    } else {
        LOGD("create thread failed");
    }
}

void *printThreadJoin(void *arg) {
    ThreadRunArgs *args = static_cast<ThreadRunArgs *>(arg);
    struct timeval begin;
    gettimeofday(&begin, nullptr);

    sleep(3);

    struct timeval end;
    gettimeofday(&end, nullptr);

    LOGD("Time used is %ld", end.tv_sec - begin.tv_sec);
    return reinterpret_cast<void *>(args->result);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_joinNativeThread(JNIEnv *env, jclass clazz) {
    pthread_t handle;
    ThreadRunArgs *args = new ThreadRunArgs;
    args->id = 2;
    args->result = 100;

    int result = pthread_create(&handle, nullptr, printThreadJoin, args);
    void *ret = nullptr;
    if (result == 0) {
        pthread_join(handle, &ret);
        LOGD("result is %ld", ret);
    }
}


// 线程的同步 - 模型
// 这里的控制条件: lock, cond, flag
pthread_mutex_t mutex; // 互斥锁
pthread_cond_t cond; // 信号量
pthread_t waitHandle;
pthread_t notifyHandle;

int flag = 0; // 全局控制

void *waitThread(void *) {
    LOGE("wait thread lock");
    pthread_mutex_lock(&mutex); // ---

    while (flag == 0) {
        LOGE("Waiting...");
        pthread_cond_wait(&cond, &mutex); // 会释放锁，并等待信号量被唤醒, 处于阻塞状态
    }

    LOGE("wait thread unlock");
    pthread_mutex_unlock(&mutex);

    pthread_exit(0);
}

void *notifyThread(void *) {
    LOGW("notify thread lock");
    pthread_mutex_lock(&mutex); // ---

    flag = 1;

    LOGW("notify thread unlock");
    pthread_mutex_unlock(&mutex);

    pthread_cond_signal(&cond);
    LOGW("signal...");

    pthread_exit(0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_waitNativeThread(JNIEnv *env, jclass clazz) {
    pthread_mutex_init(&mutex, nullptr);
    pthread_cond_init(&cond, nullptr);

    pthread_create(&waitHandle, nullptr, waitThread, nullptr);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_notifyNativeThread(JNIEnv *env, jclass clazz) {
    pthread_create(&notifyHandle, nullptr, notifyThread, nullptr);
}


////////////////////////////////////////////////////////////
// 生产者-消费者模型(可以扩展)
// bug出现点: 代码段直接的间隙
// 线程的低级原语， 有其产生的意义，促成了高级语言方便化发展

std::queue<int> data;
pthread_mutex_t dataMutex;
pthread_cond_t dataCond;

void *productThread(void *) {
    while (data.size() < 10) {
        pthread_mutex_lock(&dataMutex);

        LOGD("生产物品");

        data.push(1);  // 生产任务

        if (!data.empty()) {
            LOGD("等待消费");
            pthread_cond_signal(&dataCond);
        }

        pthread_mutex_unlock(&dataMutex);
        sleep(3);
    }

    pthread_exit(0);
}

[[noreturn]] void *consumerThread(void *) {
    while (true) {
        pthread_mutex_lock(&dataMutex);
        if (!data.empty()) {
            LOGD("消费物品");
            data.pop();
        } else {
            LOGD("等待生产");
            pthread_cond_wait(&dataCond, &dataMutex);
        }
        pthread_mutex_unlock(&dataMutex);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jni_MainActivity_startProductAndConsumerThread(JNIEnv *env, jclass clazz) {
    pthread_mutex_init(&dataMutex, nullptr);
    pthread_cond_init(&dataCond, nullptr);

    pthread_t product;
    pthread_t consumer;

    pthread_create(&product, nullptr, productThread, nullptr);
    pthread_create(&consumer, nullptr, consumerThread, nullptr);
}


JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    setJvm(vm); // jvm全局
    return JNI_VERSION_1_6;
}

