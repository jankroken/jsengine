var foo = function( selector, context, rootjQuery ) {
		var match, elem, ret, doc;

		if ( !selector ) {
			return this
		};

		if ( selector.nodeType ) {
			this.context = this[0] = selector;
			this.length = 1;
			return this
		};

		if ( selector === "body" && !context && document.body ) {
			this.context = document;
			this[0] = document.body;
			this.selector = selector;
			this.length = 1;
			return this
		} ;

		print('#JKR# so far so good') ;
		
		selector.charAt(0) === "<" && selector.charAt( selector.length - 1 ) === ">" && selector.length >= 3 ;
		
		print('#JKR# so far so good 1') ;

		if ( selector.charAt(0) === "<" && selector.charAt( selector.length - 1 ) === ">" && selector.length >= 3 ) {
			match = [ null, selector, null ]
		} else {
			match = quickExpr.exec( selector )
		};

		if ( (typeof selector) === "string" ) {
			
			print('#JKR# so far so good 3') ;
			
			
			if ( selector.charAt(0) === "<" && selector.charAt( selector.length - 1 ) === ">" && selector.length >= 3 ) {
				match = [ null, selector, null ]
			} else {
				match = quickExpr.exec( selector )
			};

			if ( match && (match[1] || !context) ) {

				if ( match[1] ) {
					context = context instanceof jQuery ? context[0] : context;
					doc = (context ? context.ownerDocument || context : document);

					ret = rsingleTag.exec( selector );
			
					if ( ret ) {
						if ( jQuery.isPlainObject( context ) ) {
							selector = [ document.createElement( ret[1] ) ];
							jQuery.fn.attr.call( selector, context, true )

						} else {
							selector = [ doc.createElement( ret[1] ) ]
						}

					} else {
						ret = jQuery.buildFragment( [ match[1] ], [ doc ] );
						selector = (ret.cacheable ? jQuery.clone(ret.fragment) : ret.fragment).childNodes
					};

					return jQuery.merge( this, selector )

				} else {
					elem = document.getElementById( match[2] );

					if ( elem && elem.parentNode ) {
						if ( elem.id !== match[2] ) {
							return rootjQuery.find( selector )
						};

						this.length = 1;
						this[0] = elem
					};

					this.context = document;
					this.selector = selector;
					return this
				}

			} else if ( !context || context.jquery ) {
				return (context || rootjQuery).find( selector )

			} else {
				return this.constructor( context ).find( selector )
			}

		} else if ( jQuery.isFunction( selector ) ) {
			return rootjQuery.ready( selector )
		};

		if (selector.selector !== undefined) {
			this.selector = selector.selector;
			this.context = selector.context
		};

		return jQuery.makeArray( selector, this )
	}
