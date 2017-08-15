package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import controller.JukeboxController;
import model.CachedUser;
import model.dataservice.Song;
import model.dataservice.SongDb;

/**
 * A simple view for the Jukebox, showing the currently logged in user (and
 * options for logging in and out) along with the current playlist. The
 * controller should bind this to the SongQueue to listen for changes there.
 * 
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class JukeboxView extends JFrame implements Observer {
	private static final long serialVersionUID = 5916141608303483336L;

	private JButton login;
	private JLabel status;
	private JList<Song> queue;
	private JTable library;

	/**
	 * Display a message to the user. In this case, we use Swing dialog boxes
	 * for this purpose.
	 * 
	 * @param message
	 *            the message to show the user
	 */
	public static void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	/**
	 * Prompt the user to read saved data.
	 * 
	 * @return whether to read the data or not
	 */
	public static boolean promptRead() {
		int read = JOptionPane.showConfirmDialog(null, "Would you like to read the saved state?", "Read data",
				JOptionPane.YES_NO_OPTION);
		return read == JOptionPane.YES_OPTION ? true : false;
	}

	/**
	 * Prompt the user to write save data.
	 * 
	 * @return whether to write the data or not
	 */
	public static boolean promptWrite() {
		int write = JOptionPane.showConfirmDialog(null, "Would you like to save the current state?", "Save data",
				JOptionPane.YES_NO_OPTION);
		return write == 0 ? true : false;
	}

	/**
	 * Set up the Jukebox view and display that no user is logged in.
	 * 
	 * @param songDb
	 *            the song database for use as a library
	 */
	public JukeboxView(SongDb songDb) {
		setTitle("Jukebox");
		setBounds(100, 100, 600, 400);
		setMinimumSize(new Dimension(600, 400));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		this.login = new JButton();
		this.status = new JLabel();

		signOut();

		this.queue = new JList<>();
		JScrollPane queueScroll = new JScrollPane(this.queue);

		SongTable songTable = new SongTable(songDb);
		TableRowSorter<SongTable> librarySorter = new TableRowSorter<>(songTable);
		this.library = new JTable(songTable);
		this.library.setRowSorter(librarySorter);
		this.library.setPreferredScrollableViewportSize(this.library.getMinimumSize());
		this.library.setMaximumSize(this.library.getPreferredScrollableViewportSize());
		JScrollPane libraryScroll = new JScrollPane(this.library);

		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.library.setSelectionModel(listSelectionModel);

		JPanel user = new JPanel();
		user.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
		user.setLayout(new BoxLayout(user, BoxLayout.LINE_AXIS));
		user.add(this.login);
		user.add(Box.createHorizontalGlue());
		user.add(this.status);

		JPanel player = new JPanel(new BorderLayout());
		player.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		player.add(queueScroll);
		player.add(libraryScroll, BorderLayout.SOUTH);

		add(user);
		add(player);
	}

	/**
	 * Bind this view to a controller. Sets up ActionListeners and a
	 * MouseListener and WindowListener.
	 * 
	 * @param controller
	 *            the controller to bind to this view
	 */
	public void bind(JukeboxController controller) {
		this.queue.setModel(controller.addModelListener(this));
		addWindowListener(controller.getWindowAdapter());
		this.library.addMouseListener(controller.getMouseAdapter());
		this.login.addActionListener(controller);

		// set up enter key to select songs
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		this.library.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "select");
		this.library.getActionMap().put("select", new SelectAction(controller));
	}

	@Override
	public void update(Observable model, Object user) {
		if (user instanceof CachedUser) {
			this.login.setText("Sign out");
			this.status.setText(formatStatus((CachedUser) user));
		} else {
			signOut();
		}
	}

	protected JTable getLibrary() {
		return this.library;
	}

	private static String formatStatus(CachedUser user) {
		StringBuilder status = new StringBuilder();
		status.append("Logged in as ");
		status.append(user.getName());
		status.append(". Played ");
		status.append(user.getPlays());
		status.append(". Remaining time: ");
		long time = user.getAllottedTime().getSeconds();
		Long hours = Long.valueOf(time / 3600);
		Long mins = Long.valueOf((time % 3600) / 60);
		Long secs = Long.valueOf(time % 60);
		status.append(String.format("%d:%02d:%02d", hours, mins, secs));
		return status.toString();
	}

	private void signOut() {
		this.login.setText("Login");
		this.status.setText("Not logged in.");
	}

	private class SelectAction extends AbstractAction {
		private static final long serialVersionUID = 7703926469306001909L;

		private ActionListener listener;

		public SelectAction(ActionListener listener) {
			this.listener = listener;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTable library = JukeboxView.this.getLibrary();
			int row = library.convertRowIndexToModel(library.getSelectedRow());
			UUID song = ((SongTable) library.getModel()).getIdAt(row);
			this.listener
					.actionPerformed(new ActionEvent(JukeboxView.this, ActionEvent.ACTION_PERFORMED, song.toString()));
		}
	}
}
