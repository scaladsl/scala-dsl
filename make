rm -rf build/*
rm -rf lib/sgf.jar
mkdir -p build/
scalac -deprecation -d build/ src/*.scala
