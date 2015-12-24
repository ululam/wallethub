package com.wallethub.entrancetask.az;

import com.sun.xml.internal.ws.client.SEIPortInfo;
import com.wallethub.entrancetask.az.Task3.PhraseCount;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wallethub.entrancetask.az.Task3.SEP;
import static com.wallethub.entrancetask.az.Task3.topStrings;
import static org.testng.Assert.*;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
public class Task3Test {

    @Test
    public void testTopStrings() throws Exception {
        File file = File.createTempFile("phrase", "count");
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

    public void integerationTestOnHugeFile() throws Exception {
        File file = new File(Utils.USER_HOME + File.separator + "largefile.txt");
        prepareLargeFile(file);

        topStrings(file, 100_000);
    }

    private static final String[] STRINGS = {
        "First string which is not too short",
        "Second string which is not too short string which is not too short",
        "Thirds string which is not too short string which is not too short string which is not too short"
    };


    private static void prepareLargeFile(File file) throws IOException {
        System.out.println("Writing to " + file.getAbsolutePath());
        file.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(file)) {
            List<String> tokens = new ArrayList<>(50);
            for (int i = 1; i < 200_000_000; i++) {
                int index = RandomUtils.nextInt(2);
                int suffix = RandomUtils.nextInt(500);
                tokens.add(STRINGS[index] + suffix);
                if (i%51 == 0) {
                    fw.write(String.join("|", tokens) + Utils.LINE_END);
                    tokens.clear();
                }


                if (i%10_000_000 == 0)
                    System.out.println("Wrote " + i);

            }

        }

    }


    public static void main(String[] args) throws IOException {
        prepareLargeFile(new File(Utils.USER_HOME + File.separator + "largefile.txt"));
//        prepareLargeFile(new File(Utils.USER_HOME + File.separator + "mediumfile.txt"));
    }
}