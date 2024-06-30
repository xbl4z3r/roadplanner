package me.xbl4z3r.roadplanner.panels;

import me.xbl4z3r.roadplanner.Main;
import me.xbl4z3r.roadplanner.utils.SettingsProvider;
import me.xbl4z3r.roadplanner.utils.SpringUtilities;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Objects;

public class SettingsPanel extends JPanel {
	NumberFormat format = NumberFormat.getInstance();
	NumberFormatter formatter = new NumberFormatter(format);
	private final Main main;
	private final LinkedList<JTextField> fields = new LinkedList<>();
	private final String[] labels = {"Robot Width", "Robot Length", "Resolution", "Import/Export", "Track Width", "Max Velo", "Max Accel", "Max Angular Velo", "Max Angular Accel", "Background"};
	private final SettingsProvider robot;

	SettingsPanel(Main main, SettingsProvider properties) {
		this.robot = properties;
		this.main = main;
		this.setOpaque(true);
		this.setLayout(new SpringLayout());

		for (String label : labels) {
			if(label.equals("Background")) {
				JComboBox<SettingsProvider.BackgroundType> background = new JComboBox<>(SettingsProvider.BackgroundType.values());
				background.setSelectedItem(SettingsProvider.BackgroundType.valueOf(Objects.requireNonNull(robot.prop.getProperty("BACKGROUND")).toUpperCase()));
				background.addActionListener(e -> {
					Main.setBackgroundImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("backgrounds/" + Objects.requireNonNull(background.getSelectedItem()).toString().toLowerCase() + ".png"))));
					robot.prop.setProperty("BACKGROUND", Objects.requireNonNull(background.getSelectedItem()).toString().toUpperCase());
					main.reloadConfig();
					main.setState(JFrame.MAXIMIZED_BOTH);
				});
				JLabel l = new JLabel(label + ": ", JLabel.TRAILING);
				this.add(l);
				l.setLabelFor(background);
				this.add(background);
				continue;
			}
			JTextField input;
			if (Objects.equals(label, labels[3]))
				input = new JTextField();
			else
				input = new JFormattedTextField(formatter);
			input.setCursor(new Cursor(Cursor.TEXT_CURSOR));
			input.setColumns(10);
			JLabel l = new JLabel(label + ": ", JLabel.TRAILING);
			this.add(l);
			l.setLabelFor(input);
			this.add(input);
			fields.add(input);
		}
		SpringUtilities.makeCompactGrid(this, labels.length, 2, 6, 6, 6, 6);
		this.setVisible(true);
		for (int i = 0; i < fields.size(); i++) {
			JTextField field = fields.get(i);
			int finalI = i;
			field.addActionListener(e -> {
				robot.prop.setProperty(labels[finalI].replaceAll(" ", "_").toUpperCase(), field.getText());
				main.reloadConfig();
				main.setState(JFrame.MAXIMIZED_BOTH);
			});
		}
	}

	public void update() {
		for (int i = 0; i < fields.size(); i++) {
			JTextField field = fields.get(i);
			field.setText(robot.prop.getProperty(labels[i].replaceAll(" ", "_").toUpperCase()));
		}
		main.saveConfig();
	}
}
