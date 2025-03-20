#!/usr/bin/env python3

import re
import os

FILENAME_REGEX_PATTERN = r"^[a-zA-Z0-9\-\_]+$"
ALLOWED_EXTENSIONS = set(["txt", "pdf"]) # this example allows the text and pdf files 


"""This function checks if the filename is valid and doesn't contain any dot character in the name."""
def is_valid_name(filename: str) -> bool:
    name = filename.rsplit('.', 1)[0]
    regex_match = re.search(FILENAME_REGEX_PATTERN, name)
    return True if(regex_match != None) else False

"""This function checks for the valid file extensions."""
def is_valid_extension(filename: str) -> bool:
    ext = filename.rsplit(".", 1)[1]
    return True if (ext in ALLOWED_EXTENSIONS) else False

def valid_filename(filename: str) -> str:
    name, ext = filename.rsplit('.', 1)
    name = re.sub(r"\.", "", name)
    name = re.sub(r"\%[A-Za-z0-9]+", "", name)
    ext = re.sub(r"\%[A-Za-z0-9]+", "", ext)
    regex_match = re.search(FILENAME_REGEX_PATTERN, name)
    return f"{regex_match.group(0)}.{ext}" # returns the sanitized filename with the extension for upload 

def get_unique_filename(directory: str, filename:str) -> str:
    name, ext = filename.rsplit(".", 1)
    unique_filename = f"{name}.{ext}"
    count = 1

    while os.path.exists(os.path.join(directory, unique_filename)):
        unique_filename = f"{name}_{count}.{ext}"
        count += 1

    return unique_filename
