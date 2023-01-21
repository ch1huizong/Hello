# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

add_library(
        people-lib
        SHARED
        people/People.cpp
)

include_directories(people/)

add_library(
        jvm-lib
        SHARED
        base/jvm.cpp
)

add_library(
        jni-lib
        SHARED
        libjni.cpp
)


add_library(
        LogUtils
        SHARED
        base/LogUtils.cpp
)

add_library(
        FileUtils
        SHARED
        base/FileUtils.cpp
)

include_directories(base/)

add_library(
        MyTest
        SHARED
        MyTest.cpp
)


# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)



target_link_libraries( # Specifies the target library.
        jni-lib
        people-lib
        LogUtils
        jvm-lib


        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})


target_link_libraries(
        FileUtils
        LogUtils
        ${log-lib}  # TMD依赖关系没解决
)


set(var hello) # 定义变量
message(${var}) # 引用变量
message(${CMAKE_CURRENT_LIST_FILE}) # 引用预定义变量
message(${CMAKE_CURRENT_LIST_DIR})

IF (TRUE) # 条件控制
    message("This is True!!!!!!")
ENDIF ()