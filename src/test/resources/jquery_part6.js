
print("hello");
print("world");
jQuery.extend( true, jQuerySub, this );
jQuerySub.superclass = this;
jQuerySub.fn = jQuerySub.prototype = this();
jQuerySub.fn.constructor = jQuerySub;
jQuerySub.sub = this.sub;

jQuerySub.fn.init = function init( selector, context ) {
	if ( context && context instanceof jQuery && !(context instanceof jQuerySub) ) {
		context = jQuerySub( context )
	};

	return jQuery.fn.init.call( this, selector, context, rootjQuerySub )
};

function jQuerySub( selector, context ) {
	return new jQuerySub.fn.init( selector, context)
} ;

jQuerySub.fn.init.prototype = jQuerySub.fn;
var rootjQuerySub = jQuerySub(document)

