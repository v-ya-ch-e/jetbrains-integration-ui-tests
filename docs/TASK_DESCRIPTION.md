We use the following library for writing integration UI tests: https://github.com/JetBrains/intellij-community/blob/master/tools/intellij.tools.ide.starter/README.md
Here you can find an example of a UI test:
https://github.com/JetBrains/intellij-ide-starter/blob/master/intellij.tools.ide.starter.examples/testSrc/com/intellij/ide/starter/examples/driver/UiTestWithDriver.kt

Create your own project where you should write a test for the following scenario:

- Open an IDE (you can use any type of an IDE created by JetBrains)
- You can use any publicly available project as a test project
- Open Settings...
- Choose "Version Control" and then "Changelists"
- Select the checkbox that is called "Create changelists automatically"
- Check that it is selected.
- Click on the OK button