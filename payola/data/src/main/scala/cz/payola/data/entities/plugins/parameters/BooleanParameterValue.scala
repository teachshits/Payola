package cz.payola.data.entities.plugins.parameters

import cz.payola.data.entities.plugins._
import cz.payola.data.SquerylDataContextComponent

/**
  * This object converts [[cz.payola.domain.entities.plugins.parameters.BooleanParameterValue]]
  * to [[cz.payola.data.entities.plugins.parameters.BooleanParameterValue]]
  */
object BooleanParameterValue {

    def apply(p: cz.payola.domain.entities.plugins.parameters.BooleanParameterValue)
        (implicit context: SquerylDataContextComponent): BooleanParameterValue = {
        p match {
            case param: BooleanParameterValue => param
            case _ => {
                val parameter = BooleanParameter(p.parameter.asInstanceOf[cz.payola.domain.entities.plugins.parameters.BooleanParameter])
                val parameterValue = new BooleanParameterValue(p.id, parameter, p.value)

                parameter.associateParameterValue(parameterValue)

                parameterValue
            }
        }
    }
}

class BooleanParameterValue(
    override val id: String,
    param: BooleanParameter,
    override var value: Boolean)(implicit val context: SquerylDataContextComponent)
    extends cz.payola.domain.entities.plugins.parameters.BooleanParameterValue(param, value)
    with ParameterValue[Boolean]
{
    val parameterId: Option[String] = if (param == null) None else Some(param.id)

    private lazy val _parameterQuery = context.schema.valuesOfBooleanParameters.right(this)

    override def parameter: ParameterType = evaluateCollection(_parameterQuery)(0)
}
