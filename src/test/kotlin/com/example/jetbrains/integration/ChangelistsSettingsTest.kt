package com.example.jetbrains.integration

import com.intellij.driver.client.impl.DriverImpl
import com.intellij.driver.client.impl.JmxHost
import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.checkBoxWithName
import com.intellij.driver.sdk.ui.components.elements.waitSelected
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.driver.sdk.waitFor
import com.intellij.ide.starter.config.ConfigurationStorage
import com.intellij.ide.starter.config.useDockerContainer
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.IdeInfo
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes

class ChangelistsSettingsTest {
  @Test
  fun enablesAutomaticChangelistCreation() {
    ConfigurationStorage.useDockerContainer(true)

    val testContext = Starter
      .newContext(
        CurrentTestMethod.hyphenateWithClass(),
        TestCase(
          ideaCommunity,
          GitHubProject.fromGithub(
            branchName = "master",
            repoRelativeUrl = "jitpack/gradle-simple.git",
            commitHash = "abbeb794eb3ae7d9926f5bd7de58477abcbaa906",
          ),
        ),
      )
      .prepareProjectCleanImport()

    val ideRun = testContext.runIdeWithDriver()
    val driver = DriverImpl(JmxHost(address = "127.0.0.1:7777"), isRemDevMode = false)

    try {
      waitFor("Driver is connected", 3.minutes) { driver.isConnected }
      Thread.sleep(10_000)

      driver.ideFrame {
        openSettingsDialog()

        settingsDialog {
          openTreeSettingsSection("Version Control", "Changelists")

          val checkbox = content().checkBoxWithName("Create changelists automatically")
          checkbox.check()
          checkbox.waitSelected(true)
          assertTrue(checkbox.isSelected(), "'Create changelists automatically' checkbox should be selected")
          okButton.click()
        }
      }
    } finally {
      if (driver.isConnected) {
        driver.exitApplication()
        driver.close()
      }
      waitFor("IDE process exits", 1.minutes) { !ideRun.process.isAlive }
    }
  }
}

private val ideaCommunity = IdeInfo(
  productCode = "IC",
  platformPrefix = "Idea",
  executableFileName = "idea",
  fullName = "IntelliJ IDEA Community",
)
