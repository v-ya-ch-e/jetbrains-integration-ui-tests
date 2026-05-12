# JetBrains Integration UI Tests

[![CI](https://github.com/v-ya-ch-e/jetbrains-integration-ui-tests/actions/workflows/ci.yml/badge.svg)](https://github.com/v-ya-ch-e/jetbrains-integration-ui-tests/actions/workflows/ci.yml)

Kotlin/JUnit 5 project that demonstrates a JetBrains IDE Starter integration UI test.

The test opens IntelliJ IDEA Ultimate with a pinned public Gradle project (`jitpack/gradle-simple` at commit `abbeb794eb3ae7d9926f5bd7de58477abcbaa906`), navigates to **Settings...** > **Version Control** > **Changelists**, enables **Create changelists automatically**, verifies the checkbox is selected, and confirms the dialog with **OK**.

See [`docs/TASK_DESCRIPTION.md`](docs/TASK_DESCRIPTION.md) for the original task and [`docs/starter-docs/`](docs/starter-docs/) for the copied Starter reference notes used while implementing it.

## For Reviewers

- Requirement checklist: [`docs/TASK_DESCRIPTION.md`](docs/TASK_DESCRIPTION.md)
- Main UI test: [`src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsTest.kt`](src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsTest.kt)
- IDE, sample project, labels, and timing configuration: [`src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsConfig.kt`](src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsConfig.kt)
- Fast validation: `./gradlew testClasses`
- Full validation: run the integration UI test locally or via the manual GitHub Actions workflow when a GUI environment and license are available.

## Tech Stack

- Kotlin 2.3
- JUnit 5
- Gradle 9.2.1 wrapper
- JetBrains IDE Starter and Driver snapshots
- IntelliJ IDEA Ultimate launched by Starter as the IDE under test

## CI

This repository uses two GitHub Actions workflows:

- [`ci.yml`](.github/workflows/ci.yml) runs on pushes and pull requests and executes `./gradlew testClasses`. It verifies that the Kotlin test code compiles against the resolved JetBrains Starter and Driver APIs without launching an IDE.
- [`ui-test.yml`](.github/workflows/ui-test.yml) is a manual `workflow_dispatch` workflow for the full IDE UI scenario. It runs the test under Xvfb, reads `LICENSE_KEY` from GitHub Secrets, and uploads test/Starter artifacts for debugging.

The full UI test is intentionally not a required pull request gate because it downloads and starts a full IDE, needs a desktop-like environment, can require an IntelliJ IDEA Ultimate license, and depends on moving EAP snapshot artifacts.

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

The project uses JetBrains Starter and Driver `LATEST-EAP-SNAPSHOT` dependencies to match current Starter examples. For long-term archival reproducibility, pinning those snapshots to fixed versions would be the next step.

## Project Layout

- `build.gradle.kts` configures Kotlin, JUnit 5, and the direct JetBrains IDE Starter/Driver dependencies.
- `settings.gradle.kts` configures plugin and dependency repositories required by Starter snapshots.
- `src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsTest.kt` contains the integration UI test.
- `src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsConfig.kt` contains the IDE, project, and UI labels used by the test.
- `docs/` contains the task description and local copies of Starter reference material.
- `AGENTS.md` and `CLAUDE.md` contain repository guidance for automated coding assistants and are not required to run the test.

## License

This project is available under the MIT License. See [`LICENSE`](LICENSE).
