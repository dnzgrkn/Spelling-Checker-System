import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class SpellChecker {
    private HashSet<String> dictionary;
    private String dictionaryFilePath;




    public SpellChecker(String dictionaryFilePath) {
        this.dictionaryFilePath = dictionaryFilePath;
        dictionary = new HashSet<>();
        try(BufferedReader br = new BufferedReader(new FileReader(dictionaryFilePath))) {
            while (br.readLine() != null) {
                dictionary.add(br.readLine());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setDictionaryFilePath(String dictionaryFilePath) {
        this.dictionaryFilePath = dictionaryFilePath;
    }

    public String getDictionaryFilePath() {
        return dictionaryFilePath;
    }

    public void setDictionary(HashSet<String> dictionary) {
        this.dictionary = dictionary;
    }

    public HashSet<String> getDictionary() {
        return dictionary;
    }



    public void printDictionary() {
        for (String word : dictionary) {
            System.out.println(word);
        }
    }




    public void saveDictionary() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(dictionaryFilePath))) {
            for (String word : dictionary) {
                bw.write(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void addWord(String word) {
        dictionary.add(word);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(dictionaryFilePath, true))) {
        bw.write(word);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public void removeWord(String word) {
        try(BufferedReader br = new BufferedReader(new FileReader(dictionaryFilePath))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (line.equals(word)) {
                    dictionary.remove(line);
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(dictionaryFilePath))) {
                        bw.write(word);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }





    private int getLevenshteinDistance(String a,String b) {
        char[] aArray = a.toCharArray();
        char[] bArray = b.toCharArray();
        int sames=0;
        int substitutions=0;
        int insertions=0;

        if(a.length()>b.length()){
            insertions=a.length()-b.length();
            for(int i=0;i<b.length();i++){
                if(bArray[i]==aArray[i]){
                    sames++;
                }else if(bArray[i]!=aArray[i]){
                    substitutions++;
                }
            }
            System.out.println(a + " vs " + b + " --->>> Distance would be " + (substitutions+insertions) + " " + substitutions + " substitutions and " + insertions + " insertions.");
            return (substitutions+insertions);

        } else if (b.length()>a.length()) {
            insertions=b.length()-a.length();
            for(int i=0;i<a.length();i++){
                if(aArray[i]==bArray[i]){
                    sames++;
                }else if(aArray[i]!=bArray[i]){
                    substitutions++;
                }
            }
            System.out.println("**************************************************************************************");
            System.out.println(a + " vs " + b + " --->>> Distance would be " + (substitutions+insertions) + " " + substitutions + " substitutions and " + insertions + " insertions.");
            return (substitutions+insertions);
        } else {
            insertions=0;
            for(int i=0;i<b.length();i++){
                if(bArray[i]==aArray[i]){
                    sames++;
                }else if(bArray[i]!=aArray[i]){
                    substitutions++;
                }
            }
            System.out.println(a + " vs " + b + " --->>> Distance would be " + (substitutions+insertions) + " " + substitutions + " substitutions and " + insertions + " insertions.");
            return (substitutions+insertions);
        }
    }



    public List<String> suggestWords(String input) {
        List<String> suggestions = new ArrayList<>();
        for (String word : dictionary) {
            if (getLevenshteinDistance(word, input) <= 1) {
                suggestions.add(word);
            }
        }
        for (String word : suggestions) {
            System.out.println(word);
        }
        return suggestions;
    }



    public void processFile(String inputPath, String outputPath){
        Scanner scanner = new Scanner(System.in);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputPath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];


                    String cleanWord = word.replaceAll("[^a-zA-Z]", "");
                    String punctuation = word.replaceAll("[a-zA-Z]", "");

                    if (!cleanWord.isEmpty() && !dictionary.contains(cleanWord.toLowerCase())) {
                        System.out.println("Misspelled word found: " + cleanWord);
                        List<String> suggestions = suggestWords(cleanWord.toLowerCase());

                        if (!suggestions.isEmpty()) {
                            System.out.println("Did you mean: " + suggestions.get(0) + "? (Y/N/Enter your suggestion)");
                            String response = scanner.nextLine().trim();
                            if (response.equalsIgnoreCase("Y")) {
                                cleanWord = suggestions.get(0);
                            } else if (response.equalsIgnoreCase("N")) {
                                System.out.println("Enter your own replacement:");
                                String customWord = scanner.nextLine().trim();
                                if (!customWord.isEmpty()) {
                                    cleanWord = customWord;
                                }
                            }

                        } else {
                            System.out.println("No suggestions found for: " + cleanWord);
                            System.out.println("Enter your own replacement (or press Enter to keep it):");
                            String customWord = scanner.nextLine().trim();
                            if (!customWord.isEmpty()) {
                                cleanWord = customWord;
                            }
                        }
                    }


                    writer.write(cleanWord + punctuation);

                    if (i < words.length - 1) {
                        writer.write(" ");
                    }
                }
                writer.newLine();
            }

            reader.close();
            writer.close();
            System.out.println("File processed successfully! Output saved to: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
