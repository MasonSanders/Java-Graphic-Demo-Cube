all: GraphicDemo.java
	javac GraphicDemo.java
run: GraphicDemo.class
	java GraphicDemo
clean:
	rm *.class
