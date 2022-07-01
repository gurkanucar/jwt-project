# QR Code Login - Frontend

#### Gurkan UCAR

&nbsp;



### Used Packages

**Stomp Socket:** socket server

**Redis**: storing socket rooms and qr code values

**Security:** user authorization and authentication operations

&nbsp;

### How to run

#### clone the project: https://github.com/gurkanucar/qr-login-fe.git

```bash
  git clone https://github.com/gurkanucar/qr-login-fe.git
  cd qr-login-fe
```

#### create jar

```bash
  mvn clean install -DskipTests
```

#### build docker-compose

```bash
  docker-compose build --no-cache
```

#### run docker-compose

```bash
  docker-compose up --force-recreate
```

#### Postman Collection:

[https://github.com/gurkanucar/jwt-project/blob/master/jwt-auth-project.postman_collection.json](https://github.com/gurkanucar/jwt-project/blob/master/jwt-auth-project.postman_collection.json)





