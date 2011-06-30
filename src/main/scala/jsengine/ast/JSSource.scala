package jsengine.ast

class JSSource(val sourceElements: List[JSSourceElement]) {
	def evaluate {
		for (sourceElement <- sourceElements) {
			println("Evaluating: "+sourceElement)
			sourceElement.evaluate
		}
	}
	
	override def toString:String = {
		return "JSSource{"+sourceElements+"}"
	}
}