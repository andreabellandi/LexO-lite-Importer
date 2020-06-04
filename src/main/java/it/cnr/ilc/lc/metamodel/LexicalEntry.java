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
public class LexicalEntry {

    public enum TYPE {
        WORD,
        MULTIWORD_EXPRESSION
    }

    public enum STATUS {
        APPROVED,
        NONE
    }

    private TYPE type;
    private STATUS status;
    private String language;
    private String pos;
    private String label;
    private String creator;
    private String creation;

    private ArrayList<LexicalEntry> components;
    private ArrayList<Form> forms;
    private ArrayList<LexicalSense> senses;

    public String getType() {
        return this.type.name();
    }

    public void setType(String type) {
        this.type = TYPE.valueOf(type);
    }

    public String getStatus() {
        return this.status.name();
    }

    public void setStatus(String status) {
        this.status = STATUS.valueOf(status);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public ArrayList<LexicalEntry> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<LexicalEntry> components) {
        this.components = components;
    }

    public ArrayList<Form> getForms() {
        return forms;
    }

    public void setForms(ArrayList<Form> forms) {
        this.forms = forms;
    }

    public ArrayList<LexicalSense> getSenses() {
        return senses;
    }

    public void setSenses(ArrayList<LexicalSense> senses) {
        this.senses = senses;
    }

}
