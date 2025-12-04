/**
 * @file: Proj4.java
 * @description: Runs performance tests for hash tables
 * @author: Chris Cha {@literal <chah22@wfu.edu>}
 * @date: December 3, 2025
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Proj4 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java TestAvl <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        // For file input
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // Open the input file
        inputFileNameStream = new FileInputStream(inputFileName);
        inputFileNameScanner = new Scanner(inputFileNameStream);

        // ignore first line
        inputFileNameScanner.nextLine();

        // creates array list with F1 data
        ArrayList<F1> data = new ArrayList<>(numLines);
        while (inputFileNameScanner.hasNextLine() && data.size() < numLines) {
            String line = inputFileNameScanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] stats = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            String driver = stats[0];
            String nationality = stats[1];
            String seasonsRaw = stats[2].replace("[", "").replace("]", "").replace("\"", "").trim();
            String[] seasons;
            if (seasonsRaw.isEmpty()) {
                seasons = new String[0];
            } else {
                seasons = seasonsRaw.split("\\s*,\\s*");
            }
            int championships = Integer.parseInt(stats[3]);
            int raceWins = Integer.parseInt(stats[4]);
            int podiums = Integer.parseInt(stats[5]);
            double points = Double.parseDouble(stats[6]);
            data.add(new F1(driver, nationality, seasons, championships, raceWins, podiums, points));
        }
        inputFileNameScanner.close();
        inputFileNameStream.close();

        // create sorted, shuffled, reversed lists with F1 dataset
        ArrayList<F1> sorted = new ArrayList<>(data);
        Collections.sort(sorted);
        ArrayList<F1> shuffled = new ArrayList<>(sorted);
        Collections.shuffle(shuffled);
        ArrayList<F1> reversed = new ArrayList<>(sorted);
        reversed.sort(Collections.reverseOrder());

        FileWriter analysisWriter = new FileWriter("analysis.txt", true);

        System.out.println("-----------Number of lines: " + numLines + "----------------");
        long start;
        long end;
        double insertTime;
        double searchTime;
        double deleteTime;

        /* Sorted List */
        SeparateChainingHashTable<F1> hashSorted = new SeparateChainingHashTable<>();
        // Insert
        start = System.nanoTime();
        for (F1 f : sorted)
            hashSorted.insert(f);
        end = System.nanoTime();
        insertTime = (end - start) / 1_000_000_000.0;

        // Search
        start = System.nanoTime();
        for (F1 f : sorted)
            hashSorted.contains(f);
        end = System.nanoTime();
        searchTime = (end - start) / 1_000_000_000.0;

        // Delete
        start = System.nanoTime();
        for (F1 f : sorted)
            hashSorted.remove(f);
        end = System.nanoTime();
        deleteTime = (end - start) / 1_000_000_000.0;

        // print on screen and analysis.txt
        System.out.printf("Sorted: Insert: %.6f s | Search: %.6f s | Delete: %.6f s%n", insertTime, searchTime, deleteTime);
        analysisWriter.write(numLines + ",sorted," + insertTime + "," + searchTime + "," + deleteTime + "\n");

        /* Shuffled List */
        SeparateChainingHashTable<F1> hashShuffled = new SeparateChainingHashTable<>();
        // Insert
        start = System.nanoTime();
        for (F1 f : shuffled)
            hashShuffled.insert(f);
        end = System.nanoTime();
        insertTime = (end - start) / 1_000_000_000.0;

        // Search
        start = System.nanoTime();
        for (F1 f : shuffled)
            hashShuffled.contains(f);
        end = System.nanoTime();
        searchTime = (end - start) / 1_000_000_000.0;

        // Delete
        start = System.nanoTime();
        for (F1 f : shuffled)
            hashShuffled.remove(f);
        end = System.nanoTime();
        deleteTime = (end - start) / 1_000_000_000.0;

        // print on screen and analysis.txt
        System.out.printf("Shuffled: Insert: %.6f s | Search: %.6f s | Delete: %.6f s%n", insertTime, searchTime, deleteTime);
        analysisWriter.write(numLines + ",shuffled," + insertTime + "," + searchTime + "," + deleteTime + "\n");

        /* Reversed List */
        SeparateChainingHashTable<F1> hashReversed = new SeparateChainingHashTable<>();
        // Insert
        start = System.nanoTime();
        for (F1 f : reversed)
            hashReversed.insert(f);
        end = System.nanoTime();
        insertTime = (end - start) / 1_000_000_000.0;

        // Search
        start = System.nanoTime();
        for (F1 f : reversed)
            hashReversed.contains(f);
        end = System.nanoTime();
        searchTime = (end - start) / 1_000_000_000.0;

        // Delete
        start = System.nanoTime();
        for (F1 f : reversed)
            hashReversed.remove(f);
        end = System.nanoTime();
        deleteTime = (end - start) / 1_000_000_000.0;

        // print on screen and analysis.txt
        System.out.printf("Reversed: Insert: %.6f s | Search: %.6f s | Delete: %.6f s%n", insertTime, searchTime, deleteTime);
        analysisWriter.write(numLines + ",reversed," + insertTime + "," + searchTime + "," + deleteTime + "\n");
        analysisWriter.close();
    }
}
