#!/usr/bin/env python3

from flask import Flask, request, jsonify, render_template
import os
import logging
from .pathmanipulation import is_valid_name, is_valid_extension, valid_filename

app = Flask(__name__)
app.config["UPLOAD_DIRECTORY"] = "Uploads"

logger = logging.getLogger(__name__)

@app.route("/")
def main():
    """Render the index.html page."""
    os.makedirs(app.config["UPLOAD_DIRECTORY"], exist_ok=True)
    os.makedirs("log", exist_ok=True)

    return render_template("index.html")

@app.route("/upload_file", methods=["POST"])
def upload_file():
    """Handles file upload with validation."""
    try:
        logger.info("upload_file() method started")

        if "file" not in request.files:
            return jsonify({"error": "No file part"})

        file = request.files["file"]
        if file.filename == "":
            return jsonify({"error": "No selected file"})

        if file and is_valid_name(file.filename) and is_valid_extension(file.filename):
            filename = valid_filename(file.filename)
            if filename:
                file_path = os.path.join(app.config["UPLOAD_DIRECTORY"], filename)
                file.save(file_path)
                logger.info(f"File '{filename}' uploaded successfully")
                return jsonify({"success": "File uploaded successfully"})
            else:
                return jsonify({"error": "Invalid filename"})
        else:
            return jsonify({"error": "Invalid file format"})
    except Exception as e:
        logger.error(f"File upload error: {str(e)}")
        return jsonify({"error": "Internal server error"})
