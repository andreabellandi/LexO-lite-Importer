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
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 * @author andrea
 */
public class CoNLLImporter implements Importer {

    @Override
    public OWLOntologyManager getConversion(InputStream CoNLL, String lang) {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology ontology = manager.createOntology(IRI.create(Namespace.LEXICON.replace("#", "")));
            Reader reader = new InputStreamReader(CoNLL);
            CsvToBean<CoNLLRow> conllToBean = new CsvToBeanBuilder(reader)
                    .withSeparator('\t')
                    .withType(CoNLLRow.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            Iterator<CoNLLRow> conllIterator = conllToBean.iterator();
            ArrayList<CoNLLRow> entries = new ArrayList<>();
            while (conllIterator.hasNext()) {
                CoNLLRow conll = conllIterator.next();
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

    private void getLexicon(OWLOntologyManager manager, ArrayList<CoNLLRow> entries, String lang) {
        LexiconUtils.createLexicon(lang, manager);
        ArrayList<CoNLLRow> e = new ArrayList<>();
        for (CoNLLRow entry : entries) {
            e.add(entry);
            if (entry.getType().equals("B")) {
                addEntry(manager, e, lang);
                e.clear();
            }
        }
    }

    private void addEntry(OWLOntologyManager manager, ArrayList<CoNLLRow> entry, String lang) {
        if (entry.size() > 1) {
            // create multiword entry
            CoNLLRow e = new CoNLLRow();
            Stack<String> formS = new Stack<>();
            Stack<String> lemmaS = new Stack<>();
            // e.setForm("");
            // e.setLemma("");
            for (CoNLLRow comp : entry) {
                if (!Constant.STOP_POS.contains(comp.getFinGrainPoS())) {
                    LexiconUtils.createWord(lang, comp.getForm(), comp.getLemma(), comp.getFinGrainPoS(), comp.getCoarseGrainPoS(),
                            comp.getFirstTraitGroup(), comp.getSecondTraitGroup(), comp.getThirdTraitGroup(), manager);
                }
                //e.setForm(e.getForm() + comp.getForm() + " ");
                //e.setLemma(e.getLemma() + comp.getLemma() + " ");
                formS.push(comp.getForm());
                lemmaS.push(comp.getLemma());
                if (comp.getType().equals("B")) {
                    e.setFinGrainPoS(comp.getFinGrainPoS());
                    e.setCoarseGrainPoS(comp.getCoarseGrainPoS());
                    e.setFirstTraitGroup(comp.getFirstTraitGroup());
                    e.setSecondTraitGroup(comp.getSecondTraitGroup());
                    e.setThirdTraitGroup(comp.getThirdTraitGroup());
                }
            }

            StringBuilder forma = new StringBuilder();
            while (!formS.empty()) {
                forma.append(formS.pop()).append((formS.size() > 0) ? " " : "");
            }

            StringBuilder lemma = new StringBuilder();
            while (!lemmaS.empty()) {
                lemma.append(lemmaS.pop()).append((lemmaS.size() > 0) ? " " : "");
            }

            //System.err.println("<" + forma.toString() + "> <" + lemma.toString() + "> <" + e.getFinGrainPoS() + ">");
            LexiconUtils.createMultiWord(lang, forma.toString(), lemma.toString(), e.getFinGrainPoS(), e.getCoarseGrainPoS(),
                    e.getFirstTraitGroup(), e.getSecondTraitGroup(), e.getThirdTraitGroup(), manager);
            /*    LexiconUtils.createMultiWord(lang, reverse(e.getForm()), reverse(e.getLemma()), e.getFinGrainPoS(), e.getCoarseGrainPoS(),
                    e.getFirstTraitGroup(), e.getSecondTraitGroup(), e.getThirdTraitGroup(), manager);*/
        } else {
            // crete entry
            CoNLLRow e = entry.get(0);
            LexiconUtils.createWord(lang, e.getForm(), e.getLemma(), e.getFinGrainPoS(), e.getCoarseGrainPoS(),
                    e.getFirstTraitGroup(), e.getSecondTraitGroup(), e.getThirdTraitGroup(), manager);
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

    public static class CoNLLRow {

        @CsvBindByPosition(position = 0)
        private int row;

        @CsvBindByPosition(position = 1)
        private String form;

        @CsvBindByPosition(position = 2)
        private String lemma;

        @CsvBindByPosition(position = 3)
        private String finGrainPoS;

        @CsvBindByPosition(position = 4)
        private String coarseGrainPoS;

        @CsvBindByPosition(position = 5)
        private String firstTraitGroup;

        @CsvBindByPosition(position = 6)
        private String secondTraitGroup;

        @CsvBindByPosition(position = 7)
        private String thirdTraitGroup;

        @CsvBindByPosition(position = 8)
        private String type;

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

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public String getFinGrainPoS() {
            return finGrainPoS;
        }

        public void setFinGrainPoS(String finGrainPoS) {
            this.finGrainPoS = finGrainPoS;
        }

        public String getCoarseGrainPoS() {
            return coarseGrainPoS;
        }

        public void setCoarseGrainPoS(String coarseGrainPoS) {
            this.coarseGrainPoS = coarseGrainPoS;
        }

        public String getFirstTraitGroup() {
            return firstTraitGroup;
        }

        public void setFirstTraitGroup(String firstTraitGroup) {
            this.firstTraitGroup = firstTraitGroup;
        }

        public String getSecondTraitGroup() {
            return secondTraitGroup;
        }

        public void setSecondTraitGroup(String secondTraitGroup) {
            this.secondTraitGroup = secondTraitGroup;
        }

        public String getThirdTraitGroup() {
            return thirdTraitGroup;
        }

        public void setThirdTraitGroup(String thirdTraitGroup) {
            this.thirdTraitGroup = thirdTraitGroup;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return getRow() + " " + getForm() + " " + getLemma() + " " + getCoarseGrainPoS() + " " + getFinGrainPoS() + " " + getFirstTraitGroup() + " " + getSecondTraitGroup() + " " + getThirdTraitGroup() + " " + getType();
        }

    }

}
