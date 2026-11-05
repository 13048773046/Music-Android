cmake_minimum_required(VERSION 3.22.1)
project(guard C)

add_library(guard SHARED guard.c)

# 字符串解密密钥由构建环境注入（发布构建时 export LIUYING_XOR_KEY=0xNN）。
# 未注入时使用 guard.c 内的默认占位值，产物无法还原配置字符串。
if(DEFINED ENV{LIUYING_XOR_KEY})
    target_compile_definitions(guard PRIVATE LIUYING_XOR_KEY=$ENV{LIUYING_XOR_KEY})
    message(STATUS "LIUYING_XOR_KEY: injected from environment")
else()
    message(STATUS "LIUYING_XOR_KEY: not set, using placeholder (config strings unavailable at runtime)")
endif()

# JNI 链接
target_link_libraries(guard android log)

set_target_properties(guard PROPERTIES
    C_STANDARD 99
    C_STANDARD_REQUIRED ON
)
