package com.kierasis.healthdeclarationapp.doctor;

public class doctor_ext_view_patient_prescribe {
    private String medicine;
    private String amount;
    private String time;
    private String daynum;
    private String daytext;
    private String image;

    public doctor_ext_view_patient_prescribe(){}
    public doctor_ext_view_patient_prescribe(String medicine, String amount, String time, String daynum, String daytext,String image){
        this.medicine = medicine;
        this.amount = amount;
        this.time = time;
        this.daynum = daynum;
        this.daytext = daytext;
        this.image = image;

    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDayNum() {
        return daynum;
    }

    public void setDayNum(String daynum) {
        this.daynum = daynum;
    }

    public String getDayText() {
        return daytext;
    }

    public void setDayText(String daytext) {
        this.daytext = daytext;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}