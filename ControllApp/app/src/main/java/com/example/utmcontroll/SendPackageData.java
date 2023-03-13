package com.example.utmcontroll;

public class SendPackageData {
    private  Integer Trottle = 0;
    private Integer Yau = 0;
    private Integer Pitch = 0;
    private Integer Roll = 0;

    private Integer Aux1 = 0;
    private Integer Aux2 = 0;

    public Integer getTrottle() {
        return Trottle;
    }

    public void setTrottle(Integer trottle) {
        Trottle = trottle;
    }

    public Integer getYau() {
        return Yau;
    }

    public void setYau(Integer yau) {
        Yau = yau;
    }

    public Integer getPitch() {
        return Pitch;
    }

    public void setPitch(Integer pitch) {
        Pitch = pitch;
    }

    public Integer getRoll() {
        return Roll;
    }

    public void setRoll(Integer roll) {
        Roll = roll;
    }

    public Integer getAux1() {
        return Aux1;
    }

    public void setAux1(Integer aux1) {
        Aux1 = aux1;
    }

    public Integer getAux2() {
        return Aux2;
    }

    public void setAux2(Integer aux2) {
        Aux2 = aux2;
    }


    public Integer[] GetPackageData(){

        Integer arr[] = new Integer[6];
        arr[0] = Trottle;
        arr[1] = Pitch;
        arr[2] = Yau;
        arr[3] = Roll;
        arr[4] = Aux1;
        arr[5] = Aux2;

        return  arr;
    }

}
