# Simple File Parser Challenge

Hi! Welcome to Simple File Parser. My goal is to explain the entire development process used in the design of this system.
I will cover both topics related to class design and implementation.

# Tech Stack
    Java 11
    Maven
# How to run this application?

    In the root directory of the application, run the following commands: 
    1. mvn compile
    2. mvn exec:java -Dexec.mainClass=application.Program

# Development Process - Class Design

Below we can find the class diagram developed for project implementation. 

| UML Class Design |    
|:----------------:|
|     ![](/home/gustavo/Projects/Java/simple-file-parser-challenge/src/main/resources/images/uml_diagram.jpeg)     |

My main focus was on making the system adaptable to future changes, aiming at both ease of maintenance and elasticity.
Therefore, the first class that was created was FileParser. This class was thought of as abstract, because that way, no one will be able to instantiate the FileParser but its concrete objects, that is, objects that extend this class.
That way, if I need a class responsible for analyzing a PDF in the future, the only process that will need to be done is to create a specific class for reading PDFs
and extend the abstract FileParser class and override the analyzeFile method and implement its body in whatever way is necessary for a PDF to read the file. The same applies if you need a class to read .Doc files. This abstract method is responsible for analyzing the directory we want to monitor.

It is described in the problem that the files have STATUS and after they have been processed they will go to a processed sub-folder. To control these statuses, I created an ENUM that will have the status PROCESSED, PROCESSING, ERROR. That way, as the parsing of the file takes place, I select PROCESSING for the file. At the end I will set PROCESSED and if an exception occurs, the ERROR will be set.

In addition, a class was created responsible for taking care of the statistics part of the file. In this way, I link this class in the FileParser class, so that I will be able to have the statistics information of a given file at that moment of processing.
The statistics contain information on the number of dots within the file (.), number of words and the most used word within a given file.

## Implementation Details - Java

To monitor the directory, I used the WatchAPI, which provides several interesting classes to monitor files.
The idea is that I register a certain directory inside a class called WatchService. After that, we will create an infinite loop in the application that will be running while the program is running. This loop will wait for new events to happen in the directory informed by the user.
For each event found, it takes the name of the file that suffered this event, opens it and analyzes its information on how many points there are, how many words and the most frequently used word.
After processing, the PROCESSED value of the enum is set to the file and the statistics are made available to the user and we restart the event poll to continue monitoring the directory.
## Current Test Coverage


|                                               Current Test Coverage                                               |    
|:-----------------------------------------------------------------------------------------------------------------:|
| ![](/home/gustavo/Projects/Java/simple-file-parser-challenge/src/main/resources/images/current_code_coverage.png) |
