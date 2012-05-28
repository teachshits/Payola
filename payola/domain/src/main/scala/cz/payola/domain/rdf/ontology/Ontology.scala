package cz.payola.domain.rdf.ontology

import java.io.StringReader
import com.hp.hpl.jena.ontology._

/** A class that represents an ontology model and its companion object
  * with several apply methods for ontology creation.
  *
  */


object Ontology {

    /** Creates a new ontology from Jena's OntModel.
      *
      * @param ontologyModel Jena's ontology representation.
      * @return New Ontology model.
      */
    def apply(ontologyModel: OntModel): Ontology = {
        // Just pass it to the ontology factory
        val fact = new OntologyFactory(ontologyModel)
        fact.getOntology
    }

    /** Creates a new ontology from a string which contains a XML document
      * with either RDFS or OWL ontology.
      *
      * @param ontologyString XML document.
      * @return New Ontology model.
      */
    def apply(ontologyString: String): Ontology = {
        val reader = new StringReader(ontologyString)

        // At this moment we don't allow any other format
        // as both OWL and RDFS should theoretically come
        // in XML format only
        val inputType = "RDF/XML"

        // Create a model and read it from the input string
        val jenaModel: OntModel = com.hp.hpl.jena.rdf.model.ModelFactory.createOntologyModel()
        jenaModel.read(reader, inputType)

        val m = apply(jenaModel)
        jenaModel.close()
        m
    }

    /** Returns an empty ontology.
      *
      * @return Empty ontology.
      */
    def empty: Ontology = new Ontology(Nil)
}

class Ontology(val classes: collection.Seq[Class]) extends cz.payola.common.entities.ontology.Ontology
{
    /** Overriding toString - printing the class list.
      *
      * @return Object description.
      */
    override def toString: String = {
        super.toString + " " + classes.toString
    }
}
