Wikidata List Generator
=======================
[![DOI](https://zenodo.org/badge/48117824.svg)](https://zenodo.org/badge/latestdoi/48117824)
[![Build Status](https://travis-ci.org/physikerwelt/WikidataListGenerator.svg?branch=master)](https://travis-ci.org/physikerwelt/WikidataListGenerator)
[![MavenCentral](https://maven-badges.herokuapp.com/maven-central/com.formulasearchengine/wikidatalistgenerator/badge.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.formulasearchengine%22)
[![Coverage Status](https://coveralls.io/repos/physikerwelt/WikidataListGenerator/badge.svg?branch=master&service=github)](https://coveralls.io/github/physikerwelt/WikidataListGenerator?branch=master)

Creates a list of Page titles and their corresponding Wikidata items.
## Data download
Download the json dump [Wikidata database downloads](https://www.wikidata.org/wiki/Wikidata:Database_download) and 
extract it somewhere on your file system and extract it.
The download is approx 4G (Dez 2015) compressed json data which extracts to approx 60G.
## Run
```
mvn clean install -DskipTests
mvn test
mvn exec:java -Dexec.args="--help"
```
help displays the available options to pass instead of "--help":
```
  Options:
    -a, --aliases

       Default: false
    --help

       Default: false
    -i, --input
       path to the file with the wikidata json dump
    -l, --language
       language to extract
       Default: en
    -o, --output
       path to output file
```


## Runtime
The process seems to be disk bound. With a standard hdd (non ssd) it took about 6 minutes to generate the list.
Probably the time to read the file once.

## See also
[Wikibase data model](https://www.mediawiki.org/wiki/Wikibase/DataModel/JSON)
