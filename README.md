# A Client - Java Application To Publish Messages To A PubSub Topic

## PubSubPublisher

[PubSubPublisher](src/main/java/PubSubPublisher.java) -
A class that read messages from [names.txt](src/main/resources/names.txt) then publish to a PubSub topic mentioned in the class

## Getting Started

### Requirements

* Java 11
* Maven 3

### Run the Application

Run the application on Google Cloud Shell
```
mvn compile exec:java -Dexec.mainClass=PubSubPublisher
```
