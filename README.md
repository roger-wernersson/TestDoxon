# TestDoxon
Create documentation based on the TestDox standard.
## Eclipse plugin
	Pull the code from git. Open the project in Eclipse IDE. Go to the Eclipse marketplace. 
	Install “Eclipse PDE 3.10 Luna”. Now right-click the project and select “Build path” -> “Add libraries”. 
	Choose “Plug-in dependencies” -> “Next” -> “Finish”. Run the program as “Eclipse Application” to do this first.
	Right-click the project. Select “run as” -> “Run configurations”. Double click “Eclipse-Application” -> “Run”. 
	When you run the project as an eclipse application a new IDE window will appear with the plugin installed to for you to test your changes.

## IntelliJ plugin
	Start by pulling the code from git and open it in the intellij IDE. Run the program as a gradle project with the task “:runIde”. 
	To do this go to the dropdown at the top right of the IDE and select “Edit Configurations”. 
	Under “Default” choose “gradle” and open the TestDoxon project. Then set the task to “:runIde”. 
	Now you can modify the code and test your solution.

## Maven-site plugin 
