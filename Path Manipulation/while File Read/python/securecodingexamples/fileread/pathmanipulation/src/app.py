#!/usr/bin/env python3

from flask import Flask, request, jsonify, render_template, send_file
import os
import tempfile
import logging
from .pathmanipulation import is_valid_name, is_valid_extension, valid_filename

app = Flask(__name__)
TEMPDIR = tempfile.gettempdir()
LOGDIR = os.path.join(TEMPDIR + "/logs/")
UPLOADDIR = os.path.join(TEMPDIR + "/Uploads/")

os.makedirs(LOGDIR, exist_ok=True)

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
    files = os.listdir(UPLOADDIR)
    return render_template("index.html", files=files)  

@app.route("/download/<path:filename>", methods=["GET"])
def download_file(filename: str):
    """Handles file upload with validation."""
    logger.info("download_file() method started")
    try:
        if(filename is None or not is_valid_name(filename)):
            logger.error("Invalid filename")
            return jsonify({"error": "Invalid filename"}), 400
        
        if(filename is None or not is_valid_extension(filename)):
            logger.error("Invalid extension")
            return jsonify({"error": "Invalid extension"}), 400
        
        if(is_valid_extension(filename) and is_valid_name(filename)):
            validfilename = valid_filename(filename)
            logger.info(f"Valid filename: {validfilename}")
            downloads = os.path.join(UPLOADDIR, validfilename)
            logger.info(f"Requested file: {downloads}")
            return send_file(downloads, as_attachment=True), 200

    except Exception as e:
        logger.error("download_file() method failed")
        logger.error(e)
        return jsonify({"error": "Internal Server Error"}), 500

if __name__ == '__main__':
    app.run(debug=True)