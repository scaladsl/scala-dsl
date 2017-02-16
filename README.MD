# scala-dsl

$ cd scala-dsl

$ ./makeProject
$ ./sgfc ./test/blog.scala | ./sgfg ./test/gen-java-model.scala --output="./dsl-output/"

$ ./makeJars
$ java -jar dslc.jar ./test/blog.scala | java -jar dslg.jar ./test/gen-java-model.scala --output="./dsl-output/"