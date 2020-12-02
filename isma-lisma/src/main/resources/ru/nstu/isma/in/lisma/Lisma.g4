    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

grammar Lisma;

// ==================================================================
// 1. Parser
// ==================================================================

lisma :   (statement)*
	;
	
statement 	
    :	constant
    |   init_const
    |   init_cond 
    |   equation 
    |   state | pseudo_state
    |   spatial_var 
    |   edge
    |   macros
    |   start | end | step | out
    |   linear_eq | linear_vars
    |   for_cycle
    ;
// ------------------------------------------------------------------
// 1.1  Constant
// ------------------------------------------------------------------
    constant 
        :   CONST_KEYWORD constant_body (COMMA  constant_body)* SEMI
	    ;

	constant_body
	    :   (var_ident ASSIGN)+ expression
	    |   var_ident
	    ;

    init_const
        :   'def' CONST_KEYWORD literal SEMI
        ;

    CONST_KEYWORD : 'const';
    
// ------------------------------------------------------------------
// 1.2 Partial
// ------------------------------------------------------------------
    spatial_var 
            :	'var' var_ident LBRACK spatial_var_bound COMMA spatial_var_bound RBRACK (spatial_var_tail)? SEMI
            ;
    spatial_var_bound
            :   (SUB)?literal   |   Identifier
            ;
    spatial_var_tail 
            :	spatial_var_tail_APX DecimalLiteral
            |   spatial_var_tail_STEP (literal)
            ;
    spatial_var_tail_APX
            : 'apx'
            ;
    spatial_var_tail_STEP
             : 'step'
             ;

    partial_operand
            :	partial_operand_common
            |   partial_operand_mixed
            |   partial_operand_spatial_common
            |   partial_operand_spatial_N
            ;
    partial_operand_common
            :   partial_operand_D LPAREN partial_operand_unknown_code COMMA partial_operand_spatial_var_code (COMMA DecimalLiteral)? RPAREN
            ;
    partial_operand_mixed
            :   partial_operand_mixed_D LPAREN partial_operand_unknown_code
                    COMMA partial_operand_spatial_var_code
                    COMMA partial_operand_spatial_var_code
                   (COMMA DecimalLiteral)? RPAREN
            ;
    partial_operand_spatial_common
            :   partial_operand_func_spatial_common LPAREN partial_operand_unknown_code (COMMA DecimalLiteral)? RPAREN
            ;
    partial_operand_spatial_N
            :   partial_operand_func_spatial LPAREN partial_operand_unknown_code RPAREN
            ;
    partial_operand_func_spatial
            :   partial_operand_func_spatial_2
            |   partial_operand_func_spatial_3
            |   partial_operand_func_spatial_4
            ;
    partial_operand_D
            :   'D'
            ;
    partial_operand_mixed_D
            :   'DD'
            ;
    partial_operand_func_spatial_common
            :   'dx' | 'dy' | 'dz'
            ;
    partial_operand_func_spatial_2
            :   'dx2' | 'dy2' | 'dz2'
            ;
    partial_operand_func_spatial_3
            :   'dx3' | 'dy3' | 'dz3'
            ;
    partial_operand_func_spatial_4
            :   'dx4' | 'dy4' | 'dz4'
            ;
    partial_operand_unknown_code
            :   Identifier
            ;
    partial_operand_spatial_var_code
            :   Identifier
            ;

    edge	
            :	'edge' edge_eq 'on' Identifier edge_side SEMI
            ;
    edge_eq
            :	(Identifier | partial_operand) ASSIGN expression
            ;
    edge_side
        :   'left'  |  'right' |    'both'
        ;
    
// ------------------------------------------------------------------ 
// 1.3 Initial condition
// ------------------------------------------------------------------ 
    init_cond 
            :	variable LPAREN 't0' RPAREN ASSIGN expression ';'
            |   'ic' init_cond_body (COMMA init_cond_body)* ';'
            ;
    init_cond_body :  variable ASSIGN expression;

// ------------------------------------------------------------------ 
// 1.4 Alg's and diff's (ode, pde) equations
// ------------------------------------------------------------------ 
    equation
            :	 ode_equation | pde_equation | pde_equation_param
            ;

    ode_equation
            :   variable  ASSIGN  expression SEMI
            ;

    pde_equation
            :  (PDE)? partial_operand (ADD partial_operand)*  ASSIGN  expression SEMI
            ;

    pde_equation_param
            :   PDE partial_operand_with_param (ADD partial_operand_with_param)*  ASSIGN  expression SEMI
            ;

    PDE : 'pde' ;

    partial_operand_with_param    :   pde_param? partial_operand;

    pde_param : pde_param_atom MUL;

    pde_param_atom
            :   LBRACK expression RBRACK
            |   Identifier
            |   literal
            ;

    for_cycle
        :   FOR_KEYWORD Identifier ASSIGN for_cycle_interval (COMMA for_cycle_interval)* for_cycle_body
        ;

    FOR_KEYWORD : 'for';

    for_cycle_interval
        :   literal (COLON literal)?
        ;

    for_cycle_body
        : LBRACE (equation | init_cond | constant | pseudo_state | macros)* RBRACE
        ;

// ------------------------------------------------------------------ 
// 1.5 State
// ------------------------------------------------------------------ 
    state	
        :	STATE_KEYWORD state_name LPAREN expression RPAREN state_body  (state_from)? SEMI
        ;

    STATE_KEYWORD : 'state';

    state_body
        :	LBRACE (equation | setter | for_cycle)* RBRACE
        ;
    state_from 
        :	'from' state_name (COMMA state_name)*
        ;
    state_name
        :   Identifier | 'init'
        ;

    pseudo_state
        :   IF_KEYWORD LPAREN expression RPAREN pseudo_state_body pseudo_state_else?
        ;

    IF_KEYWORD : 'if';

    pseudo_state_body
        :   LBRACE pseudo_state_elem* RBRACE
        ;

    pseudo_state_elem
        :   equation
        |   setter
        ;

    pseudo_state_else
        :   'else' pseudo_state_body
        ;

// ------------------------------------------------------------------ 
// 1.6 Function and math mapping
// ------------------------------------------------------------------ 
    func_and_math_mapping 	:	Identifier  LPAREN arg_list RPAREN ;
    arg_list:  expression ( COMMA expression )*;

// ------------------------------------------------------------------    
// 1.7 Derivative ident
// ------------------------------------------------------------------
    derivative_ident
            :	var_ident derivative_quote_operant
            |	'der' LPAREN var_ident COMMA DecimalLiteral RPAREN
            //|	partial_ident derivative_quote_operant
            ;
    derivative_quote_operant
            :	(QUOTE1)+	|  (QUOTE2)+		
            ;
// ------------------------------------------------------------------
// 1.8 Variable
// ------------------------------------------------------------------
    variable 
            :	 var_ident | derivative_ident
            ;

    var_ident
            :	Identifier (cycle_index) ?
            ;

    cycle_index
            :   LBRACK cycle_index_idx RBRACK cycle_index_posfix?
            ;
    cycle_index_idx
            :   Identifier ((ADD|SUB) literal)?
            ;
    cycle_index_posfix
            :   Identifier | literal
            ;

// ------------------------------------------------------------------
// 1.9  Expression
// ------------------------------------------------------------------

    parExpression
        :   parExpressionLeftPar expression parExpressionRightPar
        ;
    
    parExpressionLeftPar 
        :  LPAREN
        ;
    
    parExpressionRightPar 
        :  RPAREN
        ;

    expression
        :   conditionalExpression 
        ;

    conditionalExpression
        :   conditionalOrExpression
        ;

    conditionalOrExpression
        :   conditionalAndExpression ( or_operator conditionalAndExpression )*
        ;

    conditionalAndExpression
        :   equalityExpression ( and_operator equalityExpression )*
        ;

    equalityExpression
        :   relationalExpression ( equalityExpressionOperator relationalExpression )*
        ;
   
    equalityExpressionOperator
        :   (EQUAL | NOTEQUAL)
        ;

    relationalExpression
        :   additiveExpression ( relationalOp additiveExpression )*
        ;

    relationalOp
        :   LE
        |   GE
        |   LT 
        |   GT 
        ;

    additiveExpression
        :   multiplicativeExpression (additiveExpressionOperator multiplicativeExpression)*
        ;
    
    additiveExpressionOperator
        :   ADD | SUB
        ;

    multiplicativeExpression
        :   unaryExpression ( multiplicativeExpressionOperator unaryExpression )*
        ;
    
    multiplicativeExpressionOperator
        :   MUL | DIV | MOD 
        ;

    unaryExpression
        :   unaryExpressionOperator unaryExpression
        |   unaryExpressionNotPlusMinus
        ;
    
    unaryExpressionOperator
        :   ADD | SUB // | INC | DEC
        ;

    unaryExpressionNotPlusMinus
        :   not_operator unaryExpression
        |   primary
        ;


    primary
        :   parExpression
        |   primary_id 
        |   literal
        |   partial_operand
        |   func_and_math_mapping
        ;
    
    primary_id
        :  Identifier(cycle_index)?
        ;

    literal
        :   DecimalLiteral
        |   FloatingPointLiteral
        ;

    or_operator	:	'||' |  'or'  |  'OR';
    
    and_operator :	'&&' | 'and' | 'AND';
    
    not_operator :	'!' |  'not'  |  'NOT';

// ------------------------------------------------------------------
// 1.10 macros
// ------------------------------------------------------------------
   macros : 'macro' macro_item (COMMA macro_item)* SEMI;
   macro_item : primary_id ASSIGN expression;

// ------------------------------------------------------------------
// 1.11 setter
// ------------------------------------------------------------------
    setter
           :   'set' var_ident ASSIGN expression SEMI
   	;
// ------------------------------------------------------------------
// 1.12 Linear system
// ------------------------------------------------------------------
    linear_vars : 'var' 'ls' var_ident (COMMA var_ident)* SEMI;

    linear_eq : 'ls' linear_eq_A  ASSIGN linear_eq_b SEMI;
    linear_eq_b : expression ;
    linear_eq_A : linear_eq_A_elem (ADD linear_eq_A_elem)*;
    linear_eq_A_elem : var_ident MUL linear_eq_A_elem_expr;
    linear_eq_A_elem_expr
            :   primary
            |   parExpression
            |   multiplicativeExpression
    ;

// ------------------------------------------------------------------
// 1.xx modelling settings
// ------------------------------------------------------------------
    start
           :   'start' ASSIGN expression SEMI
   	;
   	end
           :   'end' ASSIGN expression SEMI
    ;
    step
           :   'step' ASSIGN expression SEMI
    ;
    out
           :   'out' var_ident+ SEMI
    ;

// ==================================================================
// 2. Lexer
// ==================================================================

    DecimalLiteral : ('0' | '1'..'9' '0'..'9'*)  ;

    FloatingPointLiteral
        :   ('0'..'9')+ '.' ('0'..'9')* Exponent? 
        |   '.' ('0'..'9')+ Exponent? 
        |   ('0'..'9')+ Exponent 
        |   ('0'..'9')+ 
        |   '0' ('x'|'X')
            (   HexDigit+ ('.' HexDigit*)? HexExponent 
            |   '.' HexDigit+ HexExponent 
            )
        ;

    fragment
    Exponent : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

    fragment
    HexExponent : ('p'|'P') ('+'|'-')? ('0'..'9')+ ;

    fragment
    HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;

    Identifier 
        :   Letter (Letter|IDDigit)*
        ;

    fragment
    Letter
        :  '\u0024' |
           '\u0041'..'\u005a' |
           '\u005f' |
           '\u0061'..'\u007a' |
           '\u00c0'..'\u00d6' |
           '\u00d8'..'\u00f6' |
           '\u00f8'..'\u00ff' |
           '\u0100'..'\u1fff' |
           '\u3040'..'\u318f' |
           '\u3300'..'\u337f' |
           '\u3400'..'\u3d2d' |
           '\u4e00'..'\u9fff' |
           '\uf900'..'\ufaff'
        ;

    fragment
    IDDigit
        :  '\u0030'..'\u0039' |
           '\u0660'..'\u0669' |
           '\u06f0'..'\u06f9' |
           '\u0966'..'\u096f' |
           '\u09e6'..'\u09ef' |
           '\u0a66'..'\u0a6f' |
           '\u0ae6'..'\u0aef' |
           '\u0b66'..'\u0b6f' |
           '\u0be7'..'\u0bef' |
           '\u0c66'..'\u0c6f' |
           '\u0ce6'..'\u0cef' |
           '\u0d66'..'\u0d6f' |
           '\u0e50'..'\u0e59' |
           '\u0ed0'..'\u0ed9' |
           '\u1040'..'\u1049'
       ;

    WS  :  (' '|'\r'|'\t'|'\u000C'|'\n')+ -> channel(HIDDEN)
        ;

    COMMENT
        :   '/*' .*? '*/' -> channel(HIDDEN)
        ;

    SL_COMMENT : '//'.*?('\n'|EOF)->skip;

// ------------------------------------------------------------------
//  Separators
// ------------------------------------------------------------------
    LPAREN          : '(';
    RPAREN          : ')';
    LBRACE          : '{';
    RBRACE          : '}';
    LBRACK          : '[';
    RBRACK          : ']';
    SEMI            : ';';
    COMMA           : ',';
    DOT             : '.';
    
// ------------------------------------------------------------------
//  Operators
// ------------------------------------------------------------------
    QUOTE1          : '\'';
    QUOTE2          : '�';
    ASSIGN          : '=';
    GT              : '>';
    LT              : '<';
    TILDE           : '~';
    QUESTION        : '?';
    COLON           : ':';
    EQUAL           : '==';
    LE              : '<=';
    GE              : '>=';
    NOTEQUAL        : '!=';
    INC             : '++';
    DEC             : '--';
    ADD             : '+';
    SUB             : '-';
    MUL             : '*';
    DIV             : '/';
    BITAND          : '&';
    BITOR           : '|';
    CARET           : '^';
    MOD             : '%';

    ADD_ASSIGN      : '+=';
    SUB_ASSIGN      : '-=';
    MUL_ASSIGN      : '*=';
    DIV_ASSIGN      : '/=';
    AND_ASSIGN      : '&=';
    OR_ASSIGN       : '|=';
    XOR_ASSIGN      : '^=';
    MOD_ASSIGN      : '%=';
    LSHIFT_ASSIGN   : '<<=';
    RSHIFT_ASSIGN   : '>>=';
    URSHIFT_ASSIGN  : '>>>=';
