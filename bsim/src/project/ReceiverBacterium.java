package project;

import javax.vecmath.Vector3d;

import bsim.BSim;
import bsim.particle.BSimBacterium;

public class ReceiverBacterium extends BSimBacterium {
    private static String receivedA = "";
    private static String receivedB = "";
    private static int receivedInTimeslotA = 0;
    private static int receivedInTimeslotB = 0;
    private static int threshold = 20;

    public ReceiverBacterium(BSim sim, Vector3d position) {
        super(sim, position);
        replicationRadius = Math.sqrt(4);
    }

    public static String getReceivedA() {
        return receivedA;
    }

    public static String getReceivedB() {
        return receivedB;
    }

    public static int getReceivedInTimeslotA() {
        return receivedInTimeslotA;
    }
    
    public static void incrementReceivedInTimeslotA() {
        receivedInTimeslotA++;
    }

    public static void incrementReceivedInTimeslotB() {
        receivedInTimeslotB++;
    }

    public static int getReceivedInTimeslotB() {
        return receivedInTimeslotB;
    }

    public static void updateReceived() {
        if (receivedInTimeslotA >= threshold) {
            receivedA += "1";
        } else {
            receivedA += "0";
        }

        if (receivedInTimeslotB >= threshold) {
            receivedB += "1";
        } else {
            receivedB += "0";
        }

        receivedInTimeslotA = 0;
        receivedInTimeslotB = 0;
    }
}
