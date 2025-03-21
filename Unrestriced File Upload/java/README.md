# Unrestricted File Upload

This maven project is to help to mitigate the Unrestricted File Upload issues. You can use the logic in the [UploadController.java](./src/main/java/securecodingexamples/unrestricted/fileupload/UploadController.java) in your local [Maven](https://maven.apache.org/), [Gradle](https://gradle.org/) or other Java Applications.

## Code Structure

[HomeController](./src/main/java/securecodingexamples/unrestricted/fileupload/HomeController.java) file serves the [index.html](./src/main/resources/static/index.html) to serve as the Fronted for the File Upload.

[UploadController.java](./src/main/java/securecodingexamples/unrestricted/fileupload/UploadController.java) file contains the logic for the file Upload and the Filename validation, Extension Validation during the File Upload.

[resources/static](./src/main/resources/static) Directory contains the index.html.

## Installation
1. Clone the repository:
```sh
git clone https://github.com/sahildari/secure-coding-examples
cd 'Unrestriced File Upload/java'
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
