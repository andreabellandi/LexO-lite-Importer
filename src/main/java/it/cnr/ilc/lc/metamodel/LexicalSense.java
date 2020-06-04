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
public class LexicalSense {

    private String definition;
    private OntologyEntity ontologyEntity;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public OntologyEntity getOntologyEntity() {
        return ontologyEntity;
    }

    public void setOntologyEntity(OntologyEntity ontologyEntity) {
        this.ontologyEntity = ontologyEntity;
    }

}
