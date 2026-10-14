# ⭐ Star Client (Minecraft Java 1.21.11 / Fabric)

Star Client is an original client-side Fabric mod starter with a dark blue Click GUI and lightweight PvP/HUD utilities. It is **not a standalone Minecraft launcher** and it does not include automated combat or anti-cheat bypasses.

## Included in this starter build

- Minecraft Java **1.21.11** and Fabric API target
- Java 21 / Gradle project
- Press **Left Shift** to open/close the Click GUI; Escape closes it
- Dark blue/cyan UI with category tabs and module on/off cards
- FPS, coordinates, keystrokes, CPS, armor durability, ping, and direction HUD modules
- Module toggles saved to `config/starclient.json`
- GitHub Actions workflow to build the JAR remotely

Some module cards are placeholders for future features; the GUI labels their purpose, but not every card represents a completed implementation. This starter does not currently include a full HUD drag editor, zoom implementation, custom FPS engine, or a complete Lunar-style feature set.

## Requirements

- Minecraft Java Edition **1.21.11**
- Fabric Loader for 1.21.11
- Fabric API for 1.21.11
- Java 21 for building
- Network access on the first build so Gradle can download Minecraft, mappings, and dependencies

## Build on Windows

1. Install JDK 21.
2. Install Gradle 9.2.1, or open this folder in IntelliJ IDEA and import it as a Gradle project.
3. Open Command Prompt/PowerShell in this folder.
4. Run:

   ```bat
   gradle build --no-daemon
   ```

5. If successful, the mod JAR will be in `build\libs\starclient-1.0.0.jar`.

### Build without installing Gradle manually (GitHub Actions)

1. Create a repository on GitHub and upload the contents of this folder.
2. Open the repository's **Actions** tab.
3. Choose **Build Star Client**, then click **Run workflow** (or push to `main`).
4. When it finishes, open the workflow run and download the `starclient-jar` artifact.

The first build needs internet access to download dependencies.

## Install

1. Install Fabric Loader for Minecraft 1.21.11.
2. Put a matching Fabric API JAR in your `.minecraft/mods` folder.
3. Put `starclient-1.0.0.jar` in `.minecraft/mods`.
4. Launch the Fabric 1.21.11 profile.
5. Press **Left Shift** in-game to open Star Client.

## Troubleshooting

- If Minecraft says the mod is for another version, confirm you launched **1.21.11** and not 1.21.1 or 1.21.10.
- If the build fails, copy the first `What went wrong` section and the compiler error lines; those are the most useful details for fixing it.
- This project has not been build-verified in the authoring environment because network access for downloading Gradle/dependencies was unavailable. The GitHub Actions workflow can perform a real build and expose any compile errors.

## License

MIT. See `LICENSE`.
