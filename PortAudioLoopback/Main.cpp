#include <cstdio>
#include <cstdlib>
#include "paAudioInclude/portaudio.h"

int main() {
	printf("Hello World");

	int err = Pa_Initialize();
	if (err != paNoError) {
		printf("PortAudio error: %s\n", Pa_GetErrorText(err));
		exit(1);
	}

	err = Pa_Terminate();
	if (err != paNoError)
		printf("PortAudio error: %s\n", Pa_GetErrorText(err));
}