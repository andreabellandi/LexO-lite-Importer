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
    public static final List<String> stopPoS = Arrays.asList("ADP");
    public static final String RESOURCE_CREATOR = "Impoterd by LexO-lite importer";
    public static final String RESOURCE_DESCRIPTION = "https://github.com/andreabellandi/LexO-lite-Importer";
    public static final String LINGUISTIC_CATALOG = "http://www.lexinfo.net/ontologies/2.0/lexinfo";

}
