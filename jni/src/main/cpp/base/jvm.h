//
// Created by che on 21-5-17.
//

#ifndef HELLONATIVE_JVM_H
#define HELLONATIVE_JVM_H

#include "../../../../../../../Android/Sdk/ndk/21.4.7075529/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/include/jni.h"

extern "C" void setJvm(JavaVM *jvm);
extern "C" JavaVM *getJvm();

#endif //HELLONATIVE_JVM_H
