all: edda/build/classes

edda/build/classes: edda
	cd edda; ./gradlew build

edda:
	git submodule update --init
