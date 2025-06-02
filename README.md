# LoginSignupSwingApp

A simple Java Swing desktop application for user registration (signup) and authentication (login), with file-based credential storage and detailed login logging.

## Features

- **User Signup:** Register new users with a unique username and password.
- **User Login:** Authenticate registered users.
- **Login Logging:** Every login attempt (success or failure) is recorded in `login_log.txt` with the username, date, and time.
- **Post-Login Home Screen:** After a successful login, users are redirected to a new window with a logout option.
- **Simple, Intuitive UI:** Built entirely with Java Swing for cross-platform compatibility.

## Technologies Used

- Java
- Java Swing (GUI)
- File I/O

## How to Run

1. **Clone or Download** this repository.

2. **Compile the Java file:**
    ```
    javac LoginSignupSwingApp.java
    ```

3. **Run the application:**
    ```
    java LoginSignupSwingApp
    ```

4. **Usage:**
    - Use the **Signup** tab to register a new user.
    - Use the **Login** tab to log in with your credentials.
    - After successful login, you will be redirected to a home screen with a logout option.
    - All login attempts are recorded in `login_log.txt`.

## File Structure

LoginSignupSwingApp/

├── java_login_page.java

├── users.txt # Created automatically; stores user credentials

├── login_log.txt # Created automatically; logs login attempts

├── .gitignore

├── README.md


## Notes

- **Security:** This application is for educational/demo purposes. Passwords are stored in plain text in `users.txt`. For production, always use secure password hashing and proper credential management.
- **Requirements:** Java JDK 8 or higher.


## License

This project is open source and available under the [MIT License](LICENSE).

---

