#!/usr/bin/env python3

import re

ILLEGAL_REGEX_PATTERN = r"[^a-zA-Z0-9\-\_ ]+"

def sanitize_input(string: str) -> str:
    string = string.replace("\n", "_").replace("\r", "_")
    return re.sub(ILLEGAL_REGEX_PATTERN, "_", string)
