/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.lexoimporter.lexiconUtil;

import it.cnr.ilc.lc.lexoimporter.CoNLLImporter;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 *
 * @author andrea
 */
public class LexiconUtils {
 
    
    
    public static void createLexicon(String lang) {
    }
    
    public static void createWord(String form, String lemma, String finGrainPoS, String coarseGrainPoS,
            String firstTraitGroup, String secondTraitGroup, String thirdTraitGroup) {
        
    }
    
    public static void createMultiwordExpression(String... components) {}
    
    public static String getIRI(String... params) {
        StringBuilder iri = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            iri.append(params[i]);
            if (i < (params.length - 1)) {
                iri.append("_");
            }
        }
        return iri.toString();
    }
    
//    private OWLNamedIndividual getIndividual(String uri) {
//        return factory.getOWLNamedIndividual(pm.getPrefixName2PrefixMap().get("lexicon:"), uri);
//    }
   
        // params: langName, uriLang, lingCat, descritpion, creator
//    public void addNewLangLexicon(String... params) {
//        OWLClass lexiconClass = factory.getOWLClass(pm.getPrefixName2PrefixMap().get("lime:"), "Lexicon");
//        OWLNamedIndividual lexiconEntry = factory.getOWLNamedIndividual(pm.getPrefixName2PrefixMap().get("lexicon:"), params[0] + "_lexicon");
//        addIndividualAxiom(lexiconClass, lexiconEntry);
//        addDataPropertyAxiom("language", lexiconEntry, params[0], pm.getPrefixName2PrefixMap().get("lime:"));
//        addDataPropertyAxiom("language", lexiconEntry, params[1], pm.getPrefixName2PrefixMap().get("dct:"));
//        addDataPropertyAxiom("linguisticCatalog", lexiconEntry, params[2], pm.getPrefixName2PrefixMap().get("lime:"));
//        addDataPropertyAxiom("description", lexiconEntry, params[3], pm.getPrefixName2PrefixMap().get("dct:"));
//        addDataPropertyAxiom("creator", lexiconEntry, params[4], pm.getPrefixName2PrefixMap().get("dct:"));
//    }
//        private static void setPrefixes() {
//        pm = new DefaultPrefixManager();
//        pm.setPrefix("lexicon", "");
//        pm.setPrefix("lexinfo", "https://www.lexinfo.net/ontology/2.0/lexinfo#");
//        pm.setPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
//        pm.setPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
//        pm.setPrefix("ontolex", "http://www.w3.org/ns/lemon/ontolex#");
//        pm.setPrefix("lime", "http://www.w3.org/ns/lemon/lime#");
//        pm.setPrefix("dct", "http://purl.org/dc/terms/");
//        pm.setPrefix("decomp", "http://www.w3.org/ns/lemon/decomp#");
//        pm.setPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
//        pm.setPrefix("vartrans", "http://www.w3.org/ns/lemon/vartrans#");
//        pm.setPrefix("trcat", "http://purl.org/net/translation-categories#");
//        pm.setPrefix("synsem", "http://www.w3.org/ns/lemon/synsem#");
//    }
    
}
