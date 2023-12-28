# DreamGames-CaseStudy (Backend)

I used Apache Maven for this case study. 
Organized the solution as: Controllers, Services, Repositories and Models.
lombok builder is used for models also "Scheduled cron" is used to manage the tournament opening and closing times.
__________

I've added an extra field to User table (pendingCoins) to keep track if the user can enter a tournament or not.

When player enters a tournament. They're added to a queue and start searching for other people from different countries. To check if they've found a group, a loop is running every 2 seconds and looking at the database.

To sort the user or country scores for displaying the leaderboard. I used the Comparator class to create a custom sorting for class variable (userScore).

I've created a seperate countryScore table so that I don't have to sum all the user scores every time it is called.

I've created 5 database tables:
### User:
+ userID (primary key)
+ username
+ level
+ coins
+ pendingCoins
+ country (enum)

### Tournament:
+ tournamentID (primary key)
+ tournamentState (enum)

### TournamentGroup:
+ groupID (primary key)
+ tournament (foreign key)

### TournamentUserScore
+ tournamentUserID (primary key)
+ user (foreign key)
+ tournamentGroup (foreign key)
+ score

### TournamentCountryScore
+ tournamentCountryID
+ tournament (foreign key)
+ country (enum)
+ score
