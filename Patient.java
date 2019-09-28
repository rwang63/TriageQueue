/*
 * CPSC 5003, Seattle University
 * This is free and unencumbered software released into the public domain.
 */
package rwang_P2X;

/**
 * Emergency waiting room patient representation.
 * @author Ruifeng Wang
 */
public class Patient {
    private int priorityCode;   // patient's assigned priority
    private int arrivalOrder;   // assigned arrival number
    private String name;        // patient's name

    /**
     * Creates a patient object.
     * @param priorityCode the priority code of the patient, lower will be
     *                     seen first
     * @param arrivalOrder number representing the order the patient
     *                     arrived in the waiting room.
     * @param name the name of the patient
     */
    public Patient(int priorityCode, int arrivalOrder, String name) {
        this.priorityCode = priorityCode;
        this.arrivalOrder = arrivalOrder;
        this.name = name;
    }

    /**
     * Priority code representing the emergency level
     * @return priority code representing the emergency level
     */
    public int getPriorityCode() {
        return priorityCode;
    }

    /**
     * Number representing the order the patient arrived in the waiting room.
     * @return arrival number in the emergency room
     */
    public int getArrivalOrder() {
        return arrivalOrder;
    }

    /**
     * Patient's name
     * @return Patient's name
     */
    public String getName() {
        return name;
    }

    /**
     * String representation of the object. Useful when debugging the program.
     * @return String representation of the patient object.
     */
    @Override
    public String toString() {
        return name + "{" + "pri=" + priorityCode + ", arrive=" +
               arrivalOrder + '}';
    }
}
