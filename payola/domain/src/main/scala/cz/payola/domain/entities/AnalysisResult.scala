package cz.payola.domain.entities

import cz.payola.domain.Entity

class AnalysisResult(
    protected override var analysisid: String,
    protected override var _owner: Option[User],
    protected override var evaluationid: String,
    protected override var _persist: Boolean,
    protected override var verticescount: Int,
    protected override var _touched: java.util.Date)
    extends Entity with cz.payola.common.entities.AnalysisResult
    with OptionallyOwnedEntity with NamedEntity
{

    override var _name = "http://"+analysisid+"/"+evaluationid

    override def name = "http://"+analysisid+"/"+evaluationid

    override def name_=(value: String) {
        //ain't gonna do a thing
    }

    /**
     * Sets the owner of the analysisResult.
     * @param value The new owner of the analysisResult.
     */
    override def owner_=(value: Option[UserType]) {
        _owner = value
        super[OptionallyOwnedEntity].checkInvariants()
    }

    override final def canEqual(other: Any): Boolean = {
        other.isInstanceOf[AnalysisResult]
    }

    override protected final def checkInvariants() {
        super[Entity].checkInvariants()

        validate(analysisid.length > 0, "analysisid", "Analysisid has to be specified")
        validate(evaluationid.length > 0, "evaluationid", "Evaluationid has to be specified")
    }
}
