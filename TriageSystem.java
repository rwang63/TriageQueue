/*
 * CPSC 5003, Seattle University
 * This is free and unencumbered software released into the public domain.
 */
package rwang_P2X;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * User interface for hospital emergency room triaging system
 * @author Ruifeng Wang
 * @version 1.0
 */
public class TriageSystem {
    private static final String MSG_WELCOME = "Welcome to the hospital "
    		+ "emergency room triaging system.";
    private static final String MSG_GOODBYE = "Thank you for using the triage "
    		+ "system. Goodbye.";
    private static final String MSG_HELP = "add <priority-code> <patient-"
    		+ "name>\r\n" + 
    		"            Adds the patient to the triage system.\r\n" + 
    		"            <priority-code> must be one of the 4 accepted priority"
    		+ " codes:\r\n" + 
    		"                1. immediate 2. emergency 3. urgent 4. "
    		+ "minimal\r\n            <patient-name>: patient's full legal name"
    		+ " (may contain spaces)\r\nnext        Announces the patient to be"
    		+ " seen next. Takes into account the\r\n            type of "
    		+ "emergency and the patient's arrival order.\r\npeek        "
    		+ "Displays the patient that is next in line, but keeps in "
    		+ "queue\r\nlist        Displays the list of all patients that are "
    		+ "still waiting\r\n            in the order that they have arrived"
    		+ ".\r\nload <file> Reads the file and executes the command on each"
    		+ " line\r\nchange <arrivalID> <newPriority>\r\n" +
    	    "            Changes the patient's priority in the triage "
    	    + "system.\r\n" +
            "            <arrivalID>: ID assigned to patient upon arrival\r\n" +
    	    "            <priority-code> must be one of the 4 accepted priority"
    		+ " codes:\r\n" + 
    		"                1. immediate 2. emergency 3. urgent 4. "
    		+ "minimal\r\nsave <fileName>\r\n            Saves the triage queue"
    		+ " to a file that can be reloaded again later\r\n"
    		+ "help        Displays this menu\r\n"
    		+ "quit        Exits the program";
    private static boolean keepAsking = true;

    /**
     * Entry point of the program
     * @param args not used
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        System.out.println(MSG_WELCOME);

        Scanner console = new Scanner(System.in);
        PatientPriorityQueue
            priQueue = new PatientPriorityQueue();
        while (keepAsking) {
            System.out.print("\ntriage> ");
            String line = console.nextLine();
            processLine(line, priQueue);
        }

        System.out.println(MSG_GOODBYE);
    }

    /**
     * Process the line entered from the user or read from the file
     * @param line     String command to execute
     * @param priQueue Priority Queue to operate on
     * @throws IOException 
     */
    private static void processLine(String line,
                                    PatientPriorityQueue priQueue) 
                                    		throws IOException {
    	try {
    		Scanner lineScanner = new Scanner(line); // Scanner to extract words
    		String cmd = lineScanner.next();         // The first is user's command

        // A switch statement could be used on strings, but not all have JDK7
	        if (cmd.equals("help")) {
	            System.out.println(MSG_HELP);
	        } else if (cmd.equals("add")) {
	            addPatient(lineScanner, priQueue);
	        } else if (cmd.equals("peek")) {
	            peekNextPatient(priQueue);
	        } else if (cmd.equals("next")) {
	            dequeueNextPatient(priQueue);
	        } else if (cmd.equals("list")) {
	            showPatientList(priQueue);
	        } else if (cmd.equals("load")) {
	            executeCommandsFromFile(lineScanner, priQueue);
	        } else if (cmd.equals("debug")) {
	            System.out.println(priQueue.toString());
	        } else if (cmd.equals("change")) {
	        	changePriority(lineScanner, priQueue);
	        } else if (cmd.equals("save")) {
	            saveCommands(lineScanner, priQueue);
	        } else if (cmd.equals("quit")) {
	            keepAsking = false;
	    	} else {
	            System.out.println("Error: unrecognized command: " + line);
	        }
        } catch(Exception e) {
        	System.out.println("No command entered.");
        }
    }

    /**
     * Reads a text file with each command on a separate line and executes the
     * lines as if they were typed into the command prompt.
     * @param lineScanner Scanner remaining characters after the command `load`
     * @param priQueue    priority queue to operate on
     * @throws IOException 
     */
    private static void executeCommandsFromFile(Scanner lineScanner,
                                                PatientPriorityQueue priQueue) 
                                                		throws IOException {
        // read the rest of the line into a single string
        String fileName = lineScanner.nextLine().trim();

        try {
            Scanner file = new Scanner(new File(fileName));
            while (file.hasNext()) {
                final String line = file.nextLine();
                System.out.println("\ntriage> " + line);
                processLine(line, priQueue);
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.printf("File %s was not found.%n", fileName);
        }
    }

    /**
     * Displays the next patient in the waiting room that will be called.
     * @param priQueue priority queue to operate on
     */
    private static void peekNextPatient(PatientPriorityQueue priQueue) {
    	if(priQueue.size() == 0) {
        	System.out.println("There are no patients in the waiting area.");
        } else {
        	System.out.println("Highest priority patient to be called "
        			+ "next: " + priQueue.peek().getName());
        }
    }

    /**
     * Displays the list of patients in the waiting room.
     * @param priQueue priority queue to operate on
     */
    private static void showPatientList(PatientPriorityQueue priQueue) {
        System.out.println("# patients waiting: " + priQueue.size() + "\n");
        System.out.println("  Arrival #   Priority Code   Patient Name\n" +
                           "+-----------+---------------+--------------+");
        for(int i = 0; i < priQueue.size(); i++) {
        	System.out.printf("      %-6s", priQueue.getPatientList()
        			.get(i).getArrivalOrder());
        	if(priQueue.getPatientList().get(i).getPriorityCode() == 1) {
        		System.out.printf("  %-2s    ","immediate");
        	} else if(priQueue.getPatientList().get(i).getPriorityCode() == 2) {
        		System.out.printf("  %-2s    ","emergency");
        	} else if(priQueue.getPatientList().get(i).getPriorityCode() == 3) {
        		System.out.printf("  %-2s       ","urgent");
        	} else {
        		System.out.printf("  %-2s      ","minimal");
        	}
        	System.out.printf("   %-2s",priQueue.getPatientList()
        			.get(i).getName() + "\n");
        }
    }

    /**
     * Removes a patient from the waiting room and displays the name on the
     * screen.
     * @param priQueue priority queue to operate on
     */
    private static void dequeueNextPatient(
        PatientPriorityQueue priQueue) {
        if(priQueue.size() == 0) {
        	System.out.println("There are no patients in the waiting area.");
        } else {
        	System.out.println("This patient will now be seen: " + priQueue
        			.dequeue().getName());
        }
    }

    /**
     * Adds the patient to the waiting room.
     * @param lineScanner Scanner with remaining chars after the command
     * @param priQueue    priority queue to operate on
     */
    private static void addPatient(Scanner lineScanner,
                                   PatientPriorityQueue priQueue) {
        int priority = 0;
        String priorityCode = null;
        if(!lineScanner.hasNext()) {
        	System.out.println("Missing priority code.");
        } else {
        	priorityCode = lineScanner.next();
        	if(priorityCode.equals("immediate")) {
            	priority = 1;
            } else if(priorityCode.equals("emergency")) {
            	priority = 2;
            } else if(priorityCode.equals("urgent")) {
            	priority = 3;
            } else if(priorityCode.equals("minimal")) {
            	priority = 4;
            } else {
            	System.out.println("Priority code is not recognized.");
            	return;
            }
	        StringBuilder patientNameSB = new StringBuilder();
	        while(lineScanner.hasNext()) {
		        patientNameSB.append(lineScanner.next() + " ");
	        }
	        String patientName = patientNameSB.toString().trim();
	        if(!patientName.isEmpty()) {
	        	priQueue.addPatient(priority, patientName);
	        	System.out.println("Added patient \"" + patientName + "\" "
	        			+ "to the priority system");
	        } else {
	        	System.out.println("Missing patient name.");
	        }
        }
    }
    
    /**
     * Changes priority of a patient
     * @param lineScanner		Scanner with remaining chars after the command
     * @param priQueue			priority queue to operate on
     */
    private static void changePriority(Scanner lineScanner, 
    		PatientPriorityQueue priQueue) {
    	int patientNumber = 0;
    	String newPriority = null;
    	int priority = 0;
    	if(!lineScanner.hasNextInt()) {
    		System.out.println("Error: No patient id provided");
    	} else {
    		patientNumber = lineScanner.nextInt();
    		if(!lineScanner.hasNext()) {
        		System.out.println("Error: No priority code given.");
        	} else {
        		newPriority = lineScanner.next();
        		if(!newPriority.equals("immediate") 
        				&& !newPriority.equals("urgent") 
        				&& !newPriority.equals("emergency") 
        				&& !newPriority.equals("minimal")) {
        			System.out.println("Error: invalid priority level code");	
        		} else {
        			int index = checkExists(priQueue, patientNumber);
        			if(index != -1) {
        				if(newPriority.equals("immediate")) {
        		        	priority = 1;
        		        } else if(newPriority.equals("emergency")) {
        		        	priority = 2;
        		        } else if(newPriority.equals("urgent")) {
        		        	priority = 3;
        		        } else if(newPriority.equals("minimal")) {
        		        	priority = 4;
        		        }
	        			System.out.println("Changed patient \"" + 
        		        priQueue.getPatientList().get(index).getName() 
        		        + "\"'s priority to " + newPriority);
        				priQueue.changePri(index, priority);
	        		} else {
	        			System.out.println("Error: no patient with the given "
	        					+ "id was found");
	        		}
        		}
        	}
    	}
    	
    }
    
    /**
     * Check if a patient exists in the priority queue
     * @param priQueue		Priority queue to check
     * @param id			ID of patient to find
     * @return				Index of patient if found, -1 if not found
     */
    private static int checkExists(PatientPriorityQueue priQueue, int id) {
    	for(int i = 0; i < priQueue.size(); i++) {
    		if(id == priQueue.getPatientList().get(i).getArrivalOrder()) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    /**
     * Saves current state of priority queue to file to restore later if needed
     * @param lineScanner		Scanner with remaining chars after the command			
     * @param priQueue			Priority queue to operate on
     * @throws IOException
     */
    private static void saveCommands(Scanner lineScanner, 
    		PatientPriorityQueue priQueue) throws IOException {
    	if(!lineScanner.hasNext()) {
    		System.out.println("No file name entered.");
    	} else {
    		String fileName = lineScanner.next().trim();
    		priQueue.saveFile(fileName);
    		System.out.println("Saved " + priQueue.getPatientList().size()
    				+ " patients to file " + fileName);
    	}
    }

}
