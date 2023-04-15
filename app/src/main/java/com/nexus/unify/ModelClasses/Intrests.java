package com.nexus.unify.ModelClasses;

import java.io.Serializable;

public class Intrests implements Serializable {

    private boolean isCheked=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  String name;

    public boolean isCheked() {
        return isCheked;
    }
    public void  setCheked(boolean cheked){
        isCheked=cheked;
    }


}
