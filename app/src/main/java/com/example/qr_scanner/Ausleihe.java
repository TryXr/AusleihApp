package com.example.qr_scanner;
import java.time.LocalDateTime;


public class Ausleihe {

    String vname;
    String name;
    String bez;
    LocalDateTime date;
    int borrowedid;

    public Ausleihe(String vname, String name, String bez, LocalDateTime date, int borrowedid) {
        this.vname = vname;
        this.name = name;
        this.bez = bez;
        this.date = date;
        this.borrowedid = borrowedid;
    }

    public Ausleihe(){

    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBez() {
        return bez;
    }

    public void setBez(String bez) {
        this.bez = bez;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getBorrowedid() {
        return borrowedid;
    }

    public void setBorrowedid(int borrowedid) {
        this.borrowedid = borrowedid;
    }

    @Override
    public String toString() {
        return
                bez + " " + vname + " " + name + " " +  date;
    }
}
