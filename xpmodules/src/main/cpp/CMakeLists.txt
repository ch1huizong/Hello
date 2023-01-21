# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

if (NOT TARGET dobby)
    set(DOBBY_DIR /home/che/Apps/Dobby)
    macro(SET_OPTION option value)
        set(${option} ${value} CACHE INTERNAL "" FORCE)
    endmacro()
    SET_OPTION(DOBBY_DEBUG OFF)
    SET_OPTION(DOBBY_GENERATE_SHARED OFF)
    add_subdirectory(${DOBBY_DIR} dobby)
    get_property(DOBBY_INCLUDE_DIRECTORIES
            TARGET dobby
            PROPERTY INCLUDE_DIRECTORIES)
    include_directories(
            .
            ${DOBBY_INCLUDE_DIRECTORIES}
            $<TARGET_PROPERTY:dobby,INCLUDE_DIRECTORIES>
    )
endif ()

set(dlfcn_compat_root_dir /home/che/Apps/dlfcn)
add_subdirectory(${dlfcn_compat_root_dir} dlfcn)
include_directories(
        ${dlfcn_compat_root_dir}/include/
)


add_library(
        jvm-lib
        SHARED
        base/jvm.cpp
)

add_library(
        native-hooks
        SHARED
        native-hooks.cpp
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

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)



target_link_libraries( # Specifies the target library.
        native-hooks
        LogUtils
        jvm-lib

        dobby
        dlfcn_compat_static

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})


target_link_libraries(
        FileUtils
        LogUtils
        ${log-lib}  # TMD依赖关系没解决
)