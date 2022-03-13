# Finance Manager
A Finance Manager Web Application. Create your personal account and keep track of all your Transactions and Bills.

## Screens
![Mockups](https://user-images.githubusercontent.com/8016969/158068177-8a314c06-b583-4aee-964e-9bebceae211a.png)

## Stack
![Stack](https://user-images.githubusercontent.com/8016969/158072380-614c4c43-bb3e-4bf8-be0f-d6ddbefdb809.png)

## Features
- Create your personal account.
- Keep Track of your Transactions.
- Keep track of your Bills.
- Bind a Transaction with a Bill.
- A Dashboard page with all your latest finance information.
- An Overview page where you can navigate through your stats over time.
- Get email reminders for Bills that are going to expire etc.
- Get notifications.

## Prerequisites
- Java 11
- Maven 3.5.3
- Node v12.16.0
- npm 6.13.4
- Angular-cli 9.0.0
- Docker / Docker Compose

## Development environment setup

##### Database / ElasticSearch / Kibana
In order to boot up a MariaDB database server, an ElasticSearch server and a Kibana server for development, go to the root folder and execute:
```
docker-compose -f docker-compose-dev.yml build && docker-compose -f docker-compose-dev.yml up -d
```
The MariaDB container will be created with username/password `root/root` and port 3307.

##### Frontend
In order to start the frontend application go to the root folder of `finance-manager-ui` and execute:
```
ng serve
```

##### Backend
In order to start the backend application go to the root folder of `finance-manager-server` and execute:
```
mvn spring-boot:run
```

##### Browser
Open http://localhost:4200 in a browser.

## Production environment setup
To bootstrap your production environment, go to the root folder and execute:
```
docker-compose build && docker-compose up
```
Open http://localhost in a browser.

## Configurations
In the application.properties set:
- ```finance.manager.bill.images.path``` (Absolute path of the folder which will contain Bill images etc.)
- ```finance.manager.temp.path = ``` (Absolute path of the folder which will contain temp files)
- ```mail.smtp.user```
- ```mail.smtp.password```

## Code Quality
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=finance-manager&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=finance-manager)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=finance-manager&metric=bugs)](https://sonarcloud.io/summary/new_code?id=finance-manager)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=finance-manager&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=finance-manager)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=finance-manager&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=finance-manager)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=finance-manager&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=finance-manager)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=finance-manager&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=finance-manager)
