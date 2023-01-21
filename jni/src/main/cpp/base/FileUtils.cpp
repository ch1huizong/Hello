//
// Created by che on 21-5-27.
//

#include "FileUtils.h"

#include <iostream>
#include <string>
#include <unistd.h>
#include <sys/syscall.h>
#include <sys/types.h>
#include <fcntl.h>
#include "LogUtils.h"


std::string getFileText(char *filename, int buffer_size) {
    char buffer[buffer_size];
    memset(buffer, 0, buffer_size); // 内存清洗

    std::string data;

    long fd = syscall(__NR_open, filename, O_RDONLY);

    if (fd == -1) {
        LOGD("打开文件错误， 错误原因: %s", strerror(errno));
        return "";
    }

    while (syscall(__NR_read, fd, buffer, 1) != 0) {
        data.append(buffer);
    }
    syscall(__NR_close, fd);

    return data;
}
