package com.alteredmechanism.notepad;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

/**
 * @author Bill Chatfield
 */
public class Notepad extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private AntiAliasedJTextArea textArea = new AntiAliasedJTextArea(24, 80);
	private JScrollPane textScrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JMenuBar menuBar = new JMenuBar();
	private JMenu file = new JMenu("File");
	private JMenuItem openMenuItem = new JMenuItem("Open");
	private JMenuItem saveFile = new JMenuItem("Save");
	private JMenuItem close = new JMenuItem("Close");

	public Notepad() {
		super("Notepad");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, pointToPixel(12)));

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(textScrollPane);

		// add our menu bar into the GUI
		this.setJMenuBar(this.menuBar);
		file.setMnemonic(KeyEvent.VK_F);
		menuBar.add(file);
		openMenuItem.addActionListener(this);
		openMenuItem.setMnemonic(KeyEvent.VK_O);
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		file.add(openMenuItem);
		saveFile.addActionListener(this);
		saveFile.setMnemonic(KeyEvent.VK_S);
		saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		file.add(saveFile);
		close.setMnemonic(KeyEvent.VK_Q);
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.CTRL_DOWN_MASK));
		close.addActionListener(this);
		file.add(close);
		pack();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.close) {
			this.dispose();
		} else if (e.getSource() == this.openMenuItem) {
			JFileChooser fileDialogOpen = new JFileChooser();
			fileDialogOpen.setDialogTitle("Choose a file to open");
			fileDialogOpen.showOpenDialog(this);
			File selectedFile = fileDialogOpen.getSelectedFile();
			if (selectedFile != null) {
				textArea.setText("");
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(selectedFile));
					String line = null;
					while ((line = reader.readLine()) != null) {
						textArea.append(line + "\n");
					}
					this.setTitle(selectedFile.getName());
				} catch (Exception ex) {
					new MessageBox(this, "Error", ex.getMessage());
				} finally {
					close(reader);
				}
			}
		} else if (e.getSource() == this.saveFile) {
			JFileChooser save = new JFileChooser();
			int option = save.showSaveDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				BufferedWriter out = null;
				try {
					out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
					out.write(textArea.getText());
				} catch (Exception ex) {
					new MessageBox(this, ex.getLocalizedMessage());
					ex.printStackTrace();
				} finally {
					close(out);
				}
			}
		}
	}

	private void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
				new MessageBox(this, "Error", e.getLocalizedMessage());
			}
		}
	}

	private void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (Exception e) {
				new MessageBox(this, "Error", e.getLocalizedMessage());
			}
		}
	}

	public int pointToPixel(float pointSize) {
		// Pixel density is called PPI (pixels per inch) but the
		// equation is:
		// 		PPI pixels = 1 inch
		// so:
		//		PPI = 1 inch / 1 pixel
		// that is to say:
		//		1 pixel divides into 1 inch PPI times.
		float pixelDensity = Toolkit.getDefaultToolkit().getScreenResolution();
		int pixelSize = (int) Math.round(pointSize * pixelDensity / 72f);
		return pixelSize;
	}

	public static void main(String args[]) {
		try {
			SystemPropertyConfigurator.autoConfigure();
			Notepad app = new Notepad();
			app.setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
