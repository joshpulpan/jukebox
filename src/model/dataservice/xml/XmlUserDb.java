package model.dataservice.xml;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import model.dataservice.User;
import model.dataservice.UserDb;

/**
 * An implementation of a UserDb that reads information from an XML file. An
 * example file looks like:
 * <user>
 * 	<user ...> ... </user>
 * 	<user ...> ... </user>
 * 	...
 * </users>
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
@XmlRootElement(name = "users")
public class XmlUserDb implements UserDb {
	@XmlElement(name = "user", type = XmlUser.class)
	private List<User> users;
	
	private XmlUserDb() {
		// disallow object creation outside of JAXB
	}

	@Override
	public User getUserById(UUID id) {
		Optional<User> user = this.users.stream().filter(u -> u.getId().equals(id)).findFirst();
		return user.isPresent() ? user.get() : null;
	}

	@Override
	public User getUserByName(String name) {
		Optional<User> user = this.users.stream().filter(u -> u.getName().equals(name)).findFirst();
		return user.isPresent() ? user.get() : null;
	}
}
