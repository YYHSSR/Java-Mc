# Java-Mc Workspace Rules

Read this before creating, building, or reorganizing any mod in `E:\CODE\Java-Mc`. These rules decide **where every file goes**. Follow them exactly; do not invent another layout.

For Forge coding guidance, use [MC-Forge-1.20.1/AGENTS.md](MC-Forge-1.20.1/AGENTS.md). For general workflow, use [Agent-Code/SKILL.md](Agent-Code/SKILL.md). [En-SKILL.md](En-SKILL.md) still has the highest priority.

## 1. Core Rule

**Everything related to mod development stays inside `E:\CODE\Java-Mc`.** Source code, build files, Gradle caches, downloaded Forge/Minecraft files, run directories, and built JARs must never be written to `C:\Users\<user>\.gradle` or any other location outside this workspace.

## 2. Tools in Use

| Purpose | Tools |
| :--- | :--- |
| Editors | Cursor, Antigravity IDE (both VS Code-based) |
| AI agents | ChatGPT / Codex, Claude / Claude Code, Command Code |

- Both editors read the mod's `.vscode\settings.json`. They need the Java language support and Gradle extensions installed (for example "Language Support for Java" by Red Hat and "Gradle for Java").
- Open **one mod folder** at a time in the editor, not `E:\CODE\Java-Mc` or `mods\`.
- AI agents start from `E:\CODE\Java-Mc\AGENTS.md` (Claude Code reads `CLAUDE.md`, which imports it).

## 3. Directory Layout

```text
E:\CODE\Java-Mc\
├── AGENTS.md                   # Entry point for AI agents
├── CLAUDE.md                   # Claude Code entry, imports AGENTS.md
├── README.md
├── .gitignore
├── SKIll\                      # AI guidance (this file lives here)
├── AI\                         # AI plans, reports, temp files (create on demand)
└── mods\
    ├── gradle-home\            # The ONLY Gradle user home, shared by all mods
    ├── mods\                   # Finished, distributable .jar files only
    ├── Release-code\           # The user's own mods, one folder per mod
    │   ├── performance\
    │   └── <mod2>\
    └── Source-code\            # Mods based on other people's source, one folder per mod
        └── <other-mod>\
```

Rules:

- **One mod = one folder** directly under `Release-code\` or `Source-code\`. Never nest mods inside each other and never put mod files directly in `mods\`, `Release-code\`, or `Source-code\`.
- **Each mod folder is a complete, independent Gradle project**: its own `build.gradle`, `settings.gradle`, `gradle.properties`, `gradlew`, `gradlew.bat`, `gradle\wrapper\`, `src\`, `.vscode\`, and `README.md`, plus the generated `build\` and `run\`. There is no root Gradle project in `Java-Mc` or `mods\`.
- **`Release-code\`** holds the user's own mods. **`Source-code\`** holds projects built on someone else's source; keep their original versions, structure, and license files.
- **`mods\mods\`** holds only finished JARs. No source, logs, or intermediate build output.
- **`mods\gradle-home\`** is shared by all mods so Forge and Minecraft files are downloaded once. Never create a separate Gradle home per mod. It is created automatically on the first build.
- Folder names must not contain spaces. New mod folders use the lowercase mod ID (for example `Release-code\coolmod\`).

## 4. Gradle Home Configuration (Required for Every Mod)

Gradle's cache location (`GRADLE_USER_HOME`) cannot be set in a project's `gradle.properties`. This workspace sets it in three places inside **each** mod folder. Do **not** set a system or user environment variable instead. [`Release-code\performance`](../mods/Release-code/performance/) is the reference implementation of all three.

### 4.1 `gradlew.bat` (Windows)

Find this line near the top:

```bat
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi
```

Directly below it, add:

```bat
@rem Java-Mc: keep all Gradle caches inside E:\CODE\Java-Mc\mods\gradle-home
for %%i in ("%APP_HOME%\..\..\gradle-home") do set "GRADLE_USER_HOME=%%~fi"
```

### 4.2 `gradlew` (Git Bash and other POSIX shells)

Directly above the final `exec "$JAVACMD" "$@"` line, add:

```sh
# Java-Mc: keep all Gradle caches inside E:/CODE/Java-Mc/mods/gradle-home
GRADLE_USER_HOME=$( cd "$APP_HOME/../.." && pwd -P )/gradle-home
if "$cygwin" || "$msys" ; then
    GRADLE_USER_HOME=$( cygpath --mixed "$GRADLE_USER_HOME" )
fi
export GRADLE_USER_HOME
```

The mod sits at `mods\<Release-code|Source-code>\<mod>\`, so `..\..\gradle-home` always resolves to `mods\gradle-home`. Keep this exact relative form; do not hard-code a different path.

### 4.3 `.vscode\settings.json`

The editors import Gradle projects without running the wrapper scripts, so they need the same location. Each mod's `.vscode\settings.json` must contain:

```json
{
  "java.jdt.ls.java.home": "C:\\Program Files\\Java\\jdk-17.0.20_8",
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-17",
      "path": "C:\\Program Files\\Java\\jdk-17.0.20_8",
      "default": true
    }
  ],
  "java.import.gradle.java.home": "C:\\Program Files\\Java\\jdk-17.0.20_8",
  "java.import.gradle.user.home": "E:\\CODE\\Java-Mc\\mods\\gradle-home"
}
```

If an existing settings file has other keys, keep them and only add or correct these keys.

### 4.4 How to run builds

- Always run from **inside the mod folder** with the mod's own wrapper: `.\gradlew.bat <task>` in PowerShell, `gradlew.bat <task>` in cmd, or `./gradlew <task>` in Git Bash.
- Never run a system-installed `gradle` command; it ignores the wrapper configuration.
- Never run Gradle from `E:\CODE\Java-Mc` or `mods\`; they are not Gradle projects.

## 5. Copying Finished JARs to `mods\mods`

Built JARs first appear in the mod's own `build\libs\`. Every single-loader Forge mod ends its `build.gradle` with this task so `build` also copies the release JAR to `mods\mods\`:

```groovy
// Java-Mc: copy the reobfuscated release jar to E:\CODE\Java-Mc\mods\mods
tasks.register('copyJarToMods', Copy) {
    dependsOn 'reobfJar'
    from(layout.buildDirectory.dir('libs')) {
        include "${mod_id}-*.jar"
        exclude '*-sources.jar', '*-slim.jar'
    }
    into(file("${rootDir}/../../mods"))
}
tasks.named('build') { finalizedBy 'copyJarToMods' }
```

- This assumes `base { archivesName = mod_id }`. Adjust the `include` pattern if the project names its JAR differently.
- Multi-loader projects in `Source-code\` (with `common\`, `forge\`, and `fabric\` subprojects) put the Forge JAR in `forge\build\libs\`. Only the Forge JAR goes to `mods\mods\`; add the task to the `forge` subproject and adjust `rootDir` to the mod folder.

## 6. Creating a New Mod

`Release-code\performance` is the template. It contains the full build setup, the Gradle home configuration, a config class, an isolated client class, and an empty Mixin setup.

1. Copy `mods\Release-code\performance\` to `mods\Release-code\<new_mod_id>\`. Do not copy `build\`, `run\`, `run-data\`, or `.gradle\` if they exist.
2. In `gradle.properties`, set `mod_id`, `mod_name`, `mod_group_id`, `mod_version`, `mod_description`, and `mod_license`. Keep `mod_authors=YYH` unless told otherwise.
3. In `settings.gradle`, set `rootProject.name` to the new mod ID.
4. Rename the Java package `com.yyh.performance` to `com.yyh.<new_mod_id>`, rename the main class, and set its `MOD_ID` constant to the new mod ID.
5. Rename `performance.mixins.json` to `<new_mod_id>.mixins.json` and update its `package` and `refmap` fields. If the mod needs no Mixin, remove the `org.spongepowered.mixin` plugin, the `mixin { }` block, the Mixin `annotationProcessor` dependency, the `MixinConfigs` manifest entry, the Sponge repository in `settings.gradle`, and the mixins JSON file.
6. Update the mod's `README.md`.
7. Confirm `gradlew.bat`, `gradlew`, and `.vscode\settings.json` still contain the configuration from Section 4 (they are copied unchanged).
8. Run `.\gradlew.bat build` inside the new folder only when the user asks for a build, and confirm the JAR appears in `build\libs\` and `mods\mods\`.

Default versions for new mods: Minecraft 1.20.1, Forge 47.4.23, Java 17, Gradle 8.8 (Tencent mirror in `gradle-wrapper.properties`), `official` mappings for 1.20.1, Mixin 0.8.5 with MixinGradle 0.7.x. Projects in `Source-code\` keep their own versions.

## 7. Forbidden Actions

- Writing caches, downloads, or build output to `C:\Users\<user>\.gradle`, `%APPDATA%`, or any path outside `E:\CODE\Java-Mc`.
- Setting `GRADLE_USER_HOME` (or any other variable) as a system or user environment variable.
- Creating a root `settings.gradle` or `build.gradle` in `Java-Mc` or `mods\` to combine mods.
- Placing source files, logs, or temporary files in `mods\mods\`.
- Moving, renaming, or deleting existing mod folders without the user's explicit permission.
- Changing the Forge, Minecraft, or Gradle versions of a `Source-code\` project to match the defaults.

## 8. Items That Stay Outside the Workspace

These are shared tools or editor data, not mod files. They are expected outside the workspace and are not violations:

- The JDK 17 installation at `C:\Program Files\Java\jdk-17.0.20_8`.
- Cursor's and Antigravity IDE's own extension, settings, and index data in their user-data folders.
- Short-lived system temp files under `%TEMP%` that the JVM may create during a build.

## 9. Checklist Before Finishing

- [ ] Every changed or new mod is in its own folder under `Release-code\` or `Source-code\`.
- [ ] Its `gradlew.bat`, `gradlew`, and `.vscode\settings.json` point Gradle to `mods\gradle-home`.
- [ ] Builds, if any, were run with that mod's wrapper from inside its folder.
- [ ] No new Gradle cache appeared in `C:\Users\<user>\.gradle` during the task.
- [ ] Release JARs are in `mods\mods\`; nothing else is in that folder.
