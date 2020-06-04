/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.metamodel;

/**
 *
 * @author andrea
 */
public class OntologyEntity {

    public enum TYPE {
        CLAZZ,
        DATATYPE_PROPERTY,
        OBJECT_PROPERTY,
        INDIVIDUAL
    }
    private TYPE type;
    private String name;
    private String scheme;

    public String getType() {
        return this.type.name();
    }

    public void setType(String type) {
        this.type = TYPE.valueOf(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

}
