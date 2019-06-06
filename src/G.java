import java.io.*;
import java.util.*;

public class G {
    Scanner in;
    PrintWriter out;
    String ruAlph = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    String enAlph = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String alph = "", key, cryptKey;
    List<List<Character>> table = new ArrayList<>();
    int alphLen = 0, k = 6, keyLen;
    boolean isRuAlph = false;

    String checkAlph(StringBuilder s) {
        String alph = "";
        for (int i = 0; i < s.length(); i++) {
            if (ruAlph.indexOf(s.charAt(i)) != -1) {
                alph = "абвгдежзиклмнопрстуфхцчшщьыэюя";
                break;
            }
            else if (enAlph.indexOf(s.charAt(i)) != -1) {
                alph = "abcdefghiklmnopqrstuvwxyz";
                k = 5;
                break;
            }
        }
        return alph;
    }

    void generateTable(String alph) {
        for (int i = 0; i < k; i++) {
            List<Character> temp = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                temp.add(' ');
            }
            table.add(temp);
        }

        if (isRuAlph) {
            key = key.replaceAll("ё", "е");
            key = key.replaceAll("й", "и");
            key = key.replaceAll("ъ", "ь");
        }
        else {
            key = key.replaceAll("j", "i");
        }

        int col = 0, row = 0;

        for (int i = 0; i < keyLen; i++) {
            if (i == key.lastIndexOf(key.charAt(i))) {
                table.get(row).set(col, key.charAt(i));
                row = (col < 4) ? row : row + 1;
                col = (col + 1) % 5;
            }
        }

        for (int i = 0; i < alphLen; i++) {
            if (key.indexOf(alph.charAt(i)) == -1) {
                table.get(row).set(col, alph.charAt(i));
                row = (col < 4) ? row : row + 1;
                col = (col + 1) % 5;
            }
        }
    }

    void computations() throws Exception {
        // Read the input.txt
        createInput("inputEn.txt");
        StringBuilder s = new StringBuilder();
        while (in.hasNextLine()) {
            s.append(in.nextLine());
        }
        alph = checkAlph(s);
        if (alph.compareTo("") == 0) {
            throw new Exception("Enter russian or english text");
        }
        closeInput(in);

        // Prep the data structures, generate the key
        alphLen = alph.length();
        createInput("key.txt");
        key = in.next();
        keyLen = key.length();
        in.close();

        generateTable(alph);
        cypher(s.toString());
        decypher();
    }

    void cypher(String s) {
        createOutput("crypted.txt");
        s = s.replaceAll(" ", "");
        s = s.replaceAll("й", "и");
        s = s.replaceAll("ё", "е");
        s = s.replaceAll("ъ", "ь");
        s = s.replaceAll("j", "i");
        StringBuilder crypted = new StringBuilder();
        if (s.length() % 2 == 1) {
            s += s.charAt(0);
        }
        for (int i = 0; i < s.length() - 1; i += 2) {
            Character a = Character.toLowerCase(s.charAt(i));
            Character b = Character.toLowerCase(s.charAt(i + 1));
            crypted.append(solve(a, b, true));
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
        for (int i = 0; i < crypted.length() - 1; i += 2) {
            Character a = crypted.charAt(i);
            Character b = crypted.charAt(i + 1);
            decrypted.append(solve(a, b, false));
        }
        out.println(decrypted);
        closeInput(in);
        closeOutput(out);
    }

    String solve(char a, char b, boolean shouldCrypt) {
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < 5; j++) {
                if (a == table.get(i).get(j)) {
                    x1 = i;
                    y1 = j;
                }
                if (b == table.get(i).get(j)) {
                    x2 = i;
                    y2 = j;
                }
            }
        }
        if (x1 == x2) {
            y1 = (shouldCrypt) ?
                    (y1 + 1) % 5 :
                    (y1 + 4) % 5;
            y2 = (shouldCrypt) ?
                    (y2 + 1) % 5 :
                    (y2 + 4) % 5;
        }
        else if (y1 == y2) {
            x1 = (shouldCrypt) ?
                    (x1 + 1) % k :
                    (x1 + k - 1) % k;
            x2 = (shouldCrypt) ?
                    (x2 + 1) % k :
                    (x2 + k - 1) % k;
        }
        else {
            y1 ^= y2;
            y2 ^= y1;
            y1 ^= y2;
        }
        return table.get(x1).get(y1) + "" + table.get(x2).get(y2);
    }

    void run() throws Exception {
        computations();
        exit();
    }

    public static void main(String[] args) throws Exception {
        new G().run();
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