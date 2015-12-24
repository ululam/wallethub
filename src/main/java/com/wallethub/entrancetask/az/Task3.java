package com.wallethub.entrancetask.az;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static com.wallethub.entrancetask.az.Utils.out;
import static com.wallethub.entrancetask.az.Utils.t;

/**
 * Assumptions:
 * 1) We have at least file.size() more free disk space available
 *
 * @author alexey.zakharchenko@incryptex.com
 */
public class Task3 {
    public static final String SEP = "|";

    private static final long MAX_MEM_AVAILABLE = Runtime.getRuntime().maxMemory();

    /**
     * Net complexity is O(n) + O(top), where n is total number of tokens in file
     * Memory consumption is O(n) limited by Xmx/2 + const (won't grow after Xmx/2 consumed)
     *
     * @param file File to parse. May be very large (out of RAM)
     * @param top  How many top strings to include
     * @return Top [top] strings in the given file
     */
    public static List<PhraseCount> topStrings(File file, int top) throws IOException {
        if (top < 1)
            return Collections.emptyList();

        long start = t();
        Collection<File> parts = split(file);
        out("Splitting into %s parts took %s ms", parts.size(), (t() - start));

        // After split operation, in worst case, we may have 1 file containing only 1 repeating string - but it won't lead to OEM
        //  because its same string (only count will be increased, not String objects in memory)

        long start2 = t();
        List<PhraseCount> countedParts = countInParts(parts, top);
        out("Count from parts took took %s ms", (t() - start2));
        out("Total time for %s top phrases is %s ms", top, (t() - start));

        return countedParts;
    }

    // Split large file into number of smaller part files based upon hashcodes
    // We achieve 2 goals:
    // 1) Part file completely fits in memory (most of the times)
    // 2) Same strings are in one file
    //
    // If, in worst case, we have again 1 (or more) large file that does not fit in memory - it consists of at most several different string
    //  which will not eat memory as they are used as map keys
    // O(n)
    private static Collection<File> split(File file) throws IOException {
        // Let's be paranoid and use twice less memory than max
        long memoryWeMayUse = MAX_MEM_AVAILABLE >> 1;

        out("Total memory available: " + (MAX_MEM_AVAILABLE >> 20) + " Mb");
        out("Memory we may use: " + (memoryWeMayUse >> 20) + " Mb");
        out("File size: " + (file.length() >> 20) + " Mb");

        // I believe we are not running on mobile device, so its INT
        int num = (int) (file.length() / memoryWeMayUse);
        // File completely fits into memory if 0
        final int filesNumber = num == 0 ? 1 : num;

        out("Parts number: " + filesNumber);

        final Collection<File> results = new HashSet<>(filesNumber);
        final Map<Integer, FileWriter> hashcode2WriterMap = new HashMap<>(filesNumber);

        final long start = t();
        try (Scanner scanner = new Scanner(file)) {
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Arrays.asList(line.split('\\' + SEP))
                        .forEach(phrase -> {
                                    // @todo Here, we may run out of disk space - handle it correctly
                                    File partFile = writeToProperFile(phrase, hashcode2WriterMap, filesNumber);
                                    if (partFile != null) {
                                        results.add(partFile);
                                    }
                                }
                        );

                if (++i % 100_000 == 0)
                    out("Processed %s lines in %s secs", i, (t() - start)/1000);

            }
        } finally {
            hashcode2WriterMap.entrySet().forEach(e -> {
                try {
                    e.getValue().flush();
                    e.getValue().close();
                } catch (IOException ioe) {
                    System.err.println("Unable to flush/close file, possible data loss: " + e.getValue());
                }
            });

        }

        return results;
    }

    // O(n)
    private static List<PhraseCount> countInParts(Collection<File> parts, int top) throws IOException {
        Queue<PhraseCount> orderedStack = new PriorityQueue<>(top);

        for (File part : parts) {
            int size = (int) (part.length() >> 32);
            if (size == 0)
                size = 16;

            Map<String, Integer> counts = new HashMap<>(size);
            Files.lines(part.toPath()).forEach(line -> {
                int count = counts.getOrDefault(line, 0);
                counts.put(line, ++count);
            });

            // O(n) + O(n * log(topNumber)) = O(n)
            counts.forEach((k,v) -> {
                orderedStack.add(new PhraseCount(k,v));
                if (orderedStack.size() > top)
                    orderedStack.remove();
            });

            part.delete();
        }

        List<PhraseCount> finalResult = new ArrayList<>(orderedStack.size());
        // O(top)
        while (!orderedStack.isEmpty())
            finalResult.add(orderedStack.remove());

        // O(top)
        Collections.reverse(finalResult);

        return finalResult;
    }

    private static File writeToProperFile(String phrase, Map<Integer, FileWriter> hashcode2WriterMap, int filesNumber) {
        File file = null;
        int number = Math.abs(phrase.hashCode() % filesNumber); // Don't need negative hashcodes
        try {
            FileWriter fw = hashcode2WriterMap.get(number);
            if (fw == null) {
                file = File.createTempFile("part", "count");
                file.deleteOnExit();
                fw = new FileWriter(file);
                hashcode2WriterMap.put(number, fw);
            }

            fw.write(phrase + Utils.LINE_END);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    public static class PhraseCount implements Comparable<PhraseCount> {
        public final String phrase;
        public final long count;

        public PhraseCount(String phrase, long count) {
            this.phrase = phrase;
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PhraseCount that = (PhraseCount) o;

            if (count != that.count) return false;
            return phrase != null ? phrase.equals(that.phrase) : that.phrase == null;

        }

        @Override
        public int hashCode() {
            int result = phrase != null ? phrase.hashCode() : 0;
            result = 31 * result + (int) (count ^ (count >>> 32));
            return result;
        }

        @Override
        public int compareTo(PhraseCount o) {
            if (o == null) return 1;

            return this.count > o.count ? 1 : this.count < o.count ? -1 : 0;
        }

        @Override
        public String toString() {
            return "PC {" + phrase + " => " + count + '}';
        }
    }
}
