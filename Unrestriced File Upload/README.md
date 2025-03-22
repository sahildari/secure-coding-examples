# Unrestricted File Upload

__Unrestricted File Upload__, as the name suggests, the file uploads are possible with minimal restrictions or no restrictions at all. When the developers don't put the File Upload Restrictions on the server-side code, the **attackers** are able to upload **Malicious File** with **Dangerous Types**, which can in turn result more severe issues.

You can use the repo as refernce to fix the Unrestricted File Upload issue [CWE-434](https://cwe.mitre.org/data/definitions/434.html)

## Mitigation

Unrestricted File Upload issues can be mitigated by putting Restrictions on everything possible for the file like **filename, file extensions, Content Type, Magic Numbers**. 

The Unrestricted File Upload logic checks for the following:
- The Filename Validation, to only contain Alphanumeric values with the help of regex.
- The extension validation, to only allow files with certain extension.
- Content Type validation, to only allow the files matching the content type for the allowed extensions with the help of magic numbers.
- File size validation, to only allows files within the 5MB file size.
- Same Filename validation, to only allow the unique files to be uploaded and not rewrite the existing file.

You can check the file signatures tables by visiting this [link](https://www.garykessler.net/library/file_sigs.html).

## Directory Structure for UNrestricted File Upload
```
Unrestriced File Upload
├───java
│   ├───.mvn
│   │   └───wrapper
│   └───src
│       ├───main
│       │   ├───java
│       │   │   └───securecodingexamples
│       │   │       └───unrestricted
│       │   │           └───fileupload
│       │   └───resources
│       │       └───static
│       └───test
│           └───java
│               └───securecodingexamples
│                   └───fileupload
│                       └───pathmanipulation
└───python
    └───securecodingexamples
        └───unrestricted
            └───fileupload
                └───src
                    ├───templates
                    └───tests
```
