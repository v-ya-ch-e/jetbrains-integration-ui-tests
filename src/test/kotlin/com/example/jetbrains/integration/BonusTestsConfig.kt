package com.example.jetbrains.integration

import com.intellij.ide.starter.models.IdeInfo
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import kotlin.time.Duration.Companion.seconds

internal object BonusTestsConfig {
  const val useDockerContainer = false
  const val licenseKeyEnvironmentVariable = "LICENSE_KEY"

  const val editorSectionName = "Editor"
  const val editorSettingsOverviewText = "Personalize source code appearance"
  const val fontPageName = "Font"
  const val fontPageVerificationText = "Enable ligatures"
  const val colorSchemePageName = "Color Scheme"
  const val colorSchemePageVerificationText = "Scheme:"

  val ideStartupSettleDelay = 10.seconds

  // The build-server IdeaUltimate descriptor is intended for IntelliJ dev builds.
  private val ideaUltimate = IdeInfo(
    productCode = "IU",
    platformPrefix = "Idea",
    executableFileName = "idea",
    fullName = "IntelliJ IDEA Ultimate",
  )

  val testCase = TestCase(
    ideaUltimate,
    GitHubProject.fromGithub(
      branchName = "master",
      repoRelativeUrl = "jitpack/gradle-simple.git",
      commitHash = "abbeb794eb3ae7d9926f5bd7de58477abcbaa906",
    ),
  )
}
