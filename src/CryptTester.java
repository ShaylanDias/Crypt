
import java.util.*;
import java.io.*;


public class CryptTester {

	public static final String FILE_NAME = "ShakespeareWhole.txt";

	public static final String keyword = "hippopotamus";


    public static boolean compareFiles (String file1, String file2) {
    	BufferedReader bReader = null;
    	BufferedReader bReader2 = null;
    	Scanner in = null;
    	Scanner in2 = null;
    	int line = 1;
    	try {
			bReader = new BufferedReader(new FileReader(file1));
			bReader2 = new BufferedReader(new FileReader(file2));
			in = new Scanner(bReader);
			in2 = new Scanner(bReader2);
			while (in.hasNextLine() && in2.hasNextLine()) {
				boolean lineEqual = true;
				String input = in.nextLine();
				String input2 = in2.nextLine();
				if (Crypt.algorithm < 3) {
					lineEqual = input.equals(input2);
				} else if (input.length() != input2.length()) {
					lineEqual = false;
				} else {
					for (int i = 0; i < input.length(); i++) {
						char one = input.charAt(i);
						char two = input2.charAt(i);
						if (one != 'j' && one != 'J' && one != two) {
							lineEqual = false;
							break;
						}
					}
				}
				if (!lineEqual) {
					System.out.println("First difference between "+file1+" and "+file2+" found on line " + line);
					System.out.println(file1+":"+input);
					System.out.println(file2+":"+input2);
					return false;
				}
				line++;
			}
			if (in.hasNextLine() || in2.hasNextLine()) {
				System.out.println(file1 + " and " + file2 + " have different lengths");
				return false;
			}
    	} catch (IOException ex) {
    		System.out.println("File cannot be read.");
    		return false;
    	} finally {
    		if (in != null) in.close();
    		if (in2 != null) in2.close();
    	}
    	return true;
    }



	public static void main(String[] args) {

		// File splitting
		if(!(new File(FILE_NAME).exists())) {
			System.out.println("File does not exist. Quitting.");
			System.exit(1);
		}
		int i = FILE_NAME.lastIndexOf(".");
		String extension = "";
		String testFile = FILE_NAME;
		if (i >= 0) {
			extension = testFile.substring(i);
			testFile = testFile.substring(0,i);
		}

		System.out.println("TESTING: How long does it take to encrypt and decrypt?");
		long startTime = System.nanoTime();
		Crypt worker1 = new Crypt();
		worker1.encrypt(testFile+extension, testFile+"Enc"+extension, keyword);
		Crypt worker2 = new Crypt();
		worker2.decrypt(testFile+"Enc"+extension, testFile+"2"+extension, keyword);
		long endTime = System.nanoTime();
		long time = endTime - startTime;
		System.out.println("The run time (nanoseconds): " + time);
		long mins = time/60000000000L;
		long secs = time%60000000000L/1000000000L;
		long millis = time % 1000000000L/1000000L;
		long nanos = time % 1000000L;
		System.out.println("Formatted as (minutes:seconds:milliseconds:nanoseconds): " + mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", millis) + ":" + String.format("%06d", nanos));
		System.out.println();

		System.out.println("TESTING: Is your unencrypted file exactly equal to the original?");
		boolean equal1 = compareFiles(testFile+extension,testFile+"2"+extension);
		System.out.println("Is the file the same after an encrypt, then a decrypt? " + equal1);
		System.out.println();

    	System.out.println("TESTING: Is your encrypted file exactly equal to the algorithm sample?");
		boolean equal2 = compareFiles(testFile+"Enc"+extension, testFile+"Enc"+Crypt.algorithm+extension);
		System.out.println("Does the encrypted file match the sample? " + equal2);

	}


}
















