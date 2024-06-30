package me.xbl4z3r.roadplanner.utils;

import java.io.*;
import java.util.Properties;

public class SettingsProvider {

	public enum BackgroundType {CENTERSTAGE,POWERPLAY}

	public double robotWidth;
	public double robotLength;
	public double resolution;
	public String importPath;
	public double trackWidth;
	public double maxVelo;
	public double maxAccel;
	public double maxAngVelo;
	public double maxAngAccel;
	public BackgroundType background;
	public Properties prop;
	private final File file;

	public SettingsProvider(File file) {
		this.file = file;
		if (!file.exists()) generateFile(file);
		prop = new Properties();
		readFile(file);
		reload();
	}

	public void readFile(File file) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			prop.load(stream);
		} catch (Exception e) {
			Logger.error("Failed to read properties file", e);
		} finally {
			if (null != stream) {
				try {
					stream.close();
				} catch (Exception e) {
					Logger.error("Failed to close properties file", e);
				}
			}
		}
	}

	public void reload() {
		try {
			robotLength = Double.parseDouble(prop.getProperty("ROBOT_LENGTH"));
			robotWidth = Double.parseDouble(prop.getProperty("ROBOT_WIDTH"));
			resolution = Double.parseDouble(prop.getProperty("RESOLUTION"));
			importPath = prop.getProperty("IMPORT/EXPORT");
			trackWidth = Double.parseDouble(prop.getProperty("TRACK_WIDTH"));
			maxVelo = Double.parseDouble(prop.getProperty("MAX_VELO"));
			maxAccel = Double.parseDouble(prop.getProperty("MAX_ACCEL"));
			maxAngVelo = Double.parseDouble(prop.getProperty("MAX_ANGULAR_VELO"));
			maxAngAccel = Double.parseDouble(prop.getProperty("MAX_ANGULAR_ACCEL"));
			background = BackgroundType.valueOf(prop.getProperty("BACKGROUND"));
		} catch (NullPointerException e) {
			prop = new Properties();
			generateFile(file);
		}
	}

	private void generateFile(File file) {
		try {
			file.getParentFile().mkdir();
			file.createNewFile();
			FileWriter writer = new FileWriter(file, false);
			writer.write(
					"ROBOT_WIDTH=18\n" +
							"ROBOT_LENGTH=18\n" +
							"RESOLUTION=0.1\n" +
							"IMPORT/EXPORT=\n" +
							"TRACK_WIDTH=15\n" +
							"MAX_VELO=60\n" +
							"MAX_ACCEL=60\n" +
							"MAX_ANGULAR_VELO=60\n" +
							"MAX_ANGULAR_ACCEL=60\n" +
							"BACKGROUND=CENTERSTAGE"
			);
			writer.close();
			readFile(file);
		} catch (IOException e) {
			Logger.error("Failed to generate properties file", e);
		}
	}

	public void save() {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			prop.store(out, "V1.3");
		} catch (Exception e) {
			Logger.error("Failed to save properties file", e);
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (Exception e) {
					Logger.error("Failed to close properties file", e);
				}
			}
		}
	}
}
