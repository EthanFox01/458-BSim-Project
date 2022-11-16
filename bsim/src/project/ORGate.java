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
	
	/* todo
	 * Set up custom vesicle classes for SignalVesicles and CleanerVesicles
	 * Set up ReceiverBacterium class
	 * Modify Sender vesiclate() to make cleaners after a Sender makes contact with opposite SignalVesicles
	 * 
	 */

	public static void main(String[] args) {
		/*********************************************************
		 * Set up the simulation
		 */
		BSim sim = new BSim();

		class SenderBacterium extends BSimBacterium {
			protected Vector cleanerList;

			public SenderBacterium(BSim sim, Vector3d position) {
				super(sim, position);
			}

			public void setCleanerList(@SuppressWarnings("rawtypes") Vector v) {
				cleanerList = v;
			}
			
			@Override
			public void vesiculate() {
				double r = vesicleRadius();
				vesicleList.add(new BSimVesicle(this.sim, new Vector3d(this.position), r));
				cleanerList.add(new BSimVesicle(this.sim, new Vector3d(this.position), r));
				setRadiusFromSurfaceArea(getSurfaceArea()-surfaceArea(r));
			}
		}
		
		class ReceiverBacterium extends BSimBacterium {
			public ReceiverBacterium(BSim sim, Vector3d position) {
				super(sim, position);
			}
			
		}

		/*********************************************************
		 * Set up the lists for storing vesicles and add bacteria to the simulation.
		 */
		final Vector<BSimVesicle> vesiclesA = new Vector<BSimVesicle>();
		final Vector<SenderBacterium> popA = new Vector<SenderBacterium>();
		final Vector<SenderBacterium> childrenA = new Vector<SenderBacterium>();
		final Vector<BSimVesicle> cleanersA = new Vector<BSimVesicle>();
		while (popA.size() < 10) {
			SenderBacterium a = new SenderBacterium(sim,
					new Vector3d(Math.random() * 15 + 30, Math.random() * 15 + 70, Math.random() * 15 + 70));
			a.setRadius();
			a.setSurfaceAreaGrowthRate();
			a.setChildList(childrenA);
			a.pVesicle(0.2);
			a.setVesicleList(vesiclesA);
			a.setCleanerList(cleanersA);
			if (!a.intersection(popA))
				popA.add(a);
		}

		final Vector<BSimVesicle> vesiclesB = new Vector<BSimVesicle>();
		final Vector<SenderBacterium> popB = new Vector<SenderBacterium>();
		final Vector<SenderBacterium> childrenB = new Vector<SenderBacterium>();
		final Vector<BSimVesicle> cleanersB = new Vector<BSimVesicle>();
		while (popB.size() < 10) {
			SenderBacterium b = new SenderBacterium(sim,
					new Vector3d(Math.random() * 15 + 30, Math.random() * 15 + 30, Math.random() * 15 + 70));
			// If the bacterium doesn't intersect any others then add it to the overall list
			b.setRadius();
			b.setSurfaceAreaGrowthRate();
			b.setChildList(childrenB);
			b.pVesicle(0.2);
			b.setVesicleList(vesiclesB);
			b.setCleanerList(cleanersB);
			if (!b.intersection(popB))
				popB.add(b);
		}

		final Vector<BSimVesicle> vesiclesC = new Vector<BSimVesicle>();
		final Vector<ReceiverBacterium> popC = new Vector<ReceiverBacterium>();
		final Vector<ReceiverBacterium> childrenC = new Vector<ReceiverBacterium>();
		while (popC.size() < 10) {
			ReceiverBacterium c = new ReceiverBacterium(sim,
					new Vector3d(Math.random() * 15 + 70, Math.random() * 15 + 50, Math.random() * 15 + 30));
			// If the bacterium doesn't intersect any others then add it to the overall list
			c.setRadius();
			c.setSurfaceAreaGrowthRate();
			c.setChildList(childrenC);
			c.pVesicle(0.2);
			c.setVesicleList(vesiclesC);
			if (!c.intersection(popC))
				popC.add(c);
		}
		
		
		/*********************************************************
		 * Set up the ticker
		 */
		sim.setTicker(new BSimTicker() {
			@Override
			public void tick() {
				for (BSimBacterium b : popA) {
					b.action();
//					b.updatePosition();
				}
				for (BSimBacterium a : popB) {
					a.action();
				}
				for (BSimBacterium c : popC) {
					c.action();
				}
				popA.addAll(childrenA);
				childrenA.clear();
				popB.addAll(childrenB);
				childrenB.clear();
				popC.addAll(childrenC);
				childrenC.clear();
				for (BSimVesicle vesicle : vesiclesA) {
					vesicle.action();
					vesicle.updatePosition();
				}
				for (BSimVesicle vesicle : vesiclesB) {
					vesicle.action();
					vesicle.updatePosition();
				}
				for (BSimVesicle vesicle : cleanersA) {
					vesicle.action();
					vesicle.updatePosition();
				}
				for (BSimVesicle vesicle : vesiclesC) {
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
				for (BSimVesicle vesicle : vesiclesA)
					draw(vesicle, Color.YELLOW);
				for (BSimVesicle vesicle : vesiclesB)
					draw(vesicle, Color.CYAN);
				for (BSimVesicle vesicle : cleanersA)
					draw(vesicle, Color.WHITE);
				for (BSimVesicle vesicle : cleanersA)
					draw(vesicle, Color.WHITE);
				for (BSimVesicle vesicle : vesiclesC)
					draw(vesicle, Color.MAGENTA);
			}
		};
		sim.setDrawer(drawer);

		// reun the simulation
		sim.preview();

	}

}
