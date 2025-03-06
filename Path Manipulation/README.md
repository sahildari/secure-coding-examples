# Path Manipulation 

## Definition as per OWASP
**Path Manipulation** attack also known as **Path Traversal** attack, aims to access files and directories that are stored outside the web root folder. By manipulating variables that reference files with “dot-dot-slash (../)” sequences and its variations or by using absolute file paths, it may be possible to access arbitrary files and directories stored on file system including application source code or configuration and critical system files. It should be noted that access to files is limited by system operational access control (such as in the case of locked or in-use files on the Microsoft Windows operating system).

This attack is also known as _“dot-dot-slash”_, _“directory traversal”_, _“directory climbing”_ and _“backtracking”_.

## NOTE 
The code for the Path Manipulation only check for the Filename validation, Extension Validation, File Size Validation, Unique Filename Validation. ___It doesn't check for the File Contents and Magic Numbers. Use this logic when you are concerned about the Path Manipulation issue ONLY___.

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
│               └───pathmaniuplation
│                   └───src
│                       └───templates
└───while File Upload
    ├───java
    │   └───fileupload.pathmanipulation
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
                        └───templates
```
