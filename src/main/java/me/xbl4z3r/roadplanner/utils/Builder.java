package me.xbl4z3r.roadplanner.utils;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.*;
import org.jetbrains.annotations.NotNull;

import java.lang.Math;
import java.util.Arrays;
import java.util.List;

public class Builder {
	public TrajectoryActionBuilder videoBuilder(Pose2d beginPose) {
		if (beginPose == null) {
			beginPose = new Pose2d(0.0, 0.0, 0.0);
		}

		List<VelConstraint> minVelConstraints = Arrays.asList(
				new TranslationalVelConstraint(50.0),
				new AngularVelConstraint(Math.PI / 2)
		);

		return new TrajectoryActionBuilder(
				TurnAction::new,
				TrajectoryAction::new,
				beginPose,
				1e-6,
				0.0,
				new TurnConstraints(Math.PI / 2, -Math.PI / 2, Math.PI / 2),
				new MinVelConstraint(minVelConstraints),
				new ProfileAccelConstraint(-40.0, 40.0),
				0.25,
				0.1
		);
	}

	public record TrajectoryAction(TimeTrajectory t) implements Action {
		@Override
		public boolean run(@NotNull TelemetryPacket p) {
			throw new UnsupportedOperationException("Not yet implemented");
		}

		@Override
		public void preview(@NotNull Canvas fieldOverlay) {
			throw new UnsupportedOperationException("Not yet implemented");
		}

		@Override
		public String toString() {
			return "Trajectory";
		}
	}

	public record TurnAction(TimeTurn t) implements Action {
		@Override
		public boolean run(@NotNull TelemetryPacket p) {
			throw new UnsupportedOperationException("Not yet implemented");
		}

		@Override
		public void preview(@NotNull Canvas fieldOverlay) {
			throw new UnsupportedOperationException("Not yet implemented");
		}

		@Override
		public String toString() {
			return "Turn";
		}
	}
}