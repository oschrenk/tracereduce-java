# README #

## Development ##

Needed

- [Java 1.6](http://www.java.com)
- [maven](http://maven.apache.org/)
- [GNU make](http://www.gnu.org/software/make/)

The project depends on two libraries

	$ git co git@github.com:oschrenk/jocl-commons.git
	$ git co jocl-commons
	$ mvn install

	$ git co git@github.com:oschrenk/jocl-utils.git
	$ git co jocl-utils
	$ mvn install

Finally

	$ git co git@github.com:oschrenk/tracereduce-java.git
	$ cd tracereduce-java
	$ make

### Eclipse ###

- [m2eclipse](http://www.sonatype.org/m2eclipse) needed

Just point `File > Import > Maven > Existing Maven Projects > Root Directory` to the checked out directory, add to working set if needed, and import.

