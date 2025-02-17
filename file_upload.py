#!/usr/bin/env python3

from flask import Flask, request, jsonify, render_template
import os
import re
import logging
#import magic

app = Flask(__name__)
app.config["UPLOAD_DIRECTORY"] = "Uploads"

logger = logging.getLogger(__name__)

FILENAME_REGEX_PATTERN = r"[\w ]+"
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

@app.route('/')
def main():
    if not os.path.exists(app.config["UPLOAD_DIRECTORY"]):
        os.makedirs(app.config["UPLOAD_DIRECTORY"]) 
    logging.basicConfig(filename='log/app.log', level=logging.DEBUG)
    return render_template("index.html")

@app.route("/upload_file", methods=["POST"])
def upload_file():
    try:
        logger.info("upload_file() method started")
        if request.method == "POST":

            if "file" not in request.files:
                return jsonify({"error" : "No File Part"})
            file = request.files["file"]

            if file.filename == "":
                return jsonify({"error" : "No selected file"})

            try:
                logger.info("Filename validation started")
                if file and is_valid_extension(file.filename):
                    logger.info("File extension is allowed")
                    if is_valid_name(file.filename):
                        logger.info("Filename is allowed")
                        filename = valid_filename(file.filename)
                        logger.info(f"Filename: {filename} is valid")
                        logger.info("Start file upload")
                        file_path = os.path.join(app.config["UPLOAD_DIRECTORY"], filename)
                        file.save(file_path)
                        logger.info("File uploaded Successfully")
                        return jsonify({"success" : "File Uploaded Successfully"})
                    else:
                        logger.error(f"Invalid FileName provided")
                        return jsonify({"error" : "Invaid Filename"})

                else:
                    logger.info(f"Invalid File Extension")
                    return jsonify({"error" : "Invaid Extension"})
                    
            except Exception as e:
                logger.error("Filename validation error")
                logger.error(valid_filename(filename))
                return jsonify({"error" : "Filename validation failed"})

            # file_path = os.path.join(app.config["UPLOAD_DIRECTORY"], filename)
            # file.save(file_path)
            # logger.info("File uploaded Successfully")
            # return jsonify({"success" : "File Uploaded Successfully"})
        else:
            return jsonify({"error" : "Invalid File"})
    except Exception as ex:
        logger.info("Invalid File Exception")
        return jsonify({"error" : "Invalid File except"})

if __name__ == '__main__':
    app.run()