#!/usr/bin/env python3

from flask import Flask, request, jsonify, render_template
import os
import tempfile
from werkzeug.exceptions import RequestEntityTooLarge
import logging
from .pathmanipulation import is_valid_name, is_valid_extension, valid_filename, get_unique_filename

app = Flask(__name__)
TEMPDIR = tempfile.gettempdir()
UPLOADDIR = os.path.join(TEMPDIR + "/uploads/")
LOGDIR = os.path.join(TEMPDIR + "/logs/")

os.makedirs(UPLOADDIR, exist_ok=True)
os.makedirs(LOGDIR, exist_ok=True)

app.config["UPLOAD_DIRECTORY"] = UPLOADDIR
app.config["MAX_CONTENT_LENGTH"] = 5 * 1024 * 1024  # 5 MB
loglocation = os.path.join(LOGDIR + "app.log")
logging.basicConfig(
    level=logging.INFO,
    handlers= [
        logging.FileHandler(loglocation),
        logging.StreamHandler()
        ]
    )

logger = logging.getLogger(__name__)

@app.route("/", methods=["GET"])
def main():
    """Render the index.html page."""
    return render_template("index.html")

@app.errorhandler(413)
def request_entity_too_large(error):
    return jsonify({"error": "File size exceeds the limit"}), 413

@app.route("/upload_file", methods=["POST"])
def upload_file():
    max_length = request.max_content_length
    """Handles file upload with validation."""
    try:
        logger.info("upload_file() method started")

        if "file" not in request.files:
            return jsonify({"error": "No file part"}), 400

        file = request.files["file"]
        if file.filename == "":
            return jsonify({"error": "No selected file"}), 400
        
        if request.content_length > max_length:
            return jsonify({"error": "File size exceeds the limit"}), 400

        if file and is_valid_name(file.filename) and is_valid_extension(file.filename):
            filename = valid_filename(file.filename)
            if filename:
                unique_filename = get_unique_filename(app.config["UPLOAD_DIRECTORY"], valid_filename(file.filename))
                file_path = os.path.join(app.config["UPLOAD_DIRECTORY"], unique_filename)
                file.save(file_path)
                logger.info(f"File '{unique_filename}' uploaded successfully at {file_path}")
                return jsonify({"success": "File uploaded successfully"}), 200
            else:
                return jsonify({"error": "Invalid filename"}), 400
        else:
            return jsonify({"error": "Invalid file format"}), 400
    except RequestEntityTooLarge as e:
        logger.error(f"File upload error: {str(e)}")
        return jsonify({"error": "File size exceeds the limit"}), 413
    except Exception as e:
        logger.error(f"File upload error: {str(e)}")
        return jsonify({"error": "Internal server error"}), 500
