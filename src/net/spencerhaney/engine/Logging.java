package net.spencerhaney.engine;

import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.Date;

public class Logging
{
    private static final String OUTPUT = "[%s : %s] %s\n";

    private static PrintStream stream;
    private static PrintStream errorStream;
    private static Level level;

    private enum Level
    {
        ALL, DEBUG, FINEST, FINE, INFO, WARNING, SEVERE, NONE;
    }

    public static void init()
    {
        stream = System.out;
        errorStream = System.err;
        level = Level.ALL;
    }

    public static void debug(String message)
    {
        printMessage(Level.DEBUG, message);
    }

    public static void finest(String message)
    {
        printMessage(Level.FINEST, message);
    }

    public static void fine(String message)
    {
        printMessage(Level.FINE, message);
    }

    public static void info(String message)
    {
        printMessage(Level.INFO, message);
    }

    public static void warning(String message)
    {
        printMessage(Level.WARNING, message);
    }

    public static void severe(String message)
    {
        printMessage(Level.SEVERE, message);
    }

    public static void warning(String message, Throwable t)
    {
        if (level.compareTo(Level.WARNING) <= 0)
        {
            String timestamp = getTimestamp();
            if (!"".equals(message))
            {
                errorStream.printf(OUTPUT, Level.WARNING, timestamp, message);
            }
            if (t.getMessage() != null)
            {
                errorStream.printf(OUTPUT, Level.WARNING, timestamp, t.getMessage());
            }
        }
    }

    public static void severe(String message, Throwable t)
    {
        if (level.compareTo(Level.SEVERE) <= 0)
        {
            String timestamp = getTimestamp();
            if (!"".equals(message))
            {
                errorStream.printf(OUTPUT, Level.SEVERE, timestamp, message);
            }
            if (t.getMessage() != null)
            {
                errorStream.printf(OUTPUT, Level.SEVERE, timestamp, t.getMessage());
            }
        }
    }

    public static void warning(Throwable t)
    {
        if (level.compareTo(Level.WARNING) <= 0)
        {
            if (t.getMessage() != null)
            {
                errorStream.printf(OUTPUT, Level.WARNING, getTimestamp(), t.getMessage());
            }
        }
    }

    public static void severe(Throwable t)
    {
        if (level.compareTo(Level.SEVERE) <= 0)
        {
            if (t.getMessage() != null)
            {
                errorStream.printf(OUTPUT, Level.SEVERE, getTimestamp(), t.getMessage());
            }
        }
    }

    private static String getTimestamp()
    {
        return new Timestamp(new Date().getTime()).toString();
    }

    private static void printMessage(Level t, String message)
    {
        if (level.compareTo(t) <= 0)
        {
            stream.printf(OUTPUT, t, getTimestamp(), message);
        }    
    }
}
