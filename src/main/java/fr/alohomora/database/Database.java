package fr.alohomora.database;

import fr.alohomora.Configuration;
import fr.alohomora.model.Config;
import fr.alohomora.model.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Statement createQuery() throws SQLException {
		return this.con.createStatement();
	}

	public void close() {
		if (this.con != null){
			try {
				this.con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public static Database getInstance(){
		if (Database._INSTANCE == null)
			Database._INSTANCE = new Database();
		return Database._INSTANCE;
	}

}
