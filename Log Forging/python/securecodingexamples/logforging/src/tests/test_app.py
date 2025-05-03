#!/usr/bin/env python3

import pytest
from securecodingexamples.logforging.src.app import app
from securecodingexamples.logforging.src.logforging import sanitize_input

# -------------------
# Setup for Pytest
# -------------------
@pytest.fixture
def client():
    app.config["TESTING"] = True
    with app.test_client() as client:
        yield client

# -------------------
# Unit Tests for Name Sanitization
# -------------------

@pytest.mark.parametrize("name, expected", [
    ("j\nohn", "j_ohn"),
    ("\nJohn", "_John"),
    ("john@1", "john_1"),
    ("\n\r@#<>/", "___"),
    ("Normal name", "Normal name")
])
def test_is_valid_name(name, expected):
    assert sanitize_input(name) == expected

# -------------------
# Unit Tests for Comment Sanitization
# -------------------

@pytest.mark.parametrize("comment, expected", [
    ("This is a demo comment", "This is a demo comment"),
    ("Some Malicious Comment \r\n", "Some Malicious Comment __"),
    ("", ""),
    ("Sep 11:2018:01:07:13: Successful Login, ID=sha", "Sep 11_2018_01_07_13_ Successful Login_ ID_sha"),
    ('2025-04-13 23:44 - INFO - werkzeug - 127.0.0.1 - - [13/Apr/2025 23:44:53] "POST /admin HTTP/1.1" 200 -', "2025-04-13 23_44 - INFO - werkzeug - 127_0_0_1 - - _13_Apr_2025 23_44_53_ _POST _admin HTTP_1_1_ 200 -")
])
def test_is_valid_comment(comment, expected):
    assert sanitize_input(comment) == expected

# -------------------
# Integration Test Get Method
# -------------------

def test_home_get(client):
    response = client.get("/")
    assert response.status_code == 200
    assert b"<html" in response.data or b"<!DOCTYPE html" in response.data

# -------------------
# Integration Test Post Data Sanitization
# -------------------

def test_home_post_valid_data(client):
    data = {
        "name" : "John Doe \nINFO",
        "comment" : "Trying Log Injection or Log Forging \r\n2025-04-13 23:44 - CRITICAL - Could not fetch admin details"
    }
    response = client.post("/", data=data)
    assert response.status_code == 200
    assert b"Greetings John Doe _INFO" in response.data
    assert b"Trying Log Injection or Log Forging __2025-04-13 23_44 - CRITICAL - Could not fetch admin details" in response.data

# -------------------
# Integration Test Post Data Maximum Length Validation
# -------------------

def test_maximum_input_length(client):
    long_name = b"a" * 100
    long_comment = b"c" * 700
    data = {
        "name" : long_name,
        "comment" : long_comment
    }

    response = client.post("/", data=data)
    assert response.status_code == 200
    assert b"a" * 50 in response.data
    assert b"c" * 500 in response.data