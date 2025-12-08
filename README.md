# DNS Fare Calculator

DNS Fare Calculator is a multi-platform application designed to simplify fare calculation for public transport routes in Bulacan. It helps drivers and passengers quickly determine the correct fare and change based on the route, number of passengers, and amount paid.

## Features

*   **Multi-Platform Support:** Available for both Android and Windows operating systems
*   **Route Selection:** Supports three major routes:
    *   Bulakan - Guiguinto
    *   Bulakan - Balagtas
    *   Bulakan - Malolos
*   **Fare Calculation:** Accurately calculates fares based on specific route matrices and rules
    *   Supports both **Regular** and **Discounted** rates (e.g., for students/seniors)
*   **Quick Input Controls:**
    *   **Passengers:** Toggle buttons for 1 to 5 passengers
    *   **Amount Paid:** Quick selection buttons for common denominations (20, 50, 100)
*   **Change Computation:** Automatically computes the change or alerts if the payment is insufficient ("Short")
*   **Route-Specific Logic:** Handles distinct fare rules for different locations (e.g., specific point-to-point fares vs. distance-based steps)
*   **Simple Navigation:** Intuitive interface with clear back and reset controls

## Platforms

### Android Version
- **Installation:** APK file download
- **Interface:** Touch-optimized for mobile devices
- **Use Case:** Perfect for drivers and passengers on the go

### Windows Version  
- **Installation:** Native Windows executable (.exe)
- **Interface:** Mouse and keyboard optimized for desktop
- **Use Case:** Ideal for planning, training, or administrative use

## How to Install

### Android Installation
#### Option 1: Via Android Studio (for Developers)
1.  Clone or download the master branch
2.  Open **Android Studio**
3.  Select **Open an existing Android Studio project** and navigate to the project directory
4.  Wait for Gradle to sync
5.  Connect an Android device or start an Emulator
6.  Click the **Run** button (green play icon) to build and install the app

#### Option 2: Via APK [here](https://www.mediafire.com/file/fom8atpajrzjqto/app-release.apk/file)
1.  Download the `.apk` file to your Android device
2.  Navigate to your file manager and tap the APK file
3.  Allow installation from unknown sources if prompted
4.  Tap **Install**

### Windows Installation
1.  Download the Windows executable (.exe) file
2.  Extract the contents to your preferred location
3.  Run `DNS Fare Calculator.exe` to launch the application

*Note: The Windows version is optimized for desktop use and may have slight UI differences from the Android version.*

## How to Use

1.  **Select a Route:** Launch the app and choose one of the three available routes from the main menu
2.  **Select Locations (if applicable):**
    *   For routes like *Bulakan-Balagtas* or *Bulakan-Malolos*, select your **Pickup** point and **Destination** from the dropdown menus
3.  **Input Details:**
    *   Tap/click the button corresponding to the **Amount Paid** (20, 50, or 100)
    *   Tap/click the button corresponding to the **Number of Passengers** (1 to 5)
4.  **Calculate:**
    *   Press **Regular Fare** for standard rates
    *   Press **Discounted Fare** for special rates (students, seniors, PWDs)
5.  **View Result:** The app will display the change in the box below. If the payment is insufficient, it will display "Short"
6.  **Clear:** Press **AC** (All Clear) to reset the inputs
7.  **Go Back:** Press **Go Back** to return to the main menu and select a different route

## Tech Stack

*   **Language:** Java
*   **Framework:** Android SDK
*   **IDE:** Android Studio
*   **Cross-Platform:** Windows executable built for desktop compatibility

---

**Current Version:** v1.0.0  
