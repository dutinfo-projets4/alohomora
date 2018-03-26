package fr.alohomora.database;

import fr.alohomora.Configuration;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

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
public class Database {

	private static Database _INSTANCE;

	private Connection con;

	private Database() {
		try {
			this.con = DriverManager.getConnection("jdbc:sqlite:" + Configuration.DB_FILE.getAbsolutePath());
			if (this.checkTable())
				this.loadSqlScript();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Statement createQuery() throws SQLException {
		return this.con.createStatement();
	}

	public void close() {
		if (this.con != null) {
			try {
				this.con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public static Database getInstance() {
		if (Database._INSTANCE == null)
			Database._INSTANCE = new Database();
		return Database._INSTANCE;
	}

	/**
	 * load the the script's table of the database from the ressources folders
	 */
	public void loadSqlScript() {
		StringBuilder sqlScript = new StringBuilder();
		Statement st = null;
		try {
			st = this.con.createStatement();
			ClassLoader classLoader = getClass().getClassLoader();
			File filesql = new File(classLoader.getResource("assets/sql/db_generation.sql").getFile());
			Scanner scanner = new Scanner(filesql);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				sqlScript.append(line).append("\n");
			}
			scanner.close();
			for (String e : sqlScript.toString().split(";")) {
				st.execute(e);
			}

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * check if the table exist true if the table not exist
	 *
	 * @return boolean
	 */
	public boolean checkTable() {
		int nbTable = 0;
		try {
			Statement st = this.con.createStatement();
			ResultSet rs = st.executeQuery("SELECT COUNT(name) FROM sqlite_master;");
			while (rs.next()) {
				nbTable = rs.getInt("COUNT(name)");
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nbTable == 0 ? true : false;
	}

	public boolean insertToken(String username, String token){
		boolean res = false;
		try{
			Statement st = this.con.createStatement();
			PreparedStatement prepStmt = this.con.prepareStatement("INSERT INTO token (username, value) VALUES ( ?, ?)");
			prepStmt.setString(1, username);
			prepStmt.setString(2, token);
			res = prepStmt.execute();
		}catch (Exception e){
			e.printStackTrace();
		}
		return res;
	}
}
