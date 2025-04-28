import java.lang.reflect.Method;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        SpellChecker spellChecker = new SpellChecker("src/dictionary.txt");
        CensorModule expletivesDictionary = new CensorModule("src/badwords.txt");

        Scanner sc = new Scanner(System.in);

        int choice = 0;
        while (choice != 7) {
            System.out.println("*********************************************");
            System.out.println("*                  MENU                     *");
            System.out.println("* 1. Add a word to dictionary               *");
            System.out.println("* 2. Remove a word from dictionary          *");
            System.out.println("* 3. Add a word to expletives dictionary    *");
            System.out.println("* 4. Remove word from expletives dictionary *");
            System.out.println("* 5. Check and auto-correct a text file     *");
            System.out.println("* 6. Censor expletives from a text file     *");
            System.out.println("* 7. Exit                                   *");
            System.out.println("*********************************************");
            System.out.print("Choose an option: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter word to add: ");
                    String wordToAdd = sc.nextLine();
                    spellChecker.addWord(wordToAdd);
                    break;
                case 2:
                    System.out.print("Enter word to remove: ");
                    String wordToRemove = sc.nextLine();
                    spellChecker.removeWord(wordToRemove);
                    break;
                case 3:
                    System.out.print("Enter expletive to add: ");
                    String badWordToAdd = sc.nextLine();
                    expletivesDictionary.addExpletive(badWordToAdd);
                    break;
                case 4:
                    System.out.print("Enter expletive to remove: ");
                    String badWordToRemove = sc.nextLine();
                    expletivesDictionary.removeWord(badWordToRemove);
                    break;
                case 5:
                    System.out.print("Enter input file path: ");
                    String inputFile = sc.nextLine();
                    System.out.print("Enter output file path: ");
                    String outputFile = sc.nextLine();
                    spellChecker.processFile(inputFile, outputFile);
                    break;
                case 6:
                    System.out.print("Enter input file path: ");
                    String inputFile2 = sc.nextLine();
                    System.out.print("Enter output file path: ");
                    String outputFile2 = sc.nextLine();
                    expletivesDictionary.censorFile(inputFile2, outputFile2);
                    break;
                case 7:
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        try {
            Class<?> clazz = spellChecker.getClass();
            Method privateMethod = clazz.getDeclaredMethod("getLevenshteinDistance", String.class, String.class);
            privateMethod.setAccessible(true);
            Object result = privateMethod.invoke(spellChecker, "kitten", "sitting");
            int distance = (Integer) result;
            System.out.println("Levenshtein Distance = " + distance);
            System.out.println("**************************************************************************************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}