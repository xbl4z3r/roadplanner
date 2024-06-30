package me.xbl4z3r.roadplanner.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ExportPanel extends JPanel {

	public final JTextArea field = new JTextArea();

	public ExportPanel() {
		field.setText("Export");
		this.setOpaque(true);
		this.setBackground(Color.darkGray.darker());
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setMinimumSize(new Dimension(200, 10));
		JScrollPane scroll = new JScrollPane(field);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JButton copy = new JButton("Copy to clipboard");
		this.add(copy);
		this.add(scroll);
		this.setVisible(true);
		copy.addActionListener(e -> {
			StringSelection selection = new StringSelection(field.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}
}
