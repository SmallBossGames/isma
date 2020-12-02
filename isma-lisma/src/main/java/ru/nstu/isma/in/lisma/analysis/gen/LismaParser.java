// Generated from C:/Users/SmallBoss/Documents/Projects/ISMA/isma-lisma/src/main/resources/ru/nstu/isma/in/lisma\Lisma.g4 by ANTLR 4.8
package ru.nstu.isma.in.lisma.analysis.gen;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LismaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, CONST_KEYWORD=45, 
		PDE=46, FOR_KEYWORD=47, STATE_KEYWORD=48, IF_KEYWORD=49, DecimalLiteral=50, 
		FloatingPointLiteral=51, Identifier=52, WS=53, COMMENT=54, SL_COMMENT=55, 
		LPAREN=56, RPAREN=57, LBRACE=58, RBRACE=59, LBRACK=60, RBRACK=61, SEMI=62, 
		COMMA=63, DOT=64, QUOTE1=65, QUOTE2=66, ASSIGN=67, GT=68, LT=69, TILDE=70, 
		QUESTION=71, COLON=72, EQUAL=73, LE=74, GE=75, NOTEQUAL=76, INC=77, DEC=78, 
		ADD=79, SUB=80, MUL=81, DIV=82, BITAND=83, BITOR=84, CARET=85, MOD=86, 
		ADD_ASSIGN=87, SUB_ASSIGN=88, MUL_ASSIGN=89, DIV_ASSIGN=90, AND_ASSIGN=91, 
		OR_ASSIGN=92, XOR_ASSIGN=93, MOD_ASSIGN=94, LSHIFT_ASSIGN=95, RSHIFT_ASSIGN=96, 
		URSHIFT_ASSIGN=97;
	public static final int
		RULE_lisma = 0, RULE_statement = 1, RULE_constant = 2, RULE_constant_body = 3, 
		RULE_init_const = 4, RULE_spatial_var = 5, RULE_spatial_var_bound = 6, 
		RULE_spatial_var_tail = 7, RULE_spatial_var_tail_APX = 8, RULE_spatial_var_tail_STEP = 9, 
		RULE_partial_operand = 10, RULE_partial_operand_common = 11, RULE_partial_operand_mixed = 12, 
		RULE_partial_operand_spatial_common = 13, RULE_partial_operand_spatial_N = 14, 
		RULE_partial_operand_func_spatial = 15, RULE_partial_operand_D = 16, RULE_partial_operand_mixed_D = 17, 
		RULE_partial_operand_func_spatial_common = 18, RULE_partial_operand_func_spatial_2 = 19, 
		RULE_partial_operand_func_spatial_3 = 20, RULE_partial_operand_func_spatial_4 = 21, 
		RULE_partial_operand_unknown_code = 22, RULE_partial_operand_spatial_var_code = 23, 
		RULE_edge = 24, RULE_edge_eq = 25, RULE_edge_side = 26, RULE_init_cond = 27, 
		RULE_init_cond_body = 28, RULE_equation = 29, RULE_ode_equation = 30, 
		RULE_pde_equation = 31, RULE_pde_equation_param = 32, RULE_partial_operand_with_param = 33, 
		RULE_pde_param = 34, RULE_pde_param_atom = 35, RULE_for_cycle = 36, RULE_for_cycle_interval = 37, 
		RULE_for_cycle_body = 38, RULE_state = 39, RULE_state_body = 40, RULE_state_from = 41, 
		RULE_state_name = 42, RULE_pseudo_state = 43, RULE_pseudo_state_body = 44, 
		RULE_pseudo_state_elem = 45, RULE_pseudo_state_else = 46, RULE_func_and_math_mapping = 47, 
		RULE_arg_list = 48, RULE_derivative_ident = 49, RULE_derivative_quote_operant = 50, 
		RULE_variable = 51, RULE_var_ident = 52, RULE_cycle_index = 53, RULE_cycle_index_idx = 54, 
		RULE_cycle_index_posfix = 55, RULE_parExpression = 56, RULE_parExpressionLeftPar = 57, 
		RULE_parExpressionRightPar = 58, RULE_expression = 59, RULE_conditionalExpression = 60, 
		RULE_conditionalOrExpression = 61, RULE_conditionalAndExpression = 62, 
		RULE_equalityExpression = 63, RULE_equalityExpressionOperator = 64, RULE_relationalExpression = 65, 
		RULE_relationalOp = 66, RULE_additiveExpression = 67, RULE_additiveExpressionOperator = 68, 
		RULE_multiplicativeExpression = 69, RULE_multiplicativeExpressionOperator = 70, 
		RULE_unaryExpression = 71, RULE_unaryExpressionOperator = 72, RULE_unaryExpressionNotPlusMinus = 73, 
		RULE_primary = 74, RULE_primary_id = 75, RULE_literal = 76, RULE_or_operator = 77, 
		RULE_and_operator = 78, RULE_not_operator = 79, RULE_macros = 80, RULE_macro_item = 81, 
		RULE_setter = 82, RULE_linear_vars = 83, RULE_linear_eq = 84, RULE_linear_eq_b = 85, 
		RULE_linear_eq_A = 86, RULE_linear_eq_A_elem = 87, RULE_linear_eq_A_elem_expr = 88, 
		RULE_start = 89, RULE_end = 90, RULE_step = 91, RULE_out = 92;
	private static String[] makeRuleNames() {
		return new String[] {
			"lisma", "statement", "constant", "constant_body", "init_const", "spatial_var", 
			"spatial_var_bound", "spatial_var_tail", "spatial_var_tail_APX", "spatial_var_tail_STEP", 
			"partial_operand", "partial_operand_common", "partial_operand_mixed", 
			"partial_operand_spatial_common", "partial_operand_spatial_N", "partial_operand_func_spatial", 
			"partial_operand_D", "partial_operand_mixed_D", "partial_operand_func_spatial_common", 
			"partial_operand_func_spatial_2", "partial_operand_func_spatial_3", "partial_operand_func_spatial_4", 
			"partial_operand_unknown_code", "partial_operand_spatial_var_code", "edge", 
			"edge_eq", "edge_side", "init_cond", "init_cond_body", "equation", "ode_equation", 
			"pde_equation", "pde_equation_param", "partial_operand_with_param", "pde_param", 
			"pde_param_atom", "for_cycle", "for_cycle_interval", "for_cycle_body", 
			"state", "state_body", "state_from", "state_name", "pseudo_state", "pseudo_state_body", 
			"pseudo_state_elem", "pseudo_state_else", "func_and_math_mapping", "arg_list", 
			"derivative_ident", "derivative_quote_operant", "variable", "var_ident", 
			"cycle_index", "cycle_index_idx", "cycle_index_posfix", "parExpression", 
			"parExpressionLeftPar", "parExpressionRightPar", "expression", "conditionalExpression", 
			"conditionalOrExpression", "conditionalAndExpression", "equalityExpression", 
			"equalityExpressionOperator", "relationalExpression", "relationalOp", 
			"additiveExpression", "additiveExpressionOperator", "multiplicativeExpression", 
			"multiplicativeExpressionOperator", "unaryExpression", "unaryExpressionOperator", 
			"unaryExpressionNotPlusMinus", "primary", "primary_id", "literal", "or_operator", 
			"and_operator", "not_operator", "macros", "macro_item", "setter", "linear_vars", 
			"linear_eq", "linear_eq_b", "linear_eq_A", "linear_eq_A_elem", "linear_eq_A_elem_expr", 
			"start", "end", "step", "out"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'def'", "'var'", "'apx'", "'step'", "'D'", "'DD'", "'dx'", "'dy'", 
			"'dz'", "'dx2'", "'dy2'", "'dz2'", "'dx3'", "'dy3'", "'dz3'", "'dx4'", 
			"'dy4'", "'dz4'", "'edge'", "'on'", "'left'", "'right'", "'both'", "'t0'", 
			"'ic'", "'from'", "'init'", "'else'", "'der'", "'||'", "'or'", "'OR'", 
			"'&&'", "'and'", "'AND'", "'!'", "'not'", "'NOT'", "'macro'", "'set'", 
			"'ls'", "'start'", "'end'", "'out'", "'const'", "'pde'", "'for'", "'state'", 
			"'if'", null, null, null, null, null, null, "'('", "')'", "'{'", "'}'", 
			"'['", "']'", "';'", "','", "'.'", "'''", "'\u043F\u0457\u0405'", "'='", 
			"'>'", "'<'", "'~'", "'?'", "':'", "'=='", "'<='", "'>='", "'!='", "'++'", 
			"'--'", "'+'", "'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "'+='", 
			"'-='", "'*='", "'/='", "'&='", "'|='", "'^='", "'%='", "'<<='", "'>>='", 
			"'>>>='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "CONST_KEYWORD", 
			"PDE", "FOR_KEYWORD", "STATE_KEYWORD", "IF_KEYWORD", "DecimalLiteral", 
			"FloatingPointLiteral", "Identifier", "WS", "COMMENT", "SL_COMMENT", 
			"LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", 
			"DOT", "QUOTE1", "QUOTE2", "ASSIGN", "GT", "LT", "TILDE", "QUESTION", 
			"COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "INC", "DEC", "ADD", "SUB", 
			"MUL", "DIV", "BITAND", "BITOR", "CARET", "MOD", "ADD_ASSIGN", "SUB_ASSIGN", 
			"MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", 
			"MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Lisma.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LismaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class LismaContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public LismaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lisma; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterLisma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitLisma(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitLisma(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LismaContext lisma() throws RecognitionException {
		LismaContext _localctx = new LismaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_lisma);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__24) | (1L << T__28) | (1L << T__38) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << CONST_KEYWORD) | (1L << PDE) | (1L << FOR_KEYWORD) | (1L << STATE_KEYWORD) | (1L << IF_KEYWORD) | (1L << Identifier))) != 0)) {
				{
				{
				setState(186);
				statement();
				}
				}
				setState(191);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public Init_constContext init_const() {
			return getRuleContext(Init_constContext.class,0);
		}
		public Init_condContext init_cond() {
			return getRuleContext(Init_condContext.class,0);
		}
		public EquationContext equation() {
			return getRuleContext(EquationContext.class,0);
		}
		public StateContext state() {
			return getRuleContext(StateContext.class,0);
		}
		public Pseudo_stateContext pseudo_state() {
			return getRuleContext(Pseudo_stateContext.class,0);
		}
		public Spatial_varContext spatial_var() {
			return getRuleContext(Spatial_varContext.class,0);
		}
		public EdgeContext edge() {
			return getRuleContext(EdgeContext.class,0);
		}
		public MacrosContext macros() {
			return getRuleContext(MacrosContext.class,0);
		}
		public StartContext start() {
			return getRuleContext(StartContext.class,0);
		}
		public EndContext end() {
			return getRuleContext(EndContext.class,0);
		}
		public StepContext step() {
			return getRuleContext(StepContext.class,0);
		}
		public OutContext out() {
			return getRuleContext(OutContext.class,0);
		}
		public Linear_eqContext linear_eq() {
			return getRuleContext(Linear_eqContext.class,0);
		}
		public Linear_varsContext linear_vars() {
			return getRuleContext(Linear_varsContext.class,0);
		}
		public For_cycleContext for_cycle() {
			return getRuleContext(For_cycleContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(208);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(192);
				constant();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(193);
				init_const();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(194);
				init_cond();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(195);
				equation();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(196);
				state();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(197);
				pseudo_state();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(198);
				spatial_var();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(199);
				edge();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(200);
				macros();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(201);
				start();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(202);
				end();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(203);
				step();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(204);
				out();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(205);
				linear_eq();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(206);
				linear_vars();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(207);
				for_cycle();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode CONST_KEYWORD() { return getToken(LismaParser.CONST_KEYWORD, 0); }
		public List<Constant_bodyContext> constant_body() {
			return getRuleContexts(Constant_bodyContext.class);
		}
		public Constant_bodyContext constant_body(int i) {
			return getRuleContext(Constant_bodyContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public List<TerminalNode> COMMA() { return getTokens(LismaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LismaParser.COMMA, i);
		}
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			match(CONST_KEYWORD);
			setState(211);
			constant_body();
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(212);
				match(COMMA);
				setState(213);
				constant_body();
				}
				}
				setState(218);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(219);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Constant_bodyContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<Var_identContext> var_ident() {
			return getRuleContexts(Var_identContext.class);
		}
		public Var_identContext var_ident(int i) {
			return getRuleContext(Var_identContext.class,i);
		}
		public List<TerminalNode> ASSIGN() { return getTokens(LismaParser.ASSIGN); }
		public TerminalNode ASSIGN(int i) {
			return getToken(LismaParser.ASSIGN, i);
		}
		public Constant_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterConstant_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitConstant_body(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitConstant_body(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Constant_bodyContext constant_body() throws RecognitionException {
		Constant_bodyContext _localctx = new Constant_bodyContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_constant_body);
		try {
			int _alt;
			setState(231);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(224); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(221);
						var_ident();
						setState(222);
						match(ASSIGN);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(226); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
				} while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER );
				setState(228);
				expression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(230);
				var_ident();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Init_constContext extends ParserRuleContext {
		public TerminalNode CONST_KEYWORD() { return getToken(LismaParser.CONST_KEYWORD, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public Init_constContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init_const; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterInit_const(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitInit_const(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitInit_const(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Init_constContext init_const() throws RecognitionException {
		Init_constContext _localctx = new Init_constContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_init_const);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(233);
			match(T__0);
			setState(234);
			match(CONST_KEYWORD);
			setState(235);
			literal();
			setState(236);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Spatial_varContext extends ParserRuleContext {
		public Var_identContext var_ident() {
			return getRuleContext(Var_identContext.class,0);
		}
		public TerminalNode LBRACK() { return getToken(LismaParser.LBRACK, 0); }
		public List<Spatial_var_boundContext> spatial_var_bound() {
			return getRuleContexts(Spatial_var_boundContext.class);
		}
		public Spatial_var_boundContext spatial_var_bound(int i) {
			return getRuleContext(Spatial_var_boundContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(LismaParser.COMMA, 0); }
		public TerminalNode RBRACK() { return getToken(LismaParser.RBRACK, 0); }
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public Spatial_var_tailContext spatial_var_tail() {
			return getRuleContext(Spatial_var_tailContext.class,0);
		}
		public Spatial_varContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spatial_var; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterSpatial_var(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitSpatial_var(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitSpatial_var(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Spatial_varContext spatial_var() throws RecognitionException {
		Spatial_varContext _localctx = new Spatial_varContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_spatial_var);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			match(T__1);
			setState(239);
			var_ident();
			setState(240);
			match(LBRACK);
			setState(241);
			spatial_var_bound();
			setState(242);
			match(COMMA);
			setState(243);
			spatial_var_bound();
			setState(244);
			match(RBRACK);
			setState(246);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2 || _la==T__3) {
				{
				setState(245);
				spatial_var_tail();
				}
			}

			setState(248);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Spatial_var_boundContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode SUB() { return getToken(LismaParser.SUB, 0); }
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public Spatial_var_boundContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spatial_var_bound; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterSpatial_var_bound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitSpatial_var_bound(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitSpatial_var_bound(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Spatial_var_boundContext spatial_var_bound() throws RecognitionException {
		Spatial_var_boundContext _localctx = new Spatial_var_boundContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_spatial_var_bound);
		int _la;
		try {
			setState(255);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DecimalLiteral:
			case FloatingPointLiteral:
			case SUB:
				enterOuterAlt(_localctx, 1);
				{
				setState(251);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUB) {
					{
					setState(250);
					match(SUB);
					}
				}

				setState(253);
				literal();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(254);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Spatial_var_tailContext extends ParserRuleContext {
		public Spatial_var_tail_APXContext spatial_var_tail_APX() {
			return getRuleContext(Spatial_var_tail_APXContext.class,0);
		}
		public TerminalNode DecimalLiteral() { return getToken(LismaParser.DecimalLiteral, 0); }
		public Spatial_var_tail_STEPContext spatial_var_tail_STEP() {
			return getRuleContext(Spatial_var_tail_STEPContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public Spatial_var_tailContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spatial_var_tail; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterSpatial_var_tail(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitSpatial_var_tail(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitSpatial_var_tail(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Spatial_var_tailContext spatial_var_tail() throws RecognitionException {
		Spatial_var_tailContext _localctx = new Spatial_var_tailContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_spatial_var_tail);
		try {
			setState(263);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				enterOuterAlt(_localctx, 1);
				{
				setState(257);
				spatial_var_tail_APX();
				setState(258);
				match(DecimalLiteral);
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 2);
				{
				setState(260);
				spatial_var_tail_STEP();
				{
				setState(261);
				literal();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Spatial_var_tail_APXContext extends ParserRuleContext {
		public Spatial_var_tail_APXContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spatial_var_tail_APX; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterSpatial_var_tail_APX(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitSpatial_var_tail_APX(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitSpatial_var_tail_APX(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Spatial_var_tail_APXContext spatial_var_tail_APX() throws RecognitionException {
		Spatial_var_tail_APXContext _localctx = new Spatial_var_tail_APXContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_spatial_var_tail_APX);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(265);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Spatial_var_tail_STEPContext extends ParserRuleContext {
		public Spatial_var_tail_STEPContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spatial_var_tail_STEP; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterSpatial_var_tail_STEP(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitSpatial_var_tail_STEP(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitSpatial_var_tail_STEP(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Spatial_var_tail_STEPContext spatial_var_tail_STEP() throws RecognitionException {
		Spatial_var_tail_STEPContext _localctx = new Spatial_var_tail_STEPContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_spatial_var_tail_STEP);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operandContext extends ParserRuleContext {
		public Partial_operand_commonContext partial_operand_common() {
			return getRuleContext(Partial_operand_commonContext.class,0);
		}
		public Partial_operand_mixedContext partial_operand_mixed() {
			return getRuleContext(Partial_operand_mixedContext.class,0);
		}
		public Partial_operand_spatial_commonContext partial_operand_spatial_common() {
			return getRuleContext(Partial_operand_spatial_commonContext.class,0);
		}
		public Partial_operand_spatial_NContext partial_operand_spatial_N() {
			return getRuleContext(Partial_operand_spatial_NContext.class,0);
		}
		public Partial_operandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operandContext partial_operand() throws RecognitionException {
		Partial_operandContext _localctx = new Partial_operandContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_partial_operand);
		try {
			setState(273);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(269);
				partial_operand_common();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				setState(270);
				partial_operand_mixed();
				}
				break;
			case T__6:
			case T__7:
			case T__8:
				enterOuterAlt(_localctx, 3);
				{
				setState(271);
				partial_operand_spatial_common();
				}
				break;
			case T__9:
			case T__10:
			case T__11:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
				enterOuterAlt(_localctx, 4);
				{
				setState(272);
				partial_operand_spatial_N();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_commonContext extends ParserRuleContext {
		public Partial_operand_DContext partial_operand_D() {
			return getRuleContext(Partial_operand_DContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public Partial_operand_unknown_codeContext partial_operand_unknown_code() {
			return getRuleContext(Partial_operand_unknown_codeContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(LismaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LismaParser.COMMA, i);
		}
		public Partial_operand_spatial_var_codeContext partial_operand_spatial_var_code() {
			return getRuleContext(Partial_operand_spatial_var_codeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public TerminalNode DecimalLiteral() { return getToken(LismaParser.DecimalLiteral, 0); }
		public Partial_operand_commonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_common; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_common(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_common(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_common(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_commonContext partial_operand_common() throws RecognitionException {
		Partial_operand_commonContext _localctx = new Partial_operand_commonContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_partial_operand_common);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(275);
			partial_operand_D();
			setState(276);
			match(LPAREN);
			setState(277);
			partial_operand_unknown_code();
			setState(278);
			match(COMMA);
			setState(279);
			partial_operand_spatial_var_code();
			setState(282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(280);
				match(COMMA);
				setState(281);
				match(DecimalLiteral);
				}
			}

			setState(284);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_mixedContext extends ParserRuleContext {
		public Partial_operand_mixed_DContext partial_operand_mixed_D() {
			return getRuleContext(Partial_operand_mixed_DContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public Partial_operand_unknown_codeContext partial_operand_unknown_code() {
			return getRuleContext(Partial_operand_unknown_codeContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(LismaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LismaParser.COMMA, i);
		}
		public List<Partial_operand_spatial_var_codeContext> partial_operand_spatial_var_code() {
			return getRuleContexts(Partial_operand_spatial_var_codeContext.class);
		}
		public Partial_operand_spatial_var_codeContext partial_operand_spatial_var_code(int i) {
			return getRuleContext(Partial_operand_spatial_var_codeContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public TerminalNode DecimalLiteral() { return getToken(LismaParser.DecimalLiteral, 0); }
		public Partial_operand_mixedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_mixed; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_mixed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_mixed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_mixed(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_mixedContext partial_operand_mixed() throws RecognitionException {
		Partial_operand_mixedContext _localctx = new Partial_operand_mixedContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_partial_operand_mixed);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(286);
			partial_operand_mixed_D();
			setState(287);
			match(LPAREN);
			setState(288);
			partial_operand_unknown_code();
			setState(289);
			match(COMMA);
			setState(290);
			partial_operand_spatial_var_code();
			setState(291);
			match(COMMA);
			setState(292);
			partial_operand_spatial_var_code();
			setState(295);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(293);
				match(COMMA);
				setState(294);
				match(DecimalLiteral);
				}
			}

			setState(297);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_spatial_commonContext extends ParserRuleContext {
		public Partial_operand_func_spatial_commonContext partial_operand_func_spatial_common() {
			return getRuleContext(Partial_operand_func_spatial_commonContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public Partial_operand_unknown_codeContext partial_operand_unknown_code() {
			return getRuleContext(Partial_operand_unknown_codeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public TerminalNode COMMA() { return getToken(LismaParser.COMMA, 0); }
		public TerminalNode DecimalLiteral() { return getToken(LismaParser.DecimalLiteral, 0); }
		public Partial_operand_spatial_commonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_spatial_common; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_spatial_common(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_spatial_common(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_spatial_common(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_spatial_commonContext partial_operand_spatial_common() throws RecognitionException {
		Partial_operand_spatial_commonContext _localctx = new Partial_operand_spatial_commonContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_partial_operand_spatial_common);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(299);
			partial_operand_func_spatial_common();
			setState(300);
			match(LPAREN);
			setState(301);
			partial_operand_unknown_code();
			setState(304);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(302);
				match(COMMA);
				setState(303);
				match(DecimalLiteral);
				}
			}

			setState(306);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_spatial_NContext extends ParserRuleContext {
		public Partial_operand_func_spatialContext partial_operand_func_spatial() {
			return getRuleContext(Partial_operand_func_spatialContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public Partial_operand_unknown_codeContext partial_operand_unknown_code() {
			return getRuleContext(Partial_operand_unknown_codeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public Partial_operand_spatial_NContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_spatial_N; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_spatial_N(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_spatial_N(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_spatial_N(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_spatial_NContext partial_operand_spatial_N() throws RecognitionException {
		Partial_operand_spatial_NContext _localctx = new Partial_operand_spatial_NContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_partial_operand_spatial_N);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(308);
			partial_operand_func_spatial();
			setState(309);
			match(LPAREN);
			setState(310);
			partial_operand_unknown_code();
			setState(311);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_func_spatialContext extends ParserRuleContext {
		public Partial_operand_func_spatial_2Context partial_operand_func_spatial_2() {
			return getRuleContext(Partial_operand_func_spatial_2Context.class,0);
		}
		public Partial_operand_func_spatial_3Context partial_operand_func_spatial_3() {
			return getRuleContext(Partial_operand_func_spatial_3Context.class,0);
		}
		public Partial_operand_func_spatial_4Context partial_operand_func_spatial_4() {
			return getRuleContext(Partial_operand_func_spatial_4Context.class,0);
		}
		public Partial_operand_func_spatialContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_func_spatial; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_func_spatial(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_func_spatial(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_func_spatial(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_func_spatialContext partial_operand_func_spatial() throws RecognitionException {
		Partial_operand_func_spatialContext _localctx = new Partial_operand_func_spatialContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_partial_operand_func_spatial);
		try {
			setState(316);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
			case T__10:
			case T__11:
				enterOuterAlt(_localctx, 1);
				{
				setState(313);
				partial_operand_func_spatial_2();
				}
				break;
			case T__12:
			case T__13:
			case T__14:
				enterOuterAlt(_localctx, 2);
				{
				setState(314);
				partial_operand_func_spatial_3();
				}
				break;
			case T__15:
			case T__16:
			case T__17:
				enterOuterAlt(_localctx, 3);
				{
				setState(315);
				partial_operand_func_spatial_4();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_DContext extends ParserRuleContext {
		public Partial_operand_DContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_D; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_D(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_D(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_D(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_DContext partial_operand_D() throws RecognitionException {
		Partial_operand_DContext _localctx = new Partial_operand_DContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_partial_operand_D);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_mixed_DContext extends ParserRuleContext {
		public Partial_operand_mixed_DContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_mixed_D; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_mixed_D(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_mixed_D(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_mixed_D(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_mixed_DContext partial_operand_mixed_D() throws RecognitionException {
		Partial_operand_mixed_DContext _localctx = new Partial_operand_mixed_DContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_partial_operand_mixed_D);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_func_spatial_commonContext extends ParserRuleContext {
		public Partial_operand_func_spatial_commonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_func_spatial_common; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_func_spatial_common(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_func_spatial_common(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_func_spatial_common(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_func_spatial_commonContext partial_operand_func_spatial_common() throws RecognitionException {
		Partial_operand_func_spatial_commonContext _localctx = new Partial_operand_func_spatial_commonContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_partial_operand_func_spatial_common);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__8))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_func_spatial_2Context extends ParserRuleContext {
		public Partial_operand_func_spatial_2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_func_spatial_2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_func_spatial_2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_func_spatial_2(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_func_spatial_2(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_func_spatial_2Context partial_operand_func_spatial_2() throws RecognitionException {
		Partial_operand_func_spatial_2Context _localctx = new Partial_operand_func_spatial_2Context(_ctx, getState());
		enterRule(_localctx, 38, RULE_partial_operand_func_spatial_2);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__10) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_func_spatial_3Context extends ParserRuleContext {
		public Partial_operand_func_spatial_3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_func_spatial_3; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_func_spatial_3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_func_spatial_3(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_func_spatial_3(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_func_spatial_3Context partial_operand_func_spatial_3() throws RecognitionException {
		Partial_operand_func_spatial_3Context _localctx = new Partial_operand_func_spatial_3Context(_ctx, getState());
		enterRule(_localctx, 40, RULE_partial_operand_func_spatial_3);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__12) | (1L << T__13) | (1L << T__14))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_func_spatial_4Context extends ParserRuleContext {
		public Partial_operand_func_spatial_4Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_func_spatial_4; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_func_spatial_4(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_func_spatial_4(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_func_spatial_4(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_func_spatial_4Context partial_operand_func_spatial_4() throws RecognitionException {
		Partial_operand_func_spatial_4Context _localctx = new Partial_operand_func_spatial_4Context(_ctx, getState());
		enterRule(_localctx, 42, RULE_partial_operand_func_spatial_4);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__15) | (1L << T__16) | (1L << T__17))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_unknown_codeContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public Partial_operand_unknown_codeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_unknown_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_unknown_code(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_unknown_code(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_unknown_code(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_unknown_codeContext partial_operand_unknown_code() throws RecognitionException {
		Partial_operand_unknown_codeContext _localctx = new Partial_operand_unknown_codeContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_partial_operand_unknown_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_spatial_var_codeContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public Partial_operand_spatial_var_codeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_spatial_var_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_spatial_var_code(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_spatial_var_code(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_spatial_var_code(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_spatial_var_codeContext partial_operand_spatial_var_code() throws RecognitionException {
		Partial_operand_spatial_var_codeContext _localctx = new Partial_operand_spatial_var_codeContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_partial_operand_spatial_var_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EdgeContext extends ParserRuleContext {
		public Edge_eqContext edge_eq() {
			return getRuleContext(Edge_eqContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public Edge_sideContext edge_side() {
			return getRuleContext(Edge_sideContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public EdgeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_edge; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterEdge(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitEdge(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitEdge(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EdgeContext edge() throws RecognitionException {
		EdgeContext _localctx = new EdgeContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_edge);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(334);
			match(T__18);
			setState(335);
			edge_eq();
			setState(336);
			match(T__19);
			setState(337);
			match(Identifier);
			setState(338);
			edge_side();
			setState(339);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Edge_eqContext extends ParserRuleContext {
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public Partial_operandContext partial_operand() {
			return getRuleContext(Partial_operandContext.class,0);
		}
		public Edge_eqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_edge_eq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterEdge_eq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitEdge_eq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitEdge_eq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Edge_eqContext edge_eq() throws RecognitionException {
		Edge_eqContext _localctx = new Edge_eqContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_edge_eq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				{
				setState(341);
				match(Identifier);
				}
				break;
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
				{
				setState(342);
				partial_operand();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(345);
			match(ASSIGN);
			setState(346);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Edge_sideContext extends ParserRuleContext {
		public Edge_sideContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_edge_side; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterEdge_side(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitEdge_side(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitEdge_side(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Edge_sideContext edge_side() throws RecognitionException {
		Edge_sideContext _localctx = new Edge_sideContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_edge_side);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(348);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__20) | (1L << T__21) | (1L << T__22))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Init_condContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public List<Init_cond_bodyContext> init_cond_body() {
			return getRuleContexts(Init_cond_bodyContext.class);
		}
		public Init_cond_bodyContext init_cond_body(int i) {
			return getRuleContext(Init_cond_bodyContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(LismaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LismaParser.COMMA, i);
		}
		public Init_condContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init_cond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterInit_cond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitInit_cond(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitInit_cond(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Init_condContext init_cond() throws RecognitionException {
		Init_condContext _localctx = new Init_condContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_init_cond);
		int _la;
		try {
			setState(369);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(350);
				variable();
				setState(351);
				match(LPAREN);
				setState(352);
				match(T__23);
				setState(353);
				match(RPAREN);
				setState(354);
				match(ASSIGN);
				setState(355);
				expression();
				setState(356);
				match(SEMI);
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 2);
				{
				setState(358);
				match(T__24);
				setState(359);
				init_cond_body();
				setState(364);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(360);
					match(COMMA);
					setState(361);
					init_cond_body();
					}
					}
					setState(366);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(367);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Init_cond_bodyContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Init_cond_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init_cond_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterInit_cond_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitInit_cond_body(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitInit_cond_body(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Init_cond_bodyContext init_cond_body() throws RecognitionException {
		Init_cond_bodyContext _localctx = new Init_cond_bodyContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_init_cond_body);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			variable();
			setState(372);
			match(ASSIGN);
			setState(373);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EquationContext extends ParserRuleContext {
		public Ode_equationContext ode_equation() {
			return getRuleContext(Ode_equationContext.class,0);
		}
		public Pde_equationContext pde_equation() {
			return getRuleContext(Pde_equationContext.class,0);
		}
		public Pde_equation_paramContext pde_equation_param() {
			return getRuleContext(Pde_equation_paramContext.class,0);
		}
		public EquationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterEquation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitEquation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitEquation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EquationContext equation() throws RecognitionException {
		EquationContext _localctx = new EquationContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_equation);
		try {
			setState(378);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(375);
				ode_equation();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(376);
				pde_equation();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(377);
				pde_equation_param();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ode_equationContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public Ode_equationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ode_equation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterOde_equation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitOde_equation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitOde_equation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ode_equationContext ode_equation() throws RecognitionException {
		Ode_equationContext _localctx = new Ode_equationContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_ode_equation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			variable();
			setState(381);
			match(ASSIGN);
			setState(382);
			expression();
			setState(383);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pde_equationContext extends ParserRuleContext {
		public List<Partial_operandContext> partial_operand() {
			return getRuleContexts(Partial_operandContext.class);
		}
		public Partial_operandContext partial_operand(int i) {
			return getRuleContext(Partial_operandContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public TerminalNode PDE() { return getToken(LismaParser.PDE, 0); }
		public List<TerminalNode> ADD() { return getTokens(LismaParser.ADD); }
		public TerminalNode ADD(int i) {
			return getToken(LismaParser.ADD, i);
		}
		public Pde_equationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pde_equation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPde_equation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPde_equation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPde_equation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pde_equationContext pde_equation() throws RecognitionException {
		Pde_equationContext _localctx = new Pde_equationContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_pde_equation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(386);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PDE) {
				{
				setState(385);
				match(PDE);
				}
			}

			setState(388);
			partial_operand();
			setState(393);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ADD) {
				{
				{
				setState(389);
				match(ADD);
				setState(390);
				partial_operand();
				}
				}
				setState(395);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(396);
			match(ASSIGN);
			setState(397);
			expression();
			setState(398);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pde_equation_paramContext extends ParserRuleContext {
		public TerminalNode PDE() { return getToken(LismaParser.PDE, 0); }
		public List<Partial_operand_with_paramContext> partial_operand_with_param() {
			return getRuleContexts(Partial_operand_with_paramContext.class);
		}
		public Partial_operand_with_paramContext partial_operand_with_param(int i) {
			return getRuleContext(Partial_operand_with_paramContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public List<TerminalNode> ADD() { return getTokens(LismaParser.ADD); }
		public TerminalNode ADD(int i) {
			return getToken(LismaParser.ADD, i);
		}
		public Pde_equation_paramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pde_equation_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPde_equation_param(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPde_equation_param(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPde_equation_param(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pde_equation_paramContext pde_equation_param() throws RecognitionException {
		Pde_equation_paramContext _localctx = new Pde_equation_paramContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_pde_equation_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(400);
			match(PDE);
			setState(401);
			partial_operand_with_param();
			setState(406);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ADD) {
				{
				{
				setState(402);
				match(ADD);
				setState(403);
				partial_operand_with_param();
				}
				}
				setState(408);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(409);
			match(ASSIGN);
			setState(410);
			expression();
			setState(411);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Partial_operand_with_paramContext extends ParserRuleContext {
		public Partial_operandContext partial_operand() {
			return getRuleContext(Partial_operandContext.class,0);
		}
		public Pde_paramContext pde_param() {
			return getRuleContext(Pde_paramContext.class,0);
		}
		public Partial_operand_with_paramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partial_operand_with_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPartial_operand_with_param(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPartial_operand_with_param(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPartial_operand_with_param(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partial_operand_with_paramContext partial_operand_with_param() throws RecognitionException {
		Partial_operand_with_paramContext _localctx = new Partial_operand_with_paramContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_partial_operand_with_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DecimalLiteral) | (1L << FloatingPointLiteral) | (1L << Identifier) | (1L << LBRACK))) != 0)) {
				{
				setState(413);
				pde_param();
				}
			}

			setState(416);
			partial_operand();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pde_paramContext extends ParserRuleContext {
		public Pde_param_atomContext pde_param_atom() {
			return getRuleContext(Pde_param_atomContext.class,0);
		}
		public TerminalNode MUL() { return getToken(LismaParser.MUL, 0); }
		public Pde_paramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pde_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPde_param(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPde_param(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPde_param(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pde_paramContext pde_param() throws RecognitionException {
		Pde_paramContext _localctx = new Pde_paramContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_pde_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			pde_param_atom();
			setState(419);
			match(MUL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pde_param_atomContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(LismaParser.LBRACK, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(LismaParser.RBRACK, 0); }
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public Pde_param_atomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pde_param_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPde_param_atom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPde_param_atom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPde_param_atom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pde_param_atomContext pde_param_atom() throws RecognitionException {
		Pde_param_atomContext _localctx = new Pde_param_atomContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_pde_param_atom);
		try {
			setState(427);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACK:
				enterOuterAlt(_localctx, 1);
				{
				setState(421);
				match(LBRACK);
				setState(422);
				expression();
				setState(423);
				match(RBRACK);
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(425);
				match(Identifier);
				}
				break;
			case DecimalLiteral:
			case FloatingPointLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(426);
				literal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_cycleContext extends ParserRuleContext {
		public TerminalNode FOR_KEYWORD() { return getToken(LismaParser.FOR_KEYWORD, 0); }
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public List<For_cycle_intervalContext> for_cycle_interval() {
			return getRuleContexts(For_cycle_intervalContext.class);
		}
		public For_cycle_intervalContext for_cycle_interval(int i) {
			return getRuleContext(For_cycle_intervalContext.class,i);
		}
		public For_cycle_bodyContext for_cycle_body() {
			return getRuleContext(For_cycle_bodyContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(LismaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LismaParser.COMMA, i);
		}
		public For_cycleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_cycle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterFor_cycle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitFor_cycle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitFor_cycle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final For_cycleContext for_cycle() throws RecognitionException {
		For_cycleContext _localctx = new For_cycleContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_for_cycle);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			match(FOR_KEYWORD);
			setState(430);
			match(Identifier);
			setState(431);
			match(ASSIGN);
			setState(432);
			for_cycle_interval();
			setState(437);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(433);
				match(COMMA);
				setState(434);
				for_cycle_interval();
				}
				}
				setState(439);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(440);
			for_cycle_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_cycle_intervalContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public TerminalNode COLON() { return getToken(LismaParser.COLON, 0); }
		public For_cycle_intervalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_cycle_interval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterFor_cycle_interval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitFor_cycle_interval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitFor_cycle_interval(this);
			else return visitor.visitChildren(this);
		}
	}

	public final For_cycle_intervalContext for_cycle_interval() throws RecognitionException {
		For_cycle_intervalContext _localctx = new For_cycle_intervalContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_for_cycle_interval);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(442);
			literal();
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(443);
				match(COLON);
				setState(444);
				literal();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_cycle_bodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(LismaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(LismaParser.RBRACE, 0); }
		public List<EquationContext> equation() {
			return getRuleContexts(EquationContext.class);
		}
		public EquationContext equation(int i) {
			return getRuleContext(EquationContext.class,i);
		}
		public List<Init_condContext> init_cond() {
			return getRuleContexts(Init_condContext.class);
		}
		public Init_condContext init_cond(int i) {
			return getRuleContext(Init_condContext.class,i);
		}
		public List<ConstantContext> constant() {
			return getRuleContexts(ConstantContext.class);
		}
		public ConstantContext constant(int i) {
			return getRuleContext(ConstantContext.class,i);
		}
		public List<Pseudo_stateContext> pseudo_state() {
			return getRuleContexts(Pseudo_stateContext.class);
		}
		public Pseudo_stateContext pseudo_state(int i) {
			return getRuleContext(Pseudo_stateContext.class,i);
		}
		public List<MacrosContext> macros() {
			return getRuleContexts(MacrosContext.class);
		}
		public MacrosContext macros(int i) {
			return getRuleContext(MacrosContext.class,i);
		}
		public For_cycle_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_cycle_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterFor_cycle_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitFor_cycle_body(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitFor_cycle_body(this);
			else return visitor.visitChildren(this);
		}
	}

	public final For_cycle_bodyContext for_cycle_body() throws RecognitionException {
		For_cycle_bodyContext _localctx = new For_cycle_bodyContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_for_cycle_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(447);
			match(LBRACE);
			setState(455);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__24) | (1L << T__28) | (1L << T__38) | (1L << CONST_KEYWORD) | (1L << PDE) | (1L << IF_KEYWORD) | (1L << Identifier))) != 0)) {
				{
				setState(453);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
				case 1:
					{
					setState(448);
					equation();
					}
					break;
				case 2:
					{
					setState(449);
					init_cond();
					}
					break;
				case 3:
					{
					setState(450);
					constant();
					}
					break;
				case 4:
					{
					setState(451);
					pseudo_state();
					}
					break;
				case 5:
					{
					setState(452);
					macros();
					}
					break;
				}
				}
				setState(457);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(458);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StateContext extends ParserRuleContext {
		public TerminalNode STATE_KEYWORD() { return getToken(LismaParser.STATE_KEYWORD, 0); }
		public State_nameContext state_name() {
			return getRuleContext(State_nameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public State_bodyContext state_body() {
			return getRuleContext(State_bodyContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public State_fromContext state_from() {
			return getRuleContext(State_fromContext.class,0);
		}
		public StateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterState(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitState(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitState(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StateContext state() throws RecognitionException {
		StateContext _localctx = new StateContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_state);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(460);
			match(STATE_KEYWORD);
			setState(461);
			state_name();
			setState(462);
			match(LPAREN);
			setState(463);
			expression();
			setState(464);
			match(RPAREN);
			setState(465);
			state_body();
			setState(467);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__25) {
				{
				setState(466);
				state_from();
				}
			}

			setState(469);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class State_bodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(LismaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(LismaParser.RBRACE, 0); }
		public List<EquationContext> equation() {
			return getRuleContexts(EquationContext.class);
		}
		public EquationContext equation(int i) {
			return getRuleContext(EquationContext.class,i);
		}
		public List<SetterContext> setter() {
			return getRuleContexts(SetterContext.class);
		}
		public SetterContext setter(int i) {
			return getRuleContext(SetterContext.class,i);
		}
		public List<For_cycleContext> for_cycle() {
			return getRuleContexts(For_cycleContext.class);
		}
		public For_cycleContext for_cycle(int i) {
			return getRuleContext(For_cycleContext.class,i);
		}
		public State_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterState_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitState_body(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitState_body(this);
			else return visitor.visitChildren(this);
		}
	}

	public final State_bodyContext state_body() throws RecognitionException {
		State_bodyContext _localctx = new State_bodyContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_state_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(471);
			match(LBRACE);
			setState(477);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__28) | (1L << T__39) | (1L << PDE) | (1L << FOR_KEYWORD) | (1L << Identifier))) != 0)) {
				{
				setState(475);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
				case T__5:
				case T__6:
				case T__7:
				case T__8:
				case T__9:
				case T__10:
				case T__11:
				case T__12:
				case T__13:
				case T__14:
				case T__15:
				case T__16:
				case T__17:
				case T__28:
				case PDE:
				case Identifier:
					{
					setState(472);
					equation();
					}
					break;
				case T__39:
					{
					setState(473);
					setter();
					}
					break;
				case FOR_KEYWORD:
					{
					setState(474);
					for_cycle();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(479);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(480);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class State_fromContext extends ParserRuleContext {
		public List<State_nameContext> state_name() {
			return getRuleContexts(State_nameContext.class);
		}
		public State_nameContext state_name(int i) {
			return getRuleContext(State_nameContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(LismaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LismaParser.COMMA, i);
		}
		public State_fromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state_from; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterState_from(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitState_from(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitState_from(this);
			else return visitor.visitChildren(this);
		}
	}

	public final State_fromContext state_from() throws RecognitionException {
		State_fromContext _localctx = new State_fromContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_state_from);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			match(T__25);
			setState(483);
			state_name();
			setState(488);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(484);
				match(COMMA);
				setState(485);
				state_name();
				}
				}
				setState(490);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class State_nameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public State_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterState_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitState_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitState_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final State_nameContext state_name() throws RecognitionException {
		State_nameContext _localctx = new State_nameContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_state_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(491);
			_la = _input.LA(1);
			if ( !(_la==T__26 || _la==Identifier) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pseudo_stateContext extends ParserRuleContext {
		public TerminalNode IF_KEYWORD() { return getToken(LismaParser.IF_KEYWORD, 0); }
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public Pseudo_state_bodyContext pseudo_state_body() {
			return getRuleContext(Pseudo_state_bodyContext.class,0);
		}
		public Pseudo_state_elseContext pseudo_state_else() {
			return getRuleContext(Pseudo_state_elseContext.class,0);
		}
		public Pseudo_stateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pseudo_state; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPseudo_state(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPseudo_state(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPseudo_state(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pseudo_stateContext pseudo_state() throws RecognitionException {
		Pseudo_stateContext _localctx = new Pseudo_stateContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_pseudo_state);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			match(IF_KEYWORD);
			setState(494);
			match(LPAREN);
			setState(495);
			expression();
			setState(496);
			match(RPAREN);
			setState(497);
			pseudo_state_body();
			setState(499);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__27) {
				{
				setState(498);
				pseudo_state_else();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pseudo_state_bodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(LismaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(LismaParser.RBRACE, 0); }
		public List<Pseudo_state_elemContext> pseudo_state_elem() {
			return getRuleContexts(Pseudo_state_elemContext.class);
		}
		public Pseudo_state_elemContext pseudo_state_elem(int i) {
			return getRuleContext(Pseudo_state_elemContext.class,i);
		}
		public Pseudo_state_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pseudo_state_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPseudo_state_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPseudo_state_body(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPseudo_state_body(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pseudo_state_bodyContext pseudo_state_body() throws RecognitionException {
		Pseudo_state_bodyContext _localctx = new Pseudo_state_bodyContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_pseudo_state_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501);
			match(LBRACE);
			setState(505);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__28) | (1L << T__39) | (1L << PDE) | (1L << Identifier))) != 0)) {
				{
				{
				setState(502);
				pseudo_state_elem();
				}
				}
				setState(507);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(508);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pseudo_state_elemContext extends ParserRuleContext {
		public EquationContext equation() {
			return getRuleContext(EquationContext.class,0);
		}
		public SetterContext setter() {
			return getRuleContext(SetterContext.class,0);
		}
		public Pseudo_state_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pseudo_state_elem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPseudo_state_elem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPseudo_state_elem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPseudo_state_elem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pseudo_state_elemContext pseudo_state_elem() throws RecognitionException {
		Pseudo_state_elemContext _localctx = new Pseudo_state_elemContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_pseudo_state_elem);
		try {
			setState(512);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__28:
			case PDE:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(510);
				equation();
				}
				break;
			case T__39:
				enterOuterAlt(_localctx, 2);
				{
				setState(511);
				setter();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pseudo_state_elseContext extends ParserRuleContext {
		public Pseudo_state_bodyContext pseudo_state_body() {
			return getRuleContext(Pseudo_state_bodyContext.class,0);
		}
		public Pseudo_state_elseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pseudo_state_else; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPseudo_state_else(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPseudo_state_else(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPseudo_state_else(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pseudo_state_elseContext pseudo_state_else() throws RecognitionException {
		Pseudo_state_elseContext _localctx = new Pseudo_state_elseContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_pseudo_state_else);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(514);
			match(T__27);
			setState(515);
			pseudo_state_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Func_and_math_mappingContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public Arg_listContext arg_list() {
			return getRuleContext(Arg_listContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public Func_and_math_mappingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func_and_math_mapping; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterFunc_and_math_mapping(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitFunc_and_math_mapping(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitFunc_and_math_mapping(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Func_and_math_mappingContext func_and_math_mapping() throws RecognitionException {
		Func_and_math_mappingContext _localctx = new Func_and_math_mappingContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_func_and_math_mapping);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(517);
			match(Identifier);
			setState(518);
			match(LPAREN);
			setState(519);
			arg_list();
			setState(520);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arg_listContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(LismaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LismaParser.COMMA, i);
		}
		public Arg_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arg_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterArg_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitArg_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitArg_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Arg_listContext arg_list() throws RecognitionException {
		Arg_listContext _localctx = new Arg_listContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_arg_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(522);
			expression();
			setState(527);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(523);
				match(COMMA);
				setState(524);
				expression();
				}
				}
				setState(529);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Derivative_identContext extends ParserRuleContext {
		public Var_identContext var_ident() {
			return getRuleContext(Var_identContext.class,0);
		}
		public Derivative_quote_operantContext derivative_quote_operant() {
			return getRuleContext(Derivative_quote_operantContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(LismaParser.COMMA, 0); }
		public TerminalNode DecimalLiteral() { return getToken(LismaParser.DecimalLiteral, 0); }
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public Derivative_identContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_derivative_ident; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterDerivative_ident(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitDerivative_ident(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitDerivative_ident(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Derivative_identContext derivative_ident() throws RecognitionException {
		Derivative_identContext _localctx = new Derivative_identContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_derivative_ident);
		try {
			setState(540);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(530);
				var_ident();
				setState(531);
				derivative_quote_operant();
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 2);
				{
				setState(533);
				match(T__28);
				setState(534);
				match(LPAREN);
				setState(535);
				var_ident();
				setState(536);
				match(COMMA);
				setState(537);
				match(DecimalLiteral);
				setState(538);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Derivative_quote_operantContext extends ParserRuleContext {
		public List<TerminalNode> QUOTE1() { return getTokens(LismaParser.QUOTE1); }
		public TerminalNode QUOTE1(int i) {
			return getToken(LismaParser.QUOTE1, i);
		}
		public List<TerminalNode> QUOTE2() { return getTokens(LismaParser.QUOTE2); }
		public TerminalNode QUOTE2(int i) {
			return getToken(LismaParser.QUOTE2, i);
		}
		public Derivative_quote_operantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_derivative_quote_operant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterDerivative_quote_operant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitDerivative_quote_operant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitDerivative_quote_operant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Derivative_quote_operantContext derivative_quote_operant() throws RecognitionException {
		Derivative_quote_operantContext _localctx = new Derivative_quote_operantContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_derivative_quote_operant);
		int _la;
		try {
			setState(552);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QUOTE1:
				enterOuterAlt(_localctx, 1);
				{
				setState(543); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(542);
					match(QUOTE1);
					}
					}
					setState(545); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==QUOTE1 );
				}
				break;
			case QUOTE2:
				enterOuterAlt(_localctx, 2);
				{
				setState(548); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(547);
					match(QUOTE2);
					}
					}
					setState(550); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==QUOTE2 );
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public Var_identContext var_ident() {
			return getRuleContext(Var_identContext.class,0);
		}
		public Derivative_identContext derivative_ident() {
			return getRuleContext(Derivative_identContext.class,0);
		}
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_variable);
		try {
			setState(556);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(554);
				var_ident();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(555);
				derivative_ident();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Var_identContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public Cycle_indexContext cycle_index() {
			return getRuleContext(Cycle_indexContext.class,0);
		}
		public Var_identContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var_ident; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterVar_ident(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitVar_ident(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitVar_ident(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Var_identContext var_ident() throws RecognitionException {
		Var_identContext _localctx = new Var_identContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_var_ident);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(558);
			match(Identifier);
			setState(560);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(559);
				cycle_index();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cycle_indexContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(LismaParser.LBRACK, 0); }
		public Cycle_index_idxContext cycle_index_idx() {
			return getRuleContext(Cycle_index_idxContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(LismaParser.RBRACK, 0); }
		public Cycle_index_posfixContext cycle_index_posfix() {
			return getRuleContext(Cycle_index_posfixContext.class,0);
		}
		public Cycle_indexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cycle_index; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterCycle_index(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitCycle_index(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitCycle_index(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cycle_indexContext cycle_index() throws RecognitionException {
		Cycle_indexContext _localctx = new Cycle_indexContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_cycle_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(562);
			match(LBRACK);
			setState(563);
			cycle_index_idx();
			setState(564);
			match(RBRACK);
			setState(566);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				{
				setState(565);
				cycle_index_posfix();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cycle_index_idxContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode ADD() { return getToken(LismaParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(LismaParser.SUB, 0); }
		public Cycle_index_idxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cycle_index_idx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterCycle_index_idx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitCycle_index_idx(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitCycle_index_idx(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cycle_index_idxContext cycle_index_idx() throws RecognitionException {
		Cycle_index_idxContext _localctx = new Cycle_index_idxContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_cycle_index_idx);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(568);
			match(Identifier);
			setState(571);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADD || _la==SUB) {
				{
				setState(569);
				_la = _input.LA(1);
				if ( !(_la==ADD || _la==SUB) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(570);
				literal();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cycle_index_posfixContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public Cycle_index_posfixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cycle_index_posfix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterCycle_index_posfix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitCycle_index_posfix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitCycle_index_posfix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cycle_index_posfixContext cycle_index_posfix() throws RecognitionException {
		Cycle_index_posfixContext _localctx = new Cycle_index_posfixContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_cycle_index_posfix);
		try {
			setState(575);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(573);
				match(Identifier);
				}
				break;
			case DecimalLiteral:
			case FloatingPointLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(574);
				literal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParExpressionContext extends ParserRuleContext {
		public ParExpressionLeftParContext parExpressionLeftPar() {
			return getRuleContext(ParExpressionLeftParContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParExpressionRightParContext parExpressionRightPar() {
			return getRuleContext(ParExpressionRightParContext.class,0);
		}
		public ParExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterParExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitParExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitParExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParExpressionContext parExpression() throws RecognitionException {
		ParExpressionContext _localctx = new ParExpressionContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_parExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(577);
			parExpressionLeftPar();
			setState(578);
			expression();
			setState(579);
			parExpressionRightPar();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParExpressionLeftParContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(LismaParser.LPAREN, 0); }
		public ParExpressionLeftParContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parExpressionLeftPar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterParExpressionLeftPar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitParExpressionLeftPar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitParExpressionLeftPar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParExpressionLeftParContext parExpressionLeftPar() throws RecognitionException {
		ParExpressionLeftParContext _localctx = new ParExpressionLeftParContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_parExpressionLeftPar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(581);
			match(LPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParExpressionRightParContext extends ParserRuleContext {
		public TerminalNode RPAREN() { return getToken(LismaParser.RPAREN, 0); }
		public ParExpressionRightParContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parExpressionRightPar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterParExpressionRightPar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitParExpressionRightPar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitParExpressionRightPar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParExpressionRightParContext parExpressionRightPar() throws RecognitionException {
		ParExpressionRightParContext _localctx = new ParExpressionRightParContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_parExpressionRightPar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(583);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ConditionalExpressionContext conditionalExpression() {
			return getRuleContext(ConditionalExpressionContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(585);
			conditionalExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionalExpressionContext extends ParserRuleContext {
		public ConditionalOrExpressionContext conditionalOrExpression() {
			return getRuleContext(ConditionalOrExpressionContext.class,0);
		}
		public ConditionalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterConditionalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitConditionalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitConditionalExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalExpressionContext conditionalExpression() throws RecognitionException {
		ConditionalExpressionContext _localctx = new ConditionalExpressionContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_conditionalExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(587);
			conditionalOrExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionalOrExpressionContext extends ParserRuleContext {
		public List<ConditionalAndExpressionContext> conditionalAndExpression() {
			return getRuleContexts(ConditionalAndExpressionContext.class);
		}
		public ConditionalAndExpressionContext conditionalAndExpression(int i) {
			return getRuleContext(ConditionalAndExpressionContext.class,i);
		}
		public List<Or_operatorContext> or_operator() {
			return getRuleContexts(Or_operatorContext.class);
		}
		public Or_operatorContext or_operator(int i) {
			return getRuleContext(Or_operatorContext.class,i);
		}
		public ConditionalOrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalOrExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterConditionalOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitConditionalOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitConditionalOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalOrExpressionContext conditionalOrExpression() throws RecognitionException {
		ConditionalOrExpressionContext _localctx = new ConditionalOrExpressionContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_conditionalOrExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(589);
			conditionalAndExpression();
			setState(595);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__29) | (1L << T__30) | (1L << T__31))) != 0)) {
				{
				{
				setState(590);
				or_operator();
				setState(591);
				conditionalAndExpression();
				}
				}
				setState(597);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionalAndExpressionContext extends ParserRuleContext {
		public List<EqualityExpressionContext> equalityExpression() {
			return getRuleContexts(EqualityExpressionContext.class);
		}
		public EqualityExpressionContext equalityExpression(int i) {
			return getRuleContext(EqualityExpressionContext.class,i);
		}
		public List<And_operatorContext> and_operator() {
			return getRuleContexts(And_operatorContext.class);
		}
		public And_operatorContext and_operator(int i) {
			return getRuleContext(And_operatorContext.class,i);
		}
		public ConditionalAndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalAndExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterConditionalAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitConditionalAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitConditionalAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalAndExpressionContext conditionalAndExpression() throws RecognitionException {
		ConditionalAndExpressionContext _localctx = new ConditionalAndExpressionContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_conditionalAndExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(598);
			equalityExpression();
			setState(604);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__33) | (1L << T__34))) != 0)) {
				{
				{
				setState(599);
				and_operator();
				setState(600);
				equalityExpression();
				}
				}
				setState(606);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EqualityExpressionContext extends ParserRuleContext {
		public List<RelationalExpressionContext> relationalExpression() {
			return getRuleContexts(RelationalExpressionContext.class);
		}
		public RelationalExpressionContext relationalExpression(int i) {
			return getRuleContext(RelationalExpressionContext.class,i);
		}
		public List<EqualityExpressionOperatorContext> equalityExpressionOperator() {
			return getRuleContexts(EqualityExpressionOperatorContext.class);
		}
		public EqualityExpressionOperatorContext equalityExpressionOperator(int i) {
			return getRuleContext(EqualityExpressionOperatorContext.class,i);
		}
		public EqualityExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalityExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterEqualityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitEqualityExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitEqualityExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualityExpressionContext equalityExpression() throws RecognitionException {
		EqualityExpressionContext _localctx = new EqualityExpressionContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_equalityExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(607);
			relationalExpression();
			setState(613);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EQUAL || _la==NOTEQUAL) {
				{
				{
				setState(608);
				equalityExpressionOperator();
				setState(609);
				relationalExpression();
				}
				}
				setState(615);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EqualityExpressionOperatorContext extends ParserRuleContext {
		public TerminalNode EQUAL() { return getToken(LismaParser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(LismaParser.NOTEQUAL, 0); }
		public EqualityExpressionOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalityExpressionOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterEqualityExpressionOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitEqualityExpressionOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitEqualityExpressionOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualityExpressionOperatorContext equalityExpressionOperator() throws RecognitionException {
		EqualityExpressionOperatorContext _localctx = new EqualityExpressionOperatorContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_equalityExpressionOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(616);
			_la = _input.LA(1);
			if ( !(_la==EQUAL || _la==NOTEQUAL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelationalExpressionContext extends ParserRuleContext {
		public List<AdditiveExpressionContext> additiveExpression() {
			return getRuleContexts(AdditiveExpressionContext.class);
		}
		public AdditiveExpressionContext additiveExpression(int i) {
			return getRuleContext(AdditiveExpressionContext.class,i);
		}
		public List<RelationalOpContext> relationalOp() {
			return getRuleContexts(RelationalOpContext.class);
		}
		public RelationalOpContext relationalOp(int i) {
			return getRuleContext(RelationalOpContext.class,i);
		}
		public RelationalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterRelationalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitRelationalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitRelationalExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationalExpressionContext relationalExpression() throws RecognitionException {
		RelationalExpressionContext _localctx = new RelationalExpressionContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_relationalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(618);
			additiveExpression();
			setState(624);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (GT - 68)) | (1L << (LT - 68)) | (1L << (LE - 68)) | (1L << (GE - 68)))) != 0)) {
				{
				{
				setState(619);
				relationalOp();
				setState(620);
				additiveExpression();
				}
				}
				setState(626);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelationalOpContext extends ParserRuleContext {
		public TerminalNode LE() { return getToken(LismaParser.LE, 0); }
		public TerminalNode GE() { return getToken(LismaParser.GE, 0); }
		public TerminalNode LT() { return getToken(LismaParser.LT, 0); }
		public TerminalNode GT() { return getToken(LismaParser.GT, 0); }
		public RelationalOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationalOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterRelationalOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitRelationalOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitRelationalOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationalOpContext relationalOp() throws RecognitionException {
		RelationalOpContext _localctx = new RelationalOpContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_relationalOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(627);
			_la = _input.LA(1);
			if ( !(((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (GT - 68)) | (1L << (LT - 68)) | (1L << (LE - 68)) | (1L << (GE - 68)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AdditiveExpressionContext extends ParserRuleContext {
		public List<MultiplicativeExpressionContext> multiplicativeExpression() {
			return getRuleContexts(MultiplicativeExpressionContext.class);
		}
		public MultiplicativeExpressionContext multiplicativeExpression(int i) {
			return getRuleContext(MultiplicativeExpressionContext.class,i);
		}
		public List<AdditiveExpressionOperatorContext> additiveExpressionOperator() {
			return getRuleContexts(AdditiveExpressionOperatorContext.class);
		}
		public AdditiveExpressionOperatorContext additiveExpressionOperator(int i) {
			return getRuleContext(AdditiveExpressionOperatorContext.class,i);
		}
		public AdditiveExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterAdditiveExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitAdditiveExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitAdditiveExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveExpressionContext additiveExpression() throws RecognitionException {
		AdditiveExpressionContext _localctx = new AdditiveExpressionContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_additiveExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(629);
			multiplicativeExpression();
			setState(635);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ADD || _la==SUB) {
				{
				{
				setState(630);
				additiveExpressionOperator();
				setState(631);
				multiplicativeExpression();
				}
				}
				setState(637);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AdditiveExpressionOperatorContext extends ParserRuleContext {
		public TerminalNode ADD() { return getToken(LismaParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(LismaParser.SUB, 0); }
		public AdditiveExpressionOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpressionOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterAdditiveExpressionOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitAdditiveExpressionOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitAdditiveExpressionOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveExpressionOperatorContext additiveExpressionOperator() throws RecognitionException {
		AdditiveExpressionOperatorContext _localctx = new AdditiveExpressionOperatorContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_additiveExpressionOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(638);
			_la = _input.LA(1);
			if ( !(_la==ADD || _la==SUB) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MultiplicativeExpressionContext extends ParserRuleContext {
		public List<UnaryExpressionContext> unaryExpression() {
			return getRuleContexts(UnaryExpressionContext.class);
		}
		public UnaryExpressionContext unaryExpression(int i) {
			return getRuleContext(UnaryExpressionContext.class,i);
		}
		public List<MultiplicativeExpressionOperatorContext> multiplicativeExpressionOperator() {
			return getRuleContexts(MultiplicativeExpressionOperatorContext.class);
		}
		public MultiplicativeExpressionOperatorContext multiplicativeExpressionOperator(int i) {
			return getRuleContext(MultiplicativeExpressionOperatorContext.class,i);
		}
		public MultiplicativeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterMultiplicativeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitMultiplicativeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitMultiplicativeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExpressionContext multiplicativeExpression() throws RecognitionException {
		MultiplicativeExpressionContext _localctx = new MultiplicativeExpressionContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_multiplicativeExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(640);
			unaryExpression();
			setState(646);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & ((1L << (MUL - 81)) | (1L << (DIV - 81)) | (1L << (MOD - 81)))) != 0)) {
				{
				{
				setState(641);
				multiplicativeExpressionOperator();
				setState(642);
				unaryExpression();
				}
				}
				setState(648);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MultiplicativeExpressionOperatorContext extends ParserRuleContext {
		public TerminalNode MUL() { return getToken(LismaParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(LismaParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(LismaParser.MOD, 0); }
		public MultiplicativeExpressionOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpressionOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterMultiplicativeExpressionOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitMultiplicativeExpressionOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitMultiplicativeExpressionOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExpressionOperatorContext multiplicativeExpressionOperator() throws RecognitionException {
		MultiplicativeExpressionOperatorContext _localctx = new MultiplicativeExpressionOperatorContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_multiplicativeExpressionOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(649);
			_la = _input.LA(1);
			if ( !(((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & ((1L << (MUL - 81)) | (1L << (DIV - 81)) | (1L << (MOD - 81)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnaryExpressionContext extends ParserRuleContext {
		public UnaryExpressionOperatorContext unaryExpressionOperator() {
			return getRuleContext(UnaryExpressionOperatorContext.class,0);
		}
		public UnaryExpressionContext unaryExpression() {
			return getRuleContext(UnaryExpressionContext.class,0);
		}
		public UnaryExpressionNotPlusMinusContext unaryExpressionNotPlusMinus() {
			return getRuleContext(UnaryExpressionNotPlusMinusContext.class,0);
		}
		public UnaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitUnaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitUnaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpressionContext unaryExpression() throws RecognitionException {
		UnaryExpressionContext _localctx = new UnaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_unaryExpression);
		try {
			setState(655);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ADD:
			case SUB:
				enterOuterAlt(_localctx, 1);
				{
				setState(651);
				unaryExpressionOperator();
				setState(652);
				unaryExpression();
				}
				break;
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__35:
			case T__36:
			case T__37:
			case DecimalLiteral:
			case FloatingPointLiteral:
			case Identifier:
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(654);
				unaryExpressionNotPlusMinus();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnaryExpressionOperatorContext extends ParserRuleContext {
		public TerminalNode ADD() { return getToken(LismaParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(LismaParser.SUB, 0); }
		public UnaryExpressionOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpressionOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterUnaryExpressionOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitUnaryExpressionOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitUnaryExpressionOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpressionOperatorContext unaryExpressionOperator() throws RecognitionException {
		UnaryExpressionOperatorContext _localctx = new UnaryExpressionOperatorContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_unaryExpressionOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(657);
			_la = _input.LA(1);
			if ( !(_la==ADD || _la==SUB) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnaryExpressionNotPlusMinusContext extends ParserRuleContext {
		public Not_operatorContext not_operator() {
			return getRuleContext(Not_operatorContext.class,0);
		}
		public UnaryExpressionContext unaryExpression() {
			return getRuleContext(UnaryExpressionContext.class,0);
		}
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public UnaryExpressionNotPlusMinusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpressionNotPlusMinus; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterUnaryExpressionNotPlusMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitUnaryExpressionNotPlusMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitUnaryExpressionNotPlusMinus(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpressionNotPlusMinusContext unaryExpressionNotPlusMinus() throws RecognitionException {
		UnaryExpressionNotPlusMinusContext _localctx = new UnaryExpressionNotPlusMinusContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_unaryExpressionNotPlusMinus);
		try {
			setState(663);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__35:
			case T__36:
			case T__37:
				enterOuterAlt(_localctx, 1);
				{
				setState(659);
				not_operator();
				setState(660);
				unaryExpression();
				}
				break;
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case DecimalLiteral:
			case FloatingPointLiteral:
			case Identifier:
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(662);
				primary();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimaryContext extends ParserRuleContext {
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public Primary_idContext primary_id() {
			return getRuleContext(Primary_idContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public Partial_operandContext partial_operand() {
			return getRuleContext(Partial_operandContext.class,0);
		}
		public Func_and_math_mappingContext func_and_math_mapping() {
			return getRuleContext(Func_and_math_mappingContext.class,0);
		}
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPrimary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPrimary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPrimary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_primary);
		try {
			setState(670);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(665);
				parExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(666);
				primary_id();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(667);
				literal();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(668);
				partial_operand();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(669);
				func_and_math_mapping();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Primary_idContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(LismaParser.Identifier, 0); }
		public Cycle_indexContext cycle_index() {
			return getRuleContext(Cycle_indexContext.class,0);
		}
		public Primary_idContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterPrimary_id(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitPrimary_id(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitPrimary_id(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Primary_idContext primary_id() throws RecognitionException {
		Primary_idContext _localctx = new Primary_idContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_primary_id);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(672);
			match(Identifier);
			setState(674);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACK) {
				{
				setState(673);
				cycle_index();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode DecimalLiteral() { return getToken(LismaParser.DecimalLiteral, 0); }
		public TerminalNode FloatingPointLiteral() { return getToken(LismaParser.FloatingPointLiteral, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(676);
			_la = _input.LA(1);
			if ( !(_la==DecimalLiteral || _la==FloatingPointLiteral) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Or_operatorContext extends ParserRuleContext {
		public Or_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_or_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterOr_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitOr_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitOr_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Or_operatorContext or_operator() throws RecognitionException {
		Or_operatorContext _localctx = new Or_operatorContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_or_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(678);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__29) | (1L << T__30) | (1L << T__31))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class And_operatorContext extends ParserRuleContext {
		public And_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterAnd_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitAnd_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitAnd_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final And_operatorContext and_operator() throws RecognitionException {
		And_operatorContext _localctx = new And_operatorContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_and_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(680);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__33) | (1L << T__34))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Not_operatorContext extends ParserRuleContext {
		public Not_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_not_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterNot_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitNot_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitNot_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Not_operatorContext not_operator() throws RecognitionException {
		Not_operatorContext _localctx = new Not_operatorContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_not_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(682);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__35) | (1L << T__36) | (1L << T__37))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MacrosContext extends ParserRuleContext {
		public List<Macro_itemContext> macro_item() {
			return getRuleContexts(Macro_itemContext.class);
		}
		public Macro_itemContext macro_item(int i) {
			return getRuleContext(Macro_itemContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public List<TerminalNode> COMMA() { return getTokens(LismaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LismaParser.COMMA, i);
		}
		public MacrosContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_macros; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterMacros(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitMacros(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitMacros(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MacrosContext macros() throws RecognitionException {
		MacrosContext _localctx = new MacrosContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_macros);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(684);
			match(T__38);
			setState(685);
			macro_item();
			setState(690);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(686);
				match(COMMA);
				setState(687);
				macro_item();
				}
				}
				setState(692);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(693);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Macro_itemContext extends ParserRuleContext {
		public Primary_idContext primary_id() {
			return getRuleContext(Primary_idContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Macro_itemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_macro_item; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterMacro_item(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitMacro_item(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitMacro_item(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Macro_itemContext macro_item() throws RecognitionException {
		Macro_itemContext _localctx = new Macro_itemContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_macro_item);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(695);
			primary_id();
			setState(696);
			match(ASSIGN);
			setState(697);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SetterContext extends ParserRuleContext {
		public Var_identContext var_ident() {
			return getRuleContext(Var_identContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public SetterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterSetter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitSetter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitSetter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetterContext setter() throws RecognitionException {
		SetterContext _localctx = new SetterContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_setter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(699);
			match(T__39);
			setState(700);
			var_ident();
			setState(701);
			match(ASSIGN);
			setState(702);
			expression();
			setState(703);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Linear_varsContext extends ParserRuleContext {
		public List<Var_identContext> var_ident() {
			return getRuleContexts(Var_identContext.class);
		}
		public Var_identContext var_ident(int i) {
			return getRuleContext(Var_identContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public List<TerminalNode> COMMA() { return getTokens(LismaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(LismaParser.COMMA, i);
		}
		public Linear_varsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linear_vars; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterLinear_vars(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitLinear_vars(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitLinear_vars(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Linear_varsContext linear_vars() throws RecognitionException {
		Linear_varsContext _localctx = new Linear_varsContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_linear_vars);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(705);
			match(T__1);
			setState(706);
			match(T__40);
			setState(707);
			var_ident();
			setState(712);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(708);
				match(COMMA);
				setState(709);
				var_ident();
				}
				}
				setState(714);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(715);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Linear_eqContext extends ParserRuleContext {
		public Linear_eq_AContext linear_eq_A() {
			return getRuleContext(Linear_eq_AContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public Linear_eq_bContext linear_eq_b() {
			return getRuleContext(Linear_eq_bContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public Linear_eqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linear_eq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterLinear_eq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitLinear_eq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitLinear_eq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Linear_eqContext linear_eq() throws RecognitionException {
		Linear_eqContext _localctx = new Linear_eqContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_linear_eq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(717);
			match(T__40);
			setState(718);
			linear_eq_A();
			setState(719);
			match(ASSIGN);
			setState(720);
			linear_eq_b();
			setState(721);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Linear_eq_bContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Linear_eq_bContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linear_eq_b; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterLinear_eq_b(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitLinear_eq_b(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitLinear_eq_b(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Linear_eq_bContext linear_eq_b() throws RecognitionException {
		Linear_eq_bContext _localctx = new Linear_eq_bContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_linear_eq_b);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(723);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Linear_eq_AContext extends ParserRuleContext {
		public List<Linear_eq_A_elemContext> linear_eq_A_elem() {
			return getRuleContexts(Linear_eq_A_elemContext.class);
		}
		public Linear_eq_A_elemContext linear_eq_A_elem(int i) {
			return getRuleContext(Linear_eq_A_elemContext.class,i);
		}
		public List<TerminalNode> ADD() { return getTokens(LismaParser.ADD); }
		public TerminalNode ADD(int i) {
			return getToken(LismaParser.ADD, i);
		}
		public Linear_eq_AContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linear_eq_A; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterLinear_eq_A(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitLinear_eq_A(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitLinear_eq_A(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Linear_eq_AContext linear_eq_A() throws RecognitionException {
		Linear_eq_AContext _localctx = new Linear_eq_AContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_linear_eq_A);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(725);
			linear_eq_A_elem();
			setState(730);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ADD) {
				{
				{
				setState(726);
				match(ADD);
				setState(727);
				linear_eq_A_elem();
				}
				}
				setState(732);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Linear_eq_A_elemContext extends ParserRuleContext {
		public Var_identContext var_ident() {
			return getRuleContext(Var_identContext.class,0);
		}
		public TerminalNode MUL() { return getToken(LismaParser.MUL, 0); }
		public Linear_eq_A_elem_exprContext linear_eq_A_elem_expr() {
			return getRuleContext(Linear_eq_A_elem_exprContext.class,0);
		}
		public Linear_eq_A_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linear_eq_A_elem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterLinear_eq_A_elem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitLinear_eq_A_elem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitLinear_eq_A_elem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Linear_eq_A_elemContext linear_eq_A_elem() throws RecognitionException {
		Linear_eq_A_elemContext _localctx = new Linear_eq_A_elemContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_linear_eq_A_elem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(733);
			var_ident();
			setState(734);
			match(MUL);
			setState(735);
			linear_eq_A_elem_expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Linear_eq_A_elem_exprContext extends ParserRuleContext {
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public MultiplicativeExpressionContext multiplicativeExpression() {
			return getRuleContext(MultiplicativeExpressionContext.class,0);
		}
		public Linear_eq_A_elem_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linear_eq_A_elem_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterLinear_eq_A_elem_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitLinear_eq_A_elem_expr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitLinear_eq_A_elem_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Linear_eq_A_elem_exprContext linear_eq_A_elem_expr() throws RecognitionException {
		Linear_eq_A_elem_exprContext _localctx = new Linear_eq_A_elem_exprContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_linear_eq_A_elem_expr);
		try {
			setState(740);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(737);
				primary();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(738);
				parExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(739);
				multiplicativeExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StartContext extends ParserRuleContext {
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitStart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_start);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(742);
			match(T__41);
			setState(743);
			match(ASSIGN);
			setState(744);
			expression();
			setState(745);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EndContext extends ParserRuleContext {
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public EndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_end; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitEnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndContext end() throws RecognitionException {
		EndContext _localctx = new EndContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_end);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(747);
			match(T__42);
			setState(748);
			match(ASSIGN);
			setState(749);
			expression();
			setState(750);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StepContext extends ParserRuleContext {
		public TerminalNode ASSIGN() { return getToken(LismaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public StepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_step; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitStep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StepContext step() throws RecognitionException {
		StepContext _localctx = new StepContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_step);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(752);
			match(T__3);
			setState(753);
			match(ASSIGN);
			setState(754);
			expression();
			setState(755);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OutContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(LismaParser.SEMI, 0); }
		public List<Var_identContext> var_ident() {
			return getRuleContexts(Var_identContext.class);
		}
		public Var_identContext var_ident(int i) {
			return getRuleContext(Var_identContext.class,i);
		}
		public OutContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_out; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).enterOut(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LismaListener ) ((LismaListener)listener).exitOut(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LismaVisitor ) return ((LismaVisitor<? extends T>)visitor).visitOut(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OutContext out() throws RecognitionException {
		OutContext _localctx = new OutContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_out);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(757);
			match(T__43);
			setState(759); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(758);
				var_ident();
				}
				}
				setState(761); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Identifier );
			setState(763);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3c\u0300\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\3\2\7\2"+
		"\u00be\n\2\f\2\16\2\u00c1\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00d3\n\3\3\4\3\4\3\4\3\4\7\4\u00d9\n\4\f"+
		"\4\16\4\u00dc\13\4\3\4\3\4\3\5\3\5\3\5\6\5\u00e3\n\5\r\5\16\5\u00e4\3"+
		"\5\3\5\3\5\5\5\u00ea\n\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\5\7\u00f9\n\7\3\7\3\7\3\b\5\b\u00fe\n\b\3\b\3\b\5\b\u0102\n\b\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\5\t\u010a\n\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f"+
		"\5\f\u0114\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u011d\n\r\3\r\3\r\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u012a\n\16\3\16\3\16\3\17"+
		"\3\17\3\17\3\17\3\17\5\17\u0133\n\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\21\3\21\3\21\5\21\u013f\n\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\33\3\33\5\33\u015a\n\33\3\33\3\33\3\33\3\34\3\34\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\7\35\u016d\n\35\f\35\16"+
		"\35\u0170\13\35\3\35\3\35\5\35\u0174\n\35\3\36\3\36\3\36\3\36\3\37\3\37"+
		"\3\37\5\37\u017d\n\37\3 \3 \3 \3 \3 \3!\5!\u0185\n!\3!\3!\3!\7!\u018a"+
		"\n!\f!\16!\u018d\13!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\7\"\u0197\n\"\f\"\16"+
		"\"\u019a\13\"\3\"\3\"\3\"\3\"\3#\5#\u01a1\n#\3#\3#\3$\3$\3$\3%\3%\3%\3"+
		"%\3%\3%\5%\u01ae\n%\3&\3&\3&\3&\3&\3&\7&\u01b6\n&\f&\16&\u01b9\13&\3&"+
		"\3&\3\'\3\'\3\'\5\'\u01c0\n\'\3(\3(\3(\3(\3(\3(\7(\u01c8\n(\f(\16(\u01cb"+
		"\13(\3(\3(\3)\3)\3)\3)\3)\3)\3)\5)\u01d6\n)\3)\3)\3*\3*\3*\3*\7*\u01de"+
		"\n*\f*\16*\u01e1\13*\3*\3*\3+\3+\3+\3+\7+\u01e9\n+\f+\16+\u01ec\13+\3"+
		",\3,\3-\3-\3-\3-\3-\3-\5-\u01f6\n-\3.\3.\7.\u01fa\n.\f.\16.\u01fd\13."+
		"\3.\3.\3/\3/\5/\u0203\n/\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\62"+
		"\3\62\3\62\7\62\u0210\n\62\f\62\16\62\u0213\13\62\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\63\5\63\u021f\n\63\3\64\6\64\u0222\n\64\r"+
		"\64\16\64\u0223\3\64\6\64\u0227\n\64\r\64\16\64\u0228\5\64\u022b\n\64"+
		"\3\65\3\65\5\65\u022f\n\65\3\66\3\66\5\66\u0233\n\66\3\67\3\67\3\67\3"+
		"\67\5\67\u0239\n\67\38\38\38\58\u023e\n8\39\39\59\u0242\n9\3:\3:\3:\3"+
		":\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3?\3?\7?\u0254\n?\f?\16?\u0257\13?\3@"+
		"\3@\3@\3@\7@\u025d\n@\f@\16@\u0260\13@\3A\3A\3A\3A\7A\u0266\nA\fA\16A"+
		"\u0269\13A\3B\3B\3C\3C\3C\3C\7C\u0271\nC\fC\16C\u0274\13C\3D\3D\3E\3E"+
		"\3E\3E\7E\u027c\nE\fE\16E\u027f\13E\3F\3F\3G\3G\3G\3G\7G\u0287\nG\fG\16"+
		"G\u028a\13G\3H\3H\3I\3I\3I\3I\5I\u0292\nI\3J\3J\3K\3K\3K\3K\5K\u029a\n"+
		"K\3L\3L\3L\3L\3L\5L\u02a1\nL\3M\3M\5M\u02a5\nM\3N\3N\3O\3O\3P\3P\3Q\3"+
		"Q\3R\3R\3R\3R\7R\u02b3\nR\fR\16R\u02b6\13R\3R\3R\3S\3S\3S\3S\3T\3T\3T"+
		"\3T\3T\3T\3U\3U\3U\3U\3U\7U\u02c9\nU\fU\16U\u02cc\13U\3U\3U\3V\3V\3V\3"+
		"V\3V\3V\3W\3W\3X\3X\3X\7X\u02db\nX\fX\16X\u02de\13X\3Y\3Y\3Y\3Y\3Z\3Z"+
		"\3Z\5Z\u02e7\nZ\3[\3[\3[\3[\3[\3\\\3\\\3\\\3\\\3\\\3]\3]\3]\3]\3]\3^\3"+
		"^\6^\u02fa\n^\r^\16^\u02fb\3^\3^\3^\2\2_\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080"+
		"\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098"+
		"\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0"+
		"\u00b2\u00b4\u00b6\u00b8\u00ba\2\20\3\2\t\13\3\2\f\16\3\2\17\21\3\2\22"+
		"\24\3\2\27\31\4\2\35\35\66\66\3\2QR\4\2KKNN\4\2FGLM\4\2STXX\3\2\64\65"+
		"\3\2 \"\3\2#%\3\2&(\2\u02f8\2\u00bf\3\2\2\2\4\u00d2\3\2\2\2\6\u00d4\3"+
		"\2\2\2\b\u00e9\3\2\2\2\n\u00eb\3\2\2\2\f\u00f0\3\2\2\2\16\u0101\3\2\2"+
		"\2\20\u0109\3\2\2\2\22\u010b\3\2\2\2\24\u010d\3\2\2\2\26\u0113\3\2\2\2"+
		"\30\u0115\3\2\2\2\32\u0120\3\2\2\2\34\u012d\3\2\2\2\36\u0136\3\2\2\2 "+
		"\u013e\3\2\2\2\"\u0140\3\2\2\2$\u0142\3\2\2\2&\u0144\3\2\2\2(\u0146\3"+
		"\2\2\2*\u0148\3\2\2\2,\u014a\3\2\2\2.\u014c\3\2\2\2\60\u014e\3\2\2\2\62"+
		"\u0150\3\2\2\2\64\u0159\3\2\2\2\66\u015e\3\2\2\28\u0173\3\2\2\2:\u0175"+
		"\3\2\2\2<\u017c\3\2\2\2>\u017e\3\2\2\2@\u0184\3\2\2\2B\u0192\3\2\2\2D"+
		"\u01a0\3\2\2\2F\u01a4\3\2\2\2H\u01ad\3\2\2\2J\u01af\3\2\2\2L\u01bc\3\2"+
		"\2\2N\u01c1\3\2\2\2P\u01ce\3\2\2\2R\u01d9\3\2\2\2T\u01e4\3\2\2\2V\u01ed"+
		"\3\2\2\2X\u01ef\3\2\2\2Z\u01f7\3\2\2\2\\\u0202\3\2\2\2^\u0204\3\2\2\2"+
		"`\u0207\3\2\2\2b\u020c\3\2\2\2d\u021e\3\2\2\2f\u022a\3\2\2\2h\u022e\3"+
		"\2\2\2j\u0230\3\2\2\2l\u0234\3\2\2\2n\u023a\3\2\2\2p\u0241\3\2\2\2r\u0243"+
		"\3\2\2\2t\u0247\3\2\2\2v\u0249\3\2\2\2x\u024b\3\2\2\2z\u024d\3\2\2\2|"+
		"\u024f\3\2\2\2~\u0258\3\2\2\2\u0080\u0261\3\2\2\2\u0082\u026a\3\2\2\2"+
		"\u0084\u026c\3\2\2\2\u0086\u0275\3\2\2\2\u0088\u0277\3\2\2\2\u008a\u0280"+
		"\3\2\2\2\u008c\u0282\3\2\2\2\u008e\u028b\3\2\2\2\u0090\u0291\3\2\2\2\u0092"+
		"\u0293\3\2\2\2\u0094\u0299\3\2\2\2\u0096\u02a0\3\2\2\2\u0098\u02a2\3\2"+
		"\2\2\u009a\u02a6\3\2\2\2\u009c\u02a8\3\2\2\2\u009e\u02aa\3\2\2\2\u00a0"+
		"\u02ac\3\2\2\2\u00a2\u02ae\3\2\2\2\u00a4\u02b9\3\2\2\2\u00a6\u02bd\3\2"+
		"\2\2\u00a8\u02c3\3\2\2\2\u00aa\u02cf\3\2\2\2\u00ac\u02d5\3\2\2\2\u00ae"+
		"\u02d7\3\2\2\2\u00b0\u02df\3\2\2\2\u00b2\u02e6\3\2\2\2\u00b4\u02e8\3\2"+
		"\2\2\u00b6\u02ed\3\2\2\2\u00b8\u02f2\3\2\2\2\u00ba\u02f7\3\2\2\2\u00bc"+
		"\u00be\5\4\3\2\u00bd\u00bc\3\2\2\2\u00be\u00c1\3\2\2\2\u00bf\u00bd\3\2"+
		"\2\2\u00bf\u00c0\3\2\2\2\u00c0\3\3\2\2\2\u00c1\u00bf\3\2\2\2\u00c2\u00d3"+
		"\5\6\4\2\u00c3\u00d3\5\n\6\2\u00c4\u00d3\58\35\2\u00c5\u00d3\5<\37\2\u00c6"+
		"\u00d3\5P)\2\u00c7\u00d3\5X-\2\u00c8\u00d3\5\f\7\2\u00c9\u00d3\5\62\32"+
		"\2\u00ca\u00d3\5\u00a2R\2\u00cb\u00d3\5\u00b4[\2\u00cc\u00d3\5\u00b6\\"+
		"\2\u00cd\u00d3\5\u00b8]\2\u00ce\u00d3\5\u00ba^\2\u00cf\u00d3\5\u00aaV"+
		"\2\u00d0\u00d3\5\u00a8U\2\u00d1\u00d3\5J&\2\u00d2\u00c2\3\2\2\2\u00d2"+
		"\u00c3\3\2\2\2\u00d2\u00c4\3\2\2\2\u00d2\u00c5\3\2\2\2\u00d2\u00c6\3\2"+
		"\2\2\u00d2\u00c7\3\2\2\2\u00d2\u00c8\3\2\2\2\u00d2\u00c9\3\2\2\2\u00d2"+
		"\u00ca\3\2\2\2\u00d2\u00cb\3\2\2\2\u00d2\u00cc\3\2\2\2\u00d2\u00cd\3\2"+
		"\2\2\u00d2\u00ce\3\2\2\2\u00d2\u00cf\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d2"+
		"\u00d1\3\2\2\2\u00d3\5\3\2\2\2\u00d4\u00d5\7/\2\2\u00d5\u00da\5\b\5\2"+
		"\u00d6\u00d7\7A\2\2\u00d7\u00d9\5\b\5\2\u00d8\u00d6\3\2\2\2\u00d9\u00dc"+
		"\3\2\2\2\u00da\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00dd\3\2\2\2\u00dc"+
		"\u00da\3\2\2\2\u00dd\u00de\7@\2\2\u00de\7\3\2\2\2\u00df\u00e0\5j\66\2"+
		"\u00e0\u00e1\7E\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00df\3\2\2\2\u00e3\u00e4"+
		"\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6"+
		"\u00e7\5x=\2\u00e7\u00ea\3\2\2\2\u00e8\u00ea\5j\66\2\u00e9\u00e2\3\2\2"+
		"\2\u00e9\u00e8\3\2\2\2\u00ea\t\3\2\2\2\u00eb\u00ec\7\3\2\2\u00ec\u00ed"+
		"\7/\2\2\u00ed\u00ee\5\u009aN\2\u00ee\u00ef\7@\2\2\u00ef\13\3\2\2\2\u00f0"+
		"\u00f1\7\4\2\2\u00f1\u00f2\5j\66\2\u00f2\u00f3\7>\2\2\u00f3\u00f4\5\16"+
		"\b\2\u00f4\u00f5\7A\2\2\u00f5\u00f6\5\16\b\2\u00f6\u00f8\7?\2\2\u00f7"+
		"\u00f9\5\20\t\2\u00f8\u00f7\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00fa\3"+
		"\2\2\2\u00fa\u00fb\7@\2\2\u00fb\r\3\2\2\2\u00fc\u00fe\7R\2\2\u00fd\u00fc"+
		"\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0102\5\u009aN"+
		"\2\u0100\u0102\7\66\2\2\u0101\u00fd\3\2\2\2\u0101\u0100\3\2\2\2\u0102"+
		"\17\3\2\2\2\u0103\u0104\5\22\n\2\u0104\u0105\7\64\2\2\u0105\u010a\3\2"+
		"\2\2\u0106\u0107\5\24\13\2\u0107\u0108\5\u009aN\2\u0108\u010a\3\2\2\2"+
		"\u0109\u0103\3\2\2\2\u0109\u0106\3\2\2\2\u010a\21\3\2\2\2\u010b\u010c"+
		"\7\5\2\2\u010c\23\3\2\2\2\u010d\u010e\7\6\2\2\u010e\25\3\2\2\2\u010f\u0114"+
		"\5\30\r\2\u0110\u0114\5\32\16\2\u0111\u0114\5\34\17\2\u0112\u0114\5\36"+
		"\20\2\u0113\u010f\3\2\2\2\u0113\u0110\3\2\2\2\u0113\u0111\3\2\2\2\u0113"+
		"\u0112\3\2\2\2\u0114\27\3\2\2\2\u0115\u0116\5\"\22\2\u0116\u0117\7:\2"+
		"\2\u0117\u0118\5.\30\2\u0118\u0119\7A\2\2\u0119\u011c\5\60\31\2\u011a"+
		"\u011b\7A\2\2\u011b\u011d\7\64\2\2\u011c\u011a\3\2\2\2\u011c\u011d\3\2"+
		"\2\2\u011d\u011e\3\2\2\2\u011e\u011f\7;\2\2\u011f\31\3\2\2\2\u0120\u0121"+
		"\5$\23\2\u0121\u0122\7:\2\2\u0122\u0123\5.\30\2\u0123\u0124\7A\2\2\u0124"+
		"\u0125\5\60\31\2\u0125\u0126\7A\2\2\u0126\u0129\5\60\31\2\u0127\u0128"+
		"\7A\2\2\u0128\u012a\7\64\2\2\u0129\u0127\3\2\2\2\u0129\u012a\3\2\2\2\u012a"+
		"\u012b\3\2\2\2\u012b\u012c\7;\2\2\u012c\33\3\2\2\2\u012d\u012e\5&\24\2"+
		"\u012e\u012f\7:\2\2\u012f\u0132\5.\30\2\u0130\u0131\7A\2\2\u0131\u0133"+
		"\7\64\2\2\u0132\u0130\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0134\3\2\2\2"+
		"\u0134\u0135\7;\2\2\u0135\35\3\2\2\2\u0136\u0137\5 \21\2\u0137\u0138\7"+
		":\2\2\u0138\u0139\5.\30\2\u0139\u013a\7;\2\2\u013a\37\3\2\2\2\u013b\u013f"+
		"\5(\25\2\u013c\u013f\5*\26\2\u013d\u013f\5,\27\2\u013e\u013b\3\2\2\2\u013e"+
		"\u013c\3\2\2\2\u013e\u013d\3\2\2\2\u013f!\3\2\2\2\u0140\u0141\7\7\2\2"+
		"\u0141#\3\2\2\2\u0142\u0143\7\b\2\2\u0143%\3\2\2\2\u0144\u0145\t\2\2\2"+
		"\u0145\'\3\2\2\2\u0146\u0147\t\3\2\2\u0147)\3\2\2\2\u0148\u0149\t\4\2"+
		"\2\u0149+\3\2\2\2\u014a\u014b\t\5\2\2\u014b-\3\2\2\2\u014c\u014d\7\66"+
		"\2\2\u014d/\3\2\2\2\u014e\u014f\7\66\2\2\u014f\61\3\2\2\2\u0150\u0151"+
		"\7\25\2\2\u0151\u0152\5\64\33\2\u0152\u0153\7\26\2\2\u0153\u0154\7\66"+
		"\2\2\u0154\u0155\5\66\34\2\u0155\u0156\7@\2\2\u0156\63\3\2\2\2\u0157\u015a"+
		"\7\66\2\2\u0158\u015a\5\26\f\2\u0159\u0157\3\2\2\2\u0159\u0158\3\2\2\2"+
		"\u015a\u015b\3\2\2\2\u015b\u015c\7E\2\2\u015c\u015d\5x=\2\u015d\65\3\2"+
		"\2\2\u015e\u015f\t\6\2\2\u015f\67\3\2\2\2\u0160\u0161\5h\65\2\u0161\u0162"+
		"\7:\2\2\u0162\u0163\7\32\2\2\u0163\u0164\7;\2\2\u0164\u0165\7E\2\2\u0165"+
		"\u0166\5x=\2\u0166\u0167\7@\2\2\u0167\u0174\3\2\2\2\u0168\u0169\7\33\2"+
		"\2\u0169\u016e\5:\36\2\u016a\u016b\7A\2\2\u016b\u016d\5:\36\2\u016c\u016a"+
		"\3\2\2\2\u016d\u0170\3\2\2\2\u016e\u016c\3\2\2\2\u016e\u016f\3\2\2\2\u016f"+
		"\u0171\3\2\2\2\u0170\u016e\3\2\2\2\u0171\u0172\7@\2\2\u0172\u0174\3\2"+
		"\2\2\u0173\u0160\3\2\2\2\u0173\u0168\3\2\2\2\u01749\3\2\2\2\u0175\u0176"+
		"\5h\65\2\u0176\u0177\7E\2\2\u0177\u0178\5x=\2\u0178;\3\2\2\2\u0179\u017d"+
		"\5> \2\u017a\u017d\5@!\2\u017b\u017d\5B\"\2\u017c\u0179\3\2\2\2\u017c"+
		"\u017a\3\2\2\2\u017c\u017b\3\2\2\2\u017d=\3\2\2\2\u017e\u017f\5h\65\2"+
		"\u017f\u0180\7E\2\2\u0180\u0181\5x=\2\u0181\u0182\7@\2\2\u0182?\3\2\2"+
		"\2\u0183\u0185\7\60\2\2\u0184\u0183\3\2\2\2\u0184\u0185\3\2\2\2\u0185"+
		"\u0186\3\2\2\2\u0186\u018b\5\26\f\2\u0187\u0188\7Q\2\2\u0188\u018a\5\26"+
		"\f\2\u0189\u0187\3\2\2\2\u018a\u018d\3\2\2\2\u018b\u0189\3\2\2\2\u018b"+
		"\u018c\3\2\2\2\u018c\u018e\3\2\2\2\u018d\u018b\3\2\2\2\u018e\u018f\7E"+
		"\2\2\u018f\u0190\5x=\2\u0190\u0191\7@\2\2\u0191A\3\2\2\2\u0192\u0193\7"+
		"\60\2\2\u0193\u0198\5D#\2\u0194\u0195\7Q\2\2\u0195\u0197\5D#\2\u0196\u0194"+
		"\3\2\2\2\u0197\u019a\3\2\2\2\u0198\u0196\3\2\2\2\u0198\u0199\3\2\2\2\u0199"+
		"\u019b\3\2\2\2\u019a\u0198\3\2\2\2\u019b\u019c\7E\2\2\u019c\u019d\5x="+
		"\2\u019d\u019e\7@\2\2\u019eC\3\2\2\2\u019f\u01a1\5F$\2\u01a0\u019f\3\2"+
		"\2\2\u01a0\u01a1\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a3\5\26\f\2\u01a3"+
		"E\3\2\2\2\u01a4\u01a5\5H%\2\u01a5\u01a6\7S\2\2\u01a6G\3\2\2\2\u01a7\u01a8"+
		"\7>\2\2\u01a8\u01a9\5x=\2\u01a9\u01aa\7?\2\2\u01aa\u01ae\3\2\2\2\u01ab"+
		"\u01ae\7\66\2\2\u01ac\u01ae\5\u009aN\2\u01ad\u01a7\3\2\2\2\u01ad\u01ab"+
		"\3\2\2\2\u01ad\u01ac\3\2\2\2\u01aeI\3\2\2\2\u01af\u01b0\7\61\2\2\u01b0"+
		"\u01b1\7\66\2\2\u01b1\u01b2\7E\2\2\u01b2\u01b7\5L\'\2\u01b3\u01b4\7A\2"+
		"\2\u01b4\u01b6\5L\'\2\u01b5\u01b3\3\2\2\2\u01b6\u01b9\3\2\2\2\u01b7\u01b5"+
		"\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8\u01ba\3\2\2\2\u01b9\u01b7\3\2\2\2\u01ba"+
		"\u01bb\5N(\2\u01bbK\3\2\2\2\u01bc\u01bf\5\u009aN\2\u01bd\u01be\7J\2\2"+
		"\u01be\u01c0\5\u009aN\2\u01bf\u01bd\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0"+
		"M\3\2\2\2\u01c1\u01c9\7<\2\2\u01c2\u01c8\5<\37\2\u01c3\u01c8\58\35\2\u01c4"+
		"\u01c8\5\6\4\2\u01c5\u01c8\5X-\2\u01c6\u01c8\5\u00a2R\2\u01c7\u01c2\3"+
		"\2\2\2\u01c7\u01c3\3\2\2\2\u01c7\u01c4\3\2\2\2\u01c7\u01c5\3\2\2\2\u01c7"+
		"\u01c6\3\2\2\2\u01c8\u01cb\3\2\2\2\u01c9\u01c7\3\2\2\2\u01c9\u01ca\3\2"+
		"\2\2\u01ca\u01cc\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cc\u01cd\7=\2\2\u01cd"+
		"O\3\2\2\2\u01ce\u01cf\7\62\2\2\u01cf\u01d0\5V,\2\u01d0\u01d1\7:\2\2\u01d1"+
		"\u01d2\5x=\2\u01d2\u01d3\7;\2\2\u01d3\u01d5\5R*\2\u01d4\u01d6\5T+\2\u01d5"+
		"\u01d4\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6\u01d7\3\2\2\2\u01d7\u01d8\7@"+
		"\2\2\u01d8Q\3\2\2\2\u01d9\u01df\7<\2\2\u01da\u01de\5<\37\2\u01db\u01de"+
		"\5\u00a6T\2\u01dc\u01de\5J&\2\u01dd\u01da\3\2\2\2\u01dd\u01db\3\2\2\2"+
		"\u01dd\u01dc\3\2\2\2\u01de\u01e1\3\2\2\2\u01df\u01dd\3\2\2\2\u01df\u01e0"+
		"\3\2\2\2\u01e0\u01e2\3\2\2\2\u01e1\u01df\3\2\2\2\u01e2\u01e3\7=\2\2\u01e3"+
		"S\3\2\2\2\u01e4\u01e5\7\34\2\2\u01e5\u01ea\5V,\2\u01e6\u01e7\7A\2\2\u01e7"+
		"\u01e9\5V,\2\u01e8\u01e6\3\2\2\2\u01e9\u01ec\3\2\2\2\u01ea\u01e8\3\2\2"+
		"\2\u01ea\u01eb\3\2\2\2\u01ebU\3\2\2\2\u01ec\u01ea\3\2\2\2\u01ed\u01ee"+
		"\t\7\2\2\u01eeW\3\2\2\2\u01ef\u01f0\7\63\2\2\u01f0\u01f1\7:\2\2\u01f1"+
		"\u01f2\5x=\2\u01f2\u01f3\7;\2\2\u01f3\u01f5\5Z.\2\u01f4\u01f6\5^\60\2"+
		"\u01f5\u01f4\3\2\2\2\u01f5\u01f6\3\2\2\2\u01f6Y\3\2\2\2\u01f7\u01fb\7"+
		"<\2\2\u01f8\u01fa\5\\/\2\u01f9\u01f8\3\2\2\2\u01fa\u01fd\3\2\2\2\u01fb"+
		"\u01f9\3\2\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01fe\3\2\2\2\u01fd\u01fb\3\2"+
		"\2\2\u01fe\u01ff\7=\2\2\u01ff[\3\2\2\2\u0200\u0203\5<\37\2\u0201\u0203"+
		"\5\u00a6T\2\u0202\u0200\3\2\2\2\u0202\u0201\3\2\2\2\u0203]\3\2\2\2\u0204"+
		"\u0205\7\36\2\2\u0205\u0206\5Z.\2\u0206_\3\2\2\2\u0207\u0208\7\66\2\2"+
		"\u0208\u0209\7:\2\2\u0209\u020a\5b\62\2\u020a\u020b\7;\2\2\u020ba\3\2"+
		"\2\2\u020c\u0211\5x=\2\u020d\u020e\7A\2\2\u020e\u0210\5x=\2\u020f\u020d"+
		"\3\2\2\2\u0210\u0213\3\2\2\2\u0211\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212"+
		"c\3\2\2\2\u0213\u0211\3\2\2\2\u0214\u0215\5j\66\2\u0215\u0216\5f\64\2"+
		"\u0216\u021f\3\2\2\2\u0217\u0218\7\37\2\2\u0218\u0219\7:\2\2\u0219\u021a"+
		"\5j\66\2\u021a\u021b\7A\2\2\u021b\u021c\7\64\2\2\u021c\u021d\7;\2\2\u021d"+
		"\u021f\3\2\2\2\u021e\u0214\3\2\2\2\u021e\u0217\3\2\2\2\u021fe\3\2\2\2"+
		"\u0220\u0222\7C\2\2\u0221\u0220\3\2\2\2\u0222\u0223\3\2\2\2\u0223\u0221"+
		"\3\2\2\2\u0223\u0224\3\2\2\2\u0224\u022b\3\2\2\2\u0225\u0227\7D\2\2\u0226"+
		"\u0225\3\2\2\2\u0227\u0228\3\2\2\2\u0228\u0226\3\2\2\2\u0228\u0229\3\2"+
		"\2\2\u0229\u022b\3\2\2\2\u022a\u0221\3\2\2\2\u022a\u0226\3\2\2\2\u022b"+
		"g\3\2\2\2\u022c\u022f\5j\66\2\u022d\u022f\5d\63\2\u022e\u022c\3\2\2\2"+
		"\u022e\u022d\3\2\2\2\u022fi\3\2\2\2\u0230\u0232\7\66\2\2\u0231\u0233\5"+
		"l\67\2\u0232\u0231\3\2\2\2\u0232\u0233\3\2\2\2\u0233k\3\2\2\2\u0234\u0235"+
		"\7>\2\2\u0235\u0236\5n8\2\u0236\u0238\7?\2\2\u0237\u0239\5p9\2\u0238\u0237"+
		"\3\2\2\2\u0238\u0239\3\2\2\2\u0239m\3\2\2\2\u023a\u023d\7\66\2\2\u023b"+
		"\u023c\t\b\2\2\u023c\u023e\5\u009aN\2\u023d\u023b\3\2\2\2\u023d\u023e"+
		"\3\2\2\2\u023eo\3\2\2\2\u023f\u0242\7\66\2\2\u0240\u0242\5\u009aN\2\u0241"+
		"\u023f\3\2\2\2\u0241\u0240\3\2\2\2\u0242q\3\2\2\2\u0243\u0244\5t;\2\u0244"+
		"\u0245\5x=\2\u0245\u0246\5v<\2\u0246s\3\2\2\2\u0247\u0248\7:\2\2\u0248"+
		"u\3\2\2\2\u0249\u024a\7;\2\2\u024aw\3\2\2\2\u024b\u024c\5z>\2\u024cy\3"+
		"\2\2\2\u024d\u024e\5|?\2\u024e{\3\2\2\2\u024f\u0255\5~@\2\u0250\u0251"+
		"\5\u009cO\2\u0251\u0252\5~@\2\u0252\u0254\3\2\2\2\u0253\u0250\3\2\2\2"+
		"\u0254\u0257\3\2\2\2\u0255\u0253\3\2\2\2\u0255\u0256\3\2\2\2\u0256}\3"+
		"\2\2\2\u0257\u0255\3\2\2\2\u0258\u025e\5\u0080A\2\u0259\u025a\5\u009e"+
		"P\2\u025a\u025b\5\u0080A\2\u025b\u025d\3\2\2\2\u025c\u0259\3\2\2\2\u025d"+
		"\u0260\3\2\2\2\u025e\u025c\3\2\2\2\u025e\u025f\3\2\2\2\u025f\177\3\2\2"+
		"\2\u0260\u025e\3\2\2\2\u0261\u0267\5\u0084C\2\u0262\u0263\5\u0082B\2\u0263"+
		"\u0264\5\u0084C\2\u0264\u0266\3\2\2\2\u0265\u0262\3\2\2\2\u0266\u0269"+
		"\3\2\2\2\u0267\u0265\3\2\2\2\u0267\u0268\3\2\2\2\u0268\u0081\3\2\2\2\u0269"+
		"\u0267\3\2\2\2\u026a\u026b\t\t\2\2\u026b\u0083\3\2\2\2\u026c\u0272\5\u0088"+
		"E\2\u026d\u026e\5\u0086D\2\u026e\u026f\5\u0088E\2\u026f\u0271\3\2\2\2"+
		"\u0270\u026d\3\2\2\2\u0271\u0274\3\2\2\2\u0272\u0270\3\2\2\2\u0272\u0273"+
		"\3\2\2\2\u0273\u0085\3\2\2\2\u0274\u0272\3\2\2\2\u0275\u0276\t\n\2\2\u0276"+
		"\u0087\3\2\2\2\u0277\u027d\5\u008cG\2\u0278\u0279\5\u008aF\2\u0279\u027a"+
		"\5\u008cG\2\u027a\u027c\3\2\2\2\u027b\u0278\3\2\2\2\u027c\u027f\3\2\2"+
		"\2\u027d\u027b\3\2\2\2\u027d\u027e\3\2\2\2\u027e\u0089\3\2\2\2\u027f\u027d"+
		"\3\2\2\2\u0280\u0281\t\b\2\2\u0281\u008b\3\2\2\2\u0282\u0288\5\u0090I"+
		"\2\u0283\u0284\5\u008eH\2\u0284\u0285\5\u0090I\2\u0285\u0287\3\2\2\2\u0286"+
		"\u0283\3\2\2\2\u0287\u028a\3\2\2\2\u0288\u0286\3\2\2\2\u0288\u0289\3\2"+
		"\2\2\u0289\u008d\3\2\2\2\u028a\u0288\3\2\2\2\u028b\u028c\t\13\2\2\u028c"+
		"\u008f\3\2\2\2\u028d\u028e\5\u0092J\2\u028e\u028f\5\u0090I\2\u028f\u0292"+
		"\3\2\2\2\u0290\u0292\5\u0094K\2\u0291\u028d\3\2\2\2\u0291\u0290\3\2\2"+
		"\2\u0292\u0091\3\2\2\2\u0293\u0294\t\b\2\2\u0294\u0093\3\2\2\2\u0295\u0296"+
		"\5\u00a0Q\2\u0296\u0297\5\u0090I\2\u0297\u029a\3\2\2\2\u0298\u029a\5\u0096"+
		"L\2\u0299\u0295\3\2\2\2\u0299\u0298\3\2\2\2\u029a\u0095\3\2\2\2\u029b"+
		"\u02a1\5r:\2\u029c\u02a1\5\u0098M\2\u029d\u02a1\5\u009aN\2\u029e\u02a1"+
		"\5\26\f\2\u029f\u02a1\5`\61\2\u02a0\u029b\3\2\2\2\u02a0\u029c\3\2\2\2"+
		"\u02a0\u029d\3\2\2\2\u02a0\u029e\3\2\2\2\u02a0\u029f\3\2\2\2\u02a1\u0097"+
		"\3\2\2\2\u02a2\u02a4\7\66\2\2\u02a3\u02a5\5l\67\2\u02a4\u02a3\3\2\2\2"+
		"\u02a4\u02a5\3\2\2\2\u02a5\u0099\3\2\2\2\u02a6\u02a7\t\f\2\2\u02a7\u009b"+
		"\3\2\2\2\u02a8\u02a9\t\r\2\2\u02a9\u009d\3\2\2\2\u02aa\u02ab\t\16\2\2"+
		"\u02ab\u009f\3\2\2\2\u02ac\u02ad\t\17\2\2\u02ad\u00a1\3\2\2\2\u02ae\u02af"+
		"\7)\2\2\u02af\u02b4\5\u00a4S\2\u02b0\u02b1\7A\2\2\u02b1\u02b3\5\u00a4"+
		"S\2\u02b2\u02b0\3\2\2\2\u02b3\u02b6\3\2\2\2\u02b4\u02b2\3\2\2\2\u02b4"+
		"\u02b5\3\2\2\2\u02b5\u02b7\3\2\2\2\u02b6\u02b4\3\2\2\2\u02b7\u02b8\7@"+
		"\2\2\u02b8\u00a3\3\2\2\2\u02b9\u02ba\5\u0098M\2\u02ba\u02bb\7E\2\2\u02bb"+
		"\u02bc\5x=\2\u02bc\u00a5\3\2\2\2\u02bd\u02be\7*\2\2\u02be\u02bf\5j\66"+
		"\2\u02bf\u02c0\7E\2\2\u02c0\u02c1\5x=\2\u02c1\u02c2\7@\2\2\u02c2\u00a7"+
		"\3\2\2\2\u02c3\u02c4\7\4\2\2\u02c4\u02c5\7+\2\2\u02c5\u02ca\5j\66\2\u02c6"+
		"\u02c7\7A\2\2\u02c7\u02c9\5j\66\2\u02c8\u02c6\3\2\2\2\u02c9\u02cc\3\2"+
		"\2\2\u02ca\u02c8\3\2\2\2\u02ca\u02cb\3\2\2\2\u02cb\u02cd\3\2\2\2\u02cc"+
		"\u02ca\3\2\2\2\u02cd\u02ce\7@\2\2\u02ce\u00a9\3\2\2\2\u02cf\u02d0\7+\2"+
		"\2\u02d0\u02d1\5\u00aeX\2\u02d1\u02d2\7E\2\2\u02d2\u02d3\5\u00acW\2\u02d3"+
		"\u02d4\7@\2\2\u02d4\u00ab\3\2\2\2\u02d5\u02d6\5x=\2\u02d6\u00ad\3\2\2"+
		"\2\u02d7\u02dc\5\u00b0Y\2\u02d8\u02d9\7Q\2\2\u02d9\u02db\5\u00b0Y\2\u02da"+
		"\u02d8\3\2\2\2\u02db\u02de\3\2\2\2\u02dc\u02da\3\2\2\2\u02dc\u02dd\3\2"+
		"\2\2\u02dd\u00af\3\2\2\2\u02de\u02dc\3\2\2\2\u02df\u02e0\5j\66\2\u02e0"+
		"\u02e1\7S\2\2\u02e1\u02e2\5\u00b2Z\2\u02e2\u00b1\3\2\2\2\u02e3\u02e7\5"+
		"\u0096L\2\u02e4\u02e7\5r:\2\u02e5\u02e7\5\u008cG\2\u02e6\u02e3\3\2\2\2"+
		"\u02e6\u02e4\3\2\2\2\u02e6\u02e5\3\2\2\2\u02e7\u00b3\3\2\2\2\u02e8\u02e9"+
		"\7,\2\2\u02e9\u02ea\7E\2\2\u02ea\u02eb\5x=\2\u02eb\u02ec\7@\2\2\u02ec"+
		"\u00b5\3\2\2\2\u02ed\u02ee\7-\2\2\u02ee\u02ef\7E\2\2\u02ef\u02f0\5x=\2"+
		"\u02f0\u02f1\7@\2\2\u02f1\u00b7\3\2\2\2\u02f2\u02f3\7\6\2\2\u02f3\u02f4"+
		"\7E\2\2\u02f4\u02f5\5x=\2\u02f5\u02f6\7@\2\2\u02f6\u00b9\3\2\2\2\u02f7"+
		"\u02f9\7.\2\2\u02f8\u02fa\5j\66\2\u02f9\u02f8\3\2\2\2\u02fa\u02fb\3\2"+
		"\2\2\u02fb\u02f9\3\2\2\2\u02fb\u02fc\3\2\2\2\u02fc\u02fd\3\2\2\2\u02fd"+
		"\u02fe\7@\2\2\u02fe\u00bb\3\2\2\2=\u00bf\u00d2\u00da\u00e4\u00e9\u00f8"+
		"\u00fd\u0101\u0109\u0113\u011c\u0129\u0132\u013e\u0159\u016e\u0173\u017c"+
		"\u0184\u018b\u0198\u01a0\u01ad\u01b7\u01bf\u01c7\u01c9\u01d5\u01dd\u01df"+
		"\u01ea\u01f5\u01fb\u0202\u0211\u021e\u0223\u0228\u022a\u022e\u0232\u0238"+
		"\u023d\u0241\u0255\u025e\u0267\u0272\u027d\u0288\u0291\u0299\u02a0\u02a4"+
		"\u02b4\u02ca\u02dc\u02e6\u02fb";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}