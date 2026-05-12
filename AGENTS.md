# AGENTS.md

Behavioral guidelines to reduce common LLM coding mistakes. Merge with project-specific instructions as needed.

**Tradeoff:** These guidelines bias toward caution over speed. For trivial tasks, use judgment.

## 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

Before implementing:
- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

## 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

## 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing code:
- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:
- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

## 4. Goal-Driven Execution

**Define success criteria. Loop until verified.**

Transform tasks into verifiable goals:
- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"
- "Refactor X" → "Ensure tests pass before and after"

For multi-step tasks, state a brief plan:
```
1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]
```

Strong success criteria let you loop independently. Weak criteria ("make it work") require constant clarification.

---

## Project Context

This repository is for an integration UI test project using JetBrains IDE Starter.

Primary task source: [`docs/TASK_DESCRIPTION.md`](docs/TASK_DESCRIPTION.md).

Starter reference docs copied into this repo:
- [`docs/starter-docs/README.md`](docs/starter-docs/README.md) - overview of JetBrains IDE Starter, supported IDEs, commands, artifacts, metrics, and examples.
- [`docs/starter-docs/STARTER_CORE.md`](docs/starter-docs/STARTER_CORE.md) - core Starter behavior, JUnit5 usage, command execution, debugging, DI overrides, IDE download configuration, VM options, and metrics.
- [`docs/starter-docs/PerfTestWithDriver.kt`](docs/starter-docs/PerfTestWithDriver.kt) - Kotlin example using `Starter.newContext(...)`, `runIdeWithDriver()`, `useDriverAndCloseIde { ... }`, command execution, and driver/JMX calls.

External references from the task:
- JetBrains Starter library docs: https://github.com/JetBrains/intellij-community/blob/master/tools/intellij.tools.ide.starter/README.md
- Example UI test with Driver: https://github.com/JetBrains/intellij-ide-starter/blob/master/intellij.tools.ide.starter.examples/testSrc/com/intellij/ide/starter/examples/driver/UiTestWithDriver.kt

## Target Test Scenario

Implement an integration UI test that:
- Opens a JetBrains IDE. Any JetBrains IDE type is acceptable unless the user specifies one.
- Uses any publicly available project as the test project.
- Opens **Settings...**.
- Navigates to **Version Control** > **Changelists**.
- Selects the checkbox labeled **Create changelists automatically**.
- Verifies that the checkbox is selected.
- Clicks **OK**.

Prefer Kotlin/JUnit5 patterns consistent with JetBrains IDE Starter examples. When interacting with the IDE, use Starter/Driver APIs and performance testing commands where appropriate instead of inventing unrelated automation layers.

## Bonus Test Scenarios

Keep test configuration separated by test class: [`src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsConfig.kt`](src/test/kotlin/com/example/jetbrains/integration/ChangelistsSettingsConfig.kt) belongs to the main Changelists test, and [`src/test/kotlin/com/example/jetbrains/integration/BonusTestsConfig.kt`](src/test/kotlin/com/example/jetbrains/integration/BonusTestsConfig.kt) belongs to the bonus Settings tests.

[`src/test/kotlin/com/example/jetbrains/integration/BonusTests.kt`](src/test/kotlin/com/example/jetbrains/integration/BonusTests.kt) contains additional IDEA Ultimate Settings UI checks. Keep them focused on stable Settings navigation/rendering unless the user requests state-changing scenarios:
- **Editor**
- **Editor** > **Font**
- **Editor** > **Color Scheme**

CI is split by weight: [`ci.yml`](.github/workflows/ci.yml) compiles all test classes, [`ui-test.yml`](.github/workflows/ui-test.yml) runs the main Changelists UI test manually, and [`bonus-ui-tests.yml`](.github/workflows/bonus-ui-tests.yml) runs the bonus UI tests manually.

## Starter Notes For Agents

- Starter launches the IDE as a separate process from the test runtime, so tests control the IDE through commands and Driver/JMX-style interactions.
- Starter can download or use IDE installers, configure and launch the IDE, collect logs, metrics, freezes, exceptions, and test artifacts.
- JUnit5 is supported through Starter integration libraries and extensions.
- Driver calls do not always wait for IDE readiness automatically; use explicit waits such as `waitForIndicators(...)` when needed.
- Command-chain examples include operations such as `openProject`, `waitForSmartMode`, `flushIndexes`, `openFile`, `pressKey`, and `searchEverywhere`; see the starter docs before adding custom commands.
- If debugging a launched IDE process, use remote JVM debugging as described in [`docs/starter-docs/STARTER_CORE.md`](docs/starter-docs/STARTER_CORE.md).

## Findings From Local Starter/Driver Runs

- With current `LATEST-EAP-SNAPSHOT` Starter/Driver dependencies, test compilation requires a Java 25 test runtime; Java 21 fails on class file version 69 from Starter JUnit5 classes.
- `IdeProductProvider` was not available in the resolved Starter API. For IntelliJ IDEA Community, direct `IdeInfo(productCode = "IC", platformPrefix = "Idea", executableFileName = "idea", fullName = "IntelliJ IDEA Community")` compiled.
- The latest resolved IntelliJ IDEA Community EAP (`IC-252.28539.54` during this run) starts locally, but `runIdeWithDriver().useDriverAndCloseIde { ... }` fails before UI actions because Starter/Driver tries to call `com.jetbrains.performancePlugin.TestContext`, which is missing from the bundled Performance Testing plugin.
- `ConfigurationStorage.useDockerContainer(true)` sets `USE_DOCKER_CONTAINER=true`, but it did not prevent the `TestNameSynchronizer` call to the missing `TestContext` class. Do not treat it as a complete workaround for that failure.
- A working local workaround is to call `runIdeWithDriver()`, then create a plain `DriverImpl(JmxHost(address = "127.0.0.1:7777"), isRemDevMode = false)` and close the IDE manually in `finally`.
- In this dependency/build combination, `waitForIndicators(...)` failed inside Driver with a `kotlin.Pair.getFirst()` argument mismatch. Avoid it unless the Starter/Driver/IDE versions are changed and reverified.
- `driver.invokeAction("ShowSettings")` was rejected by the IDE in the working run. `IdeaFrameUI.openSettingsDialog()` opened Settings reliably.
- Driver connection does not mean the IDE UI is ready. A simple short startup wait after `driver.isConnected` made the Settings tree navigation stable in this project.
- Navigating `Settings...` > `Version Control` > `Changelists` through `openTreeSettingsSection("Version Control", "Changelists")` worked after the startup wait.
- The Settings search field was not reliable in this IDE build because the Driver helper could not find a text field with accessible name `Search`; prefer tree navigation for this scenario.
- The changelist checkbox setting was not found through a stable public Driver service API. Unless a reliable API is confirmed, keep the assertion UI-level and treat the selected Settings checkbox as the source of truth.
- Generated outputs from local runs include `.gradle/`, `build/`, `out/`, and `allure-results/`; keep them gitignored.

---

**These guidelines are working if:** fewer unnecessary changes in diffs, fewer rewrites due to overcomplication, and clarifying questions come before implementation rather than after mistakes.
