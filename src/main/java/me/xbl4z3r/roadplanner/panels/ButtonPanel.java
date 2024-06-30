package me.xbl4z3r.roadplanner.panels;

import me.xbl4z3r.roadplanner.*;
import me.xbl4z3r.roadplanner.node.Marker;
import me.xbl4z3r.roadplanner.node.Node;
import me.xbl4z3r.roadplanner.node.NodeManager;
import me.xbl4z3r.roadplanner.utils.Importer;
import me.xbl4z3r.roadplanner.utils.Logger;
import me.xbl4z3r.roadplanner.utils.SettingsProvider;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ButtonPanel extends JPanel {

	public final JButton flipButton = new JButton("Flip");
	private final Main main;
	private final SettingsProvider robot;

	public ButtonPanel(LinkedList<NodeManager> managers, Main main, SettingsProvider props) {
		this.robot = props;
		this.main = main;
		this.setMinimumSize(new Dimension(0, 20));
		this.setLayout(new GridLayout(1, 4, 1, 1));

		JButton exportButton = new JButton("Export");
		exportButton.setFocusable(false);
		JButton importButton = new JButton("Import");
		importButton.setFocusable(false);
		flipButton.setFocusable(false);
		JButton clearButton = new JButton("Clear");
		clearButton.setFocusable(false);
		JButton undoButton = new JButton("Undo");
		undoButton.setFocusable(false);
		JButton redoButton = new JButton("Redo");
		redoButton.setFocusable(false);
		this.add(exportButton);
		this.add(importButton);
		this.add(flipButton);
		this.add(clearButton);
		this.add(undoButton);
		this.add(redoButton);

		this.setVisible(true);

		exportButton.addActionListener(e -> {
			Logger.info("Exporting path...");
			export();
		});

		flipButton.addActionListener(e -> {
			Logger.info("Flipping path...");
			Node un = new Node(-2, -2);
			un.state = 3;
			for (int i = 0; i < getCurrentManager().size(); i++) {
				Node node = getCurrentManager().get(i);
				node.y = 144 * main.scale - node.y;
				node.splineHeading = 180 - node.splineHeading;
				getCurrentManager().set(i, node);
			}
			main.drawPanel.repaint();
		});
		undoButton.addActionListener(e -> {
			Logger.info("Undoing...");
			main.undo(true);
			main.drawPanel.repaint();
		});
		redoButton.addActionListener(e -> {
			Logger.info("Redoing...");
			main.redo();
			main.drawPanel.repaint();
		});
		clearButton.addActionListener(e -> {
			Logger.info("Clearing path...");
			getCurrentManager().undo.clear();
			getCurrentManager().redo.clear();
			getCurrentManager().clear();
			int id = main.currentM;
			for (int i = id; i < managers.size() - 1; i++) managers.set(i, managers.get(i + 1));
			if (managers.size() > 1) managers.removeLast();
			else main.currentM = 0;
			if (main.currentM > 0) main.currentM--;
			main.currentN = -1;
			main.infoPanel.editPanel.updateText();
			main.drawPanel.resetPath();
			main.drawPanel.renderBackgroundSplines();
			main.drawPanel.repaint();
			main.exportPanel.field.setText("");
		});
		importButton.addActionListener(e -> {
			Logger.info("Importing path...");
			File file;
			if (robot.importPath.matches("")) {
				JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView());
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Java Files", "java");
				chooser.setFileFilter(filter);
				int r = chooser.showOpenDialog(null);
				if (r != JFileChooser.APPROVE_OPTION) return;
				robot.importPath = chooser.getSelectedFile().getPath();
				robot.prop.setProperty("IMPORT/EXPORT", robot.importPath);
				main.saveConfig();
				main.infoPanel.settingsPanel.update();
				file = chooser.getSelectedFile();
			} else {
				main.saveConfig();
				file = new File(robot.importPath);
			}
			Importer importer = new Importer(main);
			LinkedList<NodeManager> in = importer.read(file);
			if (getCurrentManager().size() < 1)
				managers.remove(getCurrentManager());
			managers.addAll(in);
			main.currentM = managers.size() - 1;
			main.currentN = -1;
			main.infoPanel.editPanel.name.setText(getCurrentManager().name);
			main.drawPanel.renderBackgroundSplines();
			main.drawPanel.repaint();
		});
	}

	public void export() {
		if (getCurrentManager().size() > 0) {
			main.infoPanel.editPanel.saveValues();
			main.infoPanel.markerPanel.saveValues();
			Node node = getCurrentManager().getNodes().get(0);
			double x = main.toInches(node.x);
			double y = main.toInches(node.y);
			if (!robot.importPath.matches("")) {
				File outputFile = new File(robot.importPath.substring(0, robot.importPath.length() - 4) + "backup.java");
				System.out.println(outputFile.getPath());
				try {
					outputFile.createNewFile();
					FileWriter writer = new FileWriter(outputFile);
					Scanner reader = new Scanner(new File(robot.importPath));
					writer.close();
				} catch (IOException ioException) {
					Logger.error("Failed to create backup file", ioException);
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("mecanumDrive.actionBuilder(new Pose2d(%.2f, %.2f, Math.toRadians(%.2f)))%n", x, -y, (node.robotHeading + 90)));
			List<Marker> markers = getCurrentManager().getMarkers();
			markers.sort(Comparator.comparingDouble(n -> n.displacement));
			for (Marker marker : markers) sb.append(String.format(".UNSTABLE_addTemporalMarkerOffset(%.2f,() -> {%s})%n", marker.displacement, marker.code));
			boolean prev = false;
			for (int i = 0; i < getCurrentManager().size(); i++) {
				node = getCurrentManager().get(i);
				if (node.equals(getCurrentManager().getNodes().get(0))) {
					if (node.reversed != prev) {
						sb.append(String.format(".setReversed(%s)%n", node.reversed));
						prev = node.reversed;
					}
					continue;
				}
				x = main.toInches(node.x);
				y = main.toInches(node.y);

				switch (node.getType()) {
					case splineTo:
						sb.append(String.format(".splineTo(new Vector2d(%.2f, %.2f), Math.toRadians(%.2f))%n", x, -y, (node.splineHeading + 90)));
						break;
					case splineToSplineHeading:
						sb.append(String.format(".splineToSplineHeading(new Pose2d(%.2f, %.2f, Math.toRadians(%.2f)), Math.toRadians(%.2f))%n", x, -y, (node.robotHeading + 90), (node.splineHeading + 90)));
						break;
					case splineToLinearHeading:
						sb.append(String.format(".splineToLinearHeading(new Pose2d(%.2f, %.2f, Math.toRadians(%.2f)), Math.toRadians(%.2f))%n", x, -y, (node.robotHeading + 90), (node.splineHeading + 90)));
						break;
					case splineToConstantHeading:
						sb.append(String.format(".splineToConstantHeading(new Vector2d(%.2f, %.2f), Math.toRadians(%.2f))%n", x, -y, (node.splineHeading + 90)));
						break;
					default:
						sb.append("couldn't find type");
						break;
				}
				if (node.reversed != prev) {
					sb.append(String.format(".setReversed(%s)%n", node.reversed));
					prev = node.reversed;
				}
			}
			sb.append(String.format(".build();%n"));
			main.exportPanel.field.setText(sb.toString());
		}
	}

	private NodeManager getCurrentManager() {
		return main.getManagers().get(main.currentM);
	}
}
