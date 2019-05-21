import java.io.*;
import java.util.*;

public class D {
    Scanner in;
    PrintWriter out;
    String ruAlph = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    String enAlph = "abcdefghijklmnopqrsuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String key = "", alph = "";
    int keyLen = 10, alphLen = 0;

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
        createInput("input.txt");
        StringBuilder s = new StringBuilder();
        while (in.hasNextLine()) {
            s.append(in.nextLine());
        }
        closeInput(in);
        alph = checkAlph(s);
        if (alph.compareTo("") == 0) {
            throw new Exception("Enter russian or english text");
        }

        // Prep the data structures, generate the key
        generateRandomUniqueKey(keyLen);
        alph += " 0123456789 .,?:;!@#$%^*()_+-=—&[]";
        alphLen = alph.length();
        cypher(s);
        decypher();
    }

    void cypher(StringBuilder s) {
        createOutput("crypted.txt");
        StringBuilder crypted = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char a = s.charAt(i);
            char b = key.charAt(i % keyLen);
            int aPos = alph.indexOf(a);
            int bPos = alph.indexOf(b);
            char f = alph.charAt((aPos + bPos) % alphLen);
            crypted.append(f);
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
            char a = crypted.charAt(i);
            char b = key.charAt(i % keyLen);
            int aPos = alph.indexOf(a);
            int bPos = alph.indexOf(b);
            char f = alph.charAt(((aPos - bPos) % alphLen + alphLen) % alphLen);
            decrypted.append(f);
        }
        out.println(decrypted);
        closeInput(in);
        closeOutput(out);
    }

    void run() throws Exception {
        computations();
        exit();
    }

    public static void main(String[] args) throws Exception {
        new D().run();
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
