# AGENTS.md

This is a Minecraft Forge 1.20.1 mod development workspace. Before doing any work here, read these files in order and follow them:

1. [SKIll/En-SKILL.md](SKIll/En-SKILL.md) - highest priority: language, toolchain, and output rules.
2. [SKIll/Agent.md](SKIll/Agent.md) - workspace layout, Gradle cache location, and how to create and build mods.
3. [SKIll/MC-Forge-1.20.1/AGENTS.md](SKIll/MC-Forge-1.20.1/AGENTS.md) - Forge 1.20.1 coding guidance (read its references on demand).
4. [SKIll/Agent-Code/SKILL.md](SKIll/Agent-Code/SKILL.md) - general coding workflow, hard rules, and checklists.

Key rules, repeated here so they are never missed:

- Every mod is an independent Gradle project in its own folder under `mods/Release-code/` or `mods/Source-code/`.
- All Gradle caches go to `mods/gradle-home/`. Nothing related to mod development may be written outside `E:\CODE\Java-Mc`.
- Build only with the mod's own `gradlew.bat` from inside the mod folder. Never set global environment variables.
- Finished JARs go to `mods/mods/` and nowhere else.
