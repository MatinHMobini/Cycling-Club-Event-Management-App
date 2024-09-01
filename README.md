# 🚴 Cycling Club Event Management App

This project is a Cycling Club Event Management application built using Java and Android Studio. The app is designed to manage different types of cycling events, user accounts, and associated interactions within a cycling community. 

## 📺 Video Introduction
Please View Our Youtube Video explaining the application https://www.youtube.com/watch?v=tHSzEHOk_aI

## 📋 UML Overview

The core structure of the app is based on the following classes:

- **Account**: Represents a user account with attributes like `username`, `password`, and `email`.
  - **AdminAccount**: A specialized account type for administrators, capable of managing events.
  - **CyclingClubAccount**: An account type for cycling clubs that can organize events and have public-facing information.
  - **ParticipantAccount**: Represents participants who can register for events.
- **Event**: Represents an event with attributes like `eventName`, `date`, `location`, `registrationFee`, and `participantLimit`.
  - **TimeTrial**, **HillClimb**, **RoadStageRace**, **RoadRace**, **GroupRides**: Different types of events extending the base `Event` class.
- **Comment**: Represents user comments on cycling clubs, including a rating and the date.
- **AccountList** and **EventList**: Manage lists of accounts and events, respectively.

## 🛠️ Technologies Used

- **Java**: Core programming language for the app.
- **Android Studio**: IDE used for development.
- **UML Diagrams**: Used for planning and structuring the application.

## 🚀 Features

- **User Management**: Create and manage different types of user accounts.
- **Event Management**: Organize and participate in various cycling events.
- **Comments and Ratings**: Users can leave comments and ratings for cycling clubs.
- **Event Types**: Supports multiple event types such as Time Trials, Hill Climbs, Road Races, etc.

## 📦 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/MatinHMobini/grimpeurscyclingclub.git
   ```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## 📚 UML Diagram

![image](https://github.com/user-attachments/assets/07dd7f5b-3c41-4a4e-9d7e-8364b2b9d659)


## 💡 How to Use

- **Create an Account**: Sign up as an Admin, Cycling Club, or Participant.
- **Organize Events**: Cycling Clubs can organize various types of cycling events.
- **Register for Events**: Participants can browse and register for upcoming events.
- **Leave Feedback**: Comment on and rate cycling clubs based on your experience.

## Example of User Point of View
![Screenshot 2024-08-31 at 8 34 49 PM](https://github.com/user-attachments/assets/9e502bbc-d31f-417b-a5d7-53a3ec5ae8cb)


Thank you for viewing!
