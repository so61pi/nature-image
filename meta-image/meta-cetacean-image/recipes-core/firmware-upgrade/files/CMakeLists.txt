cmake_minimum_required(VERSION 3.1)
project(firmware-upgrade)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

if (CMAKE_COMPILER_IS_GNUCC)
    set(CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -Wall -Wextra -Werror")
endif()


add_executable(firmware-upgrade firmware-upgrade.cpp)

find_library(LIBBLKID blkid)
target_link_libraries(firmware-upgrade ${LIBBLKID})

find_library(LIBMOUNT mount)
target_link_libraries(firmware-upgrade ${LIBMOUNT})

find_library(LIBUDEV udev)
target_link_libraries(firmware-upgrade ${LIBUDEV})


install(TARGETS firmware-upgrade DESTINATION bin)
