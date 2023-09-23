# Makefile for compiling FSS.java and Client.java

JAVAC = javac
JFLAGS = -g

# Java source files
SRCS = FSS.java IndexServer.java PeerServer.java Client.java FileDetail.java

# Output directory
OUTDIR = bin

# Output files (class files)
CLASSES = $(SRCS:.java=.class)

# entry point
MAIN = FSS

.SUFFIXES: .java .class
.java.class:
	$(JAVAC) $(JFLAGS) $*.java

# Default target: compile all Java files
all: $(CLASSES)

# Compile Java source files to class files
%.class: %.java
	@mkdir -p $(OUTDIR)
	$(JAVAC) $<

run: all
	java $(MAIN)

clean:
	rm -rf *.class

.PHONY: all run clean
