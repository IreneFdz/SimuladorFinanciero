set PATH=C:\Program Files\Java\jdk1.8.0_271\bin;%PATH%
set classpath=src\lib\Base64.jar;src\lib\GraphLayout.jar;src\lib\bcel.jar;\src\lib\http.jar;src\lib\iiop.jar;src\lib\jade.jar;src\lib\jadeTools.jar;src\lib\jadex_examples.jar;src\lib\jadex_jadeadapter.jar;src\lib\jadex_rt.jar;src\lib\jadex_standalone.jar;src\lib\jadex_tools.jar;src\lib\jhall.jar;src\lib\jibx-bind.jar;src\lib\jibx-extras.jar;src\lib\jibx-run.jar;src\lib\xpp3.jar;src;src\lib\commons-commons\commons-codec-1.3.jar

javac src\ontology\acciones\*java 
javac src\ontology\conceptos\*java 
javac src\ontology\predicados\*java 
javac src\tablero\beliefs\*java
javac src\tablero\plans\*java
javac src\inversor\beliefs\*java
javac src\inversor\plans\*java
pause
jar cvf SimuladorJadex *
pause 

