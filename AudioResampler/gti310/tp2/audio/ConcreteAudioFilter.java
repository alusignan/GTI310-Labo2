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


import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import gti310.tp2.io.FileSink;
import gti310.tp2.io.FileSource;


public class ConcreteAudioFilter implements AudioFilter {

	
	public FileSource stereoFile;
	public FileSink monoFile;
	
	/*D�claration des variables pour l'en-t�te, s�par�es pour les manipuler plus facilement*/
	
	//HEADER du fichier .wav
	private byte[] chunkID; // 4 octets
	private byte[] chunkSize; // 4 octets 
	private byte[] format; // 4 octets
	
	//FORMAT du fichier .wav
	private byte[] subChunk1Id; // 4 octets
	private byte[] subChunk1Size; // 4 octets
 	private byte[] audioFormat; // 2 octets
	private byte[] numChannels; // 2 octets
	private byte[] sampleRate; // 4 octets
	private byte[] byteRate; // 4 octets
	private byte[] blockAllign; // 2 octets
	private byte[] bitsPerSample; // 2 octets
	
	//DATA du fichier .wav
	private byte[] subChunk2Id; // 4 octets
	private byte[] subChunk2Size; // 4 octets
	private byte[] data; //donn�es du fichier .wav
	
	//Essayer d'isoler le nombre de data
	private long myDataSize;
	
	
	/*M�thode pour lire le fichier stereo et enregistrer le fichier mono*/
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
	
	
	/*http://stackoverflow.com/questions/7619058/convert-a-byte-array-to-integer-in-java-and-vise-versa*/
	/*Little Endian 4 Bytes Array converter (from Bytes Array)*/
	int byteArrayToInt4(byte[] bytes) {
	     return bytes[0]  | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF << 24);
	}
	
	/*Little Endian 2 Bytes Array converter (from Bytes Array)*/
	int byteArrayToInt2(byte[] bytes) {
	     return bytes[0]  | (bytes[1] & 0xFF) << 8;
	}
	/*Little Endian 4 Bytes Array converter (From LONG)*/
	byte[] intToByteArray4(long value) {
	    return new byte[] { 
	    	(byte)value,
	    	(byte)(value >> 8),
	    	(byte)(value >> 16),
	        (byte)(value >> 24)
	    };
	}
	/*Little Endian 2 Bytes Array converter (From LONG)*/
	byte[] intToByteArray2(long value) {
	    return new byte[] { 
	    	(byte)value,
	    	(byte)(value >> 8)  	
	    };
	}

	
	@Override
	/*Process function where the magic happen*/
	public void process() {
		//Ces op�rations ont une complexit� de O(1)
		chunkID = stereoFile.pop(4); //Lecture de 4 octets
		chunkSize = stereoFile.pop(4); //Lecture de 4 octets
		format = stereoFile.pop(4); //Lecture de 4 octets
		subChunk1Id = stereoFile.pop(4); //Lecture de 4 octets
		subChunk1Size = stereoFile.pop(4); //Lecture de 4 octets
		audioFormat = stereoFile.pop(2); //Lecture de 2 octets
		numChannels = stereoFile.pop(2); //Lecture de 2 octets
		sampleRate = stereoFile.pop(4); //Lecture de 4 octets
		byteRate = stereoFile.pop(4); //Lecture de 4 octets
		blockAllign = stereoFile.pop(2); //Lecture de 2 octets
		bitsPerSample = stereoFile.pop(2); //Lecture de 2 octets
		subChunk2Id = stereoFile.pop(4); //Lecture de 4 octets
		subChunk2Size = stereoFile.pop(4); //Lecture de 4 octets
		
	
		//Store the number of byte in the file
		myDataSize = byteArrayToInt4(subChunk2Size);
		
		//Conversion pour valider le nombre de canal (�nonc�)
		int numCanalValidation = byteArrayToInt2(numChannels);
		
		System.out.println("La taille du fichier est de : " +myDataSize+ " octets.");
		System.out.println("Le nombre de canal est de : " +numCanalValidation);
		
		//Validation du nombre de canal (tel que demand�) si = 1 on ne fait rien
		if (numCanalValidation == 1) {
			System.out.println("Ce fichier est d�j� au format mono, aucune op�ration n'a �t� effectu�e.");
		}
		//Sinon
		else {
			//Cr�ation du nouveau chunk Size selon le calcul (donn�es totale /2 --> moiti� moins pour mono) + 36
			byte[] newChunkSize = intToByteArray4((myDataSize/2) + 36);
			
			//Il y a seulement 1 channel (conversion de 1 en byte)
			byte[] newNumChannels = intToByteArray2(1);
			
			//Cr�ation du nouveau byte rate selon le calcul (fr�quence de l'�chantillion * bits par �chantillion/8
			byte[] newByteRate = intToByteArray4(byteArrayToInt4(sampleRate) * (byteArrayToInt2(bitsPerSample)/8));
			
			//Cr�ation du nouveau block allign selon le calcul (bits par �chantillion / 8)
			byte[] newBlockAllign = intToByteArray2((byteArrayToInt2(bitsPerSample)/8));
			
			//Nouveau chunk size (on divise en 2 car il y a moiti� moins de data)
			byte[] newSubChunk2Size = intToByteArray4(myDataSize/2);
			
			//Cr�ation du nouveau fichier mono (en-t�te) seulement
			
			//Ces op�ration sont d'une complexit� O(1)
			monoFile.push(chunkID); //4 octets
			monoFile.push(newChunkSize); //4 octets
			monoFile.push(format); //4 octets
			monoFile.push(subChunk1Id); //4 octets
			monoFile.push(subChunk1Size); //4 octets
			monoFile.push(audioFormat); //2 octets
			monoFile.push(newNumChannels); //2 octets
			monoFile.push(sampleRate); //4 octets
			monoFile.push(newByteRate); //4 octets
			monoFile.push(newBlockAllign); //4 octets
			monoFile.push(bitsPerSample);//2 octets
			monoFile.push(subChunk2Id);//2 octets
			monoFile.push(newSubChunk2Size); //4 octets
			
		
			/*Boucle for de taille des donn�es/2 car il y en a moiti� moins
			 * Utilisation du ByteBuffer documentation trouv�e sur le site web de Java
			 * On isole gauche et droite ensuite on fait la moyenne des 2 avant de le 
			 * mettre dans le fichier mono*/
			
			//Complexit� O(N)
			for (int i = 0; i < ((int)myDataSize/2) - 1; i+=2) {

				//Isole canal gauche avec le ByteBuffer en lisant les 2 octets
				short left = ByteBuffer.wrap(stereoFile.pop(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
				//Isole canal droite avec le ByteBuffer en lisant les 2 octets
				short right = ByteBuffer.wrap(stereoFile.pop(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
				//On fait la moyenne
				short avg = (short) ((left + right)/2);
				//On enregistra dans le fichier a l'aide d'un Byte Buffer
				monoFile.push(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(avg).array());
				
			}
			System.out.println("Done!");
		}	
		
		//Fermeture des deux fichiers.
		monoFile.close(); //O(1)
		stereoFile.close(); //O(1)
	}

}
