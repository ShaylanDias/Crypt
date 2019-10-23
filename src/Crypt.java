import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * 
 * This class encrypts and decrypts text files using one of 3 algorithms:
 * 		Random monoalphabet, Vigenere, or Playfair
 * 
 * 
 * @author
 * @version
 * 
 */
public class Crypt {


	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	/**
	 * 
	 * An integer representing the algorithm chosen.
	 * Set to:
	 * 1 for random monoalphabet
	 * 2 for Vigenere
	 * 3 for Playfair
	 * 
	 */
	public static final int algorithm = 3;


	/**
	 * Reads from the file specified, and writes out an encrypted version of the file. If the output file already
	 * exists, overwrite it.
	 * 
	 * @param inputFilename The path of the file to be encrypted.
	 * @param outputFilename The path of the encrypted file to be saved.
	 * @param keyword The keyword to be used in the encryption algorithm.
	 * 
	 */
	public void encrypt(String inputFilename, String outputFilename, String keyword) 
	{
//		int lineNum = 0;
//		Scanner scan = null;
		FileWriter writer = null;
		BufferedWriter bWriter = null;
		StringBuffer output = new StringBuffer();

		try {
			FileReader reader = new FileReader(inputFilename);
			BufferedReader bReader = new BufferedReader(reader);
			writer = new FileWriter(outputFilename);
			bWriter = new BufferedWriter(writer);

//			scan = new Scanner(bReader);

			StringBuffer code = new StringBuffer(25);
			//Alphabet without j
			StringBuffer alphabet = new StringBuffer("abcdefghiklmnopqrstuvwxyz");
			int[] moddedAscii = new int[25];

			ArrayList<Character> used = new ArrayList<Character>();
			for(int i = 0; i < keyword.length(); i++) {
				char x = keyword.charAt(i);
				if(x == 'j')
					x = 'i';
				boolean b = true;
				for(char c : used) {
					if(c == x) {
						keyword = keyword.substring(0, i) + keyword.substring(i+1);
						b = false;
						i--;
					}
				}
				if(b)
					used.add(x);
			}


			Collections.sort(used);

			for(int i = used.size()-1; i >=0; i--) {
				char c = used.get(i);
				int offset = 0;
				if((int)c  > (int)'j')
					offset = -1;
				int index = c - 97 + offset;
				alphabet.delete(index, index + 1);
			}
			code.append(keyword);
			code.append(alphabet);
			//			System.out.println(code);
			//			for(int i = 0; i < 25; i++) { //J IS 10th letter
			//				int jMod = 0;
			//				if(i>=9)
			//					jMod = 1;
			//				if(i != code.charAt(i) - 97 + jMod) {
			//					moddedAscii[i] = code.charAt(i);
			//				} else {
			//					moddedAscii[i] = i;
			//				}
			//					
			//			}

			for(int i = 0; i < 25; i++) {
				int jMod = 0;
				if(i>=9)
					jMod = 1;
				moddedAscii[i] = code.indexOf(""+(char)(97 + i + jMod));
			}

			int ind = 0;
			ArrayList<Integer> printInd = new ArrayList<Integer>(124456);

			String line = "";

			while(line != null){
				line = bReader.readLine();
				//				lineNum++;
				//				System.out.println(lineNum);
				//				String line = scan.nextLine();
				if(line == null)
					break;
				output.append(line);
				printInd.add(output.length());

				//The issue is that this is at the wrong place so it still cuts off.
				boolean ended = false;

				//ENCRYPT THE LINE
				for(int i = ind; i < output.length(); i++) {
					char c = output.charAt(i);
					int ind1 = ind, ind2 = ind;
					char c2;
					int z = i;
					while(!Character.isLetter(c)) {
						i++;
						z = i;
						if(i >= output.length()) {
							ended = true;
							break;
						}
						c = output.charAt(i);
					}
					ind1 = i;
					if(ended) {
						break;
					}
					i++;
					if(i >= output.length()) {
						ind = ind1;
						break;
					}
					c2 = output.charAt(i);
					while(!Character.isLetter(c2)) {
						i++;
						if(i >= output.length()) {
							ended = true;
							break;
						}
						c2 = output.charAt(i);
					}
					ind2 = i;
					if(ended) {
						ind = ind1;
						break;
					}

					if(c == 'j')
						c = 'i';
					if(c2 == 'j')
						c2 = 'i';

					boolean upper1 = Character.isUpperCase(c), upper2 = Character.isUpperCase(c2);

					char cCop = Character.toLowerCase(c);
					char c2Cop = Character.toLowerCase(c2);

					//Characters are not the same so they must be changed
					if(cCop != c2Cop) {
						int jMod = 0;
						if(cCop >= 'j')
							jMod = 1;
						int code1 = moddedAscii[cCop-97-jMod];
						jMod = 0;
						if(c2Cop >= 'j') {
							jMod = 1;
						}
						int code2 = moddedAscii[c2Cop-97-jMod];

						char new1, new2;
						if(code1 % 5 == code2 % 5) {
							new2 = code.charAt(code1);
							new1 = code.charAt(code2);
						}
						else {
							new1 = code.charAt(code1 + (code2%5 - code1%5));
							new2 = code.charAt(code2 + (code1%5 - code2%5));
						}

						if(upper1)
							new1 = Character.toUpperCase(new1);
						if(upper2)
							new2 = Character.toUpperCase(new2);
						output.setCharAt(ind1, new1);
						output.setCharAt(ind2, new2);
					}

					ind = ind2 + 1;

				}
				
				//Remember to set ind
//				char fChar = ' ';
//				for(int i = output.length()-1; i >= 0; i--) {
//					if(Character.isLetter(output.charAt(i))) {
//						fChar = output.charAt(i);
//						break;
//					}
//				}

//				if(!ended) {
//					bWriter.write(output.substring(0, printInd.get(0)).toString());
//					bWriter.write(LINE_SEPARATOR);
//					for(int i = 0; i < printInd.size(); i++) {
//						if(i + 1 < printInd.size()) {
//							bWriter.write(output.substring(printInd.get(i), printInd.get(i+1)).toString());
//							System.out.println((output.substring(printInd.get(i), printInd.get(i+1))));
//						}
//						else {
//							bWriter.write(output.substring(printInd.get(i)).toString());
//						}
//						bWriter.write(LINE_SEPARATOR);
//						printInd.remove(i);
//					}
//					printInd.clear();
//				}
				
			}

			if(printInd.size() > 0) {
				bWriter.write(output.substring(0, printInd.get(0)).toString());
				bWriter.write(LINE_SEPARATOR);
				for(int i = 0; i < printInd.size(); i++) {
					if(i + 1 < printInd.size()) {
						bWriter.write(output.substring(printInd.get(i), printInd.get(i+1)).toString());
						bWriter.write(LINE_SEPARATOR);
					}
					else {
						bWriter.write(output.substring(printInd.get(i)).toString());
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
//			if(scan != null)
//				scan.close();
			if(writer != null) {
				try {
					bWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}


	/**
	 * Reads from the (previously encrypted) file specified, and writes out a decrypted version of the file. 
	 * If the output file already exists, overwrite it.
	 * 
	 * @param inputFilename The path of the encrypted file.
	 * @param outputFilename The path of the decrypted file to be saved.
	 * @param keyword The keyword to be used in the decryption algorithm.
	 * 
	 */
	public void decrypt(String inputFilename, String outputFilename, String keyword) 
	{
		encrypt(inputFilename, outputFilename, keyword);
	}

}
