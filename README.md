# Medium News Recommendation System
The Medium News Recommendation System is a personalized platform designed to enhance user engagement by recommending articles based on individual preferences and interaction history.

Key Features:

*   For Users:
Read, skip, like, or dislike articles.
View personalized article recommendations.
Access a history of interactions.
 
*   For Admins:
Update, or delete articles.
Manage article content for optimized recommendations.
Backend Integration:

MySQL for storing user data and interactions.
MongoDB for article data.
Technology Stack:
JavaFX for the frontend.
Java and Python for backend and machine learning.
Purpose:
To provide a tailored reading experience for users while enabling admins to manage articles efficiently.

### Prerequisites - Must Do these before running the application
* Articles are saved in Mongodb Atlas **make sure** that you have an active internet connection if not the articles will not be fetched.
* Usernames and Passwords for the Atlas cluster is hardcoded.
* For MySQL Database
  1. Find the ood_cw.sql in the Data Folder
  2. Make sure you have installed MySQL Server (and MySQL workbench preferred)
  3. Create a schema named **_OOD_CW_**
  4. Import the the ood_cw.sql
* For ML API 
  1. main.py is available in the ML folder
  2. Install the requirements using pip, requirements.txt is available. 
  3. Run main.py, the default port is set to **8001** if there is an existing process on that port make sure to terminate it before running the python file.
* Make sure you have the database running in the port 3306(default)

**All the ports are hardcoded into the code make sure you are running on the processes in the exact ports**
