/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.lexoimporter;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 * @author andrea
 */
public class CoNLLImporter implements Importer {
    
    private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    
    @Override
    public OWLOntologyManager getConversion(InputStream CoNLL, String iri) {
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
        getLexicon(entries);    
        return null;
    }

    private void getLexicon(ArrayList<CoNLLRow> entries) {
        ArrayList<CoNLLRow> e = new ArrayList<>();
        for (CoNLLRow entry : entries) {
            e.add(entry);
            if (entry.getType().equals("B")) {
                addEntry(e);
                e.clear();
            }
        }
    }
    
    private void addEntry(ArrayList<CoNLLRow> entry) {
        if (entry.size() > 1) {
            // create multiword entry
            for (CoNLLRow comp : entry) {
                // create comp (comp)
            }
            // crete entry
        } else {
            // crete entry
            
        }
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

}
    
}
