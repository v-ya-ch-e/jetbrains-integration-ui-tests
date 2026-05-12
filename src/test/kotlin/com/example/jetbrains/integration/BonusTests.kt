package com.example.jetbrains.integration

import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.comboBox
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.ide.starter.config.ConfigurationStorage
import com.intellij.ide.starter.config.useDockerContainer
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.awt.event.KeyEvent

class BonusTests {
  @Test
  fun changesAutoImportOnPasteToAsk() {
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
          openTreeSettingsSection(config.editorSectionName, config.generalPageName, config.autoImportPageName)

          val insertImportsOnPaste = content().comboBox()
          assertEquals(config.autoImportOnPasteDefaultValue, insertImportsOnPaste.getSelectedItem())
          insertImportsOnPaste.click()
          keyboard {
            down()
            down()
            enter()
          }
          assertEquals(config.autoImportOnPasteValue, insertImportsOnPaste.getSelectedItem())

          okButton.click()
        }
      }
    }
  }

  @Test
  fun changesEditorFontSize() {
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

          val size = content().x { byVisibleText(config.defaultFontSizeValue) }
          size.click()
          keyboard {
            hotKey(selectAllModifierKey(), KeyEvent.VK_A)
            typeText(config.fontSizeValue)
          }
          assertTrue(content().hasText(config.fontSizeValue), "Font size should be changed to ${config.fontSizeValue}")

          okButton.click()
        }
      }
    }
  }

  private fun selectAllModifierKey(): Int =
    if (System.getProperty("os.name").startsWith("Mac")) KeyEvent.VK_META else KeyEvent.VK_CONTROL
}
