package me.xbl4z3r.roadplanner.panels;

import me.xbl4z3r.roadplanner.Main;
import me.xbl4z3r.roadplanner.utils.SettingsProvider;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {

	public SettingsPanel settingsPanel;
	public EditPanel editPanel;
	public MarkerPanel markerPanel;

	public InfoPanel(Main main, SettingsProvider props) {
		this.setOpaque(true);
		this.settingsPanel = new SettingsPanel(main, props);
		this.editPanel = new EditPanel(main);
		this.markerPanel = new MarkerPanel(main);
		this.setBackground(Color.darkGray.darker());
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(editPanel, BorderLayout.NORTH);
		this.add(markerPanel, BorderLayout.NORTH);
		markerPanel.setVisible(false);
		this.add(settingsPanel, BorderLayout.SOUTH);
		this.setVisible(true);
	}

	public void changePanel(boolean marker) {
		if (!markerPanel.isVisible() && marker) {
			markerPanel.updateText();
			markerPanel.setVisible(true);
			editPanel.setVisible(false);
		} else if (!editPanel.isVisible() && !marker) {
			editPanel.updateText();
			editPanel.setVisible(true);
			markerPanel.setVisible(false);
		}
	}
}
