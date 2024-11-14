
![Optimart](https://socialify.git.ci/ngntu10/Optimart/image?description=1&font=Bitter&forks=1&issues=1&language=1&logo=https%3A%2F%2Fi.ibb.co%2FyYDzpBG%2Fngntu1.jpg&name=1&owner=1&pattern=Floating%20Cogs&stargazers=1&theme=Dark)
# 🛍️ Optimart

Backend system provides restful API for web.

[//]: # ([![CircleCI]&#40;https://circleci.com/gh/piomin/sample-spring-microservices-new.svg?style=svg&#41;]&#40;https://sonarcloud.io/project/issues?resolved=false&id=hoangtien2k3_ecommerce-microservices&#41;)


[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/project/configuration?id=ngntu10_OptiMart)

## Introduction


Welcome to `OptiMart`. This project features a Spring Boot backend combined with a Next.js frontend to deliver a fast, scalable e-commerce platform. The backend handles all business logic and data management, while the frontend ensures a seamless, responsive user experience.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit `(JDK) 17` or higher installed.
- Build tool (e.g., `Maven`) installed.
- Database system (e.g.,`PostgreSQL`) set up and configured.
- Hibernate, JPA
- Docker with docker-compose build
- Restfull API
- PostMan Testing API and Client.
- Send message and receiver using firebase cloud-messaging.
- Using Redis for data caching

## OptiMart Features


**Permission-based access control system**: Users can be granted specific permissions to perform certain actions, and users with administrative privileges (admins) can precisely assign or restrict access to specific features for other users.

### Admin Features

- **View Financial & Product Statistics**: Admins can access detailed reports on revenue, expenses, and product performance, with the ability to filter by product category or type.
- **Manage Order Statuses**: Admins can view and update the status of all orders, including processing, shipped, completed, or canceled.
- **Edit Reviews & Comments**: Admins have the authority to modify or delete user-generated reviews and comments on products to maintain content quality.
- **Modify User Info & Roles**: Admins can edit user details (e.g., contact information) and change user roles and permissions, such as promoting a user to admin or restricting access to certain features.

### User Features

- **Make Purchases & Payments**: Users can browse and buy products with multiple payment methods, including credit cards, bank transfers, and digital wallets.
- **Comment & Review Products**: Users can leave reviews and ratings on products they’ve purchased, helping other customers make informed decisions.
- **Follow & Like Products**: Users can follow products or product categories to receive updates on new arrivals or promotions and can like products to save them for future reference.
- **View & Edit Order History**: Users can view past orders, track order statuses, and update order details, such as shipping addresses or payment methods.

## Conclusion

OptiMart provides a comprehensive set of features to empower both admins and users, ensuring a flexible and scalable platform for managing e-commerce activities. The permission-based system ensures precise control over who can access and modify different parts of the platform, while the user features offer a seamless shopping experience.

## Getting Started

Follow these steps to set up and run the backend:

### Method 1: Clone the Repository and Run Locally

```bash
   git clone https://github.com/ngntu10/OptiMart.git
```

#### 1. Navigate to the project directory:

```bash
  cd Optimart
```

#### 2. Build the project:

```bash
  # Using Maven
  mvn clean install
```

#### 3. Configure the environments:

- Update `application.properties` or `application.yml` with your environments details.

#### 4. Run the application:



```bash
  # Using Maven
  mvn spring-boot:run
```

### Method 2: Run the Application Using Docker Compose
```bash
   git clone https://github.com/ngntu10/OptiMart.git
```

#### 1. Navigate to the project directory:

```bash
  cd Optimart
```

#### 2. Build and start the containers using Docker Compose:

```bash
  docker-compose -f docker-compose.yml up --build
```

[//]: # ()
[//]: # (## Demo)

[//]: # (![1715441188385]&#40;https://github.com/user-attachments/assets/ea07616a-5404-4ccd-bab0-b472b67a061a&#41;)

## API Documentation

Document the API endpoints and their functionalities. You can use tools like `Swagger` for
automated `API documentation`.

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=ngntu10/Optimart&type=Date)](https://star-history.com/#ngntu10/Optimart&Date)


## Contributing

If you would like to contribute to the development of this project, please follow our contribution guidelines.

![Alt](https://repobeats.axiom.co/api/embed/fd7fd76dafe452bdb7c2bc12856bd45c277ee732.svg "Repobeats analytics image")
## License

This project is licensed under the [`Apache License`](LICENSE).

```text
Apache License
Copyright (c) 2024 Pham Nguyen Tu
```
