# Path Manipulation 

I have written a detailed blog for Path Manipulation, you can check it [here](https://sahildari.medium.com/sast-series-part-1-a7cf18df0022)

## Definition as per OWASP
**Path Manipulation** attack also known as **Path Traversal** attack, aims to access files and directories that are stored outside the web root folder. By manipulating variables that reference files with “dot-dot-slash (../)” sequences and its variations or by using absolute file paths, it may be possible to access arbitrary files and directories stored on file system including application source code or configuration and critical system files. It should be noted that access to files is limited by system operational access control (such as in the case of locked or in-use files on the Microsoft Windows operating system).

This attack is also known as **"dot-dot-slash"**, **"directory traversal"**, **"directory climbing"** and **"backtracking"**.

You can use this repo as reference to fix the Path Manipulation issue [CWE-22](https://cwe.mitre.org/data/definitions/22.html), [CWE-34](https://cwe.mitre.org/data/definitions/34.html), [CWE-35](https://cwe.mitre.org/data/definitions/35.html), [CWE-73](https://cwe.mitre.org/data/definitions/73.html)

## Mitigation

🔍 Proper validation and strict controls over file operations are essential to prevent these threats!

✅Validate Filename: Disallow dot character (“.”), percentage character (“%”), slash character (“/”) and/or back slash characters (“\”) and other special characters.

✅Extension Whitelist: Add validation to allow only whitelisted extensions.

✅Limit File Size: Only allows files within your allowed range.

✅Unique Filename Validation: Prevent overwriting the existing files on the server.

## NOTE 
The code for the Path Manipulation only check for the Filename validation, Extension Validation, File Size Validation, Unique Filename Validation. ***It doesn't check for the File Contents and Magic Numbers. Use this logic when you are concerned about the Path Manipulation issue ONLY***.

The Path Manipulation logic checks for the following:
- The Filename Validation, to only contain Alphanumeric values with the help of regex.
- The extension validation, to only allow files with certain extension.
- File size validation, to only allows files within the 5MB file size.
- Same Filename validation, to only allow the unique files to be uploaded and not rewrite the existing file.

## Directory Structure for Path Manipulation
```
Path Manipulation
├───while File Read
│   ├───java
│   │   └───fileread.pathmanipulation
│   │       ├───.mvn
│   │       │   └───wrapper
│   │       └───src
│   │           ├───main
│   │           │   ├───java
│   │           │   │   └───securecodingexamples
│   │           │   │       └───fileread
│   │           │   │           └───pathmanipulation
│   │           │   └───resources
│   │           │       └───templates
│   │           └───test
│   │               └───java
│   │                   └───securecodingexamples
│   │                       └───fileread
│   │                           └───pathmanipulation
│   └───python
│       └───securecodingexamples
│           └───fileread
│               └───pathmanipulation
│                   └───src
│                       └───templates
└───while File Upload
    ├───java
    │   └───fileupload.pathmanipulation
    │       ├───.mvn
    │       │   └───wrapper
    │       └───src
    │           ├───main
    │           │   ├───java
    │           │   │   └───securecodingexamples
    │           │   │       └───fileupload
    │           │   │           └───pathmanipulation
    │           │   └───resources
    │           │       └───static
    │           └───test
    │               └───java
    │                   └───securecodingexamples
    │                       └───fileupload
    │                           └───pathmanipulation
    └───python
        └───securecodingexamples
            └───fileupload
                └───pathmanipulation
                    └───src
                        ├───templates
                        └───tests
```
