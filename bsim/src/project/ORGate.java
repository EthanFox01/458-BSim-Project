package project;

import java.awt.Color;
import java.util.Vector;

import javax.vecmath.Vector3d;

import bsim.BSim;
import bsim.BSimTicker;
import bsim.draw.BSimDrawer;
import bsim.draw.BSimP3DDrawer;
import bsim.particle.BSimBacterium;
import bsim.particle.BSimVesicle;
import processing.core.PGraphics3D;

@SuppressWarnings("unchecked")
public class ORGate {

	/*
	 * todo
	 * Set up custom vesicle classes for SignalVesicles and CleanerVesicles
	 * Set up ReceiverBacterium class
	 * Modify Sender vesiclate() to make cleaners after a Sender makes contact with
	 * opposite SignalVesicles
	 * 
	 */
	public static void generateSenderPopulation(BSim sim, Vector<SignalVesicle> signals,
			Vector<CleanerVesicle> cleaners,
			Vector<SenderBacterium> pop, String popId, String transmission) {
		while (pop.size() < 90) {
			Vector3d position;
			if (popId == "a")
				position = new Vector3d(Math.random() * 20 + 40, Math.random() * 20 + 70, Math.random() * 20 + 60);
			// position = new Vector3d(50, 50, 50);
			else
				position = new Vector3d(Math.random() * 20 + 40, Math.random() * 20 + 20, Math.random() * 20 + 60);
			SenderBacterium b = new SenderBacterium(sim, position, transmission, popId);
			b.setRadius();
			b.setSurfaceAreaGrowthRate();
			b.setChildList(new Vector<SenderBacterium>());
			b.pVesicle(0.2);
			b.setSignalList(signals);
			b.setCleanerList(cleaners);
			if (!b.intersection(pop))
				pop.add(b);
		}
	}

	public static void generateReceiverPopulation(BSim sim, Vector<SignalVesicle> signals,
			Vector<ReceiverBacterium> pop) {
		while (pop.size() < 230) {
			Vector3d position = new Vector3d(Math.random() * 20 + 60, Math.random() * 20 + 50, Math.random() * 20 + 55);
			ReceiverBacterium b = new ReceiverBacterium(sim, position);
			b.setRadius();
			b.setSurfaceAreaGrowthRate();
			b.setChildList(new Vector<ReceiverBacterium>());
			b.pVesicle(0.0);
			b.setVesicleList(signals);
			if (!b.intersection(pop))
				pop.add(b);
		}
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		/*********************************************************
		 * Set up the simulation
		 */
		BSim sim = new BSim();

		/*********************************************************
		 * Set up the lists for storing vesicles and add bacteria to the simulation.
		 */
		final Vector<SignalVesicle> signalsA = new Vector<SignalVesicle>();
		final Vector<SenderBacterium> popA = new Vector<SenderBacterium>();
		final Vector<CleanerVesicle> cleanersA = new Vector<CleanerVesicle>();
		generateSenderPopulation(sim, signalsA, cleanersA, popA, "a", "10001110");
		// generateSenderPopulation(sim, signalsA, cleanersA, popA, "a", "100");

		final Vector<SignalVesicle> signalsB = new Vector<SignalVesicle>();
		final Vector<SenderBacterium> popB = new Vector<SenderBacterium>();
		final Vector<CleanerVesicle> cleanersB = new Vector<CleanerVesicle>();
		generateSenderPopulation(sim, signalsB, cleanersB, popB, "b", "01010101");
		// generateSenderPopulation(sim, signalsB, cleanersB, popB, "b", "010");

		final Vector<SignalVesicle> signalsC = new Vector<SignalVesicle>();
		final Vector<ReceiverBacterium> popC = new Vector<ReceiverBacterium>();
		generateReceiverPopulation(sim, signalsC, popC);

		/*********************************************************
		 * Set up the ticker
		 */
		sim.setTicker(new BSimTicker() {
			double timeslot = 40.0;
			int iteration = 0;
			boolean clearing = false;
			boolean buffering = false;

			@SuppressWarnings("unchecked")
			@Override
			public void tick() {
				if (sim.getTime() % timeslot == 0.0) {
					if (sim.getTime() != 0.0) {
						ReceiverBacterium.updateReceived();
						System.out.println("Message from A: " + ReceiverBacterium.getReceivedA());
						System.out.println("Message from B: " + ReceiverBacterium.getReceivedB());
						iteration++;
					}
					System.out.println("Sending a new bit");

					for (SenderBacterium b : popA) {
						char mode = b.transmission.charAt(iteration);
						b.setMode(Integer.parseInt(String.valueOf(mode)));
					}
					for (SenderBacterium b : popB) {
						char mode = b.transmission.charAt(iteration);
						b.setMode(Integer.parseInt(String.valueOf(mode)));
					}
				}
				if (buffering) {
					if (clearing && sim.getTime() % timeslot == 0.6 * timeslot) {
						System.out.println("Clearing vesicles");
						for (SenderBacterium b : popA)
							b.setMode(2);
						for (SenderBacterium b : popB)
							b.setMode(2);
					} else if (clearing && sim.getTime() % timeslot == 0.6 * timeslot) {
						System.out.println("Waiting");
						for (SenderBacterium b : popA)
							b.setMode(0);
						for (SenderBacterium b : popB)
							b.setMode(0);
					} else if (!clearing && sim.getTime() % timeslot == 0.75 * timeslot) {
						System.out.println("Waiting");
						for (SenderBacterium b : popA)
							b.setMode(0);
						for (SenderBacterium b : popB)
							b.setMode(0);
					}
				} else {
					if (clearing && sim.getTime() % timeslot == 0.75 * timeslot) {
						System.out.println("Clearing vesicles");
						for (SenderBacterium b : popA)
							b.setMode(2);
						for (SenderBacterium b : popB)
							b.setMode(2);
					}
				}

				for (SenderBacterium b : popA) {
					b.action();
					b.removeCollidedVesicles();

				}
				for (SenderBacterium b : popB) {
					b.action();
					b.removeCollidedVesicles();
				}
				for (ReceiverBacterium b : popC) {
					b.action();
				}
				for (CleanerVesicle cleaner : cleanersA) {
					for (SignalVesicle s : signalsA) {
						cleaner.interaction(s);
					}
					for (SignalVesicle s : signalsB) {
						cleaner.interaction(s);
					}
					cleaner.action();
					cleaner.updatePosition();
				}
				for (CleanerVesicle cleaner : cleanersB) {
					for (SignalVesicle s : signalsA) {
						cleaner.interaction(s);
					}
					for (SignalVesicle s : signalsB) {
						cleaner.interaction(s);
					}
					cleaner.action();
					cleaner.updatePosition();
				}
				for (SignalVesicle signal : signalsA) {
					for (ReceiverBacterium r : popC) {
						signal.interaction(r);
					}
					signal.action();
					signal.updatePosition();
				}
				for (SignalVesicle signal : signalsB) {
					for (ReceiverBacterium r : popC) {
						signal.interaction(r);
					}
					signal.action();
					signal.updatePosition();
				}
				// signalsA.removeIf(signal -> signal.collided);
				// signalsA.removeIf(signal -> signal.collided);
				// cleanersA.removeIf(cleaner -> cleaner.collided);
				// cleanersB.removeIf(cleaner -> cleaner.collided);
			}
		});

		/*********************************************************
		 * Set up the drawer
		 */
		BSimDrawer drawer = new BSimP3DDrawer(sim, 800, 600) {
			@Override
			public void scene(PGraphics3D p3d) {
				for (BSimBacterium b : popA) {
					draw(b, Color.GREEN);
				}
				for (BSimBacterium a : popB) {
					draw(a, Color.BLUE);
				}
				for (BSimBacterium c : popC) {
					draw(c, Color.RED);
				}
				for (SignalVesicle vesicle : signalsA)
					draw(vesicle, Color.GREEN);
				for (SignalVesicle vesicle : signalsB)
					draw(vesicle, Color.BLUE);
				for (CleanerVesicle vesicle : cleanersA)
					draw(vesicle, Color.WHITE);
				for (CleanerVesicle vesicle : cleanersB)
					draw(vesicle, Color.WHITE);
			}
		};
		sim.setDrawer(drawer);

		// reun the simulation
		sim.preview();

	}

}
