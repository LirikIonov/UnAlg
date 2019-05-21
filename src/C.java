import java.io.*;
import java.math.*;
import java.util.*;

public class C {
    Scanner in;
    PrintWriter out;
    final String limit = "1111111";
    String ruAlph = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    String enAlph = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String alph = "", key;
    Map<String, Integer> charToInt;
    Map<Integer, String> intToChar;
    int alphLen = 0, keyLen = 0, tableLen = 10;
    char[][] t;

    String checkAlph(StringBuilder s) {
        String alph = "";
        for (int i = 0; i < s.length(); i++) {
            if (ruAlph.indexOf(s.charAt(i)) != -1) {
                alph = ruAlph;
                t = new char[tableLen][tableLen];
                break;
            }
            else if (enAlph.indexOf(s.charAt(i)) != -1) {
                alph = enAlph;
                t = new char[tableLen][tableLen];
                break;
            }
        }
        return alph;
    }

    void generateRandomKey(int len) {
        StringBuilder tempKey = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int sh = new Random().nextInt(alph.length());
            tempKey.append(alph.charAt(sh));
        }
        key = tempKey.toString();
    }

    void generateTable(){
        Set<Character> alphSet = new HashSet<>();
        for (int i = 0; i < alphLen; i++) {
            alphSet.add(alph.charAt(i));
        }
        for (int j = 0; j < tableLen; j++) {
            t[0][j] = key.charAt(j);
            alphSet.remove(key.charAt(j));
        }
        Iterator<Character> iter = alphSet.iterator();
        for (int i = 1; i < tableLen; i++) {
            for (int j = 0; j < tableLen; j++) {
                if (iter.hasNext()) {
                    char ch = iter.next();
                    t[i][j] = ch;
                }
                else {
                    t[i][j] = (char) (alph.charAt((i * tableLen + j) % alphLen) + alphLen + 1);
                }

            }
        }
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
        generateRandomKey(tableLen);
        alph += " 0123456789 .,?:;!@#$%^*()_+-=—&[]";
        alphLen = alph.length();
        keyLen = key.length();
        generateTable();
        cypher(s);
        decypher();
    }

    StringBuilder code(String s, boolean shouldCrypted) {
        StringBuilder res = new StringBuilder();
        for (int j = 0; j < s.length(); j++) {
            char ch = s.charAt(j);
            int x = -1, y = -1;
            for (int i = 0; i < keyLen; i++) {
                for (int k = 0; k < keyLen; k++) {
                    if (t[i][k] == ch) {
                        x = i;
                        y = k;
                    }
                }
            }
            x = (shouldCrypted) ? (x + 1) % keyLen : (x - 1 + keyLen) % keyLen;
            res.append(t[x][y]);
        }
        return res;
    }

    void cypher(StringBuilder input) {
        createOutput("crypted.txt");
        StringBuilder res = solve(input, true);
        out.print(res);
        closeOutput(out);
    }

    void decypher() {
        createInput("crypted.txt");
        createOutput("decrypted.txt");
        StringBuilder crypted = new StringBuilder();
        while (in.hasNextLine()) {
            crypted.append(in.nextLine());
        }
        StringBuilder decrypted = solve(crypted, false);
        out.println(decrypted);
        closeInput(in);
        closeOutput(out);
    }

    StringBuilder solve(StringBuilder input, boolean shouldCrypt) {
        StringBuilder res = new StringBuilder();
        List<List<Character>> m = new ArrayList<>();
        List<Character> row = new ArrayList<>();
        for (int i = 0; i < keyLen; i++) {
            row.add(key.charAt(i));
        }
        if (!shouldCrypt) {
            Collections.sort(row);
        }
        for (int t = 0; t < input.length(); t += keyLen) {
            StringBuilder s = new StringBuilder(input.substring(t, Math.min(t + keyLen, input.length())));
            m.clear();
            m.add(row);
            row = new ArrayList<>(Collections.nCopies(keyLen, ' '));
            m.add(row);
            int pos = 0;
            for (int i = 1; i < m.size(); i++) {
                for (int j = 0; j < keyLen; j++) {
                    if (pos < s.length()) {
                        m.get(i).set(j, s.charAt(pos));
                        pos++;
                    }
                }
            }
            res.append(code(s.substring(0, s.length()), shouldCrypt));
        }
        return res;
    }

    void run() throws Exception {
        computations();
        exit();
    }

    public static void main(String[] args) throws Exception {
        new C().run();
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
}
