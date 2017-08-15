package tests.dataservice.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import model.dataservice.User;
import model.dataservice.UserDb;

/**
 * A mock user database for testing.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public final class MockUserDb implements UserDb {
	private static final MockUserDb INSTANCE = new MockUserDb();

	private List<User> users;

	private MockUserDb() {
		this.users = new ArrayList<>();
		this.users.add(new MockUser(UUID.fromString("00000000-0000-0000-0000-000000000001"), "MockUser1", "MockPassword1"));
		this.users.add(new MockUser(UUID.fromString("00000000-0000-0000-0000-000000000002"), "MockUser2", "MockPassword2"));
		this.users.add(new MockUser(UUID.fromString("00000000-0000-0000-0000-000000000003"), "MockUser3", "MockPassword3"));
	}

	/**
	 * Get the instance of the MockUserDb. Enforces the singleton property.
	 * 
	 * @return the instance of the MockUserDb
	 */
	public static UserDb getInstance() {
		return INSTANCE;
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
