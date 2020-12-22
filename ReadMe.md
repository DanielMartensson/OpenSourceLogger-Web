# OpenSourceLogger

This software creates a web application with Vaadin and with this web application you can control and measure analog inputs and outputs and 
store them into a MySQL database.

# Features

* Vaadin 14 Long Time Support
* 4 Analog inputs of 12-bit
* 2 Analog inputs of 16-bit
* 3 Analog inputs of 16-bit differential
* 9 PWM control outputs
* 6 Digital inputs
* 3 Analog outputs of 12-bit
* Circuit protection for high voltage
* Spring Security Login
* MySQL database
* Plotting functionality
* Gmail alarm functionality
* CSV generation of measurements


The board will communicate with the TX & RX UART pins. 
All information about such as schematic and component list the board can be found in the folder KiCad & STM32.

![a](https://raw.githubusercontent.com/DanielMartensson/OpenSourceLogger/master/KiCad%20&%20STM32/3D%20Schematic.png)

![a](https://raw.githubusercontent.com/DanielMartensson/OpenSourceLogger/master/KiCad%20&%20STM32/Produced.jpg)


# How to install - Ubuntu user

1. Install Java 11, Maven, NodeJS

Java 11
```
sudo apt-get install openjdk-11-jdk
```

Maven
```
sudo apt-get install maven
```

NodeJS - This is used if you want to work on this project. If you only want to run this project, you don't need NodeJS.
```
curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
sudo apt-get install -y nodejs
```

2. Begin first to install MySQL Community Server

```
sudo apt-get install mysql-server
```


3. Then create a user e.g `myUser` with the password e.g `myPassword`

Login and enter your `sudo` password or mysql `root` password
```
sudo mysql -u root -p
```

Create user with the host `%` <-- That's important if you want to access your server from other computers.
```
CREATE USER 'myUser'@'%' IDENTIFIED BY 'myPassword';
```

Set the privileges to that user
```
GRANT ALL PRIVILEGES ON *.* TO 'myUser'@'%';
```


4. Change your MySQL server so you listening to your LAN address

Open this file
```
/etc/mysql/mysql.conf.d/mysqld.conf
```

And change this
```
bind-address            = 127.0.0.1
```

To your LAN address where the server is installed on e.g
```
bind-address            = 192.168.1.34
```

Then restart your MySQL server
```
sudo /etc/init.d/mysql restart
```

If you don't know your LAN address, you can type in this command in linux `ifconfig` in the terminal

5. Create a Gmail account

Create a Gmail account and go to `https://myaccount.google.com/security` and enable so you can login from `less secure apps`.
Because `OpenSourceLogger` uses `Java Mail` to logg into Gmail. This feature exist because if `OpenSourceLogger` is on the fly over a
night and something happens, then it will stop everything and send a message back to you.

6. Download `OpenSourceLogger`

Download the `OpenSourceLogger` and change the `application.properties` in the `/src/main/resources` folder.
Here you can set the configuration for your database LAN address, user and password. You can also set a gmail address and its
password. Don't forget that you can change the PWM frequency here as well.

```
server.port=8080
# Ensure application is run in Vaadin 14/npm mode
vaadin.compatibilityMode = false
logging.level.org.atmosphere = warn

# Database
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.datasource.url=jdbc:mysql://ServerIPAddress:3306/OpenSourceLogger?createDatabaseIfNotExist=true&serverTimezone=CET
spring.datasource.username=myUser
spring.datasource.password=myPassword

# Mail - Transmitter
configuration.MailConfiguration.host=smtp.gmail.com
configuration.MailConfiguration.port=587
configuration.MailConfiguration.username=yourGMailAddress@gmail.com
configuration.MailConfiguration.password=yourGMailPassword
configuration.MailConfiguration.properties.mail.smtp.auth=true
configuration.MailConfiguration.properties.mail.smtp.starttls.enable=true

# Mail - Reciever
service.MailService.subject=The subject message title

# Login
spring.security.user.name=myUser
spring.security.user.password=myPassword
```

7. Pack this project and run

First stand inside of the folder `OpenSourceLogger` and write inside your terminal
```
mvn package -Pproduction
```

8. Starting the application

Now a JAR file is created inside the `OpenSourceLogger/target` folder. 

You can start `opensourcelogger-1.0-SNAPSHOT.jar`. First plugin your Serial UART into your USB port first, then write. 

```
sudo java -jar opensourcelogger-1.0-SNAPSHOT.jar
```

Once the application is started. You can then enter the web application e.g.

```
http://192.168.1.35:8080
```
Notice that there is no `https`, only `http`.
