/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.lexoimporter.lexiconUtil;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andrea
 */
public class CoNLLMapToLexInfo {

    public final static Map<String, String> posMapping = new HashMap<String, String>();
    static {
        posMapping.put("NOUN", "noun");
        posMapping.put("PROPN", "noun");
        posMapping.put("ADJ", "adjective");
        posMapping.put("ADP", "preposition");
    }
    
    public final static Map<String, String> phraseTypeMapping = new HashMap<String, String>();
    static {
        phraseTypeMapping.put("NOUN", "noun");
        phraseTypeMapping.put("PROPN", "noun");
        phraseTypeMapping.put("ADJ", "adjective");
    }
    
    public final static Map<String, String> morphoTraitMapping = new HashMap<String, String>();
    static {
        morphoTraitMapping.put("Fem", "feminine");
        morphoTraitMapping.put("Masc", "masculine");
        morphoTraitMapping.put("Sing", "singular");
        morphoTraitMapping.put("Plur", "plural");
    }
    

}
