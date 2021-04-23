package com.gone.g_one_app;

public class TestScore {
    private int testid;
    private String username;
    private String teststatus;
    private int score;

    public TestScore(){

    }

    public TestScore(int testid, String username, String teststatus, int score){
        this.testid = testid;
        this.username = username;
        this.teststatus = teststatus;
        this.score = score;
    }

    public int getTestid() {
        return testid;
    }

    public void setTestid(int testid) {
        this.testid = testid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeststatus() {
        return teststatus;
    }

    public void setTeststatus(String teststatus) {
        this.teststatus = teststatus;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
