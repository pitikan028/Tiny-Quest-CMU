# TinyQuestCMU — Themed libGDX OOP Demo

## Requirements
- Java 17 (JDK 17)
- Internet access for Gradle to download libGDX dependencies OR your own Gradle installed.
  (This project includes Gradle build files; if you have Gradle installed, run `gradle --version` to verify.)

## How to Run (Desktop)
### Using your own Gradle installation
1. Open a terminal in the project root.
2. Run:
   ```
   gradle desktop:run
   ```

### Using IDE (IntelliJ IDEA recommended)
1. Open the project (select the root `build.gradle`).
2. Let the IDE import Gradle.
3. Run configuration: `DesktopLauncher` (module: `desktop`).

## Controls
- Move: WASD or Arrow Keys
- Advance dialogue: SPACE
- Change area when prompted: ENTER

## Theming Notes
This build is themed for CMU legend creatures and locations using placeholders (no external textures yet).
Once you provide pixel-art, we will swap rectangles for sprites and add four-direction 32x32 animations.
