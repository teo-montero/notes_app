# 🗒️ Note Taking App

A web-based note-taking application built using **Java**, **JSP**, **Servlets**, **JavaScript**, and **Bootstrap**. This app features a clean and intuitive interface for creating, organizing, and managing notes efficiently.

---

## ✨ Features

### 📝 Notes
- Create notes with:
  - A **required title**
  - Optional **text content**
  - Optional **image attachments**
- Edit, delete, duplicate, and manage existing notes.
- Advanced note actions:
  - **Dump File into Note**: Append content from `.txt`, `.pdf`, and `.docx` files directly into a note.
  - **Remove Text Content**
  - **Upload/Remove Image**

### 📁 Indexes
- Notes are stored in named **indexes**, which support:
  - **Nested subindexes** (tree-like structure)
  - Sorting by:
    - Title
    - Type
    - Last edit date
- Index actions apply **recursively** to all contained notes and subindexes.
- Indexes can also be duplicated or deleted.

### 🏷️ Categories
- Notes can be tagged with one or more **categories**.
- Categories can be **hidden**: notes under hidden categories will be excluded from views.
  
### 🔍 Search
- Search notes by title and/or content.
- Results ranked by **match percentage**, with title matches weighted more heavily than content matches.

---

## ⚙️ Architecture

The application is built using the **Model-View-Controller (MVC)** architecture.

### 🖼️ Frontend (View)
- Built with **JSP**, combining Java with HTML, CSS, and **Bootstrap**.
- Key JSP pages:
  - `index.jsp` — Main entry point that dynamically includes:
    - `displayIndex.jsp` — Displays contents of a selected index, and allows:
      - Sorting
      - Hiding categories
      - Deleting/duplicating notes or indexes
    - `displaySearch.jsp` — Shows search results
  - `createNote.jsp` — Form for creating a new note
  - `editNote.jsp` — Interface for editing existing notes

### 🔧 Backend (Model + Controller)
- Uses an **object-oriented approach**.
- JSP pages submit requests to **Servlets**, which act as controllers.
- Each Servlet:
  - Parses the request
  - Delegates logic to appropriate handlers
  - Returns an updated view by refreshing the page
- Application logic is modularized into three handler classes:
  - `IndexHandler`
  - `NoteHandler`
  - `CategoryHandler`

---

## 📦 Tech Stack

- **Java**
- **JSP**
- **Servlets**
- **HTML/CSS**
- **Bootstrap**
- **JavaScript**

---

## 🧪 Status

The core features are functional and the system supports structured note management. The app prioritizes simplicity and usability while supporting more advanced features like file dumping and search.
