import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;

public class GlossaryTest {

    /*
     * test cases for generateElements
     */
    @Test
    public void testGenerateElements_smallBoundary() {
        Set<Character> s = new Set1L();
        String str = "a";
        Glossary.generateElements(str, s);
        Set<Character> sExpected = new Set1L();
        sExpected.add('a');
        assertEquals(s, sExpected);
    }

    @Test
    public void testGenerateElements_smallBoundaryRepeat() {
        Set<Character> s = new Set1L();
        String str = "aa";
        Glossary.generateElements(str, s);
        Set<Character> sExpected = new Set1L();
        sExpected.add('a');
        assertEquals(s, sExpected);
    }

    @Test
    public void testGenerateElements_largeBoundary() {
        Set<Character> s = new Set1L();
        String str = "abcdefghijklmn";
        Glossary.generateElements(str, s);
        Set<Character> sExpected = new Set1L();
        sExpected.add('a');
        sExpected.add('b');
        sExpected.add('c');
        sExpected.add('d');
        sExpected.add('e');
        sExpected.add('f');
        sExpected.add('g');
        sExpected.add('h');
        sExpected.add('i');
        sExpected.add('j');
        sExpected.add('k');
        sExpected.add('l');
        sExpected.add('m');
        sExpected.add('n');
        assertEquals(s, sExpected);
    }

    @Test
    public void testGenerateElements_largeBoundaryRepeat() {
        Set<Character> s = new Set1L();
        String str = "aaaaaaaaaabbbbbbbbbccccccccddddddd";
        Glossary.generateElements(str, s);
        Set<Character> sExpected = new Set1L();
        sExpected.add('a');
        sExpected.add('b');
        sExpected.add('c');
        sExpected.add('d');
        assertEquals(s, sExpected);
    }

    @Test
    public void testGenerateElements_BoundarySpecialCharacter() {
        Set<Character> s = new Set1L();
        String str = ">[]< :)*";
        Glossary.generateElements(str, s);
        Set<Character> sExpected = new Set1L();
        sExpected.add('[');
        sExpected.add(']');
        sExpected.add(' ');
        sExpected.add('<');
        sExpected.add('>');
        sExpected.add(':');
        sExpected.add(')');
        sExpected.add('*');
        assertEquals(s, sExpected);
    }

    @Test
    public void testGenerateElements_BoundaryRepeatSpecialCharacterRepeat() {
        Set<Character> s = new Set1L();
        String str = "*****////>>>><<<<";
        Glossary.generateElements(str, s);
        Set<Character> sExpected = new Set1L();
        sExpected.add('*');
        sExpected.add('/');
        sExpected.add('<');
        sExpected.add('>');
        assertEquals(s, sExpected);
    }

    /*
     * Test for method nextWordOrSeparator
     */
    @Test
    public void testNextWordOrSeparator_separatorBoundarySmall() {
        Set<Character> separator = new Set1L<>();
        separator.add('a');
        String text = "babb";
        int position = 1;
        String subString = Glossary.nextWordOrSeparator(text, position,
                separator);
        String substringExpected = "a";
        assertEquals(subString, substringExpected);
    }

    @Test
    public void testNextWordOrSeparator_wordBoundarySmall() {
        Set<Character> separator = new Set1L<>();
        separator.add('a');
        String text = "abab";
        int position = 1;
        String subString = Glossary.nextWordOrSeparator(text, position,
                separator);
        String substringExpected = "b";
        assertEquals(subString, substringExpected);
    }

    @Test
    public void testNextWordOrSeparator_separatorBoundaryLarge() {
        Set<Character> separator = new Set1L<>();
        separator.add('a');
        separator.add('p');
        separator.add('l');
        separator.add('e');
        String text = "grab apple from tree";
        int position = 5;
        String subString = Glossary.nextWordOrSeparator(text, position,
                separator);
        String substringExpected = "apple";
        assertEquals(subString, substringExpected);
    }

    @Test
    public void testNextWordOrSeparator_wordBoundaryLarge() {
        Set<Character> separator = new Set1L<>();
        separator.add('a');
        separator.add('p');
        separator.add('l');
        separator.add('e');
        separator.add('t');
        String text = "grab apple from tree";
        int position = 11;
        String subString = Glossary.nextWordOrSeparator(text, position,
                separator);
        String substringExpected = "from ";
        assertEquals(subString, substringExpected);
    }

    @Test
    public void testNextWordOrSeparator_separatorSpecialWhiteSpace() {
        Set<Character> separator = new Set1L<>();
        separator.add(' ');
        String text = "grab apple from tree";
        int position = 0;
        String subString = Glossary.nextWordOrSeparator(text, position,
                separator);
        String substringExpected = "grab";
        assertEquals(subString, substringExpected);
    }

    @Test
    public void testNextWordOrSeparator_wordSpecialWhiteSpace() {
        Set<Character> separator = new Set1L<>();
        separator.add('b');
        separator.add('a');
        String text = "grab apple from tree";
        int position = 4;
        String subString = Glossary.nextWordOrSeparator(text, position,
                separator);
        String substringExpected = " ";
        assertEquals(subString, substringExpected);
    }

    /*
     * Test cases for readFileStoreInMap
     */
    @Test
    public void testReadFileStoreInMap_smallBoundry() {
        SimpleReader in = new SimpleReader1L("data/Test1");
        Queue<String> q = new Queue1L<>();
        Map<String, String> m = new Map1L<>();
        Glossary.readFileStoreInMap(in, q, m);
        Queue<String> qExpected = new Queue1L<>();
        Map<String, String> mExpected = new Map1L<>();
        qExpected.enqueue("meaning");
        mExpected.add("meaning",
                "something that one wishes to convey, especially by language");
        assertEquals(q, qExpected);
        assertEquals(m, mExpected);
    }

    @Test
    public void testReadFileStoreInMap_routine() {
        SimpleReader in = new SimpleReader1L("data/Test2");
        Queue<String> q = new Queue1L<>();
        Map<String, String> m = new Map1L<>();
        Glossary.readFileStoreInMap(in, q, m);
        Queue<String> qExpected = new Queue1L<>();
        Map<String, String> mExpected = new Map1L<>();
        qExpected.enqueue("meaning");
        qExpected.enqueue("term");
        mExpected.add("meaning",
                "something that one wishes to convey, especially by language");
        mExpected.add("term", "a word whose definition is in a glossary");
        assertEquals(q, qExpected);
        assertEquals(m, mExpected);
    }

    @Test
    public void testReadFileStoreInMap_specialTwoLinesDefinition() {
        SimpleReader in = new SimpleReader1L("data/Test4");
        Queue<String> q = new Queue1L<>();
        Map<String, String> m = new Map1L<>();
        Glossary.readFileStoreInMap(in, q, m);
        Queue<String> qExpected = new Queue1L<>();
        Map<String, String> mExpected = new Map1L<>();
        qExpected.enqueue("definition");
        qExpected.enqueue("glossary");
        qExpected.enqueue("meaning");
        qExpected.enqueue("term");
        qExpected.enqueue("word");
        mExpected.add("meaning",
                "something that one wishes to convey, especially by language");
        mExpected.add("term", "a word whose definition is in a glossary");
        mExpected.add("word",
                "a string of characters in a language, which has at least one character");
        mExpected.add("definition",
                "a sequence of words that gives meaning to a term");
        mExpected.add("glossary",
                "a list of difficult or specialized terms, with their definitions,usually near the end of a book");
        assertEquals(q, qExpected);
        assertEquals(m, mExpected);
    }

    @Test
    public void testReadFileStoreInMap_largeBoundry() {
        SimpleReader in = new SimpleReader1L("data/Test3");
        Queue<String> q = new Queue1L<>();
        Map<String, String> m = new Map1L<>();
        Glossary.readFileStoreInMap(in, q, m);
        Queue<String> qExpected = new Queue1L<>();
        Map<String, String> mExpected = new Map1L<>();
        qExpected.enqueue("book");
        qExpected.enqueue("definition");
        qExpected.enqueue("glossary");
        qExpected.enqueue("language");
        qExpected.enqueue("meaning");
        qExpected.enqueue("term");
        qExpected.enqueue("word");
        mExpected.add("meaning",
                "something that one wishes to convey, especially by language");
        mExpected.add("term", "a word whose definition is in a glossary");
        mExpected.add("word",
                "a string of characters in a language, which has at least one character");
        mExpected.add("definition",
                "a sequence of words that gives meaning to a term");
        mExpected.add("glossary",
                "a list of difficult or specialized terms, with their definitions,usually near the end of a book");
        mExpected.add("language",
                "a set of strings of characters, each of which has meaning");
        mExpected.add("book", "a printed or written literary work");
        assertEquals(q, qExpected);
        assertEquals(m, mExpected);
    }

    /*
     * Test cases for outputIndex
     */
    @Test
    public void testOutputIndex_smallBoundary() {
        String folder = "test";
        Queue<String> q = new Queue1L<>();
        q.enqueue("meaning");
        Glossary.outputIndex(folder, q);
        SimpleReader in = new SimpleReader1L("test/test5.html");
        SimpleReader input = new SimpleReader1L("test/index.html");
        while (!in.atEOS()) {
            String strExpected = in.nextLine();
            String str = input.nextLine();
            assertEquals(strExpected, str);
        }
        in.close();
        input.close();

    }

    @Test
    public void testOutputIndex_routine() {
        String folder = "test";
        Queue<String> q = new Queue1L<>();
        q.enqueue("book");
        q.enqueue("definition");
        Glossary.outputIndex(folder, q);
        SimpleReader in = new SimpleReader1L("test/test6.html");
        SimpleReader input = new SimpleReader1L("test/index.html");
        while (!in.atEOS()) {
            String strExpected = in.nextLine();
            String str = input.nextLine();
            assertEquals(strExpected, str);
        }
        in.close();
        input.close();

    }

    @Test
    public void testOutputIndex_routine2() {
        String folder = "test";
        Queue<String> q = new Queue1L<>();
        q.enqueue("book");
        q.enqueue("definition");
        q.enqueue("glossary");
        Glossary.outputIndex(folder, q);
        SimpleReader in = new SimpleReader1L("test/test7.html");
        SimpleReader input = new SimpleReader1L("test/index.html");
        while (!in.atEOS()) {
            String strExpected = in.nextLine();
            String str = input.nextLine();
            assertEquals(strExpected, str);
        }
        in.close();
        input.close();
    }

    @Test
    public void testOutputIndex_routine3() {
        String folder = "test";
        Queue<String> q = new Queue1L<>();
        q.enqueue("book");
        q.enqueue("definition");
        q.enqueue("glossary");
        q.enqueue("language");
        Glossary.outputIndex(folder, q);
        SimpleReader in = new SimpleReader1L("test/test8.html");
        SimpleReader input = new SimpleReader1L("test/index.html");
        while (!in.atEOS()) {
            String strExpected = in.nextLine();
            String str = input.nextLine();
            assertEquals(strExpected, str);
        }
        in.close();
        input.close();

    }

    @Test
    public void testOutputIndex_largeBoundary() {
        String folder = "test";
        Queue<String> q = new Queue1L<>();
        q.enqueue("book");
        q.enqueue("definition");
        q.enqueue("glossary");
        q.enqueue("language");
        q.enqueue("meaning");
        q.enqueue("term");
        q.enqueue("word");
        Glossary.outputIndex(folder, q);
        SimpleReader in = new SimpleReader1L("test/test9.html");
        SimpleReader input = new SimpleReader1L("test/index.html");
        while (!in.atEOS()) {
            String strExpected = in.nextLine();
            String str = input.nextLine();
            assertEquals(strExpected, str);
        }
        in.close();
        input.close();
    }

}
