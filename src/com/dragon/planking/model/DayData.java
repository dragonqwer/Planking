package com.dragon.planking.model;

import java.util.ArrayList;

public class DayData
{
    private int year;
    private int month;
    private int day;
    private long count = 0;
    private ArrayList<Data> datalist = new ArrayList<Data>();
    
    public DayData(){
        
    }
    
    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getDay()
    {
        return day;
    }

    public void setDay(int day)
    {
        this.day = day;
    }
    
    public String getData()
    {
        return year+"-"+month+"-"+day;
    }
    
    public long getCount()
    {
        return count;
    }
    
    public ArrayList<Data> getDatalist()
    {
        return datalist;
    }
    
    public void addData(Data data)
    {
        this.count = count+data.getResult(); 
        datalist.add(data);
    }

    @Override
    public String toString()
    {
        return "DayData [year=" + year + ", month=" + month + ", day=" + day + ", count=" + count + ", datalist=" + datalist + "]";
    }
    
}
