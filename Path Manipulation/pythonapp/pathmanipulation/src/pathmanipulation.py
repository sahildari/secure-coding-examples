#!/usr/bin/env python3

import re

FILENAME_REGEX_PATTERN = r"^[a-zA-Z0-9\-\_]+$"
ALLOWED_EXTENSIONS = set(["txt", "pdf"]) # this example for the txt and pdf files being allowed

def is_valid_name(filename):
    name = filename.rsplit('.', 1)[0]
    name = re.sub(r"\.", "", name)
    name = re.sub(r"^%[A-Za-z0-9]{1,5}$","", name)
    regex_match = re.search(FILENAME_REGEX_PATTERN, name)
    return True if(regex_match != None) else False

def is_valid_extension(filename):
    ext = filename.rsplit(".", 1)[1]
    return True if (ext in ALLOWED_EXTENSIONS) else False

def valid_filename(filename):
    name, ext = filename.rsplit('.', 1)
    name = re.sub(r"\.", "", name)
    name = re.sub(r"\%[A-Za-z0-9]+", "", name)
    ext = re.sub(r"\%[A-Za-z0-9]+", "", ext)
    regex_match = re.search(FILENAME_REGEX_PATTERN, name)
    return f"{regex_match.group(0)}.{ext}"
