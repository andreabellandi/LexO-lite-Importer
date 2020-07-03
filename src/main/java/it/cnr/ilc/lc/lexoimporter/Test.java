/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.lexoimporter;

import it.cnr.ilc.lc.lexoimporter.lexiconUtil.Constant;
import it.cnr.ilc.lc.lexoimporter.lexiconUtil.Namespace;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 *
 * @author andrea
 */
public class Test {

    // arg[1] : file to convert
    // arg[2] : IRI to assign to
    public static void main(String[] args) throws IOException, OWLOntologyStorageException {
        CoNLL2OntoLex();
//        TalmudGlossary2OntoLex();
    }

    private static void CoNLL2OntoLex() throws IOException, OWLOntologyStorageException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(Constant.SOURCE_FILE_PATH);
            OWLOntologyManager manager = ImporterFactory.getImporter(LexiconModel.CoNLL).getConversion(inputStream, "it");
            persist(manager);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void TalmudGlossary2OntoLex() throws IOException, OWLOntologyStorageException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(Constant.SOURCE_FILE_PATH);
            OWLOntologyManager manager = ImporterFactory.getImporter(LexiconModel.TALMUD_GLOSSARY).getConversion(inputStream, "it");
            persist(manager);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void persist(OWLOntologyManager manager) throws IOException, OWLOntologyStorageException {
        File f = new File(Constant.TARGET_FILE_PATH);
        try (FileOutputStream fos = new FileOutputStream(Constant.TARGET_FILE_PATH)) {
            manager.saveOntology(manager.getOntology(IRI.create(Namespace.LEXICON.replace("#", ""))), fos);
        }
    }

}
