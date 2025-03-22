#!/usr/bin/env python3

import re
import os

# Define Magic Numbers for allowed  file types
MAGIC_NUMBERS = {
    "png" : bytes([0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A]),
    "jpg" : bytes([0xFF, 0xD8, 0xFF]),
    "pdf" : bytes([0x25, 0x50, 0x44, 0x46])
}

FILENAME_REGEX_PATTERN = r"^[a-zA-Z0-9\-\_]+$"
ALLOWED_EXTENSIONS = set(MAGIC_NUMBERS.keys()) # this example allows the text and pdf files
MAX_READ_SIZE = max(len(m) for m in MAGIC_NUMBERS.values()) 

"""This function checks if the filename is valid and doesn't contain any dot character in the name."""
def is_valid_name(filename: str) -> bool:
    name = filename.rsplit('.', 1)[0]
    regex_match = re.search(FILENAME_REGEX_PATTERN, name)
    return True if(regex_match != None) else False

"""This function checks for the valid file extensions."""
def is_valid_extension(filename: str) -> bool:
    ext = filename.rsplit(".", 1)[1].lower()
    return True if (ext in ALLOWED_EXTENSIONS) else False

def valid_filename(filename: str) -> str:
    name, ext = filename.rsplit('.', 1)
    name = re.sub(r"\.", "", name)
    if "%" in name:
        name = re.sub(r"\%", "", name)
    if "%" in ext:
        ext = re.sub(r"\%", "", ext).lower()
    regex_match = re.search(FILENAME_REGEX_PATTERN, name)
    return f"{regex_match.group(0)}.{ext}" # returns the sanitized filename with the extension for upload 

def get_unique_filename(directory: str, filename: str) -> str:
    name, ext = filename.rsplit(".", 1)
    unique_filename = f"{name}.{ext}"
    count = 1

    while os.path.exists(os.path.join(directory, unique_filename)):
        unique_filename = f"{name}_{count}.{ext}"
        count += 1

    return unique_filename

"""Reads the first few bytes of the file to determine its magic number."""
def get_magic_number(file) -> str:
    file.seek(0)  # Reset file pointer
    magic_number = file.read(MAX_READ_SIZE)  # Read the first 8 bytes (maximum magic number length)
    file.seek(0)  # Reset pointer again after reading
    return magic_number

"""Checks if the file's magic number matches the expected magic number."""
def is_valid_magic_number(file, extension: str) -> bool:
    expected_magic = MAGIC_NUMBERS.get(extension)
    if expected_magic:
        return get_magic_number(file).startswith(expected_magic)
    return False