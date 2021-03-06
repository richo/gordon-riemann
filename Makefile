.PHONY: clean mongoproxy riemann-server riemann-jar test

all: riemann-server

lib: edda/build/exploded
	mkdir -p lib
	find edda/build/exploded -name \*.jar -exec cp {} lib \;
	cp edda/build/libs/edda-2.1.jar lib

edda/build/exploded: edda/build/libs
	mkdir -p edda/build/exploded
	cd edda/build/exploded; jar -xf ../libs/edda-2.1.war

edda/build/libs: edda/src
	cd edda; ./gradlew build

edda/src:
	git submodule update --init

riemann-server: riemann-jar gordon-jar
	java -cp "resources":"lib/*":"src":"riemann/target/riemann-0.2.10-standalone.jar":"target/gordon-riemann-0.1.0-SNAPSHOT-standalone.jar" riemann.bin riemann.config

riemann/target/riemann-0.2.10-standalone.jar: riemann/src
	cd riemann; lein uberjar

riemann-jar: riemann/target/riemann-0.2.10-standalone.jar

target/gordon-riemann-0.1.0-SNAPSHOT-standalone.jar: lib
	lein uberjar

gordon-jar: target/gordon-riemann-0.1.0-SNAPSHOT-standalone.jar


riemann/src:
	git submodule update --init

clean:
	rm -rf lib edda/build

mongoproxy:
	ssh -NL27017:localhost:27017 $(MONGOHOST)


test:
	git submodule update --init
	lein test


script/tmux-dev.tmux: script/tmux-dev
	bash script/tmux-dev > script/tmux-dev.tmux

tmux-dev: script/tmux-dev.tmux
	tmux source ${PWD}/script/tmux-dev.tmux
