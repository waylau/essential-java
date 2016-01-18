/**
 * 
 */
package com.waylau.essentialjava.io.characterstream;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2016年1月18日
 */
public class CopyCharacters {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
        FileReader inputStream = null;
        FileWriter outputStream = null;

        try {
            inputStream = new FileReader("resources/xanadu.txt");
            outputStream = new FileWriter("resources/characteroutput.txt");

            int c;
            while ((c = inputStream.read()) != -1) {
                outputStream.write(c);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
	}
}
