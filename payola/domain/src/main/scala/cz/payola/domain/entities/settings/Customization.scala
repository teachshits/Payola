package cz.payola.domain.entities.settings

import cz.payola.domain._
import cz.payola.domain.entities._
import scala.collection.immutable
import scala.collection.mutable.ListBuffer

class Customization(
    val userDefined: Option[Boolean],
    val URLs: String,
    protected var _name: String,
    protected var _owner: Option[User],
    protected var _classCustomizations: immutable.Seq[ClassCustomization])
    extends Entity
    with NamedEntity
    with OptionallyOwnedEntity
    with cz.payola.common.entities.settings.Customization
{
    checkConstructorPostConditions()

    type ClassCustomizationType = ClassCustomization

    override def canEqual(other: Any): Boolean = {
        other.isInstanceOf[OntologyCustomization]
    }

    override protected def checkInvariants() {
        super[Entity].checkInvariants()
        super[NamedEntity].checkInvariants()
        super[OptionallyOwnedEntity].checkInvariants()
    }

    def appendClassCustomization(classCust: ClassCustomization) {
        _classCustomizations = _classCustomizations ++ Seq(classCust)
    }

    override def convertToOntologyCustomization(): OntologyCustomization = {
        if(isUserDefined)
            throw new UnsupportedOperationException("Can not cast User customization to Ontology customization.")

        new OntologyCustomization(id, URLs, _name, _owner, _classCustomizations)
    }

    def toOntologyCustomization(): Option[OntologyCustomization] = {
        if(!isUserDefined) {
            Some(convertToOntologyCustomization())
        } else {
            None
        }
    }
    override def convertToUserCustomization(): UserCustomization = {
        if(!isUserDefined)
            throw new UnsupportedOperationException("Can not cast Ontology customization to User customization.")


        new UserCustomization(id, URLs, _name, _owner, immutable.Seq(_classCustomizations: _*))
    }


    def toUserCustomization(): Option[UserCustomization] = {
        if(isUserDefined) {
            Some(convertToUserCustomization())
        } else {
            None
        }
    }
}

object Customization
{
    def convertToUserCustomization(customization: Customization): UserCustomization = {

        new UserCustomization(customization.id, customization.URLs, customization.name, customization.owner,
            immutable.Seq(customization.classCustomizations: _*))
    }


    def toUserCustomization(customization: Customization): Option[UserCustomization] = {
        if(customization.isUserDefined) {
            Some(convertToUserCustomization(customization))
        } else {
            None
        }
    }

    def convertToOntologyCustomization(customization: Customization): OntologyCustomization = {
        new OntologyCustomization(customization.id, customization.URLs, customization.name,
            customization.owner, customization.classCustomizations)
    }

    def toOntologyCustomization(customization: Customization): Option[OntologyCustomization] = {
        if(!customization.isUserDefined) {
            Some(convertToOntologyCustomization(customization))
        } else {
            None
        }
    }
}
