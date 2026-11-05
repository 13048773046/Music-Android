package app.star2936.guava3291.security

/**
 * 敏感配置入口。
 *
 * 开源仓库中这些值为空占位：真实的服务端地址、证书指纹与解密密钥仅存在于
 * 私有发布构建环境（构建时注入）。空值状态下 App 可正常编译、安装、启动，
 * 但不具备联网能力，仅作为代码参考版本。
 *
 * 采集方式：hex 密文 → native 层 XOR 还原（密钥不出现于 dex）。
 * 说明：这是"防静态分析"层，配合 R8 混淆 + 签名自校验 + 证书固定构成发布包
 * 的整体加固，不代表密码学强度的持久保密。
 */
object Obfuscator {

    /** hex 密文 → 明文（解密逻辑在 native 层） */
    fun dec(hex: String): String = NativeGuard.decrypt(hex)

    // ---------- 敏感配置（开源版为空占位，发布构建注入真实密文） ----------

    /** 服务端 REST API 基址 */
    val API_BASE: String by lazy { dec("") }

    /** 实时通知 WebSocket 通道前缀 */
    val WS_PREFIX: String by lazy { dec("") }

    /** 发布包签名证书 SHA-256（防二次打包校验基准；空值跳过自校验） */
    val EXPECTED_SIGNATURE_SHA256: String by lazy { dec("") }

    /** 服务器证书 SPKI 指纹（OkHttp 证书固定；空值不启用固定） */
    val SERVER_SPKI_SHA256: String by lazy { dec("") }

    /** 服务器主机名（证书固定 host） */
    val SERVER_HOST: String by lazy { dec("") }
}
