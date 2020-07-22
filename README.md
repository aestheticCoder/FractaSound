# FractaSound
A Dynamic Audio Visualizer Utilizing Fractal Patterns

## Table of contents
* [Abstract](#abstract)
* [Technologies](#technologies)
* [Setup](#setup)
* [Inspiration](#inspiration)

## Abstract :
  FractaSound is a fractal-based audio visualizer; this Java desktop application aims to provide users with an appealing and customizable animation accompaniment for music. After installing the application, users will be able to import their own music in .WAV or .MP3 form. Users may also select one of several color theme presets to customize the animation to their liking. During the process of the visualization, pitch and volume is extracted by Fast Fourier Transform (FFT), to produce a fractal animation using the fundamental frequency of a current audio sample of the input music. This combination of pitch and volume is used to render a specific Julia Set fractal belonging to the Mandelbrot Set, creating an intuitive and interesting audio visualizer for all to enjoy.

## Technologies :
Project is created with:
* JDK Version: 11.0.5
* Delthas/Java MP3 (MP3 Library)
https://github.com/Delthas/JavaMP3
* FFT.java (FFT Algorithm in Java)
https://www.ee.columbia.edu/~ronw/code/MEAPsoft/doc/html/FFT_8java-source.html

## Setup :
To run this project, clone the master branch to your local machine.
* *After the project has been cloned,* Open the repository where you cloned the project with your favorite IDE (for best results Intellij is recommended ), or run it from the command line.
* Run the *main* method in the Setup class, located in startup package to start the application.
* Feel free to navagaite and use the application with the UI inside the application.

## Inspiration : 
These tutorials was where we started when we began creating Julia sets in Java, even as our porject grew into something unique these tutorials are still mentioned here as they helped us get started in the very begining of the project.
* Fractal Advanced : Julia Set Basics (with Java Code).  http://tech.abdulfatir.com/2014/05/julia-set-basics.html
* Fractal Advanced : Julia Set Advanced (with Java Code). http://tech.abdulfatir.com/2014/05/julia-set-advanced.html
