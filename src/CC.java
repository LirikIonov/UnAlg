import java.io.*;
import java.math.*;
import java.util.*;

public class CC {
    Scanner in;
    PrintWriter out;
    final String limit = "1111111";
    String ruAlph = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    String enAlph = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String alph = "", key;
    int alphLen = 0, keyLen = 10;
    char[][] t;

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

    void generateRandomUniqueKey(int len) {
        StringBuilder tempKey = new StringBuilder();
        for (int i = 0; i < len; i++) {
            boolean unique = false;
            int sh = -1;
            int isRu = (alph.indexOf('R') == -1) ? 33 : 26;
            while (!unique) {
                sh = new Random().nextInt(isRu);
                unique = tempKey.indexOf(alph.charAt(sh)+"") == -1;
            }
            tempKey.append(Character.toLowerCase(alph.charAt(sh)));
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
        generateRandomUniqueKey(keyLen);
        alph += " 0123456789 .,?:;!@#$%^*()_+-=—&[]";
        alphLen = alph.length();
        keyLen = key.length();
        cypher(s);
        decypher();
    }

    StringBuilder code(List<List<Character>> m, String key) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < m.get(0).size(); i++) {
            for (int j = i + 1; j < m.get(0).size(); j++) {
                int posi = key.indexOf(m.get(0).get(i));
                int posj = key.indexOf(m.get(0).get(j));
                if (posi > posj) {
                    for (int k = 0; k < m.size(); k++) {
                        char chi = m.get(k).get(i);
                        char chj = m.get(k).get(j);
                        m.get(k).set(i, chj);
                        m.get(k).set(j, chi);
                    }
                }
            }
        }
        for (int i = 1; i < m.size(); i++) {
            for (int j = 0; j < m.get(i).size(); j++) {
                res.append(m.get(i).get(j));
            }
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
        m.add(row);
        int rowIdx = 1;
        for (int t = 0; t < input.length(); t += keyLen) {
            StringBuilder s = new StringBuilder(input.substring(t, Math.min(t + keyLen, input.length())));
            row = new ArrayList<>(Collections.nCopies(keyLen, ' '));
            m.add(row);
            int pos = 0;
            for (int i = rowIdx; i < m.size(); i++) {
                for (int j = 0; j < keyLen; j++) {
                    if (pos < s.length()) {
                        m.get(i).set(j, s.charAt(pos));
                        pos++;
                    }
                }
            }
            rowIdx++;
        }
        res.append(code(m, shouldCrypt ? alph : key));
        return res;
    }

    void run() throws Exception {
        computations();
        exit();
    }

    public static void main(String[] args) throws Exception {
        new CC().run();
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
