package com.naughtyzombie.inmemds;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class FileUtil {

	/**
	 *@param  fileName
	 *@return File
	 *@exception  IOException
	 */
	public static File findFile(String fileName) {
		File file = null;
		URL fileURL = FileUtil.class.getClass().getResource(fileName);
		if (fileURL != null) {
			try {
				file = new File(new URI(fileURL.toString()));
			} catch (Exception e) {
				System.err.println("Could not convert to URI:" + fileURL);
				e.printStackTrace();
			}
		}
		if (file == null) {
			file = new File(fileName);
		}

		return file;
	}

	/**
	 *@param  fileName
	 *@return File
	 *@exception  IOException
	 */
	public static File getFile(String fileName) throws IOException {
		File file = findFile(fileName);

		if (file == null || !file.exists()) {
			throw new IOException("Could not find file:'" + fileName
					+ "' as a resource or an absolute path");
		}

		return file;
	}

	/**
	 * @param inputFile
	 * @exception FileNotFoundException
	 *                Description of the Exception
	 * @exception IOException
	 *                Description of the Exception
	 */
	public static void copy(String inputFile, String outputFile)
			throws FileNotFoundException, IOException, Exception {
		File fIn = new File(inputFile);
		FileReader in = new FileReader(fIn);
		File fOut = new File(outputFile);
		FileWriter out = new FileWriter(fOut);

		try {
			char[] buff = new char[512];
			int count = 0;

			while ((count = in.read(buff, 0, 512)) > 0) {
				out.write(buff, 0, count);
			}
		} finally {
			in.close();
			out.close();
		}
	}


    public static Properties loadProperties() {
        Properties props = new Properties();
        URL url = ClassLoader.getSystemResource("inmemds.properties");

        try {
            props.load(url.openStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return props;
    }

    public static void writeFile(String fileName, String outputDir, String outputString) throws IOException {

        if(outputDir != null && outputDir.trim().length() > 0) {
            File dir = new File(outputDir);
            if (!dir.exists()) dir.mkdir();

            File outputFile = new File(dir,fileName);

            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

            bw.write(outputString);

            bw.flush();
            bw.close();
        }
    }

    public static void concatenateTextFiles(List<File> files, File outputFile) throws IOException {
        OutputStream out = new FileOutputStream(outputFile);
        byte[] buf = new byte[1 << 20];
        for (File file : files) {
            InputStream in = new FileInputStream(file);
            int b = 0;
            while ( (b = in.read(buf)) >= 0) {
                out.write(buf, 0, b);
                out.flush();
            }
        }
    }
}
