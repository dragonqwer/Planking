package com.dragon.planking.model;

public class Data
{
    public Data(){
        
    }
    private int date_y;    //时间    
    private int date_mo;    //时间    
    private int date_d;    //时间    
    private int date_h;    //时间    
    private int date_m;    //时间    
    private int date_s;    //时间    
    private long result;      //成绩
    public int getDate_y()
    {
        return date_y;
    }
    public void setDate_y(int date_y)
    {
        this.date_y = date_y;
    }
    public int getDate_mo()
    {
        return date_mo;
    }
    public void setDate_mo(int date_mo)
    {
        this.date_mo = date_mo;
    }
    public int getDate_d()
    {
        return date_d;
    }
    public void setDate_d(int date_d)
    {
        this.date_d = date_d;
    }
    public int getDate_h()
    {
        return date_h;
    }
    public void setDate_h(int date_h)
    {
        this.date_h = date_h;
    }
    public int getDate_m()
    {
        return date_m;
    }
    public void setDate_m(int date_m)
    {
        this.date_m = date_m;
    }
    public int getDate_s()
    {
        return date_s;
    }
    public void setDate_s(int date_s)
    {
        this.date_s = date_s;
    }
    public long getResult()
    {
        return result;
    }
    public void setResult(long result)
    {
        this.result = result;
    }
    @Override
    public String toString()
    {
        return "Data [date_y=" + date_y + ", date_mo=" + date_mo + ", date_d=" + date_d + ", date_h=" + date_h + ", date_m=" + date_m + ", date_s="
                + date_s + ", result=" + result + "]";
    }

    

    
    
}
