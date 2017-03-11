#!/bin/bash
set -u

JAR_FILE=./stanford-corenlp-full-2016-10-31/stanford-corenlp-3.7.0.jar
MDL_FILE=./stanford-chinese-corenlp-models-current.jar
GROUP_ID=edu.stanford.nlp
ARTIFACT_ID=stanford-corenlp
VERSION=3.7.0


# <dependency>
#     <groupId>edu.stanford.nlp</groupId>
#     <artifactId>stanford-corenlp</artifactId>
#     <version>3.6.0</version>
# </dependency>

mvn install:install-file -Dfile=${JAR_FILE} \
        -DgroupId=${GROUP_ID} -DartifactId=${ARTIFACT_ID} -Dversion=${VERSION} \
        -Dpackaging=jar
echo "install jar file"
    

# <dependency>
#     <groupId>edu.stanford.nlp</groupId>
#     <artifactId>stanford-corenlp</artifactId>
#     <version>3.6.0</version>
#     <classifier>models</classifier>
# </dependency>

mvn install:install-file -Dfile=${MDL_FILE} \
        -DgroupId=${GROUP_ID} -DartifactId=${ARTIFACT_ID} -Dversion=${VERSION} \
        -Dclassifier=models-chinese \
        -Dpackaging=jar 
echo "install model file"
