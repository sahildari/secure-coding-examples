import tempfile
import pytest
from securecodingexamples.unrestricted.fileupload.src.app import app
from securecodingexamples.unrestricted.fileupload.src.pathmanipulation import (
    is_valid_name,
    is_valid_extension,
    valid_filename,
)
from werkzeug.datastructures import FileStorage

# -------------------
# Setup for Pytest
# -------------------
@pytest.fixture
def client():
    """Create a Flask test client with a temporary upload directory."""
    app.config["TESTING"] = True
    app.config["UPLOAD_DIRECTORY"] = tempfile.mkdtemp()  # Temporary upload folder
    with app.test_client() as client:
        yield client

# ----------------------
# TESTS FOR is_valid_name
# ----------------------
@pytest.mark.parametrize("filename, expected", [
    ("valid_filename.pdf", True),
    ("file-name_123.jpg", True),
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
    ("document.png", True),
    ("file.pdf", True),
    ("file.JPG", True),
    ("file.php%00", False),
    ("file.", False),
])
def test_is_valid_extension(filename, expected):
    assert is_valid_extension(filename) == expected

# ----------------------
# TESTS FOR valid_filename
# ----------------------
@pytest.mark.parametrize("filename, expected", [
    ("validfile.pdf", "validfile.pdf"),
    ("file.name.jpg", "filename.jpg"),
    ("file%20name.png", "file20name.png"),
    ("testfile..pdf", "testfile.pdf"),
    ("file%23.txt", "file23.txt"),
])
def test_valid_filename(filename, expected):
    assert valid_filename(filename) == expected

# -------------------
# Integration Tests for File Upload API
# -------------------

def test_upload_valid_file(client):
    """Test valid file upload."""
    test_file = tempfile.NamedTemporaryFile(suffix=".pdf")
    test_file.write(b"%PDF-1.4 Sample content")  # Simulating a PDF file
    test_file.seek(0)

    data = {
        "file": (test_file, "testfile.pdf")
    }
    response = client.post("/upload_file", content_type="multipart/form-data", data=data)
    
    assert response.status_code == 200
    assert b"File uploaded successfully" in response.data

def test_upload_invalid_filename(client):
    """Test invalid filename during upload."""
    test_file = tempfile.NamedTemporaryFile(suffix=".pdf")
    test_file.write(b"%PDF-1.4 Sample content")
    test_file.seek(0)

    data = {
        "file": (test_file, "../evilfile.pdf")
    }
    response = client.post("/upload_file", content_type="multipart/form-data", data=data)

    assert response.status_code == 400
    assert b"Invalid Filename" in response.data

def test_upload_invalid_extension(client):
    """Test file with unsupported extension."""
    test_file = tempfile.NamedTemporaryFile(suffix=".exe")
    test_file.write(b"MZ Sample content")  # Simulating an EXE file
    test_file.seek(0)

    data = {
        "file": (test_file, "malware.exe")
    }
    response = client.post("/upload_file", content_type="multipart/form-data", data=data)

    assert response.status_code == 400
    assert b"Invalid Extension" in response.data

def test_upload_invalid_magic_number(client):
    """Test upload with a mismatched magic number (e.g., PNG file renamed as PDF)."""
    test_file = tempfile.NamedTemporaryFile(suffix=".pdf")
    test_file.write(b"\x89PNG\r\n\x1A\nFake PNG content")  # PNG header in a .pdf file
    test_file.seek(0)

    data = {
        "file": (test_file, "fake.pdf")
    }
    response = client.post("/upload_file", content_type="multipart/form-data", data=data)

    assert response.status_code == 400
    assert b"Invalid File Type" in response.data

def test_upload_no_file_provided(client):
    """Test request with no file."""
    response = client.post("/upload_file", content_type="multipart/form-data", data={})
    
    assert response.status_code == 400
    assert b"No file part" in response.data

def test_file_size_exceeds_limit(client):
    """Test file exceeding size limit."""
    large_file = tempfile.NamedTemporaryFile(suffix=".pdf")
    large_file.write(b"A" * (5 * 1024 * 1024 + 1))  # Exceeds 5 MB limit
    large_file.seek(0)

    data = {
        "file": (large_file, "largefile.pdf")
    }
    response = client.post("/upload_file", content_type="multipart/form-data", data=data)
    
    assert response.status_code == 413
    assert b"File size exceeds the limit" in response.data
