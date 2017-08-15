package tests.dataservice.mock;

import java.util.UUID;

import model.dataservice.User;

/**
 * A mock user class for testing.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class MockUser implements User {
	private UUID id;
	private String name;
	private String password;

	/**
	 * Creates a new mock user.
	 * 
	 * @param id the id of the user
	 * @param name the name of the user
	 * @param password the password of the user
	 */
	public MockUser(UUID id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getPassword() {
		return this.password;
	}
}
