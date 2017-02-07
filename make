#!/bin/bash

fsc -reset
rm -rf build/*
mkdir -p build/compiler
mkdir -p build/generator
mkdir -p dsl-output/
#scalac -verbose -deprecation -d build/ src/main/scala/*.scala
scalac -verbose -deprecation -d build/compiler src/main/scala/compiler.scala src/main/scala/mainDef.scala
scalac -verbose -deprecation -d build/generator src/main/scala/generator.scala src/main/scala/mainGen.scala
jar cvfe dslc.jar mainDef -C build/compiler/ . -C libs/scala-libs .
# jar cfe dslc.jar mainDef -C build/compiler/ . -C libs/scala-libs .
jar cvfe dslg.jar mainGen -C build/generator/ . -C libs/scala-libs .
# jar cfe dslg.jar mainGen -C build/generator/ . -C libs/scala-libs .
