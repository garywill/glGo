/*
 *  BrowserControl.java
 *
 *  This is a code from the JavaWorld webpage.
 *  http://www.javaworld.com/javaworld/javatips/jw-javatip66.html
 *
 *  Slightly modified by Peter Strempel, added Mac OsX support
 */
package ggo.utils;

import java.io.IOException;

/**
 * A simple, static class to display a URL in the system browser.
 *
 * Under Unix, the system browser is hard-coded to be 'netscape'.
 * Netscape must be in your PATH for this to work.  This has been
 * tested with the following platforms: AIX, HP-UX and Solaris.
 *
 * Under Windows, this will bring up the default browser under windows,
 * usually either Netscape or Microsoft IE.  The default browser is
 * determined by the OS.  This has been tested under Windows 95/98/NT.
 *
 * Adjusted this class to work with Mac Os X, too.
 *
 * Examples:
 * BrowserControl.displayURL("http://www.javaworld.com")
 * BrowserControl.displayURL("file://c:\\docs\\index.html")
 * BrowserContorl.displayURL("file:///user/joe/index.html");
 *
 * Note - you must include the url type -- either "http://" or "file://".
 *
 *@author     Steven Spencer, Peter Strempel
 *@version    $Revision: 1.2 $, $Date: 2002/09/21 02:38:03 $
 */
public class BrowserControl {
    /**
     * Display a file in the system browser.  If you want to display a
     * file, you must include the absolute path name.
     *
     *@param  url  the file's url (the url must start with either "http://" or "file://").
     */
    public static void displayURL(String url) {
        String cmd = null;
        try {
            if (isWindowsPlatform()) {
                System.err.println("Windows detected");
                // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
                cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
                Runtime.getRuntime().exec(cmd);
            }
            // Adjustment for Mac Os X
            else if (isMacOsXPlatform()) {
                // OsX wants the commandline as String array
                System.err.println("OsX detected");
                String[] commandLine = {"netscape", url};
                Runtime.getRuntime().exec(commandLine);
            }
            else {
                System.err.println("Unix detected");
                // Under Unix, Netscape has to be running for the "-remote"
                // command to work.  So, we try sending the command and
                // check for an exit value.  If the exit command is 0,
                // it worked, otherwise we need to start the browser.
                // cmd = 'netscape -remote openURL(http://www.javaworld.com)'
                cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
                Process p = Runtime.getRuntime().exec(cmd);
                try {
                    // wait for exit code -- if it's 0, command worked,
                    // otherwise we need to start the browser up.
                    int exitCode = p.waitFor();
                    if (exitCode != 0) {
                        // Command failed, start up the browser
                        // cmd = 'netscape http://www.javaworld.com'
                        cmd = UNIX_PATH + " " + url;
                        p = Runtime.getRuntime().exec(cmd);
                    }
                } catch (InterruptedException x) {
                    System.err.println("Error bringing up browser, cmd='" + cmd + "'\nCaught: " + x);
                }
            }
        } catch (IOException x) {
            // couldn't exec browser
            System.err.println("Could not invoke browser, command=" + cmd + "\nCaught: " + x);
        }
    }

    /**
     * Try to determine whether this application is running under Windows
     * or some other platform by examing the "os.name" property.
     *
     *@return    true if this application is running under a Windows OS
     */
    public static boolean isWindowsPlatform() {
        String os = System.getProperty("os.name");
        if (os != null && os.startsWith(WIN_ID))
            return true;
        return false;
    }

    /**
     * Try to determine whether this application is running under Mac Os X
     * or some other platform by examing the "os.name" property.
     *
     *@return    true if this application is running under a Mac Os X
     */
    public static boolean isMacOsXPlatform() {
        String os = System.getProperty("os.name");
        if (os != null && os.equals(OSX_ID))
            return true;
        return false;
    }


    // Used to identify the windows platform.
    private final static String WIN_ID = "Windows";
    // Ditto for Os x
    private final static String OSX_ID = "Mac OS X";
    // The default system browser under windows.
    private final static String WIN_PATH = "rundll32";
    // The flag to display a url.
    private final static String WIN_FLAG = "url.dll,FileProtocolHandler";
    // The default browser under unix.
    private final static String UNIX_PATH = "netscape";
    // The flag to display a url.
    private final static String UNIX_FLAG = "-remote openURL";
}

