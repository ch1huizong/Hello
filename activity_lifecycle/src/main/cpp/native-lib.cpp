#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_che_helloworld_MainActivity_stringFromJNI( JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    __android_log_print(ANDROID_LOG_DEBUG, "Che", "Log in native file!");

    return env->NewStringUTF(hello.c_str());
}

