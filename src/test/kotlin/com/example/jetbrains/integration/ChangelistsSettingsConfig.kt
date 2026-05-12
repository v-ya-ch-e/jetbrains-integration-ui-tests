package com.example.jetbrains.integration

import com.intellij.ide.starter.models.IdeInfo
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import kotlin.time.Duration.Companion.seconds

internal object ChangelistsSettingsConfig {
  const val useDockerContainer = true
  const val licenseKeyEnvironmentVariable = "LICENSE_KEY"
  const val versionControlSectionName = "Version Control"
  const val changelistsPageName = "Changelists"
  const val automaticChangelistsCheckboxName = "Create changelists automatically"

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
