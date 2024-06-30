# RoadPlanner

RoadPlanner is a tool built on top of [RRPathGen](https://github.com/Jarhead20/RRPathGen) to generate [Road Runner v1.0](https://github.com/acmerobotics/road-runner) paths.
![RoadPlanner](https://imgur.com/PJ0cewB.gif)

## Installation (Jar)

1. Download the jar from the [releases page](https://github.com/xbl4z3r/RoadPlanner/releases).
2. Check that you have at least java 8 installed `java --version`
3. Run the jar either by double-clicking it or through the command line with `java -jar RoadPlanner-X.X.X.jar`

## Installation (Intellij)

1. Clone the repo `git clone https://github.com/xbl4z3r/RoadPlanner.git`
2. Setup a run configuration
3. Run the app

## Usage

Generate your paths using the key binds below and once you are done export the path with the export button and copy
and paste it into your autonomous program.

| Key Bind                 | Action                  |
|--------------------------|-------------------------|
| Left Click               | Add New Point           |
| Left Drag (Point)        | Drags Selected Point    |
| Alt + Left Click         | Change End Tangent      |
| Shift + Alt + Left Click | Change Robot Heading    |
| Left Arrow               | Next Path               |
| Right Arrow              | Previous Path           |
| R                        | Reverse Robot Direction |
| Delete / Backspace       | Delete Selected Node    |
| Ctrl + Z                 | Undo Previous Action    |

If you accidentally do something wrong with the config, just delete it at `%appdata%/RoadPlanner` for
Windows, `~/Library/Application Support/RoadPlanner/config.properties` for macOS and `~/.RoadPlanner/config.properties` for
Linux.

## Acknowledgements

Base version for RoadRunner v0.5.6 by [Jarhead20](https://github.com/Jarhead20/RRPathGen).<br />
The field images were aquired from [MeepMeep](https://github.com/NoahBres/MeepMeep).<br />
And a big thank you to [Ryan Brott](https://github.com/rbrott) for making RoadRunner.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](https://choosealicense.com/licenses/mit/)
