fsc -reset
rm -rf build/*
rm -rf lib/sgf.jar
mkdir -p build/
mkdir -p dsl-output/
scalac -deprecation -d build/ src/main/scala/*.scala
