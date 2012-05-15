package cz.payola.data.entities.analyses.parameters

import cz.payola.data.entities.analyses.{Plugin, Parameter}
import cz.payola.data.entities.{PayolaDB, PersistableEntity}
import cz.payola.domain.entities.analyses.ParameterValue

class BooleanParameter(
    name: String,
    defaultValue: Boolean)
    extends cz.payola.domain.entities.analyses.parameters.BooleanParameter(name, defaultValue)
    with PersistableEntity
    with Parameter[Boolean]
{
    private lazy val _instances = PayolaDB.valuesOfBooleanParameters.left(this)

    def instances: Seq[BooleanParameterValue] = evaluateCollection(_instances)

    override def createValue(value: Boolean): ParameterValue[Boolean] = {
        new BooleanParameterValue(this, value)
    }
}


