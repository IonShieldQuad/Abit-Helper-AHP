package com.ionshield.abithelperahp.core;

public abstract class Config {
    public static abstract class InputTable {
        public static String[] COLUMNS = new String[]{"Program", "University", "Demand", "Salary", "Numbers", "Score"};
        public static int getIndex(String header) {
            if (header != null) {
                for (int i = 0; i < COLUMNS.length; i++) {
                    String s = COLUMNS[i];
                    if (s.equalsIgnoreCase(header)) return i;
                }
            }
            throw new IllegalArgumentException("No such table header: " + header);
        }
    }

    public static abstract class OutputTable {
        public static String[] COLUMNS = new String[]{"Program", "University", "Value"};
        public static int getIndex(String header) {
            if (header != null) {
                for (int i = 0; i < COLUMNS.length; i++) {
                    String s = COLUMNS[i];
                    if (s.equalsIgnoreCase(header)) return i;
                }
            }
            throw new IllegalArgumentException("No such table header: " + header);
        }
    }

    public static abstract class ProgramSettingsTable {
        public static String[] COLUMNS = new String[]{"Program", "Preference score"};
        public static int getIndex(String header) {
            if (header != null) {
                for (int i = 0; i < COLUMNS.length; i++) {
                    String s = COLUMNS[i];
                    if (s.equalsIgnoreCase(header)) return i;
                }
            }
            throw new IllegalArgumentException("No such table header: " + header);
        }
    }

    public static abstract class UniversitySettingsTable {
        public static String[] COLUMNS = (new String[]{"University", "Preference score"});
        public static int getIndex(String header) {
            if (header != null) {
                for (int i = 0; i < COLUMNS.length; i++) {
                    String s = COLUMNS[i];
                    if (s.equalsIgnoreCase(header)) return i;
                }
            }
            throw new IllegalArgumentException("No such table header: " + header);
        }
    }

    public static abstract class RulesSettingsTable {
        public static String[] COLUMNS = new String[]{"Name", "Value"};
        public static int getIndex(String header) {
            if (header != null) {
                for (int i = 0; i < COLUMNS.length; i++) {
                    String s = COLUMNS[i];
                    if (s.equalsIgnoreCase(header)) return i;
                }
            }
            throw new IllegalArgumentException("No such table header: " + header);
        }
    }
}
