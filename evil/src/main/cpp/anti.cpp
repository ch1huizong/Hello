#include <jni.h>
#include <string>
#include <android/log.h>
#include <unistd.h>
#include <iostream>
#include <utility>
#include <thread>
#include <chrono>
#include <functional>
#include <atomic>
#include <stdio.h>

#include "LogUtils.h"


void be_attached_check() {
    try {
        const int bufsize = 1024;
        char filename[bufsize];
        char line[bufsize];
        int pid = getpid();
        sprintf(filename, "/proc/%d/status", pid);
        FILE *fd = fopen(filename, "r");

        if (fd != nullptr) {
            while (fgets(line, bufsize, fd)) {
                if (strncmp(line, "TracerPid", 9) == 0) {
                    int status = atoi(&line[10]);
                    LOGD("%s", line);

                    if (status != 0) {
                        LOGD("be attached !! kill %d", pid);
                        fclose(fd);
                        kill(pid, SIGKILL);
                    }

                    break;
                }
            }

            fclose(fd);
        } else {
            LOGD("open %s failed...", filename);
        }
    } catch (...) {

    }
}

[[noreturn]] void thread_task(int n) {
    while (true) {
        LOGD("start be_attached_check...");
        be_attached_check();
        std::this_thread::sleep_for(std::chrono::seconds(n));
    }
}

void anti_debug() {
    LOGD("call anti_debug...");
    //ptrace(PTRACE_TRACEME, 0, 0, 0);

    auto checkThread = std::thread(thread_task, 1);
    checkThread.detach();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_evil_MainActivity_getMyString(JNIEnv *env, jobject thiz, jstring str) {
    const char *s = env->GetStringUTFChars(str, 0);
    LOGD("[Native - getMyString]:%s", s);
    env->ReleaseStringUTFChars(str, s);

    return env->NewStringUTF(s);
}


jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    anti_debug();

    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    return JNI_VERSION_1_6;
}
