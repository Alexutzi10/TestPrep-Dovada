package com.example.testprep_dovada.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "dovezi")
public class Dovada {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private Date date;
    private String name;
    private int period;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Dovada(long id, Date date, String name, int period) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.period = period;
    }

    @Ignore
    public Dovada(Date date, String name, int period) {
        this.date = date;
        this.name = name;
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dovada dovada = (Dovada) o;
        return period == dovada.period && Objects.equals(date, dovada.date) && Objects.equals(name, dovada.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, name, period);
    }

    @Override
    public String toString() {
        return "Dovada{" +
                "id=" + id +
                ", date=" + date +
                ", name='" + name + '\'' +
                ", period=" + period +
                '}';
    }
}
