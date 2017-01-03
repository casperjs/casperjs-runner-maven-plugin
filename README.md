casperjs-runner-maven-plugin
============================

[![Build Status Images](https://travis-ci.org/casperjs/casperjs-runner-maven-plugin.svg)](https://travis-ci.org/casperjs/casperjs-runner-maven-plugin)
[![Build status](https://ci.appveyor.com/api/projects/status/upk40tu5vs2q1abj?svg=true)](https://ci.appveyor.com/project/bguerin/casperjs-runner-maven-plugin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.casperjs/casperjs-runner-maven-plugin/badge.svg?style=flat)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.casperjs%22%20AND%20a%3A%22casperjs-runner-maven-plugin%22)
[![Apache License](http://img.shields.io/badge/license-APACHE2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

This project aims to run [CasperJS](http://casperjs.org/) tests in a Maven build.

Available on Maven central.
Add in your pom.xml:

```
<plugin>
    <groupId>com.github.casperjs</groupId>
    <artifactId>casperjs-runner-maven-plugin</artifactId>
    <version>1.0.5</version>
    <configuration></configuration>
</plugin>
```

## System requirements

This plugin has been tested on these environments :
- **Linux**, PhantomJS v **1.8.2** and CasperJS v **1.0.4**
- **Linux**, PhantomJS v **1.9.8** and CasperJS v **1.1.3**
- **Windows**, PhantomJS v **1.8.2** and CasperJS v **1.0.4**
- **Windows**, PhantomJS v **1.9.8** and CasperJS v **1.1.3**

## Usage

See the [plugin site](http://casperjs.github.io/casperjs-runner-maven-plugin/)

## Examples

See the [integration tests](https://github.com/casperjs/casperjs-runner-maven-plugin/tree/master/src/it/casperjs-runner)

## Build

Download the sources, and build the plugin using the ```mvn clean install``` command. You can build this plugin using Maven 2.2.x or Maven 3.x, and a JDK 1.6.

## Issues / Enhancements

If you encounter issues or think about any kind of enhancements, you can add them in the [adequate section of the project](https://github.com/casperjs/casperjs-runner-maven-plugin/issues). Do not hesitate also to make Push Requests if you want to enhance this project.

