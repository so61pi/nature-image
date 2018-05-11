cmake_minimum_required(VERSION 3.1)
project(swupdate-helper)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

if (CMAKE_COMPILER_IS_GNUCC)
    set(CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -Wall -Wextra -Werror")
endif()


add_executable(swupdate-helper swupdate-helper.cpp)

find_library(LIBBLKID blkid)
target_link_libraries(swupdate-helper ${LIBBLKID})

find_library(LIBMOUNT mount)
target_link_libraries(swupdate-helper ${LIBMOUNT})

find_library(LIBUDEV udev)
target_link_libraries(swupdate-helper ${LIBUDEV})


install(TARGETS swupdate-helper DESTINATION bin)
