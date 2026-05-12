# JetBrains Integration UI Tests

Kotlin/JUnit 5 project for JetBrains IDE Starter integration UI tests.

The initial goal is to implement a UI test that opens a JetBrains IDE, navigates to **Settings...** > **Version Control** > **Changelists**, enables **Create changelists automatically**, verifies the checkbox state, and confirms the settings dialog.

See [`docs/TASK_DESCRIPTION.md`](docs/TASK_DESCRIPTION.md) for the task and [`docs/starter-docs/`](docs/starter-docs/) for Starter reference material.

## Build Tool

This project uses Gradle with Kotlin DSL. Maven can build Kotlin/JUnit projects, but Gradle is a better fit here because JetBrains IDE Starter examples are Gradle-based and the Kotlin test setup stays close to the upstream Starter project.

The build targets Java 25 because current Starter snapshots require it. Gradle is configured to resolve the required toolchain automatically.

## Project Layout

- `build.gradle.kts` configures Kotlin, JUnit 5, and the minimum direct JetBrains IDE Starter/Driver dependencies for this task.
- `settings.gradle.kts` configures plugin and dependency repositories.
- `src/test/kotlin/` contains integration UI tests.
- `src/test/resources/` is reserved for test resources.

## Commands

Run the build and tests:

```shell
./gradlew test
```

Run the target test class once the scenario is implemented:

```shell
./gradlew test --tests com.example.jetbrains.integration.ChangelistsSettingsTest
```
