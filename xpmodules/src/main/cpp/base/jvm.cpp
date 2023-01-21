//
// Created by che on 21-5-17.
//

#include "jvm.h"

static JavaVM *gvm = nullptr;

extern "C" void setJvm(JavaVM *jvm) {
    gvm = jvm;
}

extern "C" JavaVM *getJvm() {
    return gvm;
}
