/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.lexoimporter;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.cnr.ilc.lc.lexoimporter.lexiconUtil.Constant;
import it.cnr.ilc.lc.lexoimporter.lexiconUtil.LexiconUtils;
import it.cnr.ilc.lc.lexoimporter.lexiconUtil.Namespace;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 * @author andreabellandi
 */
public class TalmudGlossaryImporter implements Importer {

    @Override
    public OWLOntologyManager getConversion(InputStream glossary, String lang) {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology ontology = manager.createOntology(IRI.create(Namespace.LEXICON.replace("#", "")));
            Reader reader = new InputStreamReader(glossary);
            CsvToBean<GlossaryRow> conllToBean = new CsvToBeanBuilder(reader)
                    .withSeparator('\t')
                    .withType(GlossaryRow.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            Iterator<GlossaryRow> conllIterator = conllToBean.iterator();
            ArrayList<GlossaryRow> entries = new ArrayList<>();
            while (conllIterator.hasNext()) {
                GlossaryRow conll = conllIterator.next();
                entries.add(conll);
            }
            Collections.reverse(entries);
            getLexicon(manager, entries, lang);
            return manager;
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(CoNLLImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void getLexicon(OWLOntologyManager manager, ArrayList<GlossaryRow> entries, String lang) {
        LexiconUtils.createLexicon(lang, manager);
        LexiconUtils.createLexicon("heb", manager);
        ArrayList<GlossaryRow> e = new ArrayList<>();
        for (GlossaryRow entry : entries) {
            e.add(entry);
            if (entry.getType().equals("B")) {
                addEntry(manager, e, lang);
                e.clear();
            }
        }
    }

    private void addEntry(OWLOntologyManager manager, ArrayList<GlossaryRow> entry, String lang) {
        OWLNamedIndividual sense = null;
        if (entry.size() > 1) {
            // create multiword entry
            GlossaryRow e = new GlossaryRow();
//            Stack<String> formS = new Stack<>();
            Stack<String> lemmaS = new Stack<>();
            for (GlossaryRow comp : entry) {
                if (!Constant.STOP_POS.contains(comp.getFinGrainPoS()) && !comp.getLemma().startsWith("-") && !comp.getLemma().startsWith("(")) {
//                    LexiconUtils.createWord(lang, comp.getLemma(), comp.getLemma(), comp.getFinGrainPoS(), comp.getType(),
//                            comp.getTranslation(), comp.getTransliteration(), comp.getHebrew(), comp.getGloss(), manager, true);
                }
//                formS.push(comp.getForm());
                lemmaS.push(comp.getLemma());
                if (comp.getType().equals("B")) {
                    e.setFinGrainPoS(comp.getFinGrainPoS());
                    e.setTranslation(comp.getTranslation());
                    e.setTransliteration(comp.getTransliteration());
                    e.setHebrew(comp.getHebrew());
                    e.setGloss(comp.getGloss());
                }
            }

//            StringBuilder forma = new StringBuilder();
//            while (!formS.empty()) {
//                forma.append(formS.pop()).append((formS.size() > 0) ? " " : "");
//            }
            StringBuilder lemma = new StringBuilder();
            while (!lemmaS.empty()) {
                lemma.append(lemmaS.pop()).append((lemmaS.size() > 0) ? " " : "");
            }

            sense = LexiconUtils.createMultiWord(lang, lemma.toString(), e.getFinGrainPoS(), e.getTranslation(),
                    e.getTransliteration(), e.getHebrew(), e.getGloss(), manager, false);
        } else {
            // crete entry
            GlossaryRow e = entry.get(0);
            sense = LexiconUtils.createWord(lang, e.getLemma(), e.getLemma(), e.getFinGrainPoS(), e.getType(),
                    e.getTranslation(), e.getTransliteration(), e.getHebrew(), e.getGloss(), manager, false);
        }
        addHebrewEntry(entry.get(0), sense, manager);
    }

    private void addHebrewEntry(GlossaryRow entry, OWLNamedIndividual sense, OWLOntologyManager manager) {
        if (entry.getHebrew() != null) {
            if (!entry.getHebrew().isEmpty()) {
                String hebrew = entry.getHebrew();
                if (hebrew.contains(" ")) {
                    // multiword case
                    for (String hebComp : hebrew.split(" ")) {
                        if (!hebComp.startsWith("-") && !hebComp.startsWith("(")) {
//                            LexiconUtils.createHebrewWord(hebComp, "NOUN", "heb", null, null, manager, true);
                        }
                    }
                    LexiconUtils.createHebrewMultiword(hebrew, "NOUN", "heb", entry.getTransliteration(), sense, manager, false);
                } else {
                    // word case
                    LexiconUtils.createHebrewWord(hebrew, "NOUN", "heb", entry.getTransliteration(), sense, manager, false);
                }
            }
        }
    }

    private String reverse(String s) {
        String ret = "";
        String[] a = s.split(" ");
        for (int counter = a.length - 1; counter >= 0; counter--) {
            ret = ret + a[counter] + " ";
        }
        return ret.trim();
    }

    // ID	FORMA	POS	B/I	TRADUZIONE 	TRASLITTERAZIONE	EBRAICO	GLOSSA
    public static class GlossaryRow {

        @CsvBindByPosition(position = 0)
        private int row;

        @CsvBindByPosition(position = 1)
        private String lemma;

        @CsvBindByPosition(position = 2)
        private String finGrainPoS;

        @CsvBindByPosition(position = 3)
        private String type;

        @CsvBindByPosition(position = 4)
        private String translation;

        @CsvBindByPosition(position = 5)
        private String transliteration;

        @CsvBindByPosition(position = 6)
        private String hebrew;

        @CsvBindByPosition(position = 7)
        private String gloss;

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public String getLemma() {
            return lemma;
        }

        public void setLemma(String lemma) {
            this.lemma = lemma;
        }

        public String getFinGrainPoS() {
            return finGrainPoS;
        }

        public void setFinGrainPoS(String finGrainPoS) {
            this.finGrainPoS = finGrainPoS;
        }

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }

        public String getTransliteration() {
            return transliteration;
        }

        public void setTransliteration(String transliteration) {
            this.transliteration = transliteration;
        }

        public String getHebrew() {
            return hebrew;
        }

        public void setHebrew(String hebrew) {
            this.hebrew = hebrew;
        }

        public String getGloss() {
            return gloss;
        }

        public void setGloss(String gloss) {
            this.gloss = gloss;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return getRow() + " " + " " + getLemma() + " " + getFinGrainPoS() + " " + getType() + " " + getTranslation() + " " + getTransliteration() + " " + getHebrew() + " " + getGloss();
        }

    }

}
