# Performance

面向 Minecraft 1.20.1 Forge 的性能优化模组，目前只搭好了工程框架，还没有任何优化功能。

## 环境

| 项目 | 版本 |
| :--- | :--- |
| Minecraft | 1.20.1 |
| Forge | 47.4.23 |
| Java | 17 |
| Gradle | 8.8（腾讯镜像） |
| 映射 | Mojang Official（`official` 1.20.1） |
| Mixin | 0.8.5（MixinGradle 0.7.x） |

## 结构

```text
performance/
├── build.gradle / settings.gradle / gradle.properties
├── gradlew / gradlew.bat           # 已配置 Gradle 缓存到 mods/gradle-home
├── .vscode/settings.json           # Cursor / Antigravity 的 Java 与 Gradle 配置
└── src/main/
    ├── java/com/yyh/performance/
    │   ├── Performance.java        # 模组入口
    │   ├── config/                 # Forge 配置（CLIENT / COMMON）
    │   ├── client/                 # 仅客户端代码
    │   └── mixin/                  # Mixin 类（按需创建）
    └── resources/
        ├── META-INF/mods.toml
        ├── pack.mcmeta
        └── performance.mixins.json # Mixin 配置
```

## 构建

在本目录中运行：

```powershell
.\gradlew.bat build
```

构建出的 jar 会自动复制到 `E:\CODE\Java-Mc\mods\mods`。
