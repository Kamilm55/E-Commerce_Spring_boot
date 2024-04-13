# E-Commerce Application 
This application is multimodule app is built with Java,Spring Boot and it is built with up-to-date technologies such as , CI(github actions),JWT,Spring Security,Server-Sent-Events (SSE) , Docker container etc.
## Overview
In this project people can register as a user by default.There are 4 roles (USER,VENDOR,ADMIN,SUPER_ADMIN) which a user could have many.More than one role can be belong to a user.For instance , there is a user with ADMIN,USER,VENDOR role or with ADMIN and USER role.
### Role details:
- ROLE_USER: Every user has user role by default.Users only can buy products.
- ROLE_VENDOR: Every vendor can open shop/s with products.Between vendor and shop there is OneToMany relationship ,there is ManyToMany relationship between products and shop.Vendors can sell products.
- ROLE_ADMIN: Every admin can manage users which has more prievelige than the others except SUPER_ADMIN.They can give role to users and can delete/deactivate users,shops,products.
- ROLE_SUPER_ADMIN: The specific user who is only can be managed with db (manually).This can give all roles to users.
## Notification , Adding roles
- When user enters into app , he should automatically subscribe user-notification events that informs whether role request accept or not.
- When admin enters into app , he should automatically subscribe vendor-request-notification that he can accept or reject request and then messages sent to users.
- For reading messages there are methods that changes status of notification.
## Server-Sent-Events logic , real time notification delivery (even offline)
Whenever user subscribe events , if there are unread messages it contains in response body.If user is active and offline , we store notifications in db when user login we send it via SSE. 
