# Path Manipulation 
This python project is to help to mitigate the path manipulation issues. You can use the logic in the [pathmanipulation.py](securecodingexamples/logforging/src/pathmanipulation.py) in your [Flask](https://pypi.org/project/Flask/) or [Django](https://pypi.org/project/Django/) projects or any other Python projects.

## Code Structure

[app.py](securecodingexamples/logforging/src/app.py) contains the Flask Code to handle the file upload logic with maximum file size.

[pathmanipulation.py](securecodingexamples/logforging/src/pathmanipulation.py) contains logic for Filename, File extension, Double extension check and null byte checks.

[template](securecodingexamples/logforging/src/templates/) directory contains the index.html as frontend for the file upload with file type check on the client side.

You can try to play around by following the [Installation](#) steps, check the Usage to run the Flask app.

## Installation

1. Clone the repository:
```sh
git clone https://github.com/sahildari/secure-coding-examples
cd 'Path Manipulation/while File Upload/python'
```
2. Install the package:   
```sh
pip install .
```
## Usage
1. Run the Flask app
```sh
python run.py
```
2. Open in Browser:
```
http://127.0.0.1:5000
```
