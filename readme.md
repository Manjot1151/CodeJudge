# CodeRunner App

A desktop app that lets you write and run code in **Java**, **C++**, or **Python** — using a **JavaFX frontend** and **Flask backend**.

---

## How to Run

### 1. Start the Backend (Flask)

```bash
cd backend
pip install -r requirements.txt
python app.py
```
This runs the server at `http://localhost:5000`.

### 2. Start the Frontend (JavaFX)

```bash
cd frontend
./gradlew run
```