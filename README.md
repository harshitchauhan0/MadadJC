Emergency Guardian App
An Android application built using Kotlin and Jetpack Compose that allows users to quickly and easily contact their selected guardians in case of an emergency. The app provides functionality for making emergency calls to a designated super guardian, sending location-based messages to selected guardians, and managing guardian contacts.

Features
Emergency Call to Super Guardian

Quickly make an emergency call to a pre-selected super guardian from the contact list.
Message Guardians with Location Info

Send a message containing your current location to selected guardians.
Simultaneously make an emergency call to the super guardian.
Profile Screen

Change the user's name.
View and manage the list of guardians.
Remove guardians from the list if needed.
Guardian Selection Screen

Fetch the contact list from the device.
Select contacts to designate as guardians and a super guardian.
Installation
Clone the repository:
bash
Copy code
git clone https://github.com/yourusername/emergency-guardian-app.git
Open the project in Android Studio.
Build the project to download dependencies and generate necessary files.
Run the app on your emulator or Android device.
Usage
Emergency Call and Messaging
Set Up Guardians:

Navigate to the "Guardians" screen.
Select contacts to add as guardians and designate one as the super guardian.
Profile Management:

Go to the "Profile" screen.
Change your name and manage the list of guardians.
Emergency Call:

Press the emergency call button to immediately call the super guardian.
Send Location Info:

Press the button to send your current location to all selected guardians and call the super guardian simultaneously.
Screens
Profile Screen
Change Name: Allows the user to update their displayed name.
Manage Guardians: View the list of current guardians and remove any if necessary.
Guardians Screen
Fetch Contacts: Retrieves the contact list from the user's device.
Select Guardians: Choose contacts to add as guardians and designate one as the super guardian.
Dependencies
Kotlin: The primary programming language used.
Jetpack Compose: Used for building the UI components.
Hilt: Dependency injection library for managing dependencies.
Location Services: To fetch the user's current location.
Permissions: Handling runtime permissions for accessing contacts and location.
Contributing
Fork the repository.
Create a new branch:
bash
Copy code
git checkout -b feature/your-feature
Make your changes and commit them:
bash
Copy code
git commit -m "Add your message"
Push to the branch:
bash
Copy code
git push origin feature/your-feature
Open a pull request.
License
This project is licensed under the MIT License - see the LICENSE file for details.

Contact
If you have any questions or suggestions, feel free to open an issue or contact us at your-email@example.com.
