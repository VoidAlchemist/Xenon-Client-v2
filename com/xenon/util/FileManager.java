package com.xenon.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.google.gson.Gson;
import com.xenon.XenonClient;
import com.xenon.modules.ModSettings;
import com.xenon.util.readability.Static;
/**
 * Now use java.nio & Gson instead of java.io.
 * @since 2.0
 * @author VoidAlchemist
 *
 */
@Static
public class FileManager {

	public static Path root_dir, main_conf, logger;
	private static Gson gson;
	/**
	 * Must run this method before calling anything else in this class.
	 * @see com.xenon.XenonClient#preInit()
	 * @since 1.0
	 */
	public static void init()
	{
		root_dir = Paths.get("XenonClient");
		main_conf = Paths.get(root_dir.toAbsolutePath().toString(), "main_conf.txt");
		gson = new Gson();
		logger = Paths.get(Paths.get("logs").toAbsolutePath().toString(), "xenon.log");
		
		try {
			if (!Files.exists(root_dir))
				Files.createDirectory(root_dir);
			
			if (!Files.exists(main_conf))
				Files.createFile(main_conf);
			
			if (!Files.exists(logger))
				Files.createFile(logger);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Cannot launch Client without configuration files.");
		}
		XenonClient.instance.printer.info("conf files initialized.");
	}
	
	
	public static void writeToJson(Path path, Object o) {
		try {
			Files.write(path, gson.toJson(o).getBytes(Charset.forName("UTF-8")));	//UTF-8 is REALLY important here.
			//was getting errors because of UTF-8 reads on ANSI written files.
		}catch(IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Couldn't write into configuration files.");
		}
	}
	
	public static <T> T readFromJson(Path path, Class<T> clazz) {
		try {
			
			StringBuilder builder = new StringBuilder();
			for (String line : Files.readAllLines(path)) {
				builder.append(line);
			}
			if (builder == null || builder.length() < 1)	return null;
			
			return gson.fromJson(builder.toString(), clazz);
		}catch(IOException e) {
			XenonClient.instance.printer.error("Couldn't read Xenon modules configuration files.");
			return null;
		}
	}
	
	public static void log(String msg) {
		try {
			Files.write(logger, (">>[Xenon] "+msg+"\n").getBytes(), StandardOpenOption.APPEND);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static ModSettings instanciateModSettings() {
		ModSettings result = readFromJson(main_conf, ModSettings.class);
		return result == null ? new ModSettings() : result;
	}
}
