# Brokage Firm Backend

## Requirements
- **JDK 21**
- **Docker** and **Docker Compose**

## Setup
Run the docker-compose file to start PostgreSQL

### PostgreSQL credentials:
- **Username**: `user`
- **Password**: `pass`

## API Testing
- **Postman Collection**: Import `BrokageFirm.postman_collection.json` in Postman.
- Create an account using the `/auth/register` endpoint.
- Login using `/auth/login` to get a bearer token.
- Use the bearer token to test other endpoints (deposit, withdraw, list assets, orders, etc.).
