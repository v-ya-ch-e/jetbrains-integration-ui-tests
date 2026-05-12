package com.example.jetbrains.integration

import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.checkBoxWithName
import com.intellij.driver.sdk.ui.components.elements.waitSelected
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.ide.starter.config.ConfigurationStorage
import com.intellij.ide.starter.config.useDockerContainer
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ChangelistsSettingsTest {
  @Test
  fun enablesAutomaticChangelistCreation() {
    val config = ChangelistsSettingsConfig

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
          openTreeSettingsSection(config.versionControlSectionName, config.changelistsPageName)

          val checkbox = content().checkBoxWithName(config.automaticChangelistsCheckboxName)
          checkbox.check()
          checkbox.waitSelected(true)
          assertTrue(checkbox.isSelected(), "'${config.automaticChangelistsCheckboxName}' checkbox should be selected")
          okButton.click()
        }
      }
    }
  }
}
