jQuery.extend({
	noConflict: function( deep ) {
		if ( window.$ === jQuery ) {
			window.$ = _$
		};

		if ( deep && window.jQuery === jQuery ) {
			window.jQuery = _jQuery
		};

		return jQuery
	},

	isReady: false,

	readyWait: 1,

	holdReady: function( hold ) {
		if ( hold ) {
			jQuery.readyWait++
		} else {
			jQuery.ready( true )
		}
	},

	ready: function( wait ) {
		if ( (wait === true && !--jQuery.readyWait) || (wait !== true && !jQuery.isReady) ) {
			if ( !document.body ) {
				return setTimeout( jQuery.ready, 1 )
			};

			jQuery.isReady = true;

			if ( wait !== true && --jQuery.readyWait > 0 ) {
				return
			};

			readyList.resolveWith( document, [ jQuery ] );

			if ( jQuery.fn.trigger ) {
				jQuery( document ).trigger( "ready" ).unbind( "ready" )
			}
		}
	},

	bindReady: function() {
		if ( readyList ) {
			return
		};

		readyList = jQuery._Deferred();

		if ( document.readyState === "complete" ) {
			return setTimeout( jQuery.ready, 1 )
		};

		if ( document.addEventListener ) {
			document.addEventListener( "DOMContentLoaded", DOMContentLoaded, false );

			window.addEventListener( "load", jQuery.ready, false )

		} else if ( document.attachEvent ) {
			document.attachEvent( "onreadystatechange", DOMContentLoaded );

			window.attachEvent( "onload", jQuery.ready );

			var toplevel = false;

			try {
				toplevel = window.frameElement == null
			} catch(e) {};

			if ( document.documentElement.doScroll && toplevel ) {
				doScrollCheck()
			}
		}
	},
	isFunction: function( obj ) {
		return jQuery.type(obj) === "function"
	},

	isArray: Array.isArray || function( obj ) {
		return jQuery.type(obj) === "array"
	},

	isWindow: function( obj ) {
		return obj && typeof obj === "object" && "setInterval" in obj
	},

	isNaN: function( obj ) {
		return obj == null || !rdigit.test( obj ) || isNaN( obj )
	},

	type: function( obj ) {
		return obj == null ?
			String( obj ) :
			class2type[ toString.call(obj) ] || "object"
	},

	isPlainObject: function( obj ) {
		if ( !obj || jQuery.type(obj) !== "object" || obj.nodeType || jQuery.isWindow( obj ) ) {
			return false
		};

		if ( obj.constructor &&
			!hasOwn.call(obj, "constructor") &&
			!hasOwn.call(obj.constructor.prototype, "isPrototypeOf") ) {
			return false
		};


		var key;
		for ( key in obj ) {};

		return key === undefined || hasOwn.call( obj, key )
	},

	isEmptyObject: function( obj ) {
		for ( var name in obj ) {
			return false
		};
		return true
	},

	error: function( msg ) {
		throw msg
	},

	parseJSON: function( data ) {
		if ( typeof data !== "string" || !data ) {
			return null
		};

		data = jQuery.trim( data );

		if ( window.JSON && window.JSON.parse ) {
			return window.JSON.parse( data )
		};

		if ( rvalidchars.test( data.replace( rvalidescape, "@" )
			.replace( rvalidtokens, "]" )
			.replace( rvalidbraces, "")) ) {

			return (new Function( "return " + data ))()

		};
		jQuery.error( "Invalid JSON: " + data )
	},

	parseXML: function( data , xml , tmp ) {

		if ( window.DOMParser ) {
			tmp = new DOMParser();
			xml = tmp.parseFromString( data , "text/xml" )
		} else {
			xml = new ActiveXObject( "Microsoft.XMLDOM" );
			xml.async = "false";
			xml.loadXML( data )
		};

		tmp = xml.documentElement;

		if ( ! tmp || ! tmp.nodeName || tmp.nodeName === "parsererror" ) {
			jQuery.error( "Invalid XML: " + data )
		};

		return xml
	},

	noop: function() {},

	globalEval: function( data ) {
		if ( data && rnotwhite.test( data ) ) {
			( window.execScript || function( data ) {
				window[ "eval" ].call( window, data )
			} )( data )
		}
	},

	camelCase: function( string ) {
		return string.replace( rdashAlpha, fcamelCase )
	},

	nodeName: function( elem, name ) {
		return elem.nodeName && elem.nodeName.toUpperCase() === name.toUpperCase()
	},

	each: function( object, callback, args ) {
		var name, i = 0,
			length = object.length,
			isObj = length === undefined || jQuery.isFunction( object );

		if ( args ) {
			if ( isObj ) {
				for ( name in object ) {
					if ( callback.apply( object[ name ], args ) === false ) {
						break
					}
				}
			} else {
				for ( ; i < length; ) {
					if ( callback.apply( object[ i++ ], args ) === false ) {
						break
					}
				}
			}

		} else {
			if ( isObj ) {
				for ( name in object ) {
					if ( callback.call( object[ name ], name, object[ name ] ) === false ) {
						break
					}
				}
			} else {
				for ( ; i < length; ) {
					if ( callback.call( object[ i ], i, object[ i++ ] ) === false ) {
						break
					}
				}
			}
		};

		return object
	},

	trim: trim ?
		function( text ) {
			return text == null ?
				"" :
				trim.call( text )
		} :

		function( text ) {
			return text == null ?
				"" :
				text.toString().replace( trimLeft, "" ).replace( trimRight, "" )
		},

	makeArray: function( array, results ) {
		var ret = results || [];

		if ( array != null ) {
			var type = jQuery.type( array );

			if ( array.length == null || type === "string" || type === "function" || type === "regexp" || jQuery.isWindow( array ) ) {
				push.call( ret, array )
			} else {
				jQuery.merge( ret, array )
			}
		};

		return ret
	},

	inArray: function( elem, array ) {

		if ( indexOf ) {
			return indexOf.call( array, elem )
		};

		for ( var i = 0, length = array.length; i < length; i++ ) {
			if ( array[ i ] === elem ) {
				return i
			}
		};

		return -1
	},

	merge: function( first, second ) {
		var i = first.length,
			j = 0;

		if ( typeof second.length === "number" ) {
			for ( var l = second.length; j < l; j++ ) {
				first[ i++ ] = second[ j ]
			}

		} else {
			while ( second[j] !== undefined ) {
				first[ i++ ] = second[ j++ ]
			}
		};

		first.length = i;

		return first
	},

	grep: function( elems, callback, inv ) {
		var ret = [], retVal;
		inv = !!inv;

		for ( var i = 0, length = elems.length; i < length; i++ ) {
			retVal = !!callback( elems[ i ], i );
			if ( inv !== retVal ) {
				ret.push( elems[ i ] )
			}
		};

		return ret
	},

	map: function( elems, callback, arg ) {
		var value, key, ret = [],
			i = 0,
			length = elems.length,
			isArray = elems instanceof jQuery || length !== undefined && typeof length === "number" && ( ( length > 0 && elems[ 0 ] && elems[ length -1 ] ) || length === 0 || jQuery.isArray( elems ) ) ;

		if ( isArray ) {
			for ( ; i < length; i++ ) {
				value = callback( elems[ i ], i, arg );

				if ( value != null ) {
					ret[ ret.length ] = value
				}
			}

		} else {
			for ( key in elems ) {
				value = callback( elems[ key ], key, arg );

				if ( value != null ) {
					ret[ ret.length ] = value
				}
			}
		};

		return ret.concat.apply( [], ret )
	},

	guid: 1,

	proxy: function( fn, context ) {
		if ( typeof context === "string" ) {
			var tmp = fn[ context ];
			context = fn;
			fn = tmp
		};

		if ( !jQuery.isFunction( fn ) ) {
			return undefined
		};

		var args = slice.call( arguments, 2 ),
			proxy = function() {
				return fn.apply( context, args.concat( slice.call( arguments ) ) )
			};

		proxy.guid = fn.guid = fn.guid || proxy.guid || jQuery.guid++;

		return proxy
	},

	access: function( elems, key, value, exec, fn, pass ) {
		var length = elems.length;

		if ( typeof key === "object" ) {
			for ( var k in key ) {
				jQuery.access( elems, k, key[k], exec, fn, value )
			};
			return elems
		};

		if ( value !== undefined ) {
			exec = !pass && exec && jQuery.isFunction(value);

			for ( var i = 0; i < length; i++ ) {
				fn( elems[i], key, exec ? value.call( elems[i], i, fn( elems[i], key ) ) : value, pass )
			};

			return elems
		};

		return length ? fn( elems[0], key ) : undefined
	},

	now: function() {
		return (new Date()).getTime()
	},

	uaMatch: function( ua ) {
		ua = ua.toLowerCase();

		var match = rwebkit.exec( ua ) ||
			ropera.exec( ua ) ||
			rmsie.exec( ua ) ||
			ua.indexOf("compatible") < 0 && rmozilla.exec( ua ) ||
			[];

		return { browser: match[1] || "", version: match[2] || "0" }
	},

	sub: function() {
	}
})
