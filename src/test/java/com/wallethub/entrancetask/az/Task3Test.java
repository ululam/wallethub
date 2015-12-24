package com.wallethub.entrancetask.az;

import com.wallethub.entrancetask.az.Task3.PhraseCount;
import org.apache.commons.lang.math.RandomUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wallethub.entrancetask.az.Task3.SEP;
import static com.wallethub.entrancetask.az.Task3.topStrings;
import static com.wallethub.entrancetask.az.Utils.out;
import static com.wallethub.entrancetask.az.Utils.t;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
public class Task3Test {

    @Test
    public void testTopStrings() throws Exception {
        File file = File.createTempFile("phrase", "count");
        file.deleteOnExit();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("One one one"); fw.write(SEP);
            fw.write("Two"); fw.write(SEP);
            fw.write("Two"); fw.write(SEP);
            fw.write("Two"); fw.write(SEP);
            fw.write("Three33"); fw.write(SEP);
            fw.write("Three33"); fw.write(SEP);
            fw.write("Three33"); fw.write(SEP);
            fw.write("Three33"); fw.write(SEP);
            fw.write("4"); fw.write(SEP);
            fw.write("4");
        }

        assertTrue(topStrings(file, 0).isEmpty());

        List<PhraseCount> list = topStrings(file, 1);
        assertEquals(list.size(), 1);
        assertTrue(list.contains(new PhraseCount("Three33", 4)));

        list = topStrings(file, 2);
        assertEquals(list.size(), 2);

        assertEquals(list.get(0), new PhraseCount("Three33", 4));
        assertEquals(list.get(1), new PhraseCount("Two", 3));

        list = topStrings(file, 3);
        assertEquals(list.size(), 3);
        assertEquals(list.get(0), new PhraseCount("Three33", 4));
        assertEquals(list.get(1), new PhraseCount("Two", 3));
        assertEquals(list.get(2), new PhraseCount("4", 2));

        list = topStrings(file, 4);
        assertEquals(list.size(), 4);
        assertEquals(list.get(0), new PhraseCount("Three33", 4));
        assertEquals(list.get(1), new PhraseCount("Two", 3));
        assertEquals(list.get(2), new PhraseCount("4", 2));
        assertEquals(list.get(3), new PhraseCount("One one one", 1));

        list = topStrings(file, 5);
        assertEquals(list.size(), 4);

        file.delete();
    }

    @Test(enabled = false, groups = "Integration")
    public void integrationTestOnHugeFile() throws Exception {
        File file = new File(Utils.USER_HOME + File.separator + "largefile.txt");

        if (!file.exists())
            prepareLargeFile(file); // Very slow operation

        long start = t();
        int top = 100_000;
        List<PhraseCount> list = topStrings(file, top);
        out("Got top %s phrases in %s ms", top, (t() - start));
        out("Printing first 100 of them");
        for (int i = 0; i < 100; i++) {
            out((i+1) + "\t\t" + list.get(i) );
        }

        assertEquals(list.size(), top);
    }

    private static final String[] STRINGS = {
        "First string which is not too short",
        "Second string which is not too short string which is not too short",
        "Thirds string which is not too short string which is not too short string which is not too short"
    };


    private static void prepareLargeFile(File file) throws IOException {
        System.out.println("Writing to " + file.getAbsolutePath());
        file.getParentFile().mkdirs();

        final long start = t();
        try (FileWriter fw = new FileWriter(file)) {
            List<String> tokens = new ArrayList<>(50);
            for (int i = 1; i < 200_000_000; i++) {
                int index = RandomUtils.nextInt(2);
                int suffix = RandomUtils.nextInt(100_000); // Provide enough cardinality
                tokens.add(STRINGS[index] + suffix);
                if (i%51 == 0) {
                    fw.write(String.join("|", tokens));
                    fw.write(Utils.LINE_END);
                    tokens.clear();
                }


                if (i%10_000_000 == 0)
                    out("Wrote %s, time passed %s ms, %s records per second", i, (t() - start), (long)i*1000/(t() - start));
            }

        }

    }

}