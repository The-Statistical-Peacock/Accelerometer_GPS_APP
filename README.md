# Accelerometer_GPS_App

This is an Android Application that I build for a project in School.
It takes all three axes of the phones Accelerometer and stores the data into an SQLite database on the phone.
It will also take the current GPS coordinates and phones speed and store them with the Acceleromter data in the database.

This data was used to detect road surface conditions on rural roads in Ireland. We used the K-Means Clustering Algorithm after the data was collected to analyze the data. 


<img src="https://github.com/The-Statistical-Peacock/Accelerometer_GPS_App/blob/main/Screenshot_20201010-113929.png" width="250"> | <img src="https://github.com/The-Statistical-Peacock/Accelerometer_GPS_App/blob/main/Screenshot_20201010-113936.png" width="250">


The values for each **X-Y-Z** axis can be displayed with a simple manipulation of the _**updateTextView**_ Code. **But this should be done Asynchronously, As the high rate of data storage and displaying will cause the APP to crash!!** Ideally, the database Add operation should be run in the background.
