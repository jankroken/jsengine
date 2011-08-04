var document = window.document,
	navigator = window.navigator,
	location = window.location;

var jQuery = (function() {

var jQuery = function( selector, context ) {
		return new jQuery.fn.init( selector, context, rootjQuery )
	},

	_jQuery = window.jQuery,

	_$ = window.$,

	rootjQuery,


	quickExpr = /^(?:[^<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/,

	rnotwhite = /\S/,

	trimLeft = /^\s+/,
	trimRight = /\s+$/,

	rdigit = /\d/,

	rsingleTag = /^<(\w+)\s*\/?>(?:<\/\1>)?$/,

	rvalidchars = /^[\],:{}\s]*$/,
	rvalidescape = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,
	rvalidtokens = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
	rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g,

	rwebkit = /(webkit)[ \/]([\w.]+)/,
	ropera = /(opera)(?:.*version)?[ \/]([\w.]+)/,
	rmsie = /(msie) ([\w.]+)/,
	rmozilla = /(mozilla)(?:.*? rv:([\w.]+))?/,

	rdashAlpha = /-([a-z])/ig,

	fcamelCase = function( all, letter ) {
		return letter.toUpperCase()
	},

	userAgent = navigator.userAgent,

	browserMatch,

	readyList,

	DOMContentLoaded,

	toString = Object.prototype.toString,
	hasOwn = Object.prototype.hasOwnProperty,
	push = Array.prototype.push,
	slice = Array.prototype.slice,
	trim = String.prototype.trim,
	indexOf = Array.prototype.indexOf,

	class2type = {}
})
