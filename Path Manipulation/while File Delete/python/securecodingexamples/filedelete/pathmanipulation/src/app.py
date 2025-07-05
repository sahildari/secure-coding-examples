#!/usr/bin/env python3

from flask import Flask, request, jsonify, render_template, redirect, url_for
import os
import tempfile
import logging
from .pathmanipulation import is_valid_name, is_valid_extension, valid_filename

app = Flask(__name__)
TEMPDIR = tempfile.gettempdir()
LOGDIR = os.path.join(TEMPDIR + "/logs/")
UPLOADDIR = os.path.join(TEMPDIR + "/Uploads/")

os.makedirs(LOGDIR, exist_ok=True)
os.makedirs(UPLOADDIR, exist_ok=True)

loglocation = os.path.join(LOGDIR + "app.log")
logging.basicConfig(
    level=logging.INFO,
    handlers= [
        logging.FileHandler(loglocation),
        logging.StreamHandler()
        ]
    )

logger = logging.getLogger(__name__)

@app.route("/")
def index():
    # List files in the directory
    files = sorted(f for f in os.listdir(UPLOADDIR) if os.path.isfile(os.path.join(UPLOADDIR, f)))
    return render_template("index.html", files=files)

@app.route("/delete", methods=["POST"])
def delete_file():
    filename = request.form.get("filename", "")
    if not filename:
        logger.warning("No filename provided for deletion")
        return "No filename provided", 400

    if not is_valid_name(filename):
        logger.warning("Invalid filename: %s", filename)
        return "Invalid filename", 400

    if not is_valid_extension(filename):    
        logger.warning("Invalid extension: %s", filename)
        return "Invalid file extension", 400

    filename = valid_filename(filename)
    file_path = os.path.abspath(os.path.join(UPLOADDIR, filename))

    if not file_path.startswith(os.path.abspath(UPLOADDIR)):
        logger.warning("Path traversal attempt: %s", filename)
        return "Path traversal detected", 400

    if not os.path.exists(file_path):
        logger.warning("File does not exist: %s", filename)
        return "File not found", 404

    try:
        os.remove(file_path)
        logger.info("Deleted file: %s", filename)
        return redirect(url_for('index'))
    except Exception as e:
        logger.error("Error deleting file: %s", e)
        return "Error deleting file", 500

if __name__ == "__main__":
    app.run(debug=True)