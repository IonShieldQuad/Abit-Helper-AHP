package com.ionshield.abithelperahp.core;

public class Option {

    public int id;
    public String program;
    public String university;
    public double demand;
    public double salary;
    public int numbers;
    public int score;

    public Option() {
    }

    public Option(int id, String program, String university, double demand, double salary, int numbers, int score) {
        this.id = id;
        this.program = program;
        this.university = university;
        this.demand = demand;
        this.salary = salary;
        this.numbers = numbers;
        this.score = score;
    }
}
