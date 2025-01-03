# SootDemo

环境:

jdk: 1.8


## 遇到的错误
```shell
RefType java.lang.RuntimeException not loaded. If you tried to get the RefType of a library class, did you call loadNecessaryClasses()? Otherwise please check Soot's classpath.
```
在最开始中添加
```java
Scene.v().loadNecessaryClasses();
```
