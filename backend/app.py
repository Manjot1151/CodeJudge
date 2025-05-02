from flask import Flask, request, jsonify
from flask_cors import CORS
from coderunner import run_code

app = Flask(__name__)
CORS(app)

@app.route('/run', methods=['POST'])
def run():
    data = request.json
    language = data.get("language")
    code = data.get("code")
    output = run_code(language, code)
    return jsonify({"output": output})

if __name__ == '__main__':
    app.run(debug=True)
