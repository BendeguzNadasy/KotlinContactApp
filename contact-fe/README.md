# Contact App - Frontend

This project is the client-side (Frontend) of the Contact Manager application. 
The Backend (Spring Boot) code is located in a separate directory.

## Features

* **Contact Listing:** Display contacts with custom profile pictures in a clean list view.
* **CRUD Operations:**
    * **Create:** Add new contacts with image uploads.
    * **Update:** Edit existing contact information and profile pictures.
    * **Delete:** Remove contacts permanently.

## Tech Stack

This project was built using the following technologies:

* **[React](https://react.dev/)** (powered by Vite) - UI Library.
* **[Bootstrap 5](https://getbootstrap.com/)** - Base styling.
* **[Axios](https://axios-http.com/)** - HTTP client for Backend communication.
* **CSS3** - Custom styling, font-face imports, and animations.

## Installation & Setup

Follow these steps to run the project locally:

### 1. Prerequisites
* [Node.js](https://nodejs.org/) (v18 or higher recommended).
* The **Spring Boot Backend** must be running on port `8080`.

### 3. Install Dependencies
Navigate to the project directory in your terminal:
```bash
npm install
```

### 4. Run Development Server
Start the application:
```bash
npm run dev
```
Frontend application will run on this port:
http://localhost:5173