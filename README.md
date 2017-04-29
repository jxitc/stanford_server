# Server Wrapper of Stanford


## Install StanfordCoreNLP

#### Option 1: maven repo auto configuration

Simply add following dependency to maven ``pom.xml`` and maven will download for you:

```
<dependency>
    <groupId>edu.stanford.nlp</groupId>
    <artifactId>stanford-corenlp</artifactId>
    <version>3.7.0</version>
    <classifier>models-chinese</classifier>
</dependency>
<dependency>
    <groupId>edu.stanford.nlp</groupId>
    <artifactId>stanford-corenlp</artifactId>
    <version>3.7.0</version>
</dependency>
```

#### Option 2: download for the jar yourself

You need to download two necessary files:

- stanford corenlp code: ``http://nlp.stanford.edu/software/stanford-corenlp-full-2016-10-31.zip``
- chinese model jar: ``http://nlp.stanford.edu/software/stanford-chinese-corenlp-models-current.jar``

after download the two (unzip the first one first), and use ``mvn install:install-file`` to install the dependency to local ``.m2`` repo:

```
#!/bin/bash
set -u

JAR_FILE=./stanford-corenlp-full-2016-10-31/stanford-corenlp-3.7.0.jar
MDL_FILE=./stanford-chinese-corenlp-models-current.jar
GROUP_ID=edu.stanford.nlp
ARTIFACT_ID=stanford-corenlp
VERSION=3.7.0

mvn install:install-file -Dfile=${JAR_FILE} \
        -DgroupId=${GROUP_ID} -DartifactId=${ARTIFACT_ID} -Dversion=${VERSION} \
        -Dpackaging=jar
echo "install jar file"
    
mvn install:install-file -Dfile=${MDL_FILE} \
        -DgroupId=${GROUP_ID} -DartifactId=${ARTIFACT_ID} -Dversion=${VERSION} \
        -Dclassifier=models-chinese \
        -Dpackaging=jar 
echo "install model file"
```

## Setup the resource property file

Like the one in ``./src/main/resources/xjiang-stanford-chinese.properties``, config the annotation pipeline as you want. And simple initialize using following API:

```
StanfordCoreNLP corenlp = new StanfordCoreNLP("xjiang-stanford-chinese.properties");
``` 

## Stanalone jetty runner

#### Using ``jetty-runner.jar`` to host your server

Simply add following code into your ``pom.xml``, and maven will download the ``jetty-runner.jar`` for you at compilation time.

```
<plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <version>2.3</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals><goal>copy</goal></goals>
                <configuration>
                    <artifactItems>
                        <artifactItem>
                            <groupId>org.eclipse.jetty</groupId>
                            <artifactId>jetty-runner</artifactId>
                            <version>9.3.3.v20150827</version>
                            <destFileName>jetty-runner.jar</destFileName>
                        </artifactItem>
                    </artifactItems>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```


#### Start the server

``mvn package``, cd to ``./target`` and run:

```
java -jar ./dependency/jetty-runner.jar --port 9123 --path / ./stanford-1.0-SNAPSHOT.war
```

Open your brower and typein:

```
http://localhost:9123/nlp?query=北京周三的天气
```

You are done!
