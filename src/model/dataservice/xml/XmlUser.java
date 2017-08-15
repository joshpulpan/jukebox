package model.dataservice.xml;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import model.dataservice.User;

/**
 * An implementation of a User that reads information from an XML file. A
 * user looks like this:
 * <user id="...">
 * 	<name>...</name>
 * 	<password></password>
 * </user>
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
@XmlRootElement(name = "user")
public class XmlUser implements User {
	@XmlAttribute
	private UUID id;
	@XmlElement
	private String name;
	@XmlElement
	private String password;

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
