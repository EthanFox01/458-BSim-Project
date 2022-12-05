package project;

import java.util.Vector;

import javax.vecmath.Vector3d;

import bsim.BSim;
import bsim.particle.BSimBacterium;

public class SenderBacterium extends BSimBacterium {
	protected Vector<CleanerVesicle> cleanerList;
	protected Vector<SignalVesicle> signalList;
	protected String transmission;
	protected int mode;
	protected String popId;

	public SenderBacterium(BSim sim, Vector3d position, String transmission, String popId) {
		super(sim, position);
		cleanerList = new Vector<CleanerVesicle>();
		signalList = new Vector<SignalVesicle>();
		this.transmission = transmission;
		mode = 0;
		this.popId = popId;
		this.vesicleRadius = 0.02;
	}

	public Vector<CleanerVesicle> getCleanerList() {
		return cleanerList;
	}

	public Vector<SignalVesicle> getSignalList() {
		return signalList;
	}

	public int getMode() {
		return mode;
	}

	public void setCleanerList(Vector<CleanerVesicle> v) {
		cleanerList = v;
	}

	public void setSignalList(Vector<SignalVesicle> v) {
		signalList = v;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	@SuppressWarnings("unchecked")
	public int removeCollidedVesicles() {
		int count = 0;
		Vector<SignalVesicle> signalRemoval = new Vector<SignalVesicle>();
		for (SignalVesicle signal : signalList) {
			if (signal.collided) {
				signalRemoval.add(signal);
				count++;
			}
		}
		signalList.removeAll(signalRemoval);

		Vector<CleanerVesicle> cleanerRemoval = new Vector<CleanerVesicle>();
		for (CleanerVesicle cleaner : cleanerList) {
			if (cleaner.collided) {
				cleanerRemoval.add(cleaner);
				count++;
			}
		}
		cleanerList.removeAll(cleanerRemoval);

		return count;
	}

	@Override
	public void vesiculate() {
		if (mode == 0)
			return;
		double r = vesicleRadius();
		if (mode == 1)
			signalList.add(new SignalVesicle(this.sim, new Vector3d(this.position), r, popId));
		else
			cleanerList.add(new CleanerVesicle(this.sim, new Vector3d(this.position), r * 2));
		setRadiusFromSurfaceArea(getSurfaceArea() - surfaceArea(r));
	}
}
