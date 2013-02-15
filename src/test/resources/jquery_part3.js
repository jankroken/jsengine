
jQuery.fn = jQuery.prototype = {
	constructor: jQuery,
	init: "moved to part 2",
	selector: "",
	jquery: "1.6.2",
	length: 0,

	size: function() {
		return this.length
	},

	toArray: function() {
		return slice.call( this, 0 )
	},


	get: function( num ) {
		return num == null ?
			this.toArray() :
			( num < 0 ? this[ this.length + num ] : this[ num ] )
	},

	pushStack: function( elems, name, selector ) {
		var ret = this.constructor();

		if ( jQuery.isArray( elems ) ) {
			push.apply( ret, elems )
		} else {
			jQuery.merge( ret, elems )
		};

		ret.prevObject = this;

		ret.context = this.context;

		if ( name === "find" ) {
			ret.selector = this.selector + (this.selector ? " " : "") + selector
		} else if ( name ) {
			ret.selector = this.selector + "." + name + "(" + selector + ")"
		};

		return ret
	},

	each: function( callback, args ) {
		return jQuery.each( this, callback, args )
	},

	ready: function( fn ) {
		jQuery.bindReady();

		readyList.done( fn );

		return this
	},

	eq: function( i ) {
		return i === -1 ?
			this.slice( i ) :
			this.slice( i, +i + 1 )
	},

	first: function() {
		return this.eq( 0 )
	},

	last: function() {
		return this.eq( -1 )
	},

	slice: function() {
		return this.pushStack( slice.apply( this, arguments ),
			"slice", slice.call(arguments).join(",") )
	},

	map: function( callback ) {
		return this.pushStack( jQuery.map(this, function( elem, i ) {
			return callback.call( elem, i, elem )
		}))
	},

	end: function() {
		return this.prevObject || this.constructor(null)
	},

	push: push,
	sort: [].sort,
	splice: [].splice
}
