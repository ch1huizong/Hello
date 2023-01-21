#include <jni.h>
#include <string>
#include <android/log.h>

extern "C"
JNIEXPORT void JNICALL
Java_com_example_proguard_ndkreverse_Lesson_main(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";
    __android_log_print(ANDROID_LOG_DEBUG, "Che", "<lesson> main be called!");

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_proguard_ndkreverse_Lesson_GetLessonName(JNIEnv *env, jobject thiz) {
    std::string lesson = "C++ Programming";
    __android_log_print(ANDROID_LOG_DEBUG, "Che", "<lesson> GetLessonName be Called!");
    return env->NewStringUTF(lesson.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_proguard_ndkreverse_Lesson_SetLessonName(JNIEnv *env, jobject thiz, jstring name) {
    __android_log_print(ANDROID_LOG_DEBUG, "Che", "<lesson> SetLessonName be Callled!");
}
