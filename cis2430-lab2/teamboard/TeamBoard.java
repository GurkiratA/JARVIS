/*
 * CIS*2430 F26 - Lab Exercise Two: Team Noticeboard
 *
 * Name:       <YOUR NAME>
 * Student ID: <YOUR STUDENT ID>
 *
 * Compile (from the folder that contains the "teamboard" folder):
 *     javac teamboard/*.java
 *
 * Run:
 *     java teamboard.TeamBoard
 *
 * Bundle and run as a JAR (optional):
 *     jar cvfe TeamBoard.jar teamboard.TeamBoard teamboard/*.class
 *     java -jar TeamBoard.jar
 */
package teamboard;

import java.util.ArrayList;
import java.util.Scanner;

public class TeamBoard {

    private ArrayList<Captain> captains;
    private ArrayList<Notice> notices;
    private Scanner input;

    public TeamBoard(Scanner input) {
        this.captains = new ArrayList<>();
        this.notices = new ArrayList<>();
        this.input = input;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TeamBoard board = new TeamBoard(scanner);
        board.run();
        scanner.close();
    }

    /**
     * Main command loop.
     */
    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Enter your choice: ");
            if (choice == null) {
                break; // end of input
            }
            switch (choice.trim()) {
                case "1":
                    createCaptain();
                    break;
                case "2":
                    createNotice();
                    break;
                case "3":
                    viewAllNotices();
                    break;
                case "4":
                    viewNoticesByHandle();
                    break;
                case "5":
                    viewNoticesByKeyword();
                    break;
                case "6":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 6.");
            }
            System.out.println();
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("(1) Create new captain");
        System.out.println("(2) Create new notice");
        System.out.println("(3) View all notices");
        System.out.println("(4) View all notices with a given handle");
        System.out.println("(5) View all notices with a given keyword");
        System.out.println("(6) End Program");
    }

    /**
     * Option 1: register a new captain.
     */
    private void createCaptain() {
        String fullName = readLine("Enter full name: ");
        if (fullName == null) {
            return;
        }
        fullName = fullName.trim();
        if (fullName.isEmpty()) {
            System.out.println("Full name cannot be empty. Captain not created.");
            return;
        }

        String handle = readLine("Enter handle (leave blank to use first name): ");
        if (handle == null) {
            return;
        }
        handle = handle.trim();
        if (handle.isEmpty()) {
            // Default to the first word of the full name
            handle = fullName.split("[ ]+")[0];
        } else {
            // A handle is a single word
            handle = handle.split("[ ]+")[0];
        }
        handle = handle.toLowerCase();

        if (findCaptain(handle) != null) {
            System.out.println("A captain with handle @" + handle + " already exists. Request rejected.");
            return;
        }

        Captain captain = new Captain(fullName, handle);
        captains.add(captain);
        System.out.println("Captain " + captain + " registered.");
    }

    /**
     * Option 2: create a new notice for a registered captain.
     */
    private void createNotice() {
        String handle = readLine("Enter your handle: ");
        if (handle == null) {
            return;
        }
        Captain captain = findCaptain(handle);
        if (captain == null) {
            System.out.println("Captain @" + handle.trim().toLowerCase()
                    + " is not registered and does not have permission to post. Request rejected.");
            return;
        }

        String title = readLine("Enter title: ");
        if (title == null) {
            return;
        }
        String body = readLine("Enter body: ");
        if (body == null) {
            return;
        }

        notices.add(new Notice(title, body, captain));
        System.out.println("Notice posted.");
    }

    /**
     * Option 3: print every notice, oldest first.
     */
    private void viewAllNotices() {
        if (notices.isEmpty()) {
            System.out.println("There are no notices.");
            return;
        }
        for (Notice notice : notices) {
            printNotice(notice);
        }
    }

    /**
     * Option 4: print every notice posted by the captain with a given handle.
     */
    private void viewNoticesByHandle() {
        String handle = readLine("Enter handle: ");
        if (handle == null) {
            return;
        }
        int count = 0;
        for (Notice notice : notices) {
            if (notice.isPostedBy(handle)) {
                printNotice(notice);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No notices found for @" + handle.trim().toLowerCase() + ".");
        }
    }

    /**
     * Option 5: print every notice whose title or body contains a keyword.
     */
    private void viewNoticesByKeyword() {
        String keyword = readLine("Enter keyword: ");
        if (keyword == null) {
            return;
        }
        int count = 0;
        for (Notice notice : notices) {
            if (notice.containsKeyword(keyword)) {
                printNotice(notice);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No notices found with keyword \"" + keyword.trim() + "\".");
        }
    }

    /**
     * Searches the captain list for a captain with the given handle.
     *
     * @return the matching captain, or null if none is registered
     */
    private Captain findCaptain(String handle) {
        for (Captain captain : captains) {
            if (captain.hasHandle(handle)) {
                return captain;
            }
        }
        return null;
    }

    private void printNotice(Notice notice) {
        System.out.println();
        System.out.println(notice);
    }

    /**
     * Prints a prompt and reads one line, or returns null at end of input.
     */
    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!input.hasNextLine()) {
            return null;
        }
        return input.nextLine();
    }
}
