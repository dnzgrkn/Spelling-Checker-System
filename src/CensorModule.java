import java.io.*;
import java.util.HashSet;

public class CensorModule {
    private HashSet<String> badWords;
    private String expletiveDictPath;

    public CensorModule(String expletiveDictPath) {
        this.expletiveDictPath = expletiveDictPath;
        badWords = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(expletiveDictPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                badWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void saveExpletiveDictionary() {
        try(BufferedWriter writer= new BufferedWriter(new FileWriter(expletiveDictPath))){
            for (String word : badWords) {
                writer.write(word);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addExpletive(String word) {
        for (String badWord : badWords) {
            if (word.contains(badWord)) {
                System.out.println("The word " + word + " is already in the dictionary.");
            }
            else {
                badWords.add(word);
                try(BufferedWriter writer = new BufferedWriter(new FileWriter(expletiveDictPath,true))){
                    writer.write(word);

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void removeWord(String word) {
        try(BufferedReader br = new BufferedReader(new FileReader(expletiveDictPath))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (line.equals(word)) {
                    badWords.remove(line);
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(expletiveDictPath))) {
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




    public void censorFile(String inputPath, String outputPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");

                for (int i = 0; i < words.length; i++) {
                    String word = words[i];


                    String cleanWord = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
                    String punctuation = word.replaceAll("[a-zA-Z]", "");

                    if (badWords.contains(cleanWord)) {

                        String censored = "\u001B[31m[CENSORED]\u001B[0m";
                        writer.write(censored + punctuation);
                    } else {
                        writer.write(word);
                    }

                    if (i < words.length - 1) {
                        writer.write(" ");
                    }
                }
                writer.newLine();
            }

            System.out.println("File censored successfully! Output saved to: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
