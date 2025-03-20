# Path Manipulation

This maven project is to help to mitigate the path manipulation issues. You can use the logic in the [DownloadController.java](./fileread.pathmanipulation/src/main/java/securecodingexamples/fileread/pathmanipulation/DownloadController.java) in your local [Maven](https://maven.apache.org/), [Gradle](https://gradle.org/) or other Java Applications.

## Code Structure

[HomeController](./fileread.pathmanipulation/src/main/java/securecodingexamples/fileread/pathmanipulation/HomeController.java) file serves the [index.html](./fileread.pathmanipulation/src/main/resources/templates/index.html) to serve as the Fronted for the File Upload.

[DownloadController.java](./fileread.pathmanipulation/src/main/java/securecodingexamples/fileread/pathmanipulation/DownloadController.java) file contains the logic for the file Upload and the Filename validation, Extension Validation during the File Upload.

[resources/templates](./fileread.pathmanipulation/src/main/resources/templates/) Directory contains the index.html.

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
