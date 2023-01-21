# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

enable_language(ASM)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

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

add_library( # Sets the name of the library.
        static-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        libstatic.cpp
        people/People.cpp

        #inlineHook/inlineHook.c
        #inlineHook/relocate.c
        )
include_directories(src/main/cpp/include/)


# myself add
add_library( # Sets the name of the library.
        dynamic-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        libdynamic.cpp)


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

# substrate
add_library(
        substrate
        SHARED
        IMPORTED
)
set_target_properties(
        substrate

        PROPERTIES IMPORTED_LOCATION

        ../jniLibs/armeabi-v7a/libsubstrate.so
)

add_library(
        substrate-dvm
        SHARED
        IMPORTED
)

set_target_properties(
        substrate-dvm

        PROPERTIES IMPORTED_LOCATION

        ../jniLibs/armeabi-v7a/libsubstrate-dvm.so
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

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
target_link_libraries(
        static-lib
        #substrate-dvm
        #substrate
        MyTest
        LogUtils
        FileUtils
        ${log-lib}
        dobby

)

target_link_libraries(  #TMD cmake依赖没解决
        dynamic-lib

        ${log-lib}
        )


target_link_libraries(
        FileUtils
        LogUtils
        ${log-lib}  # TMD依赖关系没解决
)


# self set
set(var hello)
message($var)
message(${CMAKE_CURRENT_LIST_FILE})
message(${CMAKE_CURRENT_LIST_DIR})

IF (TRUE)
    message("This is True!!!!!!")
ENDIF ()