/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.lexoimporter;

/**
 *
 * @author andrea
 */
public class ImporterFactory {

    public static Importer getImporter(LexiconModel model) {
        Importer importer = null;
        switch (model) {
            case CoNLL:
                importer = new CoNLLImporter();
                break;
        }
        return importer;
    }
}
