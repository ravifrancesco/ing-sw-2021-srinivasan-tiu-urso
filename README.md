# Master of Renaissance

[![CircleCI](https://img.shields.io/circleci/build/gh/ravifrancesco/ing-sw-2021-srinivasan-tiu-urso?style=for-the-badge&token=e30efbb709d41ac851479f2a553ca2077d99b251)](https://app.circleci.com/pipelines/github/ravifrancesco/ing-sw-2021-srinivasan-tiu-urso)

## Installation and requirements

Download the JAR file matching your OS from the [release tab](https://github.com/ravifrancesco/ing-sw-2021-srinivasan-tiu-urso/releases/).

To run the project JAVA 15 is required. Please find the jdk for your OS [here](https://www.oracle.com/java/technologies/javase-downloads.html).

Please be sure to set the path for the `java` command to the JAVA 15 JDK.

## How to run

To run the program use CD on the terminal to find the directory where you downloaded the JAR file, from there run:
* `java -jar name_of_jar.jar server <ip> <port>` to run the server
* `java -jar name_of_jar.jar ui cli` to run the command line interface UI
* `java -jar name_of_jar.jar ui gui` to run the GUI UI

Then, follow instructions on screen.

## Advanced features

We decided to implement two advanced features in our project:
1. Multiple games: the project includes a main lobby that is accessed when opening the game and connecting to the server. From there, the user is able to visualize current games, create one or join one.
2. Local single player: from the main menu, the user is able to create a local single player game without the need of connecting to a server.

## Test coverage

We implemented tests for the model and controller side of our project. In our project, the state of the model is always correct (meaning that when trying to modify it, the model will throw exceptions in cases of states that do not sadisfy the rules of the game). For this reason, we mainly tested the model, and tested the controller parts that do not depend on the model. These are our test coverage reults:

![Full Model Coverage](https://github.com/ravifrancesco/ing-sw-2021-srinivasan-tiu-urso/blob/master/deliverables/full_model_coverage.png)
![Controller Coverage](https://github.com/ravifrancesco/ing-sw-2021-srinivasan-tiu-urso/blob/master/deliverables/controller_coverage.png)
