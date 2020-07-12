# OpenSourceLogger

This software creates a web application with Pi4J and Vaadin and with this, you can control and measure analog inputs and outputs and 
store them into a MySQL database. You need to have a Raspberry Pi for this.

# How to install

* 0. Install Java 11, Maven, NodeJS

Java 11
```
sudo apt-get install openjdk-11-jdk
```

Maven
```
sudo apt-get install mvn
```

NodeJS
```
curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
sudo apt-get install -y nodejs
```

* 1. Begin first to install MySQL Community Server

```
sudo apt-get install mysql-server
```

* 2. Then create a user e.g `myUser` with the password e.g `myPassword`

Login and enter your sudo password
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


* 3. Change your MySQL server so you listening to your LAN address

Open this file
```
/etc/mysql/mysql.conf.d
```

And change this
```
bind-address            = 127.0.0.1
```

To your LAN address where the server is installed on
```
bind-address            = 192.168.1.34
```

If you don't know your LAN address, you can type in this command in linux `ifconfig` in the terminal

* 4. Create a Gmail account

Create a Gmail account and go to `https://myaccount.google.com/security` and enable so you can login from `less secure apps`.
Because `OpenSourceLogger` uses `Java Mail` to logg into Gmail. This feature exist because if `OpenSourceLogger` is on the fly over a
night and something happens, then it will stop everything and send a message back to you.

* 5. Download `OpenSourceLogger`

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
spring.datasource.url=jdbc:mysql://YOUR_SERVER_LAN_ADDRESS:3306/OpenSourceLogger?createDatabaseIfNotExist=true&serverTimezone=CET
spring.datasource.username=myUser
spring.datasource.password=myPassword

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL_ADDRESS@gmail.com
spring.mail.password=YOUR_GMAIL_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
spring.mail.properties.mail.smtp.starttls.enable=true

# Pi4J
pi4j.pwmFrequency=100
```

* 6. Pack this project and run

First stand inside of the folder `ÒpenSourceLogger` and write
```
mvn package -Pproduction
```

Now a JAR file is created inside the `ÒpenSourceLogger/target` folder. Run it on your Raspberry Pi.
```
sudo java -jar opensourcelogger-1.0-SNAPSHOT.jar
```

* 7. Access the web application

To enter the web application, you need to find out what IP address your Raspberry Pi as. Assume that the LAN address of the server is `192.168.1.34`. It's a regular computer. Your Raspberry Pi have the address `192.168.1.35`. Then you will access the web application with this URL link

```
http://192.168.1.35:8080
```
