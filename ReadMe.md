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
* USB Camera view (Does not work with Raspberry Pi)
* Plotting functionality
* SMTP alarm functionality
* CSV generation of measurements
* Connect with RX and TX from Raspberry Pi pins

![a](https://raw.githubusercontent.com/DanielMartensson/OpenSourceLogger/master/KiCad%20&%20STM32/UiView.png)

The board will communicate with the TX & RX UART pins. 
All information about such as schematic and component list the board can be found in the folder KiCad & STM32.

Made in KiCad.

![a](https://raw.githubusercontent.com/DanielMartensson/OpenSourceLogger/master/KiCad%20&%20STM32/3D%20Schematic.png)

I have solder all components by my self.

![a](https://raw.githubusercontent.com/DanielMartensson/OpenSourceLogger/master/KiCad%20&%20STM32/Produced.jpg)

I have solder this microcontroller by hand. 

![a](https://raw.githubusercontent.com/DanielMartensson/OpenSourceLogger/master/KiCad%20&%20STM32/HandSolder.jpg)

Applied inside a metallic box. Here I'm using Raspberry Pi to communicate with the board.
You can use a regular computer as well, as long you have a USB to serial converter, e.g `USB/UART converter FTDI FT232` where you connect `RXD -> TX` and `TXD -> RX` on the board.

![a](https://raw.githubusercontent.com/DanielMartensson/OpenSourceLogger/master/KiCad%20&%20STM32/Box.jpg)


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

NodeJS
```
curl -sL https://deb.nodesource.com/setup_lts.x | sudo -E bash -
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

5. Download `OpenSourceLogger`

Download the `OpenSourceLogger` and change the `application.properties` in the `/src/main/resources` folder.
Here you can set the configuration for your database IP address, user and password. Here set the address of your `Outlook` account.
The `service.MailService.from` must be the same as `configuration.MailConfiguration.username`.

```
server.port=8080
# Ensure application is run in Vaadin 14/npm mode
vaadin.compatibilityMode = false
logging.level.org.atmosphere = warn

# Database
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.datasource.url=jdbc:mysql://yourIPAddressOfTheServerWhereMySQLIsInstalled:3306/OpenSourceLogger?createDatabaseIfNotExist=true&serverTimezone=CET
spring.datasource.username=myUser
spring.datasource.password=myPassword

# Mail - Transmitter
configuration.MailConfiguration.host=smtp.office365.com
configuration.MailConfiguration.port=587
configuration.MailConfiguration.username=yourOutlookAccount@outlook.com
configuration.MailConfiguration.password=yourOutlookPassword

# Mail - Receiver
service.MailService.subject=Alarm Message Subject Title
service.MailService.from=yourOutlookAccount@outlook.com

# Login
spring.security.user.name=myUser
spring.security.user.password=myPassword
```

6. Pack this project and run

First stand inside of the folder `OpenSourceLogger` and write inside your terminal
```
mvn package -Pproduction
```

7. Starting the application

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

# Deploy onto Raspberry Pi

Notice that camera won't work on Raspberry Pi due to the latest library from [Sarox](https://github.com/sarxos/webcam-capture) cannot support Raspberry Pi 4B. But you can 
do everything else. If you want to work with this camera stuff inside `OpenSourceLogger`, please go to `CameraThread.java` class and begin to explore how to solve that problem.

Anyway! Once you got `opensourcelogger-1.0-SNAPSHOT.jar` ready, you can send it to your Raspberry Pi.

```
sudo scp opensourcelogger-1.0-SNAPSHOT.jar pi@RaspberryPiIPAddress:/The/Place/You/Want/The/Jar/File/To/Be/Saved
```

Example:

```
sudo scp opensourcelogger-1.0-SNAPSHOT.jar pi@192.168.1.35:/home/pi
```

Then open `rc.local`

```
sudo nano /etc/rc.local
```

And place these line above `exit 0`

```
cd /The/Path/To/Where/The/Jar/File/Is/Placed
sudo java -jar opensourcelogger-1.0-SNAPSHOT.jar &
```
Important with `&`, else Raspberry Pi is going to get stuck there with the `Spring Boot` terminal at the startup.

Then you need to enable `Serial` in Raspberry Pi.
```
sudo raspi-config
```
Then enable `Serial`. It's at the same page where you enable `SSH`, `VNC`, `I2C` etc. 
Once you found `Serial` option, answer `NO` to the question about login shell.
Then answer `YES` to the question about serial hardware port.
Then when you exit the `raspi-config`, it will ask you if you want to reboot, press `YES`

Now your `OpenSourceLogger` will starts automatically. Select the port `ttyS0` in `Device settings` at `OpenSourceLogger` and save the configuration
inside the database. `ttyS0` is the `RX` and `TX` pins of the Raspberry Pi. Done!

# Build an own board

Download `DAC ADC PWM IO - Board Schematic` and `DAC ADC PWM IO - STM32 Code` folder.
Inside the `DAC ADC PWM IO - Board Schematic` folder, there is a `.csv` Bill Of Materials list with article numbers from Mouser Electronics. 
I have been using `KiCad` to create these files and I used `https://jlcpcb.com` to give them the `gerber` files from the `DAC ADC PWM IO - Board Schematic` folder.

To program the board, use a `ST-LINK-V2` programmer to program the board with `STM32CubeIDE 1.5.0`.
