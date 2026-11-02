# Java-Mc

面向 Minecraft Forge 1.20.1 的 Java 模组开发工作区。所有模组开发相关的内容（源码、构建文件、Gradle 缓存、运行目录、打包好的 jar）都放在本目录内，不写到 C 盘或其他位置。

## 默认环境

| 项目 | 版本 |
| :--- | :--- |
| Minecraft | 1.20.1 |
| Forge | 47.4.23 |
| Java | 17（`C:\Program Files\Java\jdk-17.0.20_8`） |
| Gradle | 8.8 |
| 映射 | Mojang Official（`official` 1.20.1） |
| 作者 | YYH |

`Source-code/` 中基于他人源码的工程，以该工程自身的版本为准。

## 目录结构

```text
Java-Mc/
├── AGENTS.md            # AI 工具入口说明（Codex、Cursor、Antigravity、Command Code 等）
├── CLAUDE.md            # Claude Code 入口，引用 AGENTS.md
├── SKIll/               # AI 开发规范与 Forge 1.20.1 知识库
├── AI/                  # AI 产出的计划、报告、临时文件（按需创建）
└── mods/
    ├── gradle-home/     # 所有模组共用的 Gradle 缓存（首次构建时自动生成）
    ├── mods/            # 打包好的 .jar
    ├── Release-code/    # 自己开发的模组，每个模组一个文件夹
    │   └── performance/
    └── Source-code/     # 基于他人源码继续开发的模组，每个模组一个文件夹
```

## 约定

- 每个模组都是独立完整的 Gradle 工程，在自己的文件夹里用 `gradlew.bat` 构建。
- 每个模组的 `gradlew.bat`、`gradlew` 和 `.vscode/settings.json` 都把 Gradle 缓存指向 `mods/gradle-home`。
- 构建完成后 jar 自动复制到 `mods/mods/`。
- 编辑器使用 Cursor 或 Antigravity IDE，直接打开单个模组文件夹。

详细规则见 [SKIll/Agent.md](SKIll/Agent.md)。
