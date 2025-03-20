# import os
import tempfile
import pytest
from securecodingexamples.fileupload.pathmanipulation.src.app import app
from securecodingexamples.fileupload.pathmanipulation.src.pathmanipulation import (
    is_valid_name,
    is_valid_extension,
    valid_filename,
)

# -------------------
# Setup for Pytest
# -------------------
@pytest.fixture
def client():
    app.config["TESTING"] = True
    app.config["UPLOAD_DIRECTORY"] = tempfile.mkdtemp()  # Temporary upload folder
    with app.test_client() as client:
        yield client

# -------------------
# Unit Tests for Path Manipulation Functions
# -------------------

# ----------------------
# TESTS FOR is_valid_name
# ----------------------
@pytest.mark.parametrize("filename, expected", [
    ("valid_filename.exe", True),
    ("file-name_123.txt", True),
    ("invalid.file.name.doc", False),
    ("file%20name.php", False),
    ("../etc/passwd.pdf", False),
    (".pdf", False),
    ("onlynumbers123.ini", True),
    ("file@name.ini", False),
])
def test_is_valid_name(filename, expected):
    assert is_valid_name(filename) == expected

# -------------------------
# TESTS FOR is_valid_extension
# -------------------------
@pytest.mark.parametrize("filename, expected", [
    ("document.txt", True),
    ("file.pdf", True),
    ("file.doc", False),
    ("file.TXT", False),
    ("file.php%00", False),
    ("file.", False),
])
def test_is_valid_extension(filename, expected):
    assert is_valid_extension(filename) == expected

# ----------------------
# TESTS FOR valid_filename
# ----------------------
@pytest.mark.parametrize("filename, expected", [
    ("validfile.txt", "validfile.txt"),
    ("file.name.txt", "filename.txt"),
    ("file%20name.txt", "file.txt"),
    ("testfile..pdf", "testfile.pdf"),
    ("file%23.txt", "file.txt"),
])
def test_valid_filename(filename, expected):
    assert valid_filename(filename) == expected

# -------------------
# Integration Tests for File Upload API
# -------------------

def test_upload_valid_file(client):
    """Test valid file upload."""
    data = {
        "file": (tempfile.NamedTemporaryFile(suffix=".txt"), "testfile.txt")
    }
    response = client.post("/upload_file", content_type='multipart/form-data', data=data)
    assert response.status_code == 200
    assert b"File uploaded successfully" in response.data

def test_upload_file_with_invalid_name(client):
    """Test invalid filename during upload."""
    data = {
        "file": (tempfile.NamedTemporaryFile(suffix=".txt"), "../evilfile.txt")
    }
    response = client.post("/upload_file", content_type='multipart/form-data', data=data)
    assert response.status_code == 400
    assert b"Invalid file format" in response.data

def test_upload_file_with_invalid_extension(client):
    """Test file with unsupported extension."""
    data = {
        "file": (tempfile.NamedTemporaryFile(suffix=".exe"), "malware.exe")
    }
    response = client.post("/upload_file", content_type='multipart/form-data', data=data)
    assert response.status_code == 400
    assert b"Invalid file format" in response.data

def test_upload_no_file_provided(client):
    """Test request with no file."""
    response = client.post("/upload_file", content_type='multipart/form-data', data={})
    assert response.status_code == 400
    assert b"No file part" in response.data

def test_file_size_exceeds_limit(client):
    """Test file exceeding size limit."""
    large_file = tempfile.NamedTemporaryFile(suffix=".txt")
    large_file.write(b"A" * (5 * 1024 * 1024 + 1))  # Exceeds 5 MB limit
    large_file.seek(0)

    data = {
        "file": (large_file, "largefile.txt")
    }
    response = client.post("/upload_file", content_type='multipart/form-data', data=data)
    assert response.status_code == 413
    assert b"File size exceeds the limit" in response.data