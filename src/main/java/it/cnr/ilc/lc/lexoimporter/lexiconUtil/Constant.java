/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.lexoimporter.lexiconUtil;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author andrea
 */
public class Constant {

    public static final String LEXICON_INDIVIDUAL_NAME = "conll";
    public static final String WORD_TYPE = OntoLexEntity.Class.WORD.getLabel();
    public static final String MULTIWORD_TYPE = OntoLexEntity.Class.MULTIWORD.getLabel();
    public static final List<String> STOP_POS = Arrays.asList("ADP");
    public static final String RESOURCE_CREATOR = "Impoterd by LexO-lite importer";
    public static final String RESOURCE_DESCRIPTION = "https://github.com/andreabellandi/LexO-lite-Importer";
    public static final String LINGUISTIC_CATALOG = "http://www.lexinfo.net/ontologies/3.0/lexinfo";
    public static final String SOURCE_FILE_PATH = System.getProperty("user.home")+"/Documents/RABBINI/conll4lexo.txt";
    public static final String TARGET_FILE_PATH = System.getProperty("user.home") + "/.LexO-lite/mylexicon.owl";

}
