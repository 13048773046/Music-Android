# 流萤音乐 · Android 客户端

流萤音乐（FireflyMusic）安卓原生前端，Jetpack Compose 实现，与 iOS 版共用同一套后端接口与音源。

## 工程信息

| 项 | 值 |
|---|---|
| 应用名 | 流萤音乐 |
| 包名 | `app.star2936.guava3291` |
| 版本 | 1.1.24（versionCode 25） |
| 语言 / UI | Kotlin / Jetpack Compose（Material3） |
| minSdk / targetSdk / compileSdk | 29 / 35 / 35 |
| NDK | 27.1.12297006（自带 NativeGuard 防护层，CMake 构建） |
| 构建 | Gradle 8.9 + AGP 8.5.2 + JDK 17 |

## 技术栈

- 网络：OkHttp + kotlinx.serialization
- 图片：Coil
- 音频：Media3 ExoPlayer（200MB LRU 流缓存 + 弱网自愈）
- 实时：OkHttp WebSocket（通知推送 / 行为上报，25s 心跳 + 指数退避重连）
- 安全存储：EncryptedSharedPreferences（登录态 / 播放会话）
- 播放器 Web 资源：`app/src/main/assets/folia`（与 iOS Resources/folia 同源）

## 目录结构

```
android/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/                  # folia 播放器 Web 资源
│       ├── cpp/                     # NativeGuard（CMake）
│       ├── res/
│       └── java/app/star2936/guava3291/
│           ├── MainActivity.kt
│           ├── MusicApplication.kt
│           ├── state/               # PlayerManager / AuthManager / NotificationBus / AppState / Library / WeatherContext / PlaybackService
│           ├── data/                # Api / Models（REST 数据层）
│           └── ui/                  # main / home / discover / mine / notifications / sidebar / search / upload / settings / profile / playlists / history / login / comment
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## 构建

### 环境

- JDK 17
- Android SDK：platforms android-35、build-tools 34.0.0+、NDK 27.1.12297006、CMake 3.22.1
- Gradle 8.9（本仓库不含 wrapper，可使用本机 Gradle 或自行 `gradle wrapper`）

### 命令

```bash
# Debug（无需签名配置）
gradle assembleDebug

# Release（需要 app 级签名配置，见下）
gradle assembleRelease
```

### Release 签名

仓库不包含签名密钥。发布包需要以下两项（均已在 `.gitignore` 中排除）：

1. `keystore.properties`（工程根目录），内容：
   ```
   storeFile=keystore/<文件名>.keystore
   storePassword=***
   keyAlias=***
   keyPassword=***
   ```
2. `keystore/` 目录下对应的 keystore 文件

缺少上述文件时 `assembleRelease` 仍可编译，但产物为未签名 APK；`assembleDebug` 不受影响。

## 开源版说明（敏感配置）

本仓库为公开发布版，以下敏感内容**不进仓库**，由私有发布构建环境注入：

| 项 | 位置 | 开源版状态 |
|---|---|---|
| 服务端地址 / 证书指纹 | `security/Obfuscator.kt` | 空占位（运行时解密为空） |
| native 解密密钥（XOR） | `app/src/main/cpp/guard.c` | 由 CMake 环境变量 `LIUYING_XOR_KEY` 注入，未注入时为占位值 |
| 签名 keystore / 密码 | `keystore/`、`keystore.properties` | 已 `.gitignore` 排除 |

构建命令示例（注入密钥）：

```bash
LIUYING_XOR_KEY=0xNN gradle assembleRelease
```

因此：**用本仓库直接构建出的 APK 可正常编译、安装、启动，但不具备联网能力**（API 基址为空时回退到不可解析的占位域名），仅用于代码参考。正式发布包需在私有环境注入上述配置。

## 版本发布

后端通过 `/api/check-update?platform=android&v=<版本号>` 下发更新；
发布新版本时同步修改 `versionCode` / `versionName`，并把签名后的 APK 上传到下载站。
