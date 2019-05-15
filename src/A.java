import java.io.*;
import java.math.*;
import java.util.*;

public class A {
    Scanner in;
    PrintWriter out;
    String ruAlph = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    String enAlph = "abcdefghijklmnopqrsuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String key = "";
    int keyLen = 0;

    void computations() throws Exception {
        createInput("input.txt");
        StringBuilder s = new StringBuilder();
        while (in.hasNextLine()) {
            s.append(in.nextLine());
        }
        closeInput(in);
        for (int i = 0; i < s.length(); i++) {
            if (ruAlph.indexOf(s.charAt(i)) != -1) {
                key = ruAlph;
                break;
            }
            else if (ruAlph.indexOf(s.charAt(i)) != -1) {
                key = enAlph;
                break;
            }
        }
        if (key.compareTo("") == 0) {
            throw new Exception("Enter russian or english text");
        }
        key += " 0123456789 .,-!?:();";
        keyLen = key.length();
        int sh = (new Random().nextInt() % keyLen + keyLen) % keyLen;
        cypher(s, sh);
        decypher(sh);
    }

    void cypher(StringBuilder s, int sh) {
        createOutput("crypted.txt");
        StringBuilder crypted = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char a = s.charAt(i);
            int j = key.indexOf(a);
            int idx = j + ((i == 0) ?
                    sh :
                    key.indexOf(crypted.charAt(i - 1)));
            char b = key.charAt(idx % keyLen);
            crypted.append(b);
        }
        out.print(crypted);
        closeOutput(out);
    }

    void decypher(int sh) {
        createInput("crypted.txt");
        createOutput("decrypted.txt");
        StringBuilder crypted = new StringBuilder();
        StringBuilder decrypted = new StringBuilder();
        while (in.hasNextLine()) {
            crypted.append(in.nextLine());
        }
        for (int i = 0; i < crypted.length(); i++) {
            char a = crypted.charAt(i);
            char b;
            int j = key.indexOf(a);
            int idx = j - ((i == 0) ?
                    sh :
                    key.indexOf(crypted.charAt(i - 1)));
            b = key.charAt((idx + keyLen) % keyLen);
            decrypted.append(b);
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
        new A().run();
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
