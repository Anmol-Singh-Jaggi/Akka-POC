package com.anmol.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtil {
	private final static Logger logger = LoggerFactory.getLogger(IOUtil.class);
	public final static String classPathPrefix = "classpath://";

	/**
	 * Read a file given its location in the classpath
	 * 
	 * @param filePath The relative path of the file with respect to the root of the
	 *                 classpath.
	 * @return The file contents, or null if an error occurs.
	 * @throws IOException If there is an error reading the file.
	 */
	private static String readFileFromClassPath(String filePath) throws IOException {
		String fileContent = null;
		try (InputStream inputStream = IOUtil.class.getResourceAsStream(filePath)) {
			fileContent = readFromInputStream(inputStream);
		} catch (Exception e) {
			throw e;
		}
		return fileContent;
	}

	private static String readFileFromAbsolutePath(String pathString) {
		String fileContentString = null;
		try {
			List<String> FileContentLines = Files.readAllLines(Paths.get(pathString), Charset.defaultCharset());
			fileContentString = String.join("\n", FileContentLines);
		} catch (IOException e) {
			String logMsg = String.format("Error in reading from absolute path '%s'", pathString);
			logger.error(logMsg, e);
		}
		return fileContentString;
	}

	public static String getFileContents(String filePath) throws IOException {
		String fileContents = null;
		if (filePath.startsWith(classPathPrefix)) {
			filePath = filePath.substring(classPathPrefix.length());
			fileContents = IOUtil.readFileFromClassPath(filePath);
		} else {
			fileContents = IOUtil.readFileFromAbsolutePath(filePath);
		}
		return fileContents;
	}

	/**
	 * Returns the contents of an InputStream.
	 * 
	 * @param inputStream The InputStream to read.
	 * @return The contents of the InputStream.
	 * @throws IOException If there is an error reading the file.
	 */
	private static String readFromInputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		String content = result.toString("UTF-8");
		return content;
	}

	/**
	 * Creates and writes contents to file. Creates all the necessary directories
	 * mentioned in the destination file path string if not present.
	 * 
	 * @param fileContents   The contents to write to file.
	 * @param filePathString The file path.
	 * @throws IOException If there is any IO error.
	 */
	public static void writeToFile(String fileContents, String filePathString) throws IOException {
		Path filePath = Paths.get(filePathString);
		filePath = filePath.normalize().toAbsolutePath();
		File file = filePath.toFile();

		// String logMsg = String.format("File will be created at the location
		// '%s'", file.getCanonicalPath());

		Path filePathParentDir = filePath.getParent();
		Files.createDirectories(filePathParentDir);
		try (PrintWriter writer = new PrintWriter(file)) {
			writer.print(fileContents);
			writer.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Returns the location of the currently executing code.
	 * 
	 * @return A string representing path of the current running executable.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static String getCurrentExecutablePath() throws URISyntaxException, IOException {
		URI uri = IOUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI();
		File file = new File(uri);
		file = file.getParentFile();
		String path = file.getCanonicalPath();
		return path;
	}

	public static Properties getPropertiesFromFileInClasspath(String filePath) {
		Properties configProps = new Properties();
		try (InputStream inputStream = IOUtil.class.getClassLoader().getResourceAsStream(filePath)) {
			configProps.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			String logMsg = "Error while loading properties from file : '%s'";
			logger.info(String.format(logMsg, e.getMessage()));
		}
		return configProps;
	}

	public static void copyFile(String sourceFilePath, String destFilePath) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(new File(sourceFilePath));
			os = new FileOutputStream(new File(destFilePath));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}
}
