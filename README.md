# DNS Fare Calculator

DNS Fare Calculator is an Android application designed to simplify fare calculation for public transport routes in Bulacan. It helps drivers and passengers quickly determine the correct fare and change based on the route, number of passengers, and amount paid.

## Features

*   **Route Selection:** Supports three major routes:
    *   Bulakan - Guiguinto
    *   Bulakan - Balagtas
    *   Bulakan - Malolos
*   **Fare Calculation:** accurately calculates fares based on specific route matrices and rules.
    *   Supports both **Regular** and **Discounted** rates (e.g., for students/seniors).
*   **Quick Input Controls:**
    *   **Passengers:** Toggle buttons for 1 to 5 passengers.
    *   **Amount Paid:** Quick selection buttons for common denominations (20, 50, 100).
*   **Change Computation:** Automatically computes the change or alerts if the payment is insufficient ("Short").
*   **Route-Specific Logic:** Handles distinct fare rules for different locations (e.g., specific point-to-point fares vs. distance-based steps).

## How to Install

### Option 1: Via Android Studio (for Developers)
1.  Clone or download this repository.
2.  Open **Android Studio**.
3.  Select **Open an existing Android Studio project** and navigate to the project directory.
4.  Wait for Gradle to sync.
5.  Connect an Android device or start an Emulator.
6.  Click the **Run** button (green play icon) to build and install the app.

### Option 2: Via APK [here](https://www.mediafire.com/file/0wy0ls2sfuu0njv/DNS+Fare+Change+Calculator.apk/file)
1.  Download the `.apk` file to your Android device.
2.  Navigate to your file manager and tap the APK file.
3.  Allow installation from unknown sources if prompted.
4.  Tap **Install**.

## How to Use

1.  **Select a Route:** Upon launching the app, choose one of the three available routes from the main menu.
2.  **Select Locations (if applicable):**
    *   For routes like *Bulakan-Balagtas* or *Bulakan-Malolos*, select your **Pickup** point and **Destination** from the dropdown menus.
3.  **Input Details:**
    *   Tap the button corresponding to the **Amount Paid** (20, 50, or 100).
    *   Tap the button corresponding to the **Number of Passengers** (1 to 5).
4.  **Calculate:**
    *   Press **Regular Fare** for standard rates.
    *   Press **Discounted Fare** for special rates (students, seniors, PWDs).
5.  **View Result:** The app will display the change in the box below. If the payment is insufficient, it will display "Short".
6.  **Clear:** Press **AC** (All Clear) to reset the inputs.
7.  **Go Back:** Press **Go Back** to return to the main menu and select a different route.

## Tech Stack

*   **Language:** Java
*   **Framework:** Android SDK
*   **IDE:** Android Studio
