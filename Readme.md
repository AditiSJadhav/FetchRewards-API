# Fetch Rewards API
A Web service that exposes basic end-points for user registration, add, deduct and get the balance reward points for the user.

## Architecture: Framework: Spring-Boot

Data storage: In memory  
Data structures used – HashMap, Array List, PriorityQueue 
- HashMap <String, Integer> - Store the Payer name and the total points for each user.
- HashMap <String, Integer> - Store the Payer name and negative value points in the `add_points` endpoint.
- PriorityQueue - Stores the Payer information(Payername, points, date). Used to access the earliest transaction first.
- ArrayList - Used to return the list of users and the remaining balances.

## How to execute
1.  Install maven
2.  Download/Clone the project repository
3.  Change directory to project home directory
4.  Run the following command in command line to run project  `mvn spring-boot:run`
5.  Server runs on port  **8080** i.e. `localhost:8080`

## API Endpoints
1. **User Registration**
API URL - `localhost:8080/register` 
Request Method - **GET**
Example:
`localhost:8080/register?username=Aditi`

   Expected Response
  - If the user is not already registered, the user information will be stored and the unique userID is sent in the response header. This userID will be used to add, deduct and check the balance points for this user.
  - If the user is already registered, a `This user is already registered` exception is thrown.

2. **Get all the users**
API URL - `localhost:8080/get_users`
Request Method - **GET**
Example:
`localhost:8080/get_users`

   Expected Response
  - Returns the list of username and userID of all the registered users.

3. **Add points**
API URL - `localhost:8080/add_points`
Request Method - **POST**
Example Request body:
```
{
    "userID" : "ecf447e6fcd4971920365ac1e2e451e840d2218c04236698d725359838cf69eb",
    "payer" : "DANNON",
    "points" : 1000,
    "date" : "2021-02-12T00:00:00"
}
```

   Expected Response
   - If the past date is entered, a `Cannot add points for a past date` exception is thrown.
   - If the provided userID has not been registed, a `User not registered` exception is thrown.
   - If the above two exceptions do not occur, the points for the specified payer will be added to the user specified by the userID and `Points Updated Successfully` message is returned.

4. **Deduct Points**
API URL - `localhost:8080/deduct_points`
Request Method - **POST**
Example Request Body:
```
{
    "userID" : "ecf447e6fcd4971920365ac1e2e451e840d2218c04236698d725359838cf69eb",
    "points" : 5000
}
```

Expected Response:
- If the provided userID has not been registed, a `User not registered` exception is thrown.
- If the points in the user account is less than the points to be deducted, a `Not enough points to deduct` exception is thrown.
- If the above two exceptions do not occur, the points in the user account will be deducted in the order of the earliest payer first. The points deducted from each payer is returned as a response to this request. The example response is shown below
```
[
    {
        "payer": "UNILEVER",
        "points": -200,
        "date": "2021-02-08T00:25:58.086"
    },
    {
        "payer": "MILLER COORS",
        "points": -4700,
        "date": "2021-02-08T00:25:58.086"
    },
    {
        "payer": "DANNON",
        "points": -100,
        "date": "2021-02-08T00:25:58.086"
    }
]
```

5. **Get Remaining Balance**
API URL - `localhost:8080/points_balance`
Request Method - **GET**
Example:
`http://localhost:8080/points_balance?userID=ecf447e6fcd4971920365ac1e2e451e840d2218c04236698d725359838cf69eb`

   Expected Response
  - If the provided userID has not been registed, a `User not registered` exception is thrown.
   - If the user is registered, the points balance of each payer for the user specified by the userID is returned as the response. The example response is shown below
```
{
    "UNILEVER": 0,
    "MILLER COORS": 5300,
    "DANNON": 1000
}
```

