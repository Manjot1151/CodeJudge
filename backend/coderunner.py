import subprocess
import tempfile
import os

def run_code(language, code):
    with tempfile.TemporaryDirectory() as temp_dir:
        if language == "python":
            file_path = os.path.join(temp_dir, "main.py")
            with open(file_path, "w") as f:
                f.write(code)
            cmd = ["python3", file_path]
        
        elif language == "cpp":
            src = os.path.join(temp_dir, "main.cpp")
            out = os.path.join(temp_dir, "main.out")
            with open(src, "w") as f:
                f.write(code)
            compile_result = subprocess.run(["g++", src, "-o", out], capture_output=True, text=True)
            if compile_result.returncode != 0:
                return compile_result.stderr
            cmd = [out]
        
        elif language == "java":
            src = os.path.join(temp_dir, "Main.java")
            with open(src, "w") as f:
                f.write(code)
            compile_result = subprocess.run(["javac", src], capture_output=True, text=True)
            if compile_result.returncode != 0:
                return compile_result.stderr
            cmd = ["java", "-cp", temp_dir, "Main"]
        
        else:
            return "Unsupported language."

        try:
            result = subprocess.run(cmd, capture_output=True, text=True, timeout=5)
            return result.stdout + result.stderr
        except subprocess.TimeoutExpired:
            return "Error: Execution timed out."
