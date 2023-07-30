import java.io.Serializable;
import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Glossary project. Read an input file from user and generate a series of
 * corresponding html files that contains glossary and their definition in each
 * page.
 *
 *
 * @author Zheyuan Gao
 */
public final class Glossary {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private Glossary() {
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param strSet
     *            the {@code Set} to be replaced
     * @replaces strSet
     * @ensures strSet = entries(str)
     */
    public static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!strSet.contains(ch)) {
                strSet.add(ch);
            }
        }

    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        char c = text.charAt(position);
        boolean isSeparator = false;

        /*
         * First check if the character at the given position is a separator
         */
        for (char separator : separators) {
            if (!isSeparator && separator == c) {
                isSeparator = true;
            }
        }

        StringBuilder subString = new StringBuilder();
        int endIndex = position;
        boolean end = false;
        /*
         * if the character is not a separator, then return the substring start
         * from position to the appearance of the next separator
         */
        if (!isSeparator) {
            while (!end && endIndex < text.length()) {

                if (!separators.contains(text.charAt(endIndex))) {
                    subString.append(text.charAt(endIndex));
                } else {
                    end = true;
                }
                endIndex++;

            }

        } else {
            /*
             * if the character is a separator, then return the substring start
             * at position and end at the first appearance of the character that
             * not in the separator set.
             */
            while (!end && endIndex < text.length()) {

                if (separators.contains(text.charAt(endIndex))) {
                    subString.append(text.charAt(endIndex));
                } else {
                    end = true;
                }
                endIndex++;
            }

        }
        return subString.toString();
    }

    /**
     * override the comparator to compare the alphabet of two strings.
     *
     * @ensure return negative number if str2's first character is in front of
     *         the str1's first character in alphabet order. 0 if the are same
     *         character. positive number if str1's first character is in front
     *         of str2's first character.
     */
    public static class StringLT implements Comparator<String>, Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        @Override
        public final int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /**
     * Read a text file from user and put sorted glossaries into the given queue
     * and put the glossary-definition pairs into the given map.
     *
     * @param input
     *            the simple reader to read from the file
     * @param glossary
     *            the queue to store the glossaries
     * @param wordMap
     *            the map to store pairs of word-definition
     * @require input is open. file exist and is not empty.
     *
     * @ensure Glossary stored in glossary queue in alphabet order. Glossary and
     *         their corresponding definition pairs stored into the given
     *         wordMap.
     */
    public static void readFileStoreInMap(SimpleReader input,
            Queue<String> glossary, Map<String, String> wordMap) {

        String word = "";
        String definition = "";
        while (!input.atEOS()) {
            /*
             * the first line of the file is always a glossary
             */
            word = input.nextLine();
            glossary.enqueue(word);
            /*
             * word is followed by its definition
             */
            definition = input.nextLine();
            /*
             * Definition can be longer than one line, check the next to see if
             * it's an empty line. Also need to make sure it's not the end of
             * the file
             */
            StringBuilder sb = new StringBuilder();
            String possibleDefination = "something";
            while (possibleDefination.length() != 0 && !input.atEOS()) {
                possibleDefination = input.nextLine();
                sb.append(possibleDefination);
            }
            definition += sb.toString();
            wordMap.add(word, definition.toString());
            //use the comparator to sort the string
            Comparator<String> compare = new StringLT();
            glossary.sort(compare);
        }
    }

    /**
     * Generate index.html(The main page).
     *
     * @param folder
     *            the string of the name of the folder to store the index page
     * @param termQueue
     *            Queue contains all the glossaries in the alphabet order
     * @require folder name corresponding folder exists, termQueue is not empty.
     *          terms in termQueue is in alphabet order.
     * @ensure output standard html format index page in the given folder.
     */
    public static void outputIndex(String folder, Queue<String> termQueue) {

        SimpleWriter out = new SimpleWriter1L(folder + "/index.html");
        /*
         * output the html format header
         */
        out.println("<html>");
        out.println(" <head>");
        out.println("  <title>Glossary</title>");
        out.println(" </head>");
        out.println(" <body>");
        out.println("  <h2>Glossary</h2>");
        out.println("  <hr>");
        out.println("  <h3>Index</h3>");
        /*
         * output the main body
         */
        out.println("  <ul>");
        for (int i = 0; i < termQueue.length(); i++) {
            String term = termQueue.dequeue();
            termQueue.enqueue(term);
            out.println("   <li>");
            out.println("    <a href=" + term + ".html>" + term + "</a>");
            out.println("   </li>");
        }
        out.println("  </ul>");
        /*
         * output the footer
         */
        out.println(" </body>");
        out.println("</html>");

        /*
         * close the simple writer
         */
        out.close();
    }

    /**
     * Generate glossary page that contain the definition of the term and link
     * to other glossary page.
     *
     * @param folder
     *            Folder choose by user to store all the html files
     * @param termQueue
     *            Queue contains all the glossaries in the alphabet order
     * @param termMap
     *            Map that contains the terms and their corresponding
     *            definitions
     * @param separator
     *            set that contains all the separator characters
     * @requires folder exists. termQueue is not empty. termMap is not empty.
     *           term in termQueue also are keys in termMap
     */
    public static void outputWordPage(String folder, Queue<String> termQueue,
            Map<String, String> termMap, Set<Character> separator) {

        for (int i = 0; i < termQueue.length(); i++) {

            String term = termQueue.dequeue();
            termQueue.enqueue(term);
            SimpleWriter out = new SimpleWriter1L(
                    folder + "/" + term + ".html");
            /*
             * output header for the term page
             */
            out.println("<html>");
            out.println(" <head>");
            out.println("  <title>term</title>");
            out.println(" </head>");
            out.println(" <body>");
            out.println("  <h2>");
            out.println("   <b>");
            out.println("    <i>");
            out.println("     <font color = red>" + term + "</font>");
            out.println("    </i>");
            out.println("   </b>");
            out.println("  </h2>");
            String defination = termMap.value(term);
            /*
             * start to split the definition word by word, and print the body of
             * the html file
             */
            out.println("  <blockquote>");
            out.print("    ");
            int position = 0;
            while (position < defination.length()) {
                String token = nextWordOrSeparator(defination, position,
                        separator);
                //if the given token is not a glossary
                if (!termMap.hasKey(token)) {
                    out.print(token);
                } else { //if the token is a glossary, create a link to its on page
                    out.println();
                    out.println(
                            "    <a href=" + token + ".html>" + token + "</a>");
                }
                position += token.length();
            }
            /*
             * output the footer for the word page
             */
            out.println("   </blockquote>");
            out.println("  <hr>");
            out.println("  <p>");
            out.println("   Return to ");
            out.println("   <a href = index.html>index</a>");
            out.println("   .");
            out.println("  </p>");
            out.println(" </body>");
            out.println("</html>");
            /*
             * close the simple writer
             */
            out.close();

        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        /*
         * Define separator characters for test
         */
        final String separatorStr = " \t, ";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separatorStr, separatorSet);
        /*
         * Open input and output streams
         */
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Ask for test cases
         */
        out.println();
        out.print("Please input the name of the glossary file: ");
        String file = in.nextLine();
        out.print("Please enter the folder you want to store the html files: ");
        String folder = in.nextLine();
        SimpleReader input = new SimpleReader1L(file);
        Queue<String> terms = new Queue1L<>();
        Map<String, String> termDefinition = new Map1L<>();
        /*
         * store the data into queue and map
         */
        readFileStoreInMap(input, terms, termDefinition);
        /*
         * generate index page
         */
        outputIndex(folder, terms);
        /*
         * generate glossaries pages
         */
        outputWordPage(folder, terms, termDefinition, separatorSet);
        /*
         * Close input and output streams
         */
        in.close();
        out.close();
        input.close();
    }
}
