import java.io.*;
import java.util.*;

public class E {
    Scanner in;
    PrintWriter out;

    void computations() throws Exception {
        createInput("inputEn.txt");
        int n = in.nextInt() + 1;
        StringBuilder nStr = new StringBuilder(String.valueOf(n));
        int e = String.valueOf(n - (int) Math.pow(10, nStr.length() - 2)).length() - 1;
        n -= Math.pow(10, e);
        nStr = new StringBuilder(String.valueOf(n));
        int x = (n >= Math.pow(10, e)) ? nStr.length() - 1 : nStr.length();
        String res = nStr.substring(0, x) + nStr.reverse();
        createOutput("decrypted.txt");
        out.println(res);
        closeOutput(out);
    }

    void run() throws Exception {
        computations();
        exit();
    }

    public static void main(String[] args) throws Exception {
        new E().run();
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
