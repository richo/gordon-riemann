all: lib

lib: edda/build/exploded
	mkdir -p lib; find edda/build/exploded -name \*.jar -exec cp {} lib \;

edda/build/exploded: edda/build/classes
	mkdir -p edda/build/exploded; cd edda/build/exploded; jar -xf ../libs/edda-2.1-SNAPSHOT.war

edda/build/classes: edda
	cd edda; ./gradlew build

edda:
	git submodule update --init
