#  Student Manager — Android + PHP

A small Android app that talks to a PHP/MySQL backend to manage a list of students. You can add, view, edit, and delete them. That's it. Simple, clean, and it works.

---

## What's inside?

**On the server side**, a PHP backend exposes four endpoints (create, read, update, delete) and talks to a MySQL database called `school1` with a single table: `Etudiant`.

**On the Android side**, the app uses **Volley** to fire HTTP requests and **Gson** to parse the JSON responses. Two screens: one to add a student, one to see the full list.

---

## Tech stack

- **Android** (Java) — Volley · Gson · RecyclerView
- **PHP 8** — PDO · REST-ish web services
- **MySQL** — one table, five columns, zero drama

---

## Getting it running locally

**1. Set up the database**

Fire up XAMPP (Apache + MySQL must be running), then run this in phpMyAdmin:

```sql
CREATE DATABASE school1;
USE school1;

CREATE TABLE Etudiant (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nom    VARCHAR(50),
    prenom VARCHAR(50),
    ville  VARCHAR(50),
    sexe   VARCHAR(10)
);
```
an overview for the database : 
<img width="1490" height="688" alt="image" src="https://github.com/user-attachments/assets/3e5248d9-72ca-4528-b4bb-2a0ab8cf41c7" />


**2. Drop the PHP project in the right place**

Copy the `projet/` folder to `C:/xampp/htdocs/projet/`.  
Test it by visiting `http://localhost/projet/ws/loadEtudiant.php` — you should get `[]`.

**3. Open the Android project in Android Studio**

The emulator reaches your machine's localhost via `10.0.2.2`, which is already set as `BASE_URL` in the code. If you're testing on a **real device**, swap that with your machine's local IP (e.g. `192.168.1.x`).

**4. Run the app**

Hit ▶ and you're good to go.

screnvideo :


https://github.com/user-attachments/assets/c8e50c38-cda8-462c-af70-0ef5770d09d2




---

## Project structure

```
projet/                  ← PHP backend (goes in htdocs)
├── connexion/           ← PDO connection (singleton)
├── classes/             ← Etudiant model
├── dao/                 ← IDao interface (CRUD contract)
├── service/             ← Business logic
└── ws/                  ← Endpoints called by the app

app/src/                 ← Android
├── models/Etudiant.java
├── AddEtudiant.java     ← Form to add a student (launcher activity)
├── ListEtudiant.java    ← RecyclerView list with edit/delete
└── EtudiantAdapter.java ← RecyclerView adapter
```

---

## How the Android ↔ PHP communication works

Every action (add, update, delete) sends a **POST request** with form parameters and gets back the **full updated list** as JSON. The app then re-renders the list from that response. No state management library needed — keep it simple.

---

## A few things to know

- The emulator uses `10.0.2.2` to reach `localhost` on your host machine — don't change it unless you're on a real device.
- HTTP (not HTTPS) is allowed for that address via `network_security_config.xml` — this is fine for local dev.
- The modification dialog reuses the add-student layout and just pre-fills the fields.
- Min SDK is **API 26** (Android 8.0).

---

## What you can do in the app

| Action | How |
|---|---|
| Add a student | Fill the form on the main screen and tap "Ajouter" |
| See all students | Tap "Voir la liste" |
| Edit a student | Tap their name in the list → "Modifier" |
| Delete a student | Tap their name in the list → "Supprimer" → confirm |

---

Made as a learning project 
