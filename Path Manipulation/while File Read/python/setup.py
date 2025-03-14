#!/usr/bin/env python3

from setuptools import setup, find_packages

with open("README.md", "r") as f:
    long_description = f.read()

setup(
    name="fileread.pathmaniuplation",
    version="0.0.1",
    description="A simple path manipulation example",
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
        "securecodingexamples.fileread.pathmanipulation": [
            "src/templates/*.html"
            ]
    },
)
