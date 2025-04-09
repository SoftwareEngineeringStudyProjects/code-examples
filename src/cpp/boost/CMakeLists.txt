cmake_minimum_required(VERSION 3.10)
project(BoostExample)

# Specify C++ standard (optional)
set(CMAKE_CXX_STANDARD 17)

# Find Boost
find_package(Boost 1.87 REQUIRED)

# Add the executable
add_executable(my_program demo_string.cpp)

# Link Boost libraries
target_link_libraries(my_program Boost::headers)
