#!/usr/bin/env python3

from flask import Flask, request, render_template
import os
import tempfile
import logging
from .logforging import sanitize_input

app = Flask(__name__)
TEMPDIR = tempfile.gettempdir()
LOGDIR = os.path.join(TEMPDIR + "/logs/")
MAX_NAME_LENGTH = 50
MAX_COMMENT_LENGTH = 500

os.makedirs(LOGDIR, exist_ok=True)

app.config["MAX_CONTENT_LENGTH"] = 5 * 1024 * 1024  # 5 MB
LOG_LOCATION = os.path.join(LOGDIR + "/app.log")

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(name)s - %(message)s",
    style="%",
    datefmt="%Y-%m-%d %H:%M",
    handlers= [
        logging.FileHandler(LOG_LOCATION),
        logging.StreamHandler()
        ]
    )

logger = logging.getLogger(__name__)

@app.errorhandler(405)
def method_not_allowed():
    return "Method not allowed", 405

@app.route("/", methods=["GET", "POST"])
def main():
    if request.method == "POST":
        name = request.form.get("name")[:MAX_NAME_LENGTH]
        comment = request.form.get("comment")[:MAX_COMMENT_LENGTH]
        name = sanitize_input(name)
        comment = sanitize_input(comment)

        # logging sanitizedInput only
        logger.info("user %s added some comments", name)
        logger.info("comments: %s", comment)

        return f"Greetings {name}! thanks for your comment \n {comment}", 200
    elif request.method == "GET":
        """Render the index.html page."""
        return render_template("index.html"), 200

