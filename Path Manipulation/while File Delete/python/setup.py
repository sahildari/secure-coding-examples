#!/usr/bin/env python3

from setuptools import setup, find_packages

with open("README.md", "r") as f:
    long_description = f.read()

setup(
    name="filedelete.pathmaniuplation",
    version="0.0.1",
    description="Secure Coding Example Mitigating the issue of Path Manipulation (CWE-22), (CWE-34), (CWE-35), (CWE-73)",
    long_description=long_description,
    long_description_content_type="text/markdown",
    package_dir={"": "securecodingexamples"},
    packages=find_packages(where="securecodingexamples"),
    include_package_data=True,
    url="https://github.com/sahildari/secure-coding-examples",
    author="Sahil Dari",
    author_email="sahil9dari@gmail.com",
    license="GPLv3",
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: GNU General Public License v3 (GPLv3)",
        "Operating System :: OS Independent",
    ],
    install_requires=[
        "flask > 3.0.0"
    ],
    python_requires=">=3.6",
    package_data={
        "securecodingexamples.filedelete.pathmanipulation": [
            "src/templates/*.html"
            ]
    },
)
