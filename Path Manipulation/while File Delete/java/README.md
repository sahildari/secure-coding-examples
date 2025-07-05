# Path Manipulation

This maven project is to help to mitigate the path manipulation issues. You can use the logic in the [DownloadController.java](filedelete.pathmanipulation/src/main/java/securecodingexamples/filedelete/pathmanipulation/DeleteController.java) in your local [Maven](https://maven.apache.org/), [Gradle](https://gradle.org/) or other Java Applications.

## Code Structure

[HomeController](filedelete.pathmanipulation/src/main/java/securecodingexamples/filedelete/pathmanipulation/HomeController.java) file serves the [index.html](filedelete.pathmanipulation/src/main/resources/templates/index.html) to serve as the Fronted for the File Upload.

[DownloadController.java](filedelete.pathmanipulation/src/main/java/securecodingexamples/filedelete/pathmanipulation/DeleteController.java) file contains the logic for the file Upload and the Filename validation, Extension Validation during the File Upload.

[resources/templates](filedelete.pathmanipulation/src/main/resources/templates/) Directory contains the index.html.

Please note that this project will try to fetch the files from your ***TEMP/Uploads*** directory. You can either manually create your files in the directory, or you can navigate to [Path Manipulation while File Upload Java Project](../../while%20File%20Upload/java/) and follow the installation steps and Upload the test files.

*TEMP : temporary Folder in your OS*

*%TEMP% Directory in Windows*

*/tmp Directory in Linux/MacOS*

## Installation
1. Clone the repository:
```sh
git clone https://github.com/sahildari/secure-coding-examples
cd 'Path Manipulation/while File Read/java/fileread.pathmanipulation'
```
2. Install the package:

**MacOS/Linux:**
```sh
./mvnw clean spring-boot:run
```

**Windows:**
```sh
./mvnw.cmd clean spring-boot:run
```
3. Open in Browser:
```
http://127.0.0.1:8080
```
