package cz.payola.web.client.views.graph.visual.tables

import cz.payola.web.client.views.graph.visual.graph._
import cz.payola.web.client.views._
import bootstrap.Icon
import cz.payola.web.client.views.algebra._
import cz.payola.web.client.events.SimpleUnitEvent
import collection.mutable.ArrayBuffer
import cz.payola.web.client.models.PrefixApplier
import cz.payola.web.client.views.elements._
import cz.payola.web.client.views.elements.lists._
import cz.payola.common.rdf._
import cz.payola.web.client
import form.fields._

class VertexGroupInfoTable(group: VertexViewGroup, position: Point2D, prefixApplier: Option[PrefixApplier]) extends InfoTable
{
    var removeVertexFromGroup = new SimpleUnitEvent[VertexViewElement]
    val groupNameField = new TextInput("GroupName", group.getName)

    def createSubViews: Seq[client.View] = {
        val buffer = new ArrayBuffer[ElementView[_]]()

        group.vertexViews.foreach { vertex =>
            val label = vertex match {
                case view: VertexView => view.vertexModel match {
                    case idVertex: IdentifiedVertex =>
                        prefixApplier.map(_.applyPrefix(idVertex.uri)).getOrElse(vertex.toString())
                    case _ =>
                        vertex.toString()
                }
                case _ =>
                    vertex.toString()

            }
            val removeVertexIcon = new Anchor(List(new Icon(Icon.remove)))
            removeVertexIcon.mouseClicked += { e =>
                removeVertexFromGroup.triggerDirectly(vertex)
                false
            }

            buffer += new DefinitionTerm(List(removeVertexIcon, new Text(label)))
        }

        val popoverTitle =
            new Heading(List(new Text("Group: "), groupNameField), 3, "popover-title")

        val popoverContent = if(!buffer.isEmpty) {
            new Div(List( new DefinitionList(buffer, "unstyled well")), "popover-content")
        } else {
            new Div(List(), "popover-content")
        }
        val popoverInner = new Div(List(popoverTitle, popoverContent), "popover-inner")
        val div = new Div(List(popoverInner))
        div.setAttribute("class", "popover fade in vitable")
        div.setAttribute("style",
            "top: %dpx; left: %dpx; z-index: 1000;".format(position.y, position.x))
        List(div)
    }

    def getSize: Vector2D = {
        Vector2D(this.blockHtmlElement.clientWidth, this.blockHtmlElement.clientHeight)
    }

    def setPosition(position: Point2D) {
        blockHtmlElement.setAttribute("style",
            "top: %dpx; left: %dpx; z-index: 1000;".format(position.y, position.x))
    }
}
