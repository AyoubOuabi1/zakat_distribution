# Zakat App

## Prerequisites
To run the Zakat App, ensure you have the following installed:
- **PostgreSQL**
- **Java 21**
- **Angular 16**

## Setup Instructions

### Step 1: Database Configuration
1. Start PostgreSQL and create the database:
   ```sql
   CREATE DATABASE zakat_db;
   ```
2. Create a user and grant necessary permissions:
   ```sql
   CREATE USER zakat_user WITH PASSWORD 'root';
   GRANT ALL PRIVILEGES ON DATABASE zakat_db TO zakat_user;
   ```

### Step 2: Clone the Project
Pull the project from GitHub:
```sh
git clone https://github.com/AyoubOuabi1/zakat_distribution.git
cd zakat_distribution
```

### Step 3: Backend Setup

1. Build the project using Maven:
   ```sh
   mvn install
   ```
   
### Step 4: Frontend Setup
1. Navigate to the frontend directory:
   ```sh
   cd zakat_distribution_front
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the frontend server:
   ```sh
   ng serve
   ```

### Step 5: Run the Backend
1. Start the backend Spring Boot application:
   ```sh
   mvn spring-boot:run
   ```

### Accessing the Application
- The frontend should be available at: `http://localhost:4200`
- The backend API should be available at: `http://localhost:8080`

### Notes
- Ensure PostgreSQL is running before launching the backend.



# Deployment Process for Zakat App

## Step 1: Push Code to GitHub
After making changes locally, push your code to GitHub:
```sh
git add .
git commit -m "Your commit message"
git push origin main
```

## Step 2: Connect to the Server
1. Use an SSH client like **PuTTY** or **MobaXterm** or Connect to the server using local terminal:
   ```sh
   ssh sbelahbib@37.148.202.12
   ```
   Password: `user password`
3. Navigate to the application directory:
   ```sh
   cd /home/sbelahbib/zakat_distribution/
   ```

## Step 3: Pull Latest Changes
1. Pull the latest changes from GitHub:
   ```sh
   git pull
   ```
2. If you are not logged into your GitHub account, connect using the required GitHub authentication command.

## Step 4: Deploy Backend Changes
If the changes are only in the backend:
1. Run the following command to build the application without tests:
   ```sh
   mvn clean package -DskipTests
   ```
2. Restart the backend service:
   ```sh
   sudo systemctl restart zakat-backend
   ```
3. To see logs:
   ```sh
    sudo journalctl -u zakat-backend.service -n 200
   ```
## Step 5: Deploy Frontend Changes
If the changes are in the frontend:
1. Navigate to the frontend directory:
   ```sh
   cd zakat_distribution_front
   ```
2. Build the frontend:
   ```sh
   npm run build --prod
   ```
3. Navigate to the `dist` directory:
   ```sh
   cd dist
   ```
4. Remove the existing deployment:
   ```sh
   sudo rm -rf /var/www/html/zakat-app/
   ```
5. Create a new deployment directory:
   ```sh
   sudo mkdir -p /var/www/html/zakat-app/
   ```
6. Copy the built frontend files to the deployment location:
   ```sh
   sudo cp -r zakat_distribution_front/* /var/www/html/zakat-app/
   ```

## Step 6: Grant Admin Access to a User
1. Connect to the PostgreSQL database:
   ```sh
   psql -U zakat_user -d zakat_db
   ```
   Password: `root`
2. Run the following command to grant admin access:
   ```sql
   UPDATE public."user" SET "role"='ADMIN' WHERE email='replace_this_with_user_email';
   ```

Your Zakat App should now be deployed successfully!

