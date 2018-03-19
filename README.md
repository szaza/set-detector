# SET detector
## Application to detect SET of cards
This application is able to recognize the SET cards and to play the classic game. It is composed up from three parts:
* Server side application (created by using Spring Framework)
* Android application
* Web application

### The SET card game
Set is a real-time card game published by the Set Enterprises in 1991. The deck consist of 81 cards varying in four features:
* number (one, two, three);
* symbol (diamond, squiggle, oval);
* shading (solid, striped, open);
* color (red, green and purple);

A set consist of three cards satisfying all of the following conditions:
* They all have the same number or have three different numbers;
* They all have the same symbol or have three different symbols;
* They all have the same shading or have three different shadnings;
* They all have the same color or have three different colors;

For example these three cards form a set:

<img src="https://github.com/szaza/set-detector/blob/master/samples/set-game-cards.png" 
title="sample set cards" alt="sample set cards" width="400"/>

## The SET detector application
This application is composed up by two main part:
* server part - recognize the image and check if a SET is present on the cards
* client part - uploads a picture and presents the results

Let's see how the android application works:

1) First you have to take a picture and send it to the server;

<img src="https://github.com/szaza/set-detector/blob/master/samples/set-detector-android-take-picture.png"
alt="SET detector take a picture" title="SET detector take a picture" width="300" />

2) The server process your image and sends back the response what will be presented by the client application;

<img src="https://github.com/szaza/set-detector/blob/master/samples/android-recognized-cards-1.png"
alt="recognized SET cards" title="recognized SET cards" width="260"/>
<img src="https://github.com/szaza/set-detector/blob/master/samples/android-recognized-cards-2.png"
alt="SET detected" title="SET detected" width="260"/>
<img src="https://github.com/szaza/set-detector/blob/master/samples/android-recognized-cards-3.png"
alt="SET2 detected" title="SET2 detected" width="260"/>

### Compile and run the application
#### 1) Compile and run the server side
Before compiling the server project, you have to do the following steps:
* Step #1: Change the configuration parameters in the `src/main/resources/application.yml` [configuration file](https://github.com/szaza/set-detector/blob/master/server/src/main/resources/application.yml);
Change the **host** and **port** settings acccording to your server settings. If you run the server on localhost you can run the `ifconfig` command on linux or `ipconfig` command on windows in order to obtain your ip address.
* Step #2: Download the frozen graphs from my Google Drive [here](https://drive.google.com/open?id=1yJXjBWfMGGfw_viOzHgRU6088aRJNov_). Create the `graph/YOLO` directory under the `/server/` root and rename the *yolo-voc_19500.pb* file to *yolo-setcards.pb*.
* Step #3: From the `/server/` root directory open a terminal window and type the following command:<br/>
`./gradlew clean build -xtest`<br/>
`./gradlew bootRun`<br/>
Now, you can open the http://localhost:8080 and you should be able to upload and recognize cards.
It should looks like this one:

<img src="https://github.com/szaza/set-detector/blob/master/samples/set-detector-web.png"
alt="SET detector web application" title="SET detector web application" width="500"/>

#### 2) Compile and run the cliens side application
* Step #1: modify the configuration file: [Config.java](https://github.com/szaza/set-detector/blob/master/android/src/main/java/edu/tensorflow/client/Config.java)
Change the **host** and **port** to point to your server. It can be either a domain or an ip address. If you run the server on localhost you can run the `ifconfig` command on linux or `ipconfig` command on windows in order to obtain your ip address.
* Step #2: open the project in Android Studio, connect your device to your computer and run the application.
You also can build the mobile project with Gradle.

## Previous work
I reused some parts of my previous project to create this one:
* [Android Real time object detection application](https://github.com/szaza/android-yolo-v2)
* [Tensorflow Java Example app with YOLOv2 model](https://github.com/szaza/tensorflow-java-yolo)
* [Tensorflow Java Tutorial with Spring](https://github.com/szaza/tensorflow-java-examples-spring)
