package com.example.utmcontroll;

public class SendPackageData {
    private Byte Trottle = 0;
    private Byte Yau = 0;
    private Byte Pitch = 0;
    private Byte Roll = 0;

    private Byte Aux1 = 0;
    private Byte Aux2 = 0;

    public Byte getTrottle() {
        return Trottle;
    }

    public void setTrottle(Byte trottle) {
        Trottle = trottle;
    }

    public Byte getYau() {
        return Yau;
    }

    public void setYau(Byte yau) {
        Yau = yau;
    }

    public Byte getPitch() {
        return Pitch;
    }

    public void setPitch(Byte pitch) {
        Pitch = pitch;
    }

    public Byte getRoll() {
        return Roll;
    }

    public void setRoll(Byte roll) {
        Roll = roll;
    }

    public Byte getAux1() {
        return Aux1;
    }

    public void setAux1(Byte aux1) {
        Aux1 = aux1;
    }

    public Byte getAux2() {
        return Aux2;
    }

    public void setAux2(Byte aux2) {
        Aux2 = aux2;
    }


    public Byte[] GetPackageData(){

        Byte arr[] = new Byte[6];
        arr[0] = Trottle;
        arr[1] = Pitch;
        arr[2] = Yau;
        arr[3] = Roll;
        arr[4] = Aux1;
        arr[5] = Aux2;

        return  arr;
    }

}
