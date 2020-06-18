# FitnessAppJavaFX
Fitness App with JavaFX GUI

1. Current Features:
- Command line interaction with multiple menus.
  - Navigate to required menus to manipulate data
- Multiple classes  with applicable controllers
- Workout Tracker
  - Read and Write capabilities to create and save workouts
  - Edit past workouts to add/remove entire workouts, exercises, and sets
  - View statistics such as max weight, reps, volume, one rep max for an exercise
  - View JavaFX graphs to see these statistics as well
- Exercise List
  - Read and Write capabilities to create and save selectable exercises
  - Manipulate exercise list to add/remove/edit exercises to be performed

2. In-Work Features:
- Create Microsoft Excel spreadsheets to view historical information through Apache POI 4.1.2
  - Currently utilizes JavaFX DirectoryChooser to select/create the directory
  - Excel sheets are able to display all the graphs and correct data. Need to cut down duplicate code
  - Excel sheets containing past workouts are also able to be created. Each column contains 5 workouts and will span the
  entire Excel sheet
  - Need to figure out how many Excel sheets/documents are required so the user doesn't have large amounts of them (By year, 
  by months, by weeks, etc)

3. Future Features:
- Support Web Application Framework to be used in realtime online.
  - Currently researching best way to accomplish this.
