.PHONY: algorithms bom core io timer ui clean
all: timer bom core io algorithms  ui
skipTests: algorithmsSkipTests coreSkipTests ioSkipTests timerSkipTests uiSkipTests

bom:
	cd bom && mvn clean install
algorithms:
	cd algorithms && mvn clean install
algorithmsSkipTests: bom
	cd algorithms && mvn clean install -DskipTests
core:
	cd core && mvn clean install
coreSkipTests: bom
	cd core && mvn clean install -DskipTests
io:
	cd io && mvn clean install
ioSkipTests: bom
	cd io && mvn clean install -DskipTests
timer:
	cd timer && mvn clean install
timerSkipTests:
	cd timer && mvn clean install -DskipTests
ui:
	cd ui && mvn clean package
uiSkipTests: bom
	cd ui && mvn clean package -DskipTests
clean:
	rm -rf `find . -type d -name target`