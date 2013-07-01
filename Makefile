.PHONY: clean mongoproxy riemann-server

all: lib riemann-server

lib: edda/build/exploded
	mkdir -p lib
	find edda/build/exploded -name \*.jar -exec cp {} lib \;
	cp edda/build/libs/edda-2.1-SNAPSHOT.jar lib

edda/build/exploded: edda/build/libs
	mkdir -p edda/build/exploded
	cd edda/build/exploded
	jar -xf ../libs/edda-2.1-SNAPSHOT.war

edda/build/libs: edda
	cd edda
	./gradlew build

edda:
	git submodule update --init

riemann-server: riemann/target


riemann/target: riemann
	cd riemann; lein install

riemann:
	git submodule update --init

clean:
	rm -rf lib edda/build

mongoproxy:
	ssh -L27017:localhost:27017 $(MONGOHOST)
