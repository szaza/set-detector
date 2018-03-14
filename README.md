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

<img src="https://upload.wikimedia.org/wikipedia/commons/8/8f/Set-game-cards.png" 
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
Before compiling the server side project, you have to change the configuration parameters.

#### 2) Compile and run the cliens side
First you have to modify the configuration file: [Config.java](https://github.com/szaza/set-detector/blob/master/android/src/main/java/edu/tensorflow/client/Config.java)
Change the host to point to your server. It can be either a domain or an ip address. If you run the server on localhost you can run
the `ifconfig` command on linux or `ipconfig` command on windows in order to obtain your ip address.
