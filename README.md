Overview

The Bus Ticket Reservation System is a web application designed to manage bus reservations. It includes functionality for user and admin registration, authentication, and authorization using JWT tokens.

API Endpoints

User Registration

Endpoint: POST /user/signup

Description: Registers a new user.

URL: http://localhost:8080/user/signup

Admin Registration

Endpoint: POST /admin/signup

Description: Registers a new admin.

URL: http://localhost:8080/admin/signup

Authentication

Endpoint: POST /login

Description: Authenticates users and admins. Returns a JWT token for authorized access.

Roles ROLE_USER: Assigned to users who can book bus tickets and manage their reservations. ROLE_ADMIN: Assigned to admins who can add and manage bus details.

Authorization JWT (JSON Web Token) is used for both authentication and authorization across the system. Ensure you include the JWT token in the Authorization header for protected endpoints using spring security.

Admin Role
1. Add the Bus
2. Add the trip
3. Add the Route

User Role
1. User can check the bus routes, trip, and reservation details.
2. User can make the reservation.

In the Bus Ticket Reservation System project, the signup, signin, and logout modules for both users and admins have been successfully implemented. JWT tokens are generated upon successful login and are used for the authorization of APIs based on the role assigned during registration. The system supports full functionality for admins, including the addition of buses, trips, and routes, and users are able to access bus details. However, the reservation feature is currently incomplete due to time constraints.
Given additional time, I would be able to complete the Bus Ticket Reservation System, including the full implementation of authorization and authentication for all APIs. This would ensure a comprehensive and secure reservation process, enhancing the functionality and robustness of the system.




