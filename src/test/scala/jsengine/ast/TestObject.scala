package jsengine.ast

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import jsengine.library.BuiltinObjects

class TestObject {

    @Test def testEmptyObject() {
		val o = new JSObject(EmptyPropertySet,Map())
		val value = o.getProperty(JSString("dontexist"))
		assertThat(value,is[Any](None));
    }
    
    @Test def testInitialValueMap() {
    	val jsobject = new JSObject(EmptyPropertySet,Map(JSString("key") -> JSString("value")))
    	val value = jsobject.getProperty(JSString("key"))
    	assertThat(value,is[Any](Some(JSString("value"))))
    }
    
    @Test def testPrototypeLookup1() {
    	val value = new JSObject(EmptyPropertySet,Map())
    	val innerObject = new JSObject(EmptyPropertySet,Map())
    	val outerObject = new JSObject(innerObject,Map())
    	innerObject.setProperty(JSString("key"),value)
    	outerObject.getProperty(JSString("key")) match {
    	  case Some(foundValue) => assertThat(foundValue,is[Any](value))
    	  case None => fail("Could not find the value")
    	}
    }
    
    @Test def testCreate() {
    	val o = BuiltinObjects._object.create
    	val o2 = o.create
    	o.setProperty(JSString("hello"),JSString("world"))
    	assertThat(BuiltinObjects._object.getProperty(JSString("hello")),is[Any](None))
    	assertThat(o2.getProperty(JSString("hello")),is[Any](JSString("world")))
    }

}