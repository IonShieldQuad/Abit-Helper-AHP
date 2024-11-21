package com.ionshield.abithelperahp.core;

import com.bulenkov.darcula.DarculaLaf;
import com.ionshield.abithelperahp.math.Matrix;
import com.ionshield.abithelperahp.math.PreferencePair;
import com.ionshield.abithelperahp.math.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainWindow {
    private JPanel rootPanel;
    private JTextArea log;
    private JButton calculateButton;
    private JTable inputTable;
    private JTable outputTable;
    private JButton addInputButton;
    private JButton deleteInputButton;
    private JButton loadInputButton;
    private JTable inputSettingsTable;
    private JTable rulesSettingsTable;
    private JTable outputSettingsTable;
    private JButton applyButton;
    private JButton addRuleButton;
    private JButton deleteRuleButton;
    private JButton loadRuleButton;
    private JButton addOutputButton;
    private JButton deleteOutputButton;
    private JButton loadOutputButton;
    private JButton addOptionButton;
    private JButton deleteOptionButton;
    private JButton loadOptionButton;
    private JButton clearButton;

    private DefaultTableModel inputTableModel;
    private DefaultTableModel outputTableModel;
    private DefaultTableModel programSettingsTableModel;
    private DefaultTableModel universitySettingsTableModel;
    private DefaultTableModel rulesSettingsTableModel;

    private Map<String, Double> universityMap = new HashMap<>();
    private Map<String, Double> programMap = new HashMap<>();
    private Map<String, Double> rulesSettings = new HashMap<>();
    private Map<Integer, Option> options = new HashMap<>();

    TableRowSorter<DefaultTableModel> inputSorter;


    public static final String TITLE = "Abit-Helper-AHP";
    
    private MainWindow() {
        initComponents();
    }
    
    private void initComponents() {
        calculateButton.addActionListener(e -> calculate());
        applyButton.addActionListener(e -> applySettings());
        inputTableModel = new DefaultTableModel(Config.InputTable.COLUMNS, 0);
        outputTableModel = new DefaultTableModel(Config.OutputTable.COLUMNS, 0);
        programSettingsTableModel = new DefaultTableModel(Config.ProgramSettingsTable.COLUMNS, 0);
        universitySettingsTableModel = new DefaultTableModel(Config.UniversitySettingsTable.COLUMNS, 0);
        rulesSettingsTableModel = new DefaultTableModel(Config.RulesSettingsTable.COLUMNS, 0);

        inputTable.setModel(inputTableModel);
        outputTable.setModel(outputTableModel);
        inputSettingsTable.setModel(programSettingsTableModel);
        outputSettingsTable.setModel(universitySettingsTableModel);
        rulesSettingsTable.setModel(rulesSettingsTableModel);

        inputSorter = new TableRowSorter<>(inputTableModel);
        inputTable.setRowSorter(inputSorter);

        addInputButton.addActionListener(e -> {
            programSettingsTableModel.addRow(new Object[programSettingsTableModel.getColumnCount()]);
        });

        deleteInputButton.addActionListener(e -> {
            int i = inputSettingsTable.getSelectedRow();
            if (i >= 0 && i < inputSettingsTable.getRowCount()) {
                programSettingsTableModel.removeRow(i);
            }
        });

        addOutputButton.addActionListener(e -> {
            universitySettingsTableModel.addRow(new Object[universitySettingsTableModel.getColumnCount()]);
        });

        deleteOutputButton.addActionListener(e -> {
            int i = outputSettingsTable.getSelectedRow();
            if (i >= 0 && i < outputSettingsTable.getRowCount()) {
                universitySettingsTableModel.removeRow(i);
            }
        });

        addRuleButton.addActionListener(e -> {
            rulesSettingsTableModel.addRow(new Object[rulesSettingsTableModel.getColumnCount()]);
        });

        deleteRuleButton.addActionListener(e -> {
            int i = rulesSettingsTable.getSelectedRow();
            if (i >= 0 && i < rulesSettingsTable.getRowCount()) {
                rulesSettingsTableModel.removeRow(i);
            }
        });

        addOptionButton.addActionListener(e -> {
            inputTableModel.addRow(new Object[inputSettingsTable.getColumnCount()]);
        });

        deleteOptionButton.addActionListener(e -> {
            int i = inputTable.getSelectedRow();
            if (i >= 0 && i < inputTable.getRowCount()) {
                inputTableModel.removeRow(i);
            }
        });

        loadInputButton.addActionListener(e -> {
            File file = loadFile();
            if (file == null) return;
            List<String> rows = readFileRows(file);
            if (rows == null) return;

            for (int i = programSettingsTableModel.getRowCount() - 1; i >= 0; i--) {
                programSettingsTableModel.removeRow(i);
            }

            for (int i = 0; i < rows.size(); i++) {
                String row = rows.get(i);
                row = row.trim();
                List<String> strings = Arrays.asList(row.split("\\s+"));
                programSettingsTableModel.addRow(new Object[programSettingsTableModel.getColumnCount()]);
                if (strings.size() >= 1) {
                    programSettingsTableModel.setValueAt(strings.get(0), i, Config.ProgramSettingsTable.getIndex("Program"));
                }
                if (strings.size() >= 2) {
                    programSettingsTableModel.setValueAt(strings.get(1), i, Config.ProgramSettingsTable.getIndex("Preference Score"));
                }
                /*if (strings.size() >= 3) {
                    StringBuilder values = new StringBuilder();
                    for (int j = 2; j < strings.size(); j++) {
                        values.append(strings.get(j));
                        if (j != strings.size() - 1) {
                            values.append(";");
                        }
                    }
                    //programSettingsTableModel.setValueAt(values, i, Config.ProgramSettingsTable.getIndex("enum values"));
                }*/
            }
        });

        loadOutputButton.addActionListener(e -> {
            File file = loadFile();
            if (file == null) return;
            List<String> rows = readFileRows(file);
            if (rows == null) return;

            for (int i = universitySettingsTableModel.getRowCount() - 1; i >= 0; i--) {
                universitySettingsTableModel.removeRow(i);
            }

            for (int i = 0; i < rows.size(); i++) {
                String row = rows.get(i);
                row = row.trim();
                List<String> strings = Arrays.asList(row.split("\\s+"));
                universitySettingsTableModel.addRow(new Object[universitySettingsTableModel.getColumnCount()]);
                if (strings.size() >= 1) {
                    universitySettingsTableModel.setValueAt(strings.get(0), i, Config.UniversitySettingsTable.getIndex("University"));
                }
                if (strings.size() >= 2) {
                    universitySettingsTableModel.setValueAt(strings.get(1), i, Config.UniversitySettingsTable.getIndex("Preference score"));
                }
                /*if (strings.size() >= 3) {
                    StringBuilder values = new StringBuilder();
                    for (int j = 2; j < strings.size(); j++) {
                        values.append(strings.get(j));
                        if (j != strings.size() - 1) {
                            values.append(";");
                        }
                    }
                    universitySettingsTableModel.setValueAt(values, i, Config.UniversitySettingsTable.getIndex("enum values"));
                }*/
            }
        });

        loadRuleButton.addActionListener(e -> {
            File file = loadFile();
            if (file == null) return;
            List<String> rows = readFileRows(file);
            if (rows == null) return;

            for (int i = rulesSettingsTableModel.getRowCount() - 1; i >= 0; i--) {
                rulesSettingsTableModel.removeRow(i);
            }

            for (int i = 0; i < rows.size(); i++) {
                String row = rows.get(i);
                row = row.trim();
                /*if (row.toLowerCase().startsWith("if")) {
                    row = row.substring(2);
                }
                if (row.toLowerCase().startsWith("если")) {
                    row = row.substring(4);
                }
                row = row.trim();
                List<String> strings = Arrays.asList(row.split(/*"\\b(?i)(if|then|else|если|то|иначе)\\b"*//*Pattern.compile("\\b(?i)(if|then|else|если|то|иначе|Если|То|Иначе|ЕСЛИ|ТО|ИНАЧЕ)\\b", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE).toString()));
                /*rulesSettingsTableModel.addRow(new Object[rulesSettingsTableModel.getColumnCount()]);
                if (strings.size() >= 1) {
                    rulesSettingsTableModel.setValueAt(strings.get(0), i, Config.RulesSettingsTable.getIndex("if"));
                }
                if (strings.size() >= 2) {
                    rulesSettingsTableModel.setValueAt(strings.get(1), i, Config.RulesSettingsTable.getIndex("then"));
                }
                if (strings.size() >= 3) {
                    rulesSettingsTableModel.setValueAt(strings.get(2), i, Config.RulesSettingsTable.getIndex("else"));
                }*/
                List<String> strings = Arrays.asList(row.split("\\s+"));
                if (strings.size() >= 1) {
                    rulesSettingsTableModel.addRow(new Object[rulesSettingsTableModel.getColumnCount()]);
                    rulesSettingsTableModel.setValueAt(strings.get(0), i, Config.RulesSettingsTable.getIndex("Name"));
                }
                if (strings.size() >= 2) {
                    rulesSettingsTableModel.setValueAt(strings.get(1), i, Config.RulesSettingsTable.getIndex("Value"));
                }

            }
        });


        loadOptionButton.addActionListener(e -> {
            File file = loadFile();
            if (file == null) return;
            List<String> rows = readFileRows(file);
            if (rows == null) return;

            for (int i = inputTableModel.getRowCount() - 1; i >= 0; i--) {
                inputTableModel.removeRow(i);
            }

            for (int i = 0; i < rows.size(); i++) {
                String row = rows.get(i);
                row = row.trim();
                List<String> strings = Arrays.asList(row.split("\\s+"));
                if (strings.size() >= 1) {
                    inputTableModel.addRow(new Object[inputTableModel.getColumnCount()]);
                    inputTableModel.setValueAt(strings.get(0), i, Config.InputTable.getIndex("Program"));
                }
                if (strings.size() >= 2) {
                    inputTableModel.setValueAt(strings.get(1), i, Config.InputTable.getIndex("University"));
                }
                if (strings.size() >= 3) {
                    inputTableModel.setValueAt(strings.get(2), i, Config.InputTable.getIndex("Demand"));
                }
                if (strings.size() >= 4) {
                    inputTableModel.setValueAt(strings.get(3), i, Config.InputTable.getIndex("Salary"));
                }
                if (strings.size() >= 5) {
                    inputTableModel.setValueAt(strings.get(4), i, Config.InputTable.getIndex("Numbers"));
                }
                if (strings.size() >= 6) {
                    inputTableModel.setValueAt(strings.get(5), i, Config.InputTable.getIndex("Score"));
                }


            }
        });

        /*clearButton.addActionListener(e -> {
            inputSorter.setRowFilter(null);
        });*/

        applySettings();
        log.setText("");

    }
    
    private void applySettings() {

        universityMap.clear();
        programMap.clear();
        rulesSettings.clear();

        //Program settings
        for (int i = 0; i < programSettingsTableModel.getRowCount(); i++) {
            String name = Optional.ofNullable(programSettingsTableModel.getValueAt(i, Config.ProgramSettingsTable.getIndex("Program"))).orElse("").toString();
            if (name == null || name.length() == 0) {
                log.append("Warning: empty program name");
                continue;
            }
            name = name.toLowerCase();
            try {
                Double value = Double.valueOf(Optional.ofNullable(programSettingsTableModel.getValueAt(i, Config.ProgramSettingsTable.getIndex("Preference score"))).orElse(1).toString());
                programMap.put(name, value);
            } catch (NumberFormatException e) {
                log.append(System.lineSeparator() + "Warning: program \"" + name + "\": invalid number format");
                continue;
            }

        }

        //University settings
        for (int i = 0; i < universitySettingsTableModel.getRowCount(); i++) {
            String name = Optional.ofNullable(universitySettingsTableModel.getValueAt(i, Config.UniversitySettingsTable.getIndex("University"))).orElse("").toString();
            if (name == null || name.length() == 0) {
                log.append("Warning: empty university name");
                continue;
            }
            name = name.toLowerCase();
            try {
                Double value = Double.valueOf(Optional.ofNullable(universitySettingsTableModel.getValueAt(i, Config.UniversitySettingsTable.getIndex("Preference score"))).orElse(1).toString());
                universityMap.put(name, value);
            } catch (NumberFormatException e) {
                log.append(System.lineSeparator() + "Warning: university \"" + name + "\": invalid number format");
                continue;
            }

        }

        //Rules settings
        for (int i = 0; i < rulesSettingsTableModel.getRowCount(); i++) {
            String name = Optional.ofNullable(rulesSettingsTableModel.getValueAt(i, Config.RulesSettingsTable.getIndex("Name"))).orElse("").toString();
            if (name == null || name.length() == 0) {
                log.append("Warning: empty university name");
                continue;
            }
            name = name.toLowerCase();
            try {
                Double value = Double.valueOf(Optional.ofNullable(rulesSettingsTableModel.getValueAt(i, Config.RulesSettingsTable.getIndex("Value"))).orElse(1).toString());
                rulesSettings.put(name, value);
            } catch (NumberFormatException e) {
                log.append(System.lineSeparator() + "Warning: param \"" + name + "\": invalid number format");
                continue;
            }

        }

        log.append(System.lineSeparator() + "Settings applied");
    }

    
    private void calculate() {
        options.clear();


        for (int i = 0; i < inputTableModel.getRowCount(); i++) {
            String programString = Optional.ofNullable(inputTableModel.getValueAt(i, Config.InputTable.getIndex("Program")).toString()).orElse("");
            String universityString = Optional.ofNullable(inputTableModel.getValueAt(i, Config.InputTable.getIndex("University")).toString()).orElse("");
            String demandString = Optional.ofNullable(inputTableModel.getValueAt(i, Config.InputTable.getIndex("Demand")).toString()).orElse("");
            String salaryString = Optional.ofNullable(inputTableModel.getValueAt(i, Config.InputTable.getIndex("Salary")).toString()).orElse("");
            String numbersString = Optional.ofNullable(inputTableModel.getValueAt(i, Config.InputTable.getIndex("Numbers")).toString()).orElse("");
            String scoreString = Optional.ofNullable(inputTableModel.getValueAt(i, Config.InputTable.getIndex("Score")).toString()).orElse("");

            if (!programMap.containsKey(programString.toLowerCase())) {
                log.append(System.lineSeparator() + "Error: program \"" + programString + "\" not defined in settings.");
                return;
            }
            if (!universityMap.containsKey(universityString.toLowerCase())) {
                log.append(System.lineSeparator() + "Error: university \"" + programString + "\" not defined in settings.");
                return;
            }

            try {
                double demand = Double.parseDouble(demandString);
                double salary = Double.parseDouble(salaryString);
                int numbers = Integer.parseInt(numbersString);
                int score = Integer.parseInt(scoreString);

                Option option = new Option(i, programString, universityString, demand, salary, numbers, score);
                options.put(i, option);
            } catch (NumberFormatException e) {
                log.append("Error: invalid number format at row " + i + ".");
                return;
            }

        }

        if (!rulesSettings.containsKey("interests_opportunities")) {
            log.append("Error: \"interests_opportunities\" not defined");
            return;
        }
        if (!rulesSettings.containsKey("interests_ease")) {
            log.append("Error: \"interests_ease\" not defined");
            return;
        }
        if (!rulesSettings.containsKey("opportunities_ease")) {
            log.append("Error: \"opportunities_ease\" not defined");
            return;
        }
        if (!rulesSettings.containsKey("program_university")) {
            log.append("Error: \"program_university\" not defined");
            return;
        }
        if (!rulesSettings.containsKey("salary_demand")) {
            log.append("Error: \"salary_demand\" not defined");
            return;
        }
        if (!rulesSettings.containsKey("numbers_score")) {
            log.append("Error: \"numbers_score\" not defined");
            return;
        }
        if (!rulesSettings.containsKey("student_score")) {
            log.append("Error: \"student_score\" not defined");
            return;
        }
        if (!rulesSettings.containsKey("min_salary")) {
            log.append("Error: \"min_salary\" not defined");
            return;
        }

        double interestsOpportunities = rulesSettings.get("interests_opportunities");
        double interestsEase = rulesSettings.get("interests_ease");
        double opportunitiesEase = rulesSettings.get("opportunities_ease");
        double programUniversity = rulesSettings.get("program_university");
        double salaryDemand = rulesSettings.get("salary_demand");
        double numbersScore = rulesSettings.get("numbers_score");

        double studentScore = rulesSettings.get("student_score");
        double minSalary = rulesSettings.get("min_salary");


        List<PreferencePair> basePairs = new ArrayList<>();
        basePairs.add(new PreferencePair(0, 1, interestsOpportunities));
        basePairs.add(new PreferencePair(0, 2, interestsEase));
        basePairs.add(new PreferencePair(1, 2, opportunitiesEase));

        Matrix m1 = Utils.makeMatrixFromPairs(basePairs);
        List<Double> weights = Utils.calculateWeights(m1);

        List<Double> weights2 = new ArrayList<>();
        weights2.add(((programUniversity) / (programUniversity + 1)) * weights.get(0));
        weights2.add((1 - (programUniversity) / (programUniversity + 1)) * weights.get(0));
        weights2.add(((salaryDemand) / (salaryDemand + 1)) * weights.get(1));
        weights2.add((1 - (salaryDemand) / (salaryDemand + 1)) * weights.get(1));
        weights2.add(((numbersScore) / (numbersScore + 1)) * weights.get(2));
        weights2.add((1 - (numbersScore) / (numbersScore + 1)) * weights.get(2));

        List<PreferencePair> programPairs = new ArrayList<>();
        List<PreferencePair> universityPairs = new ArrayList<>();
        List<PreferencePair> demandPairs = new ArrayList<>();
        List<PreferencePair> salaryPairs = new ArrayList<>();
        List<PreferencePair> numbersPairs = new ArrayList<>();
        List<PreferencePair> scorePairs = new ArrayList<>();

        List<Option> optionList = new ArrayList<>(options.values());
        if (optionList.isEmpty()) return;
        double universityMin = universityMap.get(optionList.get(0).university.toLowerCase());
        double universityMax = universityMap.get(optionList.get(0).university.toLowerCase());
        double programMin = programMap.get(optionList.get(0).program.toLowerCase());
        double programMax = programMap.get(optionList.get(0).program.toLowerCase());
        double demandMin = optionList.get(0).demand;
        double demandMax = optionList.get(0).demand;
        double salaryMin = optionList.get(0).salary;
        double salaryMax = optionList.get(0).salary;
        double numbersMin = optionList.get(0).numbers;
        double numbersMax = optionList.get(0).numbers;
        double scoreMin = optionList.get(0).score;
        double scoreMax = optionList.get(0).score;



        for (int i = 0; i < optionList.size(); i++) {
            Option o1 = optionList.get(i);
            if (universityMap.get(o1.university.toLowerCase()) > universityMax) universityMax = universityMap.get(o1.university.toLowerCase());
            if (universityMap.get(o1.university.toLowerCase()) < universityMin) universityMin = universityMap.get(o1.university.toLowerCase());
            if (programMap.get(o1.program.toLowerCase()) > programMax) programMax = programMap.get(o1.program.toLowerCase());
            if (programMap.get(o1.program.toLowerCase()) < programMin) programMin = programMap.get(o1.program.toLowerCase());
            if (o1.demand > demandMax) demandMax = o1.demand;
            if (o1.demand < demandMin) demandMin = o1.demand;
            if (o1.salary > salaryMax) salaryMax = o1.salary;
            if (o1.salary < salaryMin) salaryMin = o1.salary;
            if (o1.numbers > numbersMax) numbersMax = o1.numbers;
            if (o1.numbers < numbersMin) numbersMin = o1.numbers;
            if (o1.score > scoreMax) scoreMax = o1.score;
            if (o1.score < scoreMin) scoreMin = o1.score;
        }


        for (int i = 0; i < optionList.size(); i++) {
            Option o1 = optionList.get(i);
            for (int j = i + 1; j < optionList.size(); j++) {
                Option o2 = optionList.get(j);

                universityPairs.add(new PreferencePair(i, j, Math.pow(9, Utils.normalize(universityMap.get(o1.university.toLowerCase()), universityMin, universityMax) - Utils.normalize(universityMap.get(o2.university.toLowerCase()), universityMin, universityMax))));
                programPairs.add(new PreferencePair(i, j, Math.pow(9, Utils.normalize(programMap.get(o1.program.toLowerCase()), programMin, programMax) - Utils.normalize(programMap.get(o2.program.toLowerCase()), programMin, programMax))));
                demandPairs.add(new PreferencePair(i, j, Math.pow(9, Utils.normalize(o1.demand, demandMin, demandMax) - Utils.normalize(o2.demand, demandMin, demandMax))));
                salaryPairs.add(new PreferencePair(i, j, Math.pow(9, Utils.normalize(o1.salary, salaryMin, salaryMax) - Utils.normalize(o2.salary, salaryMin, salaryMax))));
                numbersPairs.add(new PreferencePair(i, j, Math.pow(9, Utils.normalize(o1.numbers, numbersMin, numbersMax) - Utils.normalize(o2.numbers, numbersMin, numbersMax))));
                scorePairs.add(new PreferencePair(i, j, Math.pow(9, Utils.normalize(o1.score, scoreMin, scoreMax) - Utils.normalize(o2.score, scoreMin, scoreMax))));


            }
        }

        Matrix universityMatrix = Utils.makeMatrixFromPairs(universityPairs);
        Matrix programMatrix = Utils.makeMatrixFromPairs(programPairs);
        Matrix demandMatrix = Utils.makeMatrixFromPairs(demandPairs);
        Matrix salaryMatrix = Utils.makeMatrixFromPairs(salaryPairs);
        Matrix numbersMatrix = Utils.makeMatrixFromPairs(numbersPairs);
        Matrix scoreMatrix = Utils.makeMatrixFromPairs(scorePairs);
        List<Double> universityWeights = Utils.calculateWeights(universityMatrix);
        List<Double> programWeights = Utils.calculateWeights(programMatrix);
        List<Double> demandWeights = Utils.calculateWeights(demandMatrix);
        List<Double> salaryWeights = Utils.calculateWeights(salaryMatrix);
        List<Double> numbersWeights = Utils.calculateWeights(numbersMatrix);
        List<Double> scoreWeights = Utils.calculateWeights(scoreMatrix);

        List<Double> totalWeights = new ArrayList<>();
        for (int i = 0; i < optionList.size(); i++) {
            double weight = 0;
            weight += universityWeights.get(i) * weights2.get(1);
            weight += programWeights.get(i) * weights2.get(0);
            weight += demandWeights.get(i) * weights2.get(3);
            weight += salaryWeights.get(i) * weights2.get(2);
            weight += numbersWeights.get(i) * weights2.get(4);
            weight += scoreWeights.get(i) * weights2.get(5);
            totalWeights.add(weight);
        }

        while (outputTableModel.getRowCount() > 0) outputTableModel.removeRow(outputTableModel.getRowCount() - 1);
        for (int i = 0; i < totalWeights.size(); i++) {
            outputTableModel.addRow(new Object[outputTableModel.getColumnCount()]);
            outputTableModel.setValueAt(optionList.get(i).program, i, Config.OutputTable.getIndex("Program"));
            outputTableModel.setValueAt(optionList.get(i).university, i, Config.OutputTable.getIndex("University"));
            outputTableModel.setValueAt(String.format("%.3f", totalWeights.get(i)), i, Config.OutputTable.getIndex("Value"));
        }


        /*try {
            log.setText("");

            }


        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }*/
    }


    private File loadFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TXT",  "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    private List<String> readFileRows(File file) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> l = new ArrayList<>();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                l.add(line);
            }
            return l;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static void main(String[] args) {
        BasicLookAndFeel darcula = new DarculaLaf();
        try {
            UIManager.setLookAndFeel(darcula);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame(TITLE);
        MainWindow gui = new MainWindow();
        frame.setContentPane(gui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
