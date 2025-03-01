# Path Manipulation 

## Definition as per OWASP
**Path Manipulation** attack also known as **Path Traversal** attack, aims to access files and directories that are stored outside the web root folder. By manipulating variables that reference files with “dot-dot-slash (../)” sequences and its variations or by using absolute file paths, it may be possible to access arbitrary files and directories stored on file system including application source code or configuration and critical system files. It should be noted that access to files is limited by system operational access control (such as in the case of locked or in-use files on the Microsoft Windows operating system).

This attack is also known as _“dot-dot-slash”_, _“directory traversal”_, _“directory climbing”_ and _“backtracking”_.

**NOTE:** The code for the Path Manipulation only check for the Filename validation, Extension Validation. It doesn't check for the File Contents and Magic Numbers. Use this only when you are only worried about the Path Manipulation issue.

## Directory Structure for Path Manipulation
```sh
PATH MANIPULATION
├───java
│   ├───src
│   │   ├───main
│   │   │   ├───java
│   │   │   │   └───securecodingexamples
│   │   │   │       └───pathmanipulation
│   │   │   └───resources
│   │   │       └───static
│   │   └───test
│   │       └───java
│   │           └───securecodingexamples
│   │               └───pathmanipulation
└───python
    └───pythonapp
        └───pathmanipulation
            └───src
                └───templates
```

