/*
 * CPSC 5003, Seattle University
 * This is free and unencumbered software released into the public domain.
 */
package rwang_P2X;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import rwang_P2.Patient;

/**
 * Hospital triage system implemented using a heap.
 * @author Ruifeng Wang
 */
public class PatientPriorityQueue {
    private ArrayList<Patient> patients; // heap property is always satisfied
    private int nextPatientNumber;       // num assigned to next added patient

    /**
     * Creates an empty triage system with no patients.
     */
    public PatientPriorityQueue() {
        this.patients = new ArrayList<Patient>();
        this.nextPatientNumber = 1;
    }

    /**
     * Gets the list of patients currently in the waiting room
     * @return the list of patients that have not been called
     */
    public ArrayList<Patient> getPatientList() {
        return patients;
    }

    /**
     * Adds a patient to the priority queue system.
     * @param priorityCode		Patient's priority code
     * @param patientName		Patient's name	
     */
    public void addPatient(int priorityCode, String patientName) {
    	Patient patient = new Patient(priorityCode, nextPatientNumber++
    			, patientName);
    	patients.add(patient);
    	percolateUp(size() - 1);
    	
    }

    /**
     * Views (but does not remove) patient at the top of the priority queue
     * @return		Patient at top of priority queue
     */
    public Patient peek() {
        return patients.get(0);
    }

    /**
     * Removes the patient at the top of the priority queue
     * @return		Patient at top of priority queue
     */
    public Patient dequeue() {
    	Patient dequeued = peek();
    	patients.set(0, patients.get(size() - 1));
    	patients.remove(size() - 1);
    	percolateDown(0);
        return dequeued;
    }

    /**
     * Finds number of patients in the priority queue
     * @return		Number of patients in the priority queue
     */
    public int size() {
        return patients.size();
    }
    
    /**
     * Changes priority of patient
     * @param patientIndex		Patient to change priority of
     * @param priorityCode		Priority to change to
     */
    public void changePri(int patientIndex, int priorityCode) {
    	Patient copy = new Patient(priorityCode,patients.get(patientIndex)
    			.getArrivalOrder(),patients.get(patientIndex).getName());
    	patients.set(patientIndex, copy);
    	changePercolate(patientIndex);
    }
    
    /**
     * Manipulates the priority queue and stores commands in a file
     * @param fileName		File name provided by user
     * @throws IOException
     */
    public void saveFile(String fileName) throws IOException {
        PrintWriter printWriter = new PrintWriter(fileName);
        ArrayList<Patient> sortedByArrival = new ArrayList<Patient>();
        for(int i = 0; i < patients.size(); i++) {
        	sortedByArrival.add(patients.get(i));
        }
        for (int i = 0; i < sortedByArrival.size() - 1; i++) {
            for (int j = 0; j < sortedByArrival.size() - i - 1; j++) {
                if (sortedByArrival.get(j).getArrivalOrder() > 
                	sortedByArrival.get(j + 1).getArrivalOrder()) { 
                    Patient temp = sortedByArrival.get(j); 
                    sortedByArrival.set(j, sortedByArrival.get(j+1));
                    sortedByArrival.set(j + 1, temp);
                } 
            }
        }
        for(int i = 0; i < sortedByArrival.size(); i++) {
        	printWriter.println("add " + getPriorityString(sortedByArrival
        			.get(i).getPriorityCode()) + " " + 
        			sortedByArrival.get(i).getName());
        }
        printWriter.close();

    }
    
    /**
     * Takes integer priority code and turns it into string representation
     * @param priorityCode		Priority code to change
     * @return					String representation of priority code
     */
    private String getPriorityString(int priorityCode) {
    	if(priorityCode == 1) {
    		return "immediate";
    	} else if(priorityCode == 2) {
    		return "emergency";
    	} else if(priorityCode == 3) {
    		return "urgent";
    	} else {
    		return "minimal";
    	}
    }
    
    /**
     * Decides if a patient should be percolated up or down in the queue
     * Then percolates them up / down if necessary
     * @param index		Index of the patient in question
     */
    private void changePercolate(int index) {
    	if(patients.get(parent(index)).getPriorityCode() >= patients.get(index)
    			.getPriorityCode() && index != 0) {
    		Patient temp = patients.get(parent(index));
    		patients.set(parent(index), patients.get(index));
    		patients.set(index, temp);
    		changePercolate(parent(index));
    	} else { 
    		int min = index;
    		if(hasLeft(index) && patients.get(left(index))
    				.getPriorityCode() < patients.get(min).getPriorityCode()) {
    			min = left(index);
    		}
    		if(hasRight(index) && patients.get(right(index))
    				.getPriorityCode() < patients.get(min).getPriorityCode()) {
    			min = right(index);
    		}
    		if(index != min) {
    			Patient temp = patients.get(index);
    			patients.set(index, patients.get(min));
    			patients.set(min, temp);
    			changePercolate(min);
    		}
    	}
    }

    /**
     * Finds if an index has a left child
     * @param parentIndex		Index to find out if there is left child of
     * @return					True if left child exists, false otherwise
     */
    private boolean hasLeft(int parentIndex) {
    	return left(parentIndex) < size();
    }

    /**
     * Finds if an index has a right child
     * @param parentIndex		Index to find out if there is a right child of
     * @return					True if right child exists, false otherwise
     */
    private boolean hasRight(int parentIndex) {
        return right(parentIndex) < size();
    }

    /**
     * Finds the index of the left child
     * @param parentIndex		Index to find left child of
     * @return					Index of left child
     */
    private int left(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    /**
     * Finds the index of the right child
     * @param parentIndex		Index to find right child of
     * @return					Index of right child
     */
    private int right(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    /**
     * Finds the parent index of a child
     * @param childIndex		Index to find parent of
     * @return					Index of parent
     */
    private int parent(int childIndex) {
        return (childIndex - 1) / 2;
    }

    /**
     * Brings higher priority patients to the top of the queue
     * @param index		Index of the patient to check
     */
    private void percolateUp(int index) {
    	if(patients.get(parent(index)).getPriorityCode() > patients.get(index)
    			.getPriorityCode()) {
    		Patient temp = patients.get(parent(index));
    		patients.set(parent(index), patients.get(index));
    		patients.set(index, temp);
    		percolateUp(parent(index));
    	}
    }

    /**
     * Moves lower priority patients to the bottom of the queue
     * @param index		Index of the patient to check
     */
    private void percolateDown(int index) {
    	if(index < size()) {
    		int min = index;
    		if(hasLeft(index) && patients.get(left(index))
    				.getPriorityCode() < patients.get(min).getPriorityCode()) {
    			min = left(index);
    		}
    		if(hasRight(index) && patients.get(right(index))
    				.getPriorityCode() < patients.get(min).getPriorityCode()) {
    			min = right(index);
    		}
    		if(index != min) {
    			Patient temp = patients.get(index);
    			patients.set(index, patients.get(min));
    			patients.set(min, temp);
    			percolateDown(min);
    		}
    	}
    }
    
}
