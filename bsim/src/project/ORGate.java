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

public class ORGate {

	/*
	 * todo
	 * Set up custom vesicle classes for SignalVesicles and CleanerVesicles
	 * Set up ReceiverBacterium class
	 * Modify Sender vesiclate() to make cleaners after a Sender makes contact with
	 * opposite SignalVesicles
	 * 
	*/
	public static void generatePopulation(BSim sim, Vector<SignalVesicle> signals, Vector<CleanerVesicle> cleaners,
			Vector<SenderBacterium> pop, String popID) {
		while (pop.size() < 10) {
			Vector3d position;
			if (popID == "a")
				position = new Vector3d(Math.random() * 15 + 30, Math.random() * 15 + 70, Math.random() * 15 + 70);
			else
				position = new Vector3d(Math.random() * 15 + 30, Math.random() * 15 + 30, Math.random() * 15 + 70);
			SenderBacterium b = new SenderBacterium(sim, position);
			b.setRadius();
			b.setSurfaceAreaGrowthRate();
			b.pVesicle(0.4);
			b.setSignalList(signals);
			b.setCleanerList(cleaners);
			if (!b.intersection(pop))
				pop.add(b);
		}
	}

	public static void generatePopulation(BSim sim, Vector<SignalVesicle> signals, Vector<ReceiverBacterium> pop) {
		while (pop.size() < 10) {
			Vector3d position = new Vector3d(Math.random() * 15 + 70, Math.random() * 15 + 50, Math.random() * 15 + 30);
			ReceiverBacterium b = new ReceiverBacterium(sim, position);
			b.setRadius();
			b.setSurfaceAreaGrowthRate();
			b.pVesicle(0.2);
			b.setVesicleList(signals);
			if (!b.intersection(pop))
				pop.add(b);
		}
	}



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
		generatePopulation(sim, signalsA, cleanersA, popA, "a");

		final Vector<SignalVesicle> signalsB = new Vector<SignalVesicle>();
		final Vector<SenderBacterium> popB = new Vector<SenderBacterium>();
		final Vector<CleanerVesicle> cleanersB = new Vector<CleanerVesicle>();
		generatePopulation(sim, signalsB, cleanersB, popB, "b");

		final Vector<SignalVesicle> signalsC = new Vector<SignalVesicle>();
		final Vector<ReceiverBacterium> popC = new Vector<ReceiverBacterium>();
		generatePopulation(sim, signalsC, popC);

		/*********************************************************
		 * Set up the ticker
		 */
		sim.setTicker(new BSimTicker() {
			@Override
			public void tick() {
				for (BSimBacterium b : popA) {
					b.action();
				}
				for (BSimBacterium a : popB) {
					a.action();
				}
				for (BSimBacterium c : popC) {
					c.action();
				}
				for (BSimVesicle vesicle : signalsA) {
					vesicle.action();
					vesicle.updatePosition();
				}
				for (BSimVesicle vesicle : signalsB) {
					vesicle.action();
					vesicle.updatePosition();
				}
				for (BSimVesicle vesicle : cleanersA) {
					vesicle.action();
					vesicle.updatePosition();
				}
				for (BSimVesicle vesicle : signalsC) {
					vesicle.action();
					vesicle.updatePosition();
				}
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
				for (BSimVesicle vesicle : signalsA)
					draw(vesicle, Color.YELLOW);
				for (BSimVesicle vesicle : signalsB)
					draw(vesicle, Color.CYAN);
				for (BSimVesicle vesicle : signalsC)
					draw(vesicle, Color.MAGENTA);
				for (BSimVesicle vesicle : cleanersA)
					draw(vesicle, Color.WHITE);
				for (BSimVesicle vesicle : cleanersA)
					draw(vesicle, Color.WHITE);
			}
		};
		sim.setDrawer(drawer);

		// reun the simulation
		sim.preview();

	}

}
