from setuptools import setup, find_packages

with open("README.md", "r") as f:
    long_description = f.read()
    
setup(
    name="unrestricted.fileupload",
    version="0.0.1",
    description="Secure Coding Example Mitigating the issues of Unrestricted Upload of File with Dangerous Type(CWE-434)",
    package_dir={"": "securecodingexamples"},
    packages=find_packages(where="securecodingexamples"),
    include_package_data=True, 
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/sahildari/secure-coding-examples",
    author="Sahil Dari",
    author_email="sahil9dari@gmail.com",
    license="GPLv3",
    classifiers=[
        "License :: OSI Approved :: GNU General Public License v3 (GPLv3)",
        "Programming Language :: Python :: 3",
        "Operating System :: OS Independent",
    ],
    install_requires=[
        "flask > 3.0.0",
        "pytest",
        "pytest-flask"
        ],
    tests_require=[
        "pytest",
        "pytest-flask",
        "pytest-mock"
        ],
    test_suite="tests",
    python_requires=">=3.8",
    package_data={
        "securecodingexamples.unrestricted.fileupload": [
            "src/templates/*.html"
            ]
        }
)