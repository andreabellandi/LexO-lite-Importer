/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.lexoimporter.lexiconUtil;

import it.cnr.ilc.lc.lexoimporter.CoNLLImporter;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.search.Searcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 *
 * @author andrea
 */
public class LexiconUtils {

    public static void createLexicon(String lang, OWLOntologyManager manager) {
        OWLNamedIndividual lexicon = getIndividual(Constant.LEXICON_INDIVIDUAL_NAME + "_" + lang, Namespace.LEXICON, manager);
        OWLClass lexiconClass = manager.getOWLDataFactory().getOWLClass(Namespace.LIME, "Lexicon");
        addIndividualAxiom(lexiconClass, lexicon, manager);
        addDataPropertyAxiom("language", lexicon, lang, Namespace.LIME, manager);
        addDataPropertyAxiom("language", lexicon, lang, Namespace.DCT, manager);
        addDataPropertyAxiom("linguisticCatalog", lexicon, Constant.LINGUISTIC_CATALOG, Namespace.LIME, manager);
        addDataPropertyAxiom("description", lexicon, Constant.RESOURCE_DESCRIPTION, Namespace.DCT, manager);
        addDataPropertyAxiom("creator", lexicon, Constant.RESOURCE_CREATOR, Namespace.DCT, manager);
    }

    // for CoNLL
    public static void createWord(String lang, String form, String lemma, String finGrainPoS, String coarseGrainPoS,
            String firstTraitGroup, String secondTraitGroup, String thirdTraitGroup, OWLOntologyManager manager, boolean isComponent) {

        createLemma(Constant.WORD_TYPE, lang, form, lemma, finGrainPoS, coarseGrainPoS, firstTraitGroup, secondTraitGroup, thirdTraitGroup, manager, isComponent);
        if (!form.equals(lemma)) {
            createForm(Constant.WORD_TYPE, lang, form, lemma, finGrainPoS, coarseGrainPoS, firstTraitGroup, secondTraitGroup, thirdTraitGroup, manager);
        }
    }

    // for Glossary
    public static OWLNamedIndividual createWord(String lang, String lemma, String form, String finGrainPoS, String type,
            String translation, String transliteration, String hebrew, String gloss, OWLOntologyManager manager, boolean isComponent) {
        return createLemma(Constant.WORD_TYPE, lang, lemma, finGrainPoS, translation, transliteration, hebrew, gloss, manager, isComponent);
    }

    // for CoNLL
    public static void createLemma(String type, String lang, String form, String lemma, String finGrainPoS, String coarseGrainPoS,
            String firstTraitGroup, String secondTraitGroup, String thirdTraitGroup, OWLOntologyManager manager, boolean isComponent) {

        String lemmaInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_") : lemma, CoNLLMapToLexInfo.posMapping.get(finGrainPoS), lang, "lemma");
        String senseInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_") : lemma, CoNLLMapToLexInfo.posMapping.get(finGrainPoS), lang, "sense1");
        String entryInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_") : lemma, CoNLLMapToLexInfo.posMapping.get(finGrainPoS), lang, "entry");

        OWLNamedIndividual lexicon = getIndividual(Constant.LEXICON_INDIVIDUAL_NAME + "_" + lang, Namespace.LEXICON, manager);
        OWLNamedIndividual le = getEntry(entryInstance, type, manager);
        OWLNamedIndividual cf = getForm(lemmaInstance, manager);
        OWLNamedIndividual s = getSense(senseInstance, manager);

        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.ENTRY.getLabel(), lexicon, le, Namespace.LIME, manager);
        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.CANONICALFORM.getLabel(), le, cf, Namespace.ONTOLEX, manager);
        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.SENSE.getLabel(), le, s, Namespace.ONTOLEX, manager);
        addDataPropertyAxiom(OntoLexEntity.DataProperty.WRITTENREP.getLabel(), cf, lemma, Namespace.ONTOLEX, manager);

        setMorphology(type, le, cf, finGrainPoS, coarseGrainPoS, firstTraitGroup, secondTraitGroup, thirdTraitGroup, manager);
    }

    // for Glossay
    public static OWLNamedIndividual createLemma(String type, String lang, String lemma, String finGrainPoS, String translation,
            String transliteration, String hebrew, String gloss, OWLOntologyManager manager, boolean isComponent) {

        String lemmaInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_").
                replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("–", "") : lemma, CoNLLMapToLexInfo.posMapping.get(finGrainPoS), lang, "lemma");
        String senseInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_").
                replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("–", "") : lemma, CoNLLMapToLexInfo.posMapping.get(finGrainPoS), lang, "sense1");
        String entryInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_").
                replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("–", "") : lemma, CoNLLMapToLexInfo.posMapping.get(finGrainPoS), lang, "entry");

        OWLNamedIndividual lexicon = getIndividual(Constant.LEXICON_INDIVIDUAL_NAME + "_" + lang, Namespace.LEXICON, manager);
        OWLNamedIndividual le = getEntry(entryInstance, type, manager);
        OWLNamedIndividual cf = getForm(lemmaInstance, manager);
        OWLNamedIndividual s = getSense(senseInstance, manager);

        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.ENTRY.getLabel(), lexicon, le, Namespace.LIME, manager);
        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.CANONICALFORM.getLabel(), le, cf, Namespace.ONTOLEX, manager);
        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.SENSE.getLabel(), le, s, Namespace.ONTOLEX, manager);
        addDataPropertyAxiom(OntoLexEntity.DataProperty.WRITTENREP.getLabel(), cf, lemma, Namespace.ONTOLEX, manager);

        if (!isComponent) {
            addDataPropertyAxiom("definition", s, gloss, Namespace.SKOS, manager);

            if (translation != null) {
                addDataPropertyAxiom(Constant.TALMUD_TRANSLATION, cf, translation, Namespace.EXTENSION, manager);
            }
        }

        addObjectPropertyAxiom("partOfSpeech", le,
                (Constant.WORD_TYPE.equals(type) ? getIndividual(CoNLLMapToLexInfo.posMapping.get(finGrainPoS), Namespace.LEXINFO, manager)
                : getIndividual(CoNLLMapToLexInfo.phraseTypeMapping.get(finGrainPoS), Namespace.LEXINFO, manager)),
                Namespace.LEXINFO, manager);
        addDataPropertyAxiom("valid", le, "true", Namespace.DCT, manager);

        return s;
    }

    public static void createForm(String type, String lang, String form, String lemma, String finGrainPoS, String coarseGrainPoS,
            String firstTraitGroup, String secondTraitGroup, String thirdTraitGroup, OWLOntologyManager manager) {

        String formInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_") : lemma, CoNLLMapToLexInfo.posMapping.get(finGrainPoS),
                lang, type.equals(Constant.MULTIWORD_TYPE) ? form.replaceAll(" ", "_") : form, "form");
        String entryInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_") : lemma, CoNLLMapToLexInfo.posMapping.get(finGrainPoS), lang, "entry");
        OWLNamedIndividual le = getIndividual(entryInstance, Namespace.LEXICON, manager);
        OWLNamedIndividual of = getForm(formInstance, manager);

        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.OTHERFORM.getLabel(), le, of, Namespace.ONTOLEX, manager);
        addDataPropertyAxiom(OntoLexEntity.DataProperty.WRITTENREP.getLabel(), of, form, Namespace.ONTOLEX, manager);

        setMorphology(type, le, of, finGrainPoS, coarseGrainPoS, firstTraitGroup, secondTraitGroup, thirdTraitGroup, manager);
    }

    private static void setMorphology(String type, OWLNamedIndividual le, OWLNamedIndividual cf, String finGrainPoS, String coarseGrainPoS,
            String firstTraitGroup, String secondTraitGroup, String thirdTraitGroup, OWLOntologyManager manager) {

        addObjectPropertyAxiom("partOfSpeech", le,
                (Constant.WORD_TYPE.equals(type) ? getIndividual(CoNLLMapToLexInfo.posMapping.get(finGrainPoS), Namespace.LEXINFO, manager)
                : getIndividual(CoNLLMapToLexInfo.phraseTypeMapping.get(finGrainPoS), Namespace.LEXINFO, manager)),
                Namespace.LEXINFO, manager);
        if (firstTraitGroup.contains("Gender")) {
            addObjectPropertyAxiom("gender", cf,
                    getIndividual(CoNLLMapToLexInfo.morphoTraitMapping.get(firstTraitGroup.split("\\|")[0].split("=")[1]),
                            Namespace.LEXINFO, manager), Namespace.LEXINFO, manager);
        }
        if (firstTraitGroup.contains("Number")) {
            addObjectPropertyAxiom("number", cf,
                    getIndividual(CoNLLMapToLexInfo.morphoTraitMapping.get(firstTraitGroup.split("\\|")[firstTraitGroup.contains("Gender") ? 1 : 0].split("=")[1]),
                            Namespace.LEXINFO, manager), Namespace.LEXINFO, manager);
        }
        addDataPropertyAxiom("valid", le, "false", Namespace.DCT, manager);
    }

    // for CoNLL
    public static void createMultiWord(String lang, String form, String lemma, String finGrainPoS, String coarseGrainPoS,
            String firstTraitGroup, String secondTraitGroup, String thirdTraitGroup, OWLOntologyManager manager, boolean isComponent) {

        createLemma(Constant.MULTIWORD_TYPE, lang, form, lemma, finGrainPoS, coarseGrainPoS, firstTraitGroup, secondTraitGroup, thirdTraitGroup, manager, isComponent);
        createDecomposition(lang, lemma, finGrainPoS, manager);
        if (!lemma.equals(form)) {
            createForm(Constant.MULTIWORD_TYPE, lang, form, lemma, finGrainPoS, coarseGrainPoS, firstTraitGroup, secondTraitGroup, thirdTraitGroup, manager);
        }
    }

    // for Glossary
    public static OWLNamedIndividual createMultiWord(String lang, String lemma, String finGrainPoS, String translation,
            String transliteration, String hebrew, String gloss, OWLOntologyManager manager, boolean isComponent) {

        OWLNamedIndividual sense = createLemma(Constant.MULTIWORD_TYPE, lang, lemma, finGrainPoS, translation, transliteration, hebrew, gloss, manager, isComponent);
        createDecomposition(lang, lemma, finGrainPoS, manager);
        return sense;
    }

    private static void createDecomposition(String lang, String lemma, String finGrainPoS, OWLOntologyManager manager) {

        String[] mwComponents = lemma.split(" ");
        String entryInstance = getIRI(lemma.replaceAll(" ", "_"), CoNLLMapToLexInfo.posMapping.get(finGrainPoS), lang, "entry");
        OWLNamedIndividual le = getIndividual(entryInstance, Namespace.LEXICON, manager);

        for (int i = 0; i < mwComponents.length; i++) {
            String position = Integer.toString(i);
            OWLNamedIndividual componentIndividual = getComponent(getIRI(entryInstance, "comp", position), manager);
            addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.CONSTITUENT.getLabel(), le, componentIndividual, Namespace.DECOMP, manager);
            addDataPropertyAxiom("comment", componentIndividual, position, Namespace.RDFS, manager);

            //OWLNamedIndividual leComp = getIndividual(getIRI(mwComponents[i], lang, "entry"), Namespace.LEXICON, manager);
            OWLNamedIndividual leComp = getCompOWLIndividual(mwComponents[i], manager);
            if (null != leComp) {
                addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.CORRESPONDSTO.getLabel(), componentIndividual, leComp, Namespace.DECOMP, manager);
            }
        }
    }

    private static OWLNamedIndividual getCompOWLIndividual(String comp, OWLOntologyManager manager) {
        OWLNamedIndividual ret = null;
        OWLDataProperty p = manager.getOWLDataFactory().getOWLDataProperty(Namespace.ONTOLEX, OntoLexEntity.DataProperty.WRITTENREP.getLabel());
        for (OWLIndividual i : EntitySearcher.getInstances(manager.getOWLDataFactory().getOWLClass(Namespace.ONTOLEX,
                OntoLexEntity.Class.FORM.getLabel()), manager.getOntology(IRI.create(Namespace.LEXICON.replace("#", ""))))
                .collect(Collectors.toList())) {
            Stream<OWLLiteral> s = EntitySearcher.getDataPropertyValues(i, p, manager.getOntology(IRI.create(Namespace.LEXICON.replace("#", ""))));
            if (((OWLLiteral) s.toArray()[0]).getLiteral().equals(comp)) {
                ret = manager.getOWLDataFactory().getOWLNamedIndividual(i.toStringID().replace("_lemma", "_entry"));
            }
        }
        return ret;
    }

    public static String getIRI(String... params) {
        StringBuilder iri = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            iri.append(sanitize(params[i]));
            if (i < (params.length - 1)) {
                iri.append("_");
            }
        }
        return iri.toString();
    }

    private static String sanitize(String s) {
        return s.replaceAll("'|‘", "_APOS_");
    }

    private static OWLNamedIndividual getEntry(String uri, String clazz, OWLOntologyManager manager) {
        OWLClass lexicalEntryClass = manager.getOWLDataFactory().getOWLClass(Namespace.ONTOLEX, clazz);
        OWLNamedIndividual lexicalEntry = manager.getOWLDataFactory().getOWLNamedIndividual(Namespace.LEXICON, uri);
        addIndividualAxiom(lexicalEntryClass, lexicalEntry, manager);
        return lexicalEntry;
    }

    private static OWLNamedIndividual getForm(String uri, OWLOntologyManager manager) {
        OWLClass lexicalFormClass = manager.getOWLDataFactory().getOWLClass(Namespace.ONTOLEX, OntoLexEntity.Class.FORM.getLabel());
        OWLNamedIndividual form = manager.getOWLDataFactory().getOWLNamedIndividual(Namespace.LEXICON, uri);
        addIndividualAxiom(lexicalFormClass, form, manager);
        return form;
    }

    private static OWLNamedIndividual getSense(String uri, OWLOntologyManager manager) {
        OWLClass lexicalSenseClass = manager.getOWLDataFactory().getOWLClass(Namespace.ONTOLEX, OntoLexEntity.Class.LEXICALSENSE.getLabel());
        OWLNamedIndividual sense = manager.getOWLDataFactory().getOWLNamedIndividual(Namespace.LEXICON, uri);
        addIndividualAxiom(lexicalSenseClass, sense, manager);
        return sense;
    }

    private static OWLNamedIndividual getIndividual(String uri, String ns, OWLOntologyManager manager) {
        return manager.getOWLDataFactory().getOWLNamedIndividual(ns, uri);
    }

    private static OWLNamedIndividual getComponent(String uri, OWLOntologyManager manager) {
        OWLClass ComponentClass = manager.getOWLDataFactory().getOWLClass(Namespace.DECOMP, OntoLexEntity.Class.COMPONENT.getLabel());
        OWLNamedIndividual c = manager.getOWLDataFactory().getOWLNamedIndividual(Namespace.LEXICON, uri);
        addIndividualAxiom(ComponentClass, c, manager);
        return c;
    }

    private static void addIndividualAxiom(OWLClass c, OWLNamedIndividual i, OWLOntologyManager manager) {
        OWLClassAssertionAxiom classAssertion = manager.getOWLDataFactory().getOWLClassAssertionAxiom(c, i);
        manager.addAxiom(manager.getOntology(IRI.create(Namespace.LEXICON.replace("#", ""))), classAssertion);
    }

    private static void addObjectPropertyAxiom(String objProp, OWLNamedIndividual src, OWLNamedIndividual trg, String objPropNs, OWLOntologyManager manager) {
        OWLObjectProperty p = manager.getOWLDataFactory().getOWLObjectProperty(objPropNs, objProp);
        OWLObjectPropertyAssertionAxiom propertyAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(p, src, trg);
        manager.addAxiom(manager.getOntology(IRI.create(Namespace.LEXICON.replace("#", ""))), propertyAssertion);
    }

    private static void addDataPropertyAxiom(String dataProp, OWLNamedIndividual src, String trg, String ns, OWLOntologyManager manager) {
        if (trg != null) {
            if (!trg.isEmpty()) {
                OWLDataProperty p = manager.getOWLDataFactory().getOWLDataProperty(ns, dataProp);
                OWLDataPropertyAssertionAxiom dataPropertyAssertion = manager.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(p, src, trg);
                manager.addAxiom(manager.getOntology(IRI.create(Namespace.LEXICON.replace("#", ""))), dataPropertyAssertion);
            }
        }
    }

    public static void createHebrewWord(String lemma, String pos, String lang, String transliteration, OWLNamedIndividual sense, OWLOntologyManager manager, boolean isComponent) {
        createHebrewLemma(Constant.WORD_TYPE, lemma, pos, lang, transliteration, sense, manager, isComponent);
    }

    private static void createHebrewLemma(String type, String lemma, String pos, String lang, String transliteration, OWLNamedIndividual sense, OWLOntologyManager manager, boolean isComponent) {
        String lemmaInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_").
                replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("–", "") : lemma, CoNLLMapToLexInfo.posMapping.get(pos), lang, "lemma");
        String senseInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_").
                replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("–", "") : lemma, CoNLLMapToLexInfo.posMapping.get(pos), lang, "sense1");
        String entryInstance = getIRI(type.equals(Constant.MULTIWORD_TYPE) ? lemma.replaceAll(" ", "_").
                replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("–", "") : lemma, CoNLLMapToLexInfo.posMapping.get(pos), lang, "entry");

        OWLNamedIndividual lexicon = getIndividual(Constant.LEXICON_INDIVIDUAL_NAME + "_" + lang, Namespace.LEXICON, manager);
        OWLNamedIndividual le = getEntry(entryInstance, type, manager);
        OWLNamedIndividual cf = getForm(lemmaInstance, manager);
        OWLNamedIndividual s = getSense(senseInstance, manager);

        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.ENTRY.getLabel(), lexicon, le, Namespace.LIME, manager);
        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.CANONICALFORM.getLabel(), le, cf, Namespace.ONTOLEX, manager);
        addObjectPropertyAxiom(OntoLexEntity.ObjectProperty.SENSE.getLabel(), le, s, Namespace.ONTOLEX, manager);
        addDataPropertyAxiom(OntoLexEntity.DataProperty.WRITTENREP.getLabel(), cf, lemma, Namespace.ONTOLEX, manager);

        addObjectPropertyAxiom("partOfSpeech", le,
                (Constant.WORD_TYPE.equals(type) ? getIndividual(CoNLLMapToLexInfo.posMapping.get(pos), Namespace.LEXINFO, manager)
                : getIndividual(CoNLLMapToLexInfo.phraseTypeMapping.get(pos), Namespace.LEXINFO, manager)),
                Namespace.LEXINFO, manager);
        addDataPropertyAxiom("valid", le, "false", Namespace.DCT, manager);

        if (!isComponent) {
            if (transliteration != null) {
                addDataPropertyAxiom(Constant.TRANSLITERATION, cf, transliteration, Namespace.EXTENSION, manager);
            }

            if (sense != null) {
                addObjectPropertyAxiom("translation", sense, s, Namespace.LEXINFO, manager);
                addObjectPropertyAxiom("translation", s, sense, Namespace.LEXINFO, manager);
            }
        }

    }

    public static void createHebrewMultiword(String lemma, String pos, String lang, String transliteration, OWLNamedIndividual sense, OWLOntologyManager manager, boolean isComponent) {
        createHebrewLemma(Constant.MULTIWORD_TYPE, lemma, pos, lang, transliteration, sense, manager, isComponent);
        createDecomposition(lang, lemma, pos, manager);
    }

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
