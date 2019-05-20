import java.io.*;
import java.math.*;
import java.util.*;

public class B {
    Scanner in;
    PrintWriter out;
    final String limit = "1111111";
    String ruAlph = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    String enAlph = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String alph = "", key;
    Map<String, Integer> charToInt;
    Map<Integer, String> intToChar;
    int alphLen = 0;

    String checkAlph(StringBuilder s) {
        String alph = "";
        for (int i = 0; i < s.length(); i++) {
            if (ruAlph.indexOf(s.charAt(i)) != -1) {
                alph = ruAlph;
                break;
            }
            else if (enAlph.indexOf(s.charAt(i)) != -1) {
                alph = enAlph;
                break;
            }
        }
        return alph;
    }

    int decToBinary(int dec) {
        StringBuilder binary = new StringBuilder();
        while (dec > 0) {
            binary.append(dec % 2);
            dec >>= 1;
        }
        return binary.length() == 0 ? 0 : Integer.valueOf(binary.reverse().toString());
    }

    void generateRandomKey(int len) {
        StringBuilder tempKey = new StringBuilder();
        for (int i = 0; i < len; i++) {
            tempKey.append(new Random().nextInt(2));
        }
        key = tempKey.toString();
    }

    void computations() throws Exception {
        // Read the input.txt
        createInput("input.txt");
        StringBuilder s = new StringBuilder();
        while (in.hasNextLine()) {
            s.append(in.nextLine());
        }
        alph = checkAlph(s);
        if (alph.compareTo("") == 0) {
            throw new Exception("Enter russian or english text");
        }

        // Prep the data structures, generate the key
        alph += " 0123456789 .,-!?:();";
        alphLen = alph.length();
        generateRandomKey(limit.length());
        charToInt = new TreeMap<>();
        intToChar = new TreeMap<>();

        // Generate all mappings for 7-digit binaries
        boolean isFullyGenerated = false;
        for (int i = 0; !isFullyGenerated; i++) {
            int res = decToBinary(i);
            char ch = (i < alphLen) ? alph.charAt(i): ((char) (alph.charAt(i % alphLen) + alphLen));
            String put = String.valueOf(ch);
            charToInt.put(put, res);
            intToChar.put(res, put);
            isFullyGenerated = (intToChar.containsKey(Integer.valueOf(limit))) || isFullyGenerated;
        }
        cypher(s);
        decypher();
    }

    void cypher(StringBuilder s) {
        createOutput("crypted.txt");
        StringBuilder crypted = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            String a = s.charAt(i) + "";
            int c = charToInt.get(a);
            crypted.append(solve(c));
        }
        out.print(crypted);
        closeOutput(out);
    }

    void decypher() {
        createInput("crypted.txt");
        createOutput("decrypted.txt");
        StringBuilder crypted = new StringBuilder();
        StringBuilder decrypted = new StringBuilder();
        while (in.hasNextLine()) {
            crypted.append(in.nextLine());
        }
        for (int i = 0; i < crypted.length(); i++) {
            String a = crypted.charAt(i) + "";
            int c = charToInt.get(a);
            decrypted.append(solve(c));
        }
        out.println(decrypted);
        closeInput(in);
        closeOutput(out);
    }

    /*
    XOR the given char in binary form with key (right now with hardcoded length of 7 symbols).
    1. Ensure that length of given binary char is equal with length of key.
    2. Ensure that returning char doesn't contain the leading zeroes.
     */
    String solve(int ch) {
        StringBuilder res = new StringBuilder();
        String s = String.valueOf(ch);
        // 1
        while (s.length() < key.length()) {
            s = "0" + s;
        }
        for (int i = 0; i < s.length(); i++) {
            char l = s.charAt(i);
            char r = key.charAt(i);
            res.append(l ^ r);
        }
        // 2
        res = new StringBuilder(res.toString().replaceFirst("^0+(?!$)", ""));
        return intToChar.get(Integer.valueOf(res.toString()));
    }

    void run() throws Exception {
        computations();
        exit();
    }

    public static void main(String[] args) throws Exception {
        new B().run();
    }

    void createInput(String inputName) {
        try {
            in = new Scanner(new File(inputName), "Unicode");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void createOutput(String outputName) {
        try {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputName), "Unicode"), true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeInput(Scanner in) {
        in.close();
    }

    void closeOutput(PrintWriter out) {
        out.close();
    }

    void exit() {
        System.exit(0);
    }

    static class Pair<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Pair<K, V>> {
        K first;
        V second;

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }

        K getLeft() {
            return this.first;
        }

        void setLeft(K first) {
            this.first = first;
        }

        V getRight() {
            return this.second;
        }

        void setRight(V right) {
            this.second = right;
        }

        @Override
        public int compareTo(Pair<K, V> pair) {
            if (this.getLeft().compareTo(pair.getLeft()) != 0)
                return this.getLeft().compareTo(pair.getLeft());
            else
                return this.getRight().compareTo(pair.getRight());
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) {
                return true;
            } else if(!(obj instanceof Pair)) {
                return false;
            } else {
                Pair<?, ?> other = (Pair)obj;
                return Objects.equals(this.getLeft(), other.getLeft()) && Objects.equals(this.getRight(), other.getRight());
            }
        }

        @Override
        public int hashCode() {
            return (this.getLeft() == null?0:this.getLeft().hashCode()) ^ (this.getRight() == null?0:this.getRight().hashCode());
        }

        @Override
        public String toString() {
            return "" + '(' + this.getLeft() + ',' + this.getRight() + ')';
        }

        public String toString(String format) {
            return String.format(format, new Object[]{this.getLeft(), this.getRight()});
        }
    }
}
