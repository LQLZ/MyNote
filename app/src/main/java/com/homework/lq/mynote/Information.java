package com.homework.lq.mynote;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ASUS on 2018/5/21.
 */

public class Information {
    private String content;
    private int degree;
    private String date;
    private int id;
    private String dateDifferent;



    public Information(String content, int degree, String date,int id){
        this.content = content;
        this.degree = degree;
        this.date = date;
        this.id = id;
    }

    public String getContent(){
        return content;
    }

    public int getDegree(){
        return degree;
    }

    public String getDate(){
        return date;
    }
    public int getId(){
        return id;
    }

    public String getDateDifferent(){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dt1 = format1.parse(getDate());
            Date dt2 = new Date();
            int different = differentDays(dt1, dt2);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日");
            String date1 = format2.format(dt1);
            if (different > 0) {
                dateDifferent = "距"+ date1 + "还有" + different + "天";
            } else if (different < 0) {
                different = -different;
                dateDifferent = "距"+ date1 + "已过" + different + "天";
            }else{
                dateDifferent = "距"+ date1 +  "0天";
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return dateDifferent;
    }


    public int differentDays(Date date1, Date date2)
    {
        int days = (int) ((date1.getTime() - date2.getTime()) / (1000*3600*24));
        return days;
    }




}
