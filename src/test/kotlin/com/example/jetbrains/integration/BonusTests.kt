package com.example.jetbrains.integration

import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.driver.sdk.ui.shouldBe
import com.intellij.ide.starter.config.ConfigurationStorage
import com.intellij.ide.starter.config.useDockerContainer
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Test

class BonusTests {
  @Test
  fun opensEditorSettingsOverview() {
    val config = BonusTestsConfig

    ConfigurationStorage.useDockerContainer(config.useDockerContainer)

    val testContext = Starter
      .newContext(
        CurrentTestMethod.hyphenateWithClass(),
        config.testCase,
      )
      .setLicense(System.getenv(config.licenseKeyEnvironmentVariable))
      .prepareProjectCleanImport()

    testContext.runIdeWithDriver().useDriverAndCloseIde {
      // Driver can connect before the Settings tree is ready to interact with.
      Thread.sleep(config.ideStartupSettleDelay.inWholeMilliseconds)

      ideFrame {
        openSettingsDialog()

        settingsDialog {
          openTreeSettingsSection(config.editorSectionName)

          shouldBe("Editor settings overview is visible") {
            x { byVisibleText(config.editorSettingsOverviewText) }.present()
          }

          okButton.click()
        }
      }
    }
  }

  @Test
  fun opensEditorFontSettings() {
    val config = BonusTestsConfig

    ConfigurationStorage.useDockerContainer(config.useDockerContainer)

    val testContext = Starter
      .newContext(
        CurrentTestMethod.hyphenateWithClass(),
        config.testCase,
      )
      .setLicense(System.getenv(config.licenseKeyEnvironmentVariable))
      .prepareProjectCleanImport()

    testContext.runIdeWithDriver().useDriverAndCloseIde {
      // Driver can connect before the Settings tree is ready to interact with.
      Thread.sleep(config.ideStartupSettleDelay.inWholeMilliseconds)

      ideFrame {
        openSettingsDialog()

        settingsDialog {
          openTreeSettingsSection(config.editorSectionName, config.fontPageName)

          shouldBe("Editor Font settings page is visible") {
            x { byVisibleText(config.fontPageVerificationText) }.present()
          }

          okButton.click()
        }
      }
    }
  }

  @Test
  fun opensEditorColorSchemeSettings() {
    val config = BonusTestsConfig

    ConfigurationStorage.useDockerContainer(config.useDockerContainer)

    val testContext = Starter
      .newContext(
        CurrentTestMethod.hyphenateWithClass(),
        config.testCase,
      )
      .setLicense(System.getenv(config.licenseKeyEnvironmentVariable))
      .prepareProjectCleanImport()

    testContext.runIdeWithDriver().useDriverAndCloseIde {
      // Driver can connect before the Settings tree is ready to interact with.
      Thread.sleep(config.ideStartupSettleDelay.inWholeMilliseconds)

      ideFrame {
        openSettingsDialog()

        settingsDialog {
          openTreeSettingsSection(config.editorSectionName, config.colorSchemePageName)

          shouldBe("Editor Color Scheme settings page is visible") {
            x { byVisibleText(config.colorSchemePageVerificationText) }.present()
          }

          okButton.click()
        }
      }
    }
  }
}
