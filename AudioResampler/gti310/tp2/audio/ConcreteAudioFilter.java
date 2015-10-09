/*!!!!!!CETTE SECTION PROVIENT DE CE SITE WEB!!!!!!!!!!
 * https://web.archive.org/web/20141213140451/https://ccrma.stanford.edu/courses/422/projects/WaveFormat/
 * 
Offset  Size  Name             Description

The canonical WAVE format starts with the RIFF header:

0         4   ChunkID          Contains the letters "RIFF" in ASCII form
                               (0x52494646 big-endian form).
4         4   ChunkSize        36 + SubChunk2Size, or more precisely:
                               4 + (8 + SubChunk1Size) + (8 + SubChunk2Size)
                               This is the size of the rest of the chunk 
                               following this number.  This is the size of the 
                               entire file in bytes minus 8 bytes for the
                               two fields not included in this count:
                               ChunkID and ChunkSize.
8         4   Format           Contains the letters "WAVE"
                               (0x57415645 big-endian form).

The "WAVE" format consists of two subchunks: "fmt " and "data":
The "fmt " subchunk describes the sound data's format:

12        4   Subchunk1ID      Contains the letters "fmt "
                               (0x666d7420 big-endian form).
16        4   Subchunk1Size    16 for PCM.  This is the size of the
                               rest of the Subchunk which follows this number.
20        2   AudioFormat      PCM = 1 (i.e. Linear quantization)
                               Values other than 1 indicate some 
                               form of compression.
22        2   NumChannels      Mono = 1, Stereo = 2, etc.
24        4   SampleRate       8000, 44100, etc.
28        4   ByteRate         == SampleRate * NumChannels * BitsPerSample/8
32        2   BlockAlign       == NumChannels * BitsPerSample/8
                               The number of bytes for one sample including
                               all channels. I wonder what happens when
                               this number isn't an integer?
34        2   BitsPerSample    8 bits = 8, 16 bits = 16, etc.
          2   ExtraParamSize   if PCM, then doesn't exist
          X   ExtraParams      space for extra parameters

The "data" subchunk contains the size of the data and the actual sound:

36        4   Subchunk2ID      Contains the letters "data"
                               (0x64617461 big-endian form).
40        4   Subchunk2Size    == NumSamples * NumChannels * BitsPerSample/8
                               This is the number of bytes in the data.
                               You can also think of this as the size
                               of the read of the subchunk following this 
                               number.
44        *   Data             The actual sound data.

 * */

package gti310.tp2.audio;

import java.io.File;
import java.io.FileNotFoundException;

import gti310.tp2.io.FileSink;
import gti310.tp2.io.FileSource;

public class ConcreteAudioFilter implements AudioFilter {

	
	private FileSource stereoFile;
	private FileSink monoFile;
	private String stereoLocation, monoLocation;
	
	//Constante de conversion
	private final int twobits = 16;
	private final int fourbits = 32;
	
	//HEADER du fichier .wav
	private byte[] chunkID; // 4 octets
	private byte[] chunkSize; // 4 octets 
	private byte[] format; // 4 octets
	
	//FORMAT du fichier .wav
	private byte[] subChunk1Id; // 4 octets
	private byte[] subChunk1Format; // 4 octets
 	private byte[] audioFormat; // 2 octets
	private byte[] numChannels; // 2 octets
	private byte[] sampleRate; // 4 octets
	private byte[] byteRate; // 4 octets
	private byte[] blockAllign; // 2 octets
	private byte[] bitsPerSample; // 2 octets
	
	//DATA du fichier .wav
	private byte[] subChunk2Id; // 4 octets
	private byte[] subChunk2Size; // 4 octets
	private byte[] data; //données du fichier .wav
	
	
	
	/*http://stackoverflow.com/questions/16466515/convert-audio-stereo-to-audio-byte*/
	/*http://stackoverflow.com/questions/4714542/pcm-wave-file-stereo-to-mono*/
	
	public void InuputOutputFile(String stereoLocation, String monoLocation) {
		try {
			stereoFile = new FileSource(stereoLocation);
			monoFile = new FileSink(monoLocation);
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		chunkID = stereoFile.pop(fourbits);
		chunkSize = stereoFile.pop(fourbits);
		format = stereoFile.pop(fourbits);
		subChunk1Id = stereoFile.pop(fourbits);
		subChunk1Format = stereoFile.pop(fourbits);
		audioFormat = stereoFile.pop(twobits);
		numChannels = stereoFile.pop(twobits);
		sampleRate = stereoFile.pop(fourbits);
		
		
		
	}

}
