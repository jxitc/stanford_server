#!/bin/bash
set -u

# axel is a multi-thread downloading tool (apt-get or yum can download it), you can also use wget instead

axel -n 10 http://nlp.stanford.edu/software/stanford-corenlp-full-2016-10-31.zip
axel -n 10 http://nlp.stanford.edu/software/stanford-chinese-corenlp-models-current.jar

unzip stanford-corenlp-full-2016-10-31.zip
