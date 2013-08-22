.PHONY: clean mongoproxy riemann-server riemann-jar test

all: lib riemann-server

lib: edda/build/exploded
	mkdir -p lib
	find edda/build/exploded -name \*.jar -exec cp {} lib \;
	cp edda/build/libs/edda-2.1-SNAPSHOT.jar lib

edda/build/exploded: edda/build/libs
	mkdir -p edda/build/exploded
	cd edda/build/exploded; jar -xf ../libs/edda-2.1-SNAPSHOT.war

edda/build/libs: edda
	cd edda; ./gradlew build

edda:
	git submodule update --init

riemann-server: riemann-jar
	java -cp "resources":"lib":"src":"riemann/target/uberjar+provided/riemann-0.2.3-SNAPSHOT-standalone.jar" riemann.bin riemann.config

riemann/target/uberjar+provided/riemann-0.2.3-SNAPSHOT-standalone.jar: riemann
	cd riemann; lein uberjar

riemann-jar: riemann/target/uberjar+provided/riemann-0.2.3-SNAPSHOT-standalone.jar

riemann:
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
