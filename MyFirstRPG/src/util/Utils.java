package util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class contains utility tools for the game clients
 * 
 * @author Samuel Klein
 */
public class Utils {

	/**
	 * Cast a string representation of a number into an integer object
	 * 
	 * @param number
	 * @return
	 */
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Read a file and get the string representation of it
	 * 
	 * @param path
	 * @return
	 */
	public static String loadFileAsString(String path) {
		StringBuilder builder = new StringBuilder();

		FileReader file = null;

		try {
			file = new FileReader(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (file != null) {
			try {
				BufferedReader br = new BufferedReader(file);
				String line = null;
				while ((line = br.readLine()) != null) {
					builder.append(line + "\n");
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return builder.toString();
	}

	public static byte[] extractBytes(String ImageName) {

		try {

			// open image
			File imgPath = new File(ImageName);
			BufferedImage bufferedImage = ImageIO.read(imgPath);

			// get DataBufferBytes from Raster
			WritableRaster raster = bufferedImage.getRaster();
			DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

			return data.getData();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean containsBlock(int[][] touched) {
		for (int j = 0; j < touched.length; j++) {
			for (int i = 0; i < touched[j].length; i++) {
				if (touched[j][i] > 65535)
					return true;
			}
		}
		return false;
	}

}
