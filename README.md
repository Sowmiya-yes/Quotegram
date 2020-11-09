**Quotegram**

This is a server side code for a Social Network application where Quotes can be shared to the world. 
This application is built using Spring Boot, Maven, Java 11, Spring Security, JSON Web Tokens.
This application uses MongoDb for data persistence and Redis for data caching.
The image uploaded by the user is stored in the Amazon S3 bucket.

The following are the features provided by Quotegram:

**User Module**
1. User can Signup using UserName, FirstName, LastName, Email, Password.
   New active user is created and saved in MongoDb with encrypted password (given that username and email used are unique)
2. User can authenticate every request by passing a unique token (JWT tokens).
   Eliminates the need to pass username and password in every request
3. User related details can be fetched like Email, date of creation of the account, active status, etc
4. Deactivate / Reactivate a user account
5. Delete a user account

**Post Module**
1. New post can be created with or without caption
   Post will be saved in AWS S3 bucket, and post metadata will be stored in MongoDb
2. A post can be deleted
3. A post can be liked
4. Post and post metadata can be fetched from database and from file storage

**Follow Module**
1. A user can be followed or unfollowed
2. List of followees or followers Ids can be fetched for the current user

**Feed Module**
1. Fetch the recent posts from the followers of the current user from the caching.
   Once a post is created by a person, an entry is made into the quotegram feeds of all the followers. 
   So that, when a follower access his/her quotegram feed, all the recent posts made by his/her friends are visible
Internally, the news feed of the followers gets updated in MongoDb. 
A cron job will run every 15 minutes that will fetch the uncached updates from MongoDb and cache it in Redis Key Object store.

**Work In Progress**
1. Hosting this application in AWS cloud
2. Creating the swagger page for all the REST endpoints.

**Future Work**
1. Create a frontend using React and render the server side data.
