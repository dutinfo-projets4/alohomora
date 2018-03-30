import fr.alohomora.Configuration;
import fr.alohomora.database.Database;
import fr.alohomora.model.Config;
import fr.alohomora.model.Data;
import fr.alohomora.model.Element;
import fr.alohomora.model.Group;
import org.junit.Before;
import org.junit.Test;

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
public class DatabaseTest {
	@Before
	public void init(){
		Configuration.load(new String[]{});

	}

	@Test
	public void insertConfig(){
		Database.getInstance().insertConfig("test","test", true);
	}

	@Test
	public void getDataFromBd(){
		Data data = Database.getInstance().getData();

		for(Group group : data.getGroups()){
			System.out.println(group.getContent());
		}
		for(Element element : data.getElements()){
			System.out.println(element.getContent());
		}
	}
}
