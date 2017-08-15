package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import model.JukeboxModel;

/**
 * A simple login dialog for display on request. This class is implemented as a
 * combination controller and view since splitting those up would cause needless
 * complexity.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class LoginView extends JFrame {
	private static final long serialVersionUID = -141453432848140962L;

	private final JTextField name;
	private final JPasswordField pass;
	private final JButton login;
	private final JButton cancel;

	private final LoginController controller;

	/**
	 * Create a new LoginView.
	 */
	public LoginView() {
		setTitle("Login");
		setBounds(150, 150, 350, 200);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 2, 5, 2);

		JLabel userPrompt = new JLabel("Account Name");
		JLabel passPrompt = new JLabel("Password");
		this.name = new JTextField(16);
		this.pass = new JPasswordField(16);
		this.login = new JButton("Login");
		this.cancel = new JButton("Cancel");

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		add(userPrompt, c);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		add(this.name, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		add(passPrompt, c);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		add(this.pass, c);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		add(this.cancel, c);
		c.gridx = 2;
		c.gridy = 2;
		add(this.login, c);

		this.controller = new LoginController(this.name, this.pass);

		this.login.addActionListener(this.controller);
		this.cancel.addActionListener(this.controller);

		getRootPane().setDefaultButton(this.login);
	}

	/**
	 * Bind the controller to the model.
	 * 
	 * @param model
	 *            the model to bind to the controller
	 */
	public void bind(JukeboxModel model) {
		this.controller.bind(model);
	}

	/**
	 * This method simply clears the input fields. This is useful because the
	 * alternatives are setting up a window listener or creating a new LoginView
	 * for each login attempt instead of having it as a (sometimes hidden)
	 * static singleton.
	 */
	public void clear() {
		this.name.setText(null);
		this.pass.setText(null);
		this.name.requestFocusInWindow();
	}

	private class LoginController implements ActionListener {
		private final JTextField name;
		private final JPasswordField pass;
		private JukeboxModel model;

		public LoginController(JTextField name, JPasswordField pass) {
			this.name = name;
			this.pass = pass;
		}

		public void bind(JukeboxModel model) {
			this.model = model;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (actionEvent.getActionCommand().equals("Login")) {
				if (this.model.authUser(this.name.getText(), String.valueOf(this.pass.getPassword()))) {
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Your username or password were not recognized.",
							"Invalid login", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				setVisible(false);
			}
		}
	}
}
