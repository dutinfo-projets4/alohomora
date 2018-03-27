package fr.alohomora;

import fr.alohomora.database.Database;
import fr.alohomora.model.Config;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;

/**
 * Alohomora Password Manager
 * Copyright (C) 2018 Team Alohomora
 * Léo BERGEROT, Sylvain COMBRAQUE, Sarah LAMOTTE, Nathan JANCZEWSKI
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 **/
public class Configuration {

	public static String SOFTWARE_NAME = "Alohomora";
	public static String LOGIN_TOKEN;
	public static String BASE_PATH;
	public static boolean PORTABLE = false;
	public static File KEYS_FOLDER, DB_FILE;

	/**
	 * Fired up when the software starts up
	 * Sets the config up
	 */
	public static void load(String[] arguments) {
		for (String s : arguments){
			if (s.equalsIgnoreCase("-portable")){
				Configuration.PORTABLE = true;
			}
		}

		Configuration.KEYS_FOLDER = Configuration.createFolder("keys");
		Configuration.DB_FILE	  = Configuration.createFile("alohomora.db");
		Configuration.BASE_PATH = "http://localhost:8000/";
	}

	/**
	 * Create an instance of File for the given folder
	 * Making it if it doesn't exists already
	 */
	private static File createFolder(String name){
		File f = new File(Configuration.getWorkingDirectory(), name);
		if (!f.exists())
			f.mkdirs();
		return f;
	}

	private static File createFile(String name){
		File f  = new File(Configuration.getWorkingDirectory(), name);
		if(!f.exists()){
			try {
				 f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}


	/**
	 * Returns the software's folder
	 * Should be use as a relative path everytime we need to store something
	 */
	public static File getWorkingDirectory(){
		File f = null;
		if (Configuration.PORTABLE)
			f = new File(Configuration.SOFTWARE_NAME.toLowerCase());

		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			System.out.println("APPDATA");
		} else if (os.indexOf("mac") >= 0) {
			System.out.println("Application local machin");
			String home = System.getProperty("user.home") + "/Library/Application Support";
			f = new File(home, "alohomora");
		} else {
			String home = System.getProperty( "user.home" );
			f = new File(home, ".config/alohomora");
		}

		if (!f.exists())
			f.mkdirs();

		return f;
	}


}
