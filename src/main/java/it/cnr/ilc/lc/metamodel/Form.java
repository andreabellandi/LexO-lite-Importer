/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.metamodel;

import java.util.ArrayList;

/**
 *
 * @author andrea
 */
public abstract class Form {

    private String writtenRep;
    private ArrayList<Morphology> morphology;

    public String getWrittenRep() {
        return writtenRep;
    }

    public void setWrittenRep(String writtenRep) {
        this.writtenRep = writtenRep;
    }

    public ArrayList<Morphology> getMorphology() {
        return morphology;
    }

    public void setMorphology(ArrayList<Morphology> morphology) {
        this.morphology = morphology;
    }

}
