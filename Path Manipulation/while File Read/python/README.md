# Path Manipulation 
This python project is to help to mitigate the path manipulation issues. You can use the logic in the [pathmanipulation.py](./securecodingexamples/fileread/pathmaniuplation/src/pathmanipulation.py) in your [Flask](https://pypi.org/project/Flask/) or [Django](https://pypi.org/project/Django/) projects or any other Python projects.

## Code Structure

[app.py](./securecodingexamples/fileread/pathmaniuplation/src/app.py) contains the Flask Code to handle the file upload logic with maximum file size.

[pathmanipulation.py](./securecodingexamples/fileread/pathmaniuplation/src/pathmanipulation.py) contains logic for Filename, File extension, Double extension check and null byte checks.

[template](./securecodingexamples/fileread/pathmaniuplation/src/templates/) directory contains the index.html as frontend for the file upload with file type check on the client side.

You can try to play around this by following the Installation steps, check the Usage to run the Flask app.

Please note that this project will try to fetch the files from your ___TEMP/Uploads___ directory. You can either manually create your files in the directory, or you can navigate to [Path Manipulation while File Upload Python Project](../../while%20File%20Upload/python/) and follow the installation steps and Upload the test files.

*TEMP : temporary Folder in your OS*

*%TEMP% Directory in Windows*

*/tmp Directory in Linux/MacOS*

## Installation

1. Clone the repository:
```sh
git clone https://github.com/sahildari/secure-coding-examples
cd 'Path Manipulation/while File Read/python'
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