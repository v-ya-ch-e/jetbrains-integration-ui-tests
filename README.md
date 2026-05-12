# JetBrains Integration UI Tests

Kotlin/JUnit 5 project that demonstrates a JetBrains IDE Starter integration UI test.

The test opens IntelliJ IDEA with a public Gradle project, navigates to **Settings...** > **Version Control** > **Changelists**, enables **Create changelists automatically**, verifies the checkbox is selected, and confirms the dialog with **OK**.

See [`docs/TASK_DESCRIPTION.md`](docs/TASK_DESCRIPTION.md) for the original task and [`docs/starter-docs/`](docs/starter-docs/) for the copied Starter reference notes used while implementing it.

## Tech Stack

- Kotlin 2.3
- JUnit 5
- Gradle 9.2 wrapper
- JetBrains IDE Starter and Driver snapshots
- IntelliJ IDEA launched by Starter as the IDE under test

## Prerequisites

- A desktop environment where a JetBrains IDE can be launched.
- Network access for Gradle dependencies, the IDE download, and the public sample project checkout.
- Java 25. The Gradle build is configured with the Foojay toolchain resolver, so Gradle can provision it automatically when needed.
- `LICENSE_KEY` in the environment if the downloaded IntelliJ IDEA Ultimate build requires a license in your environment.

## Run Locally

Compile the project and test classes:

```shell
./gradlew testClasses
```

Run the integration UI test:

```shell
./gradlew test --tests com.example.jetbrains.integration.ChangelistsSettingsTest --rerun-tasks
```

The UI test downloads and starts an IDE, imports a public GitHub project, and writes Starter artifacts under ignored local directories such as `out/` and `build/`. A full run can take several minutes on a fresh machine.

## Project Layout

- `build.gradle.kts` configures Kotlin, JUnit 5, and the direct JetBrains IDE Starter/Driver dependencies.
- `settings.gradle.kts` configures plugin and dependency repositories required by Starter snapshots.
- `src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsTest.kt` contains the integration UI test.
- `src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsConfig.kt` contains the IDE, project, and UI labels used by the test.
- `docs/` contains the task description and local copies of Starter reference material.

## License

This project is available under the MIT License. See [`LICENSE`](LICENSE).
