// Generated from Lisma.g4 by ANTLR 4.1
package ru.nstu.isma.in.lisma.analysis.gen;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LismaParser}.
 */
public interface LismaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LismaParser#macro_item}.
	 * @param ctx the parse tree
	 */
	void enterMacro_item(@NotNull LismaParser.Macro_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#macro_item}.
	 * @param ctx the parse tree
	 */
	void exitMacro_item(@NotNull LismaParser.Macro_itemContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(@NotNull LismaParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(@NotNull LismaParser.ConstantContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_spatial_var_code}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_spatial_var_code(@NotNull LismaParser.Partial_operand_spatial_var_codeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_spatial_var_code}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_spatial_var_code(@NotNull LismaParser.Partial_operand_spatial_var_codeContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#unaryExpressionOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionOperator(@NotNull LismaParser.UnaryExpressionOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#unaryExpressionOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionOperator(@NotNull LismaParser.UnaryExpressionOperatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#lisma}.
	 * @param ctx the parse tree
	 */
	void enterLisma(@NotNull LismaParser.LismaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#lisma}.
	 * @param ctx the parse tree
	 */
	void exitLisma(@NotNull LismaParser.LismaContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_unknown_code}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_unknown_code(@NotNull LismaParser.Partial_operand_unknown_codeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_unknown_code}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_unknown_code(@NotNull LismaParser.Partial_operand_unknown_codeContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#linear_vars}.
	 * @param ctx the parse tree
	 */
	void enterLinear_vars(@NotNull LismaParser.Linear_varsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#linear_vars}.
	 * @param ctx the parse tree
	 */
	void exitLinear_vars(@NotNull LismaParser.Linear_varsContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#pseudo_state_else}.
	 * @param ctx the parse tree
	 */
	void enterPseudo_state_else(@NotNull LismaParser.Pseudo_state_elseContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#pseudo_state_else}.
	 * @param ctx the parse tree
	 */
	void exitPseudo_state_else(@NotNull LismaParser.Pseudo_state_elseContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#cycle_index_posfix}.
	 * @param ctx the parse tree
	 */
	void enterCycle_index_posfix(@NotNull LismaParser.Cycle_index_posfixContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#cycle_index_posfix}.
	 * @param ctx the parse tree
	 */
	void exitCycle_index_posfix(@NotNull LismaParser.Cycle_index_posfixContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#spatial_var}.
	 * @param ctx the parse tree
	 */
	void enterSpatial_var(@NotNull LismaParser.Spatial_varContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#spatial_var}.
	 * @param ctx the parse tree
	 */
	void exitSpatial_var(@NotNull LismaParser.Spatial_varContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_with_param}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_with_param(@NotNull LismaParser.Partial_operand_with_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_with_param}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_with_param(@NotNull LismaParser.Partial_operand_with_paramContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#linear_eq}.
	 * @param ctx the parse tree
	 */
	void enterLinear_eq(@NotNull LismaParser.Linear_eqContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#linear_eq}.
	 * @param ctx the parse tree
	 */
	void exitLinear_eq(@NotNull LismaParser.Linear_eqContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#init_const}.
	 * @param ctx the parse tree
	 */
	void enterInit_const(@NotNull LismaParser.Init_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#init_const}.
	 * @param ctx the parse tree
	 */
	void exitInit_const(@NotNull LismaParser.Init_constContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#spatial_var_tail}.
	 * @param ctx the parse tree
	 */
	void enterSpatial_var_tail(@NotNull LismaParser.Spatial_var_tailContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#spatial_var_tail}.
	 * @param ctx the parse tree
	 */
	void exitSpatial_var_tail(@NotNull LismaParser.Spatial_var_tailContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#pde_param}.
	 * @param ctx the parse tree
	 */
	void enterPde_param(@NotNull LismaParser.Pde_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#pde_param}.
	 * @param ctx the parse tree
	 */
	void exitPde_param(@NotNull LismaParser.Pde_paramContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#edge}.
	 * @param ctx the parse tree
	 */
	void enterEdge(@NotNull LismaParser.EdgeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#edge}.
	 * @param ctx the parse tree
	 */
	void exitEdge(@NotNull LismaParser.EdgeContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#parExpressionRightPar}.
	 * @param ctx the parse tree
	 */
	void enterParExpressionRightPar(@NotNull LismaParser.ParExpressionRightParContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#parExpressionRightPar}.
	 * @param ctx the parse tree
	 */
	void exitParExpressionRightPar(@NotNull LismaParser.ParExpressionRightParContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalOrExpression(@NotNull LismaParser.ConditionalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalOrExpression(@NotNull LismaParser.ConditionalOrExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#state_name}.
	 * @param ctx the parse tree
	 */
	void enterState_name(@NotNull LismaParser.State_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#state_name}.
	 * @param ctx the parse tree
	 */
	void exitState_name(@NotNull LismaParser.State_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_func_spatial}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_func_spatial(@NotNull LismaParser.Partial_operand_func_spatialContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_func_spatial}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_func_spatial(@NotNull LismaParser.Partial_operand_func_spatialContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#derivative_ident}.
	 * @param ctx the parse tree
	 */
	void enterDerivative_ident(@NotNull LismaParser.Derivative_identContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#derivative_ident}.
	 * @param ctx the parse tree
	 */
	void exitDerivative_ident(@NotNull LismaParser.Derivative_identContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#state}.
	 * @param ctx the parse tree
	 */
	void enterState(@NotNull LismaParser.StateContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#state}.
	 * @param ctx the parse tree
	 */
	void exitState(@NotNull LismaParser.StateContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(@NotNull LismaParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(@NotNull LismaParser.UnaryExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#ode_equation}.
	 * @param ctx the parse tree
	 */
	void enterOde_equation(@NotNull LismaParser.Ode_equationContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#ode_equation}.
	 * @param ctx the parse tree
	 */
	void exitOde_equation(@NotNull LismaParser.Ode_equationContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#cycle_index_idx}.
	 * @param ctx the parse tree
	 */
	void enterCycle_index_idx(@NotNull LismaParser.Cycle_index_idxContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#cycle_index_idx}.
	 * @param ctx the parse tree
	 */
	void exitCycle_index_idx(@NotNull LismaParser.Cycle_index_idxContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_spatial_N}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_spatial_N(@NotNull LismaParser.Partial_operand_spatial_NContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_spatial_N}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_spatial_N(@NotNull LismaParser.Partial_operand_spatial_NContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#macros}.
	 * @param ctx the parse tree
	 */
	void enterMacros(@NotNull LismaParser.MacrosContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#macros}.
	 * @param ctx the parse tree
	 */
	void exitMacros(@NotNull LismaParser.MacrosContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#func_and_math_mapping}.
	 * @param ctx the parse tree
	 */
	void enterFunc_and_math_mapping(@NotNull LismaParser.Func_and_math_mappingContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#func_and_math_mapping}.
	 * @param ctx the parse tree
	 */
	void exitFunc_and_math_mapping(@NotNull LismaParser.Func_and_math_mappingContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#init_cond_body}.
	 * @param ctx the parse tree
	 */
	void enterInit_cond_body(@NotNull LismaParser.Init_cond_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#init_cond_body}.
	 * @param ctx the parse tree
	 */
	void exitInit_cond_body(@NotNull LismaParser.Init_cond_bodyContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_func_spatial_4}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_func_spatial_4(@NotNull LismaParser.Partial_operand_func_spatial_4Context ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_func_spatial_4}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_func_spatial_4(@NotNull LismaParser.Partial_operand_func_spatial_4Context ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_func_spatial_2}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_func_spatial_2(@NotNull LismaParser.Partial_operand_func_spatial_2Context ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_func_spatial_2}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_func_spatial_2(@NotNull LismaParser.Partial_operand_func_spatial_2Context ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_func_spatial_common}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_func_spatial_common(@NotNull LismaParser.Partial_operand_func_spatial_commonContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_func_spatial_common}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_func_spatial_common(@NotNull LismaParser.Partial_operand_func_spatial_commonContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_func_spatial_3}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_func_spatial_3(@NotNull LismaParser.Partial_operand_func_spatial_3Context ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_func_spatial_3}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_func_spatial_3(@NotNull LismaParser.Partial_operand_func_spatial_3Context ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand(@NotNull LismaParser.Partial_operandContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand(@NotNull LismaParser.Partial_operandContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_mixed}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_mixed(@NotNull LismaParser.Partial_operand_mixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_mixed}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_mixed(@NotNull LismaParser.Partial_operand_mixedContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#var_ident}.
	 * @param ctx the parse tree
	 */
	void enterVar_ident(@NotNull LismaParser.Var_identContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#var_ident}.
	 * @param ctx the parse tree
	 */
	void exitVar_ident(@NotNull LismaParser.Var_identContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_common}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_common(@NotNull LismaParser.Partial_operand_commonContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_common}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_common(@NotNull LismaParser.Partial_operand_commonContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#cycle_index}.
	 * @param ctx the parse tree
	 */
	void enterCycle_index(@NotNull LismaParser.Cycle_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#cycle_index}.
	 * @param ctx the parse tree
	 */
	void exitCycle_index(@NotNull LismaParser.Cycle_indexContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_spatial_common}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_spatial_common(@NotNull LismaParser.Partial_operand_spatial_commonContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_spatial_common}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_spatial_common(@NotNull LismaParser.Partial_operand_spatial_commonContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#relationalOp}.
	 * @param ctx the parse tree
	 */
	void enterRelationalOp(@NotNull LismaParser.RelationalOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#relationalOp}.
	 * @param ctx the parse tree
	 */
	void exitRelationalOp(@NotNull LismaParser.RelationalOpContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(@NotNull LismaParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(@NotNull LismaParser.EqualityExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#edge_eq}.
	 * @param ctx the parse tree
	 */
	void enterEdge_eq(@NotNull LismaParser.Edge_eqContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#edge_eq}.
	 * @param ctx the parse tree
	 */
	void exitEdge_eq(@NotNull LismaParser.Edge_eqContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_D}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_D(@NotNull LismaParser.Partial_operand_DContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_D}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_D(@NotNull LismaParser.Partial_operand_DContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#multiplicativeExpressionOperator}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpressionOperator(@NotNull LismaParser.MultiplicativeExpressionOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#multiplicativeExpressionOperator}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpressionOperator(@NotNull LismaParser.MultiplicativeExpressionOperatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#linear_eq_A_elem}.
	 * @param ctx the parse tree
	 */
	void enterLinear_eq_A_elem(@NotNull LismaParser.Linear_eq_A_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#linear_eq_A_elem}.
	 * @param ctx the parse tree
	 */
	void exitLinear_eq_A_elem(@NotNull LismaParser.Linear_eq_A_elemContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#equalityExpressionOperator}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpressionOperator(@NotNull LismaParser.EqualityExpressionOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#equalityExpressionOperator}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpressionOperator(@NotNull LismaParser.EqualityExpressionOperatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(@NotNull LismaParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(@NotNull LismaParser.PrimaryContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#linear_eq_A}.
	 * @param ctx the parse tree
	 */
	void enterLinear_eq_A(@NotNull LismaParser.Linear_eq_AContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#linear_eq_A}.
	 * @param ctx the parse tree
	 */
	void exitLinear_eq_A(@NotNull LismaParser.Linear_eq_AContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#pde_equation}.
	 * @param ctx the parse tree
	 */
	void enterPde_equation(@NotNull LismaParser.Pde_equationContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#pde_equation}.
	 * @param ctx the parse tree
	 */
	void exitPde_equation(@NotNull LismaParser.Pde_equationContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#for_cycle}.
	 * @param ctx the parse tree
	 */
	void enterFor_cycle(@NotNull LismaParser.For_cycleContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#for_cycle}.
	 * @param ctx the parse tree
	 */
	void exitFor_cycle(@NotNull LismaParser.For_cycleContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(@NotNull LismaParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(@NotNull LismaParser.AdditiveExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#not_operator}.
	 * @param ctx the parse tree
	 */
	void enterNot_operator(@NotNull LismaParser.Not_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#not_operator}.
	 * @param ctx the parse tree
	 */
	void exitNot_operator(@NotNull LismaParser.Not_operatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(@NotNull LismaParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(@NotNull LismaParser.RelationalExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#constant_body}.
	 * @param ctx the parse tree
	 */
	void enterConstant_body(@NotNull LismaParser.Constant_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#constant_body}.
	 * @param ctx the parse tree
	 */
	void exitConstant_body(@NotNull LismaParser.Constant_bodyContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#additiveExpressionOperator}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpressionOperator(@NotNull LismaParser.AdditiveExpressionOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#additiveExpressionOperator}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpressionOperator(@NotNull LismaParser.AdditiveExpressionOperatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#parExpressionLeftPar}.
	 * @param ctx the parse tree
	 */
	void enterParExpressionLeftPar(@NotNull LismaParser.ParExpressionLeftParContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#parExpressionLeftPar}.
	 * @param ctx the parse tree
	 */
	void exitParExpressionLeftPar(@NotNull LismaParser.ParExpressionLeftParContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#spatial_var_bound}.
	 * @param ctx the parse tree
	 */
	void enterSpatial_var_bound(@NotNull LismaParser.Spatial_var_boundContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#spatial_var_bound}.
	 * @param ctx the parse tree
	 */
	void exitSpatial_var_bound(@NotNull LismaParser.Spatial_var_boundContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#for_cycle_body}.
	 * @param ctx the parse tree
	 */
	void enterFor_cycle_body(@NotNull LismaParser.For_cycle_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#for_cycle_body}.
	 * @param ctx the parse tree
	 */
	void exitFor_cycle_body(@NotNull LismaParser.For_cycle_bodyContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull LismaParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull LismaParser.LiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#out}.
	 * @param ctx the parse tree
	 */
	void enterOut(@NotNull LismaParser.OutContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#out}.
	 * @param ctx the parse tree
	 */
	void exitOut(@NotNull LismaParser.OutContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#linear_eq_A_elem_expr}.
	 * @param ctx the parse tree
	 */
	void enterLinear_eq_A_elem_expr(@NotNull LismaParser.Linear_eq_A_elem_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#linear_eq_A_elem_expr}.
	 * @param ctx the parse tree
	 */
	void exitLinear_eq_A_elem_expr(@NotNull LismaParser.Linear_eq_A_elem_exprContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionNotPlusMinus(@NotNull LismaParser.UnaryExpressionNotPlusMinusContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionNotPlusMinus(@NotNull LismaParser.UnaryExpressionNotPlusMinusContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#spatial_var_tail_APX}.
	 * @param ctx the parse tree
	 */
	void enterSpatial_var_tail_APX(@NotNull LismaParser.Spatial_var_tail_APXContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#spatial_var_tail_APX}.
	 * @param ctx the parse tree
	 */
	void exitSpatial_var_tail_APX(@NotNull LismaParser.Spatial_var_tail_APXContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(@NotNull LismaParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(@NotNull LismaParser.StatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#and_operator}.
	 * @param ctx the parse tree
	 */
	void enterAnd_operator(@NotNull LismaParser.And_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#and_operator}.
	 * @param ctx the parse tree
	 */
	void exitAnd_operator(@NotNull LismaParser.And_operatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#pde_equation_param}.
	 * @param ctx the parse tree
	 */
	void enterPde_equation_param(@NotNull LismaParser.Pde_equation_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#pde_equation_param}.
	 * @param ctx the parse tree
	 */
	void exitPde_equation_param(@NotNull LismaParser.Pde_equation_paramContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#end}.
	 * @param ctx the parse tree
	 */
	void enterEnd(@NotNull LismaParser.EndContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#end}.
	 * @param ctx the parse tree
	 */
	void exitEnd(@NotNull LismaParser.EndContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#for_cycle_interval}.
	 * @param ctx the parse tree
	 */
	void enterFor_cycle_interval(@NotNull LismaParser.For_cycle_intervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#for_cycle_interval}.
	 * @param ctx the parse tree
	 */
	void exitFor_cycle_interval(@NotNull LismaParser.For_cycle_intervalContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#pseudo_state_elem}.
	 * @param ctx the parse tree
	 */
	void enterPseudo_state_elem(@NotNull LismaParser.Pseudo_state_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#pseudo_state_elem}.
	 * @param ctx the parse tree
	 */
	void exitPseudo_state_elem(@NotNull LismaParser.Pseudo_state_elemContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void enterParExpression(@NotNull LismaParser.ParExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void exitParExpression(@NotNull LismaParser.ParExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#derivative_quote_operant}.
	 * @param ctx the parse tree
	 */
	void enterDerivative_quote_operant(@NotNull LismaParser.Derivative_quote_operantContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#derivative_quote_operant}.
	 * @param ctx the parse tree
	 */
	void exitDerivative_quote_operant(@NotNull LismaParser.Derivative_quote_operantContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#linear_eq_b}.
	 * @param ctx the parse tree
	 */
	void enterLinear_eq_b(@NotNull LismaParser.Linear_eq_bContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#linear_eq_b}.
	 * @param ctx the parse tree
	 */
	void exitLinear_eq_b(@NotNull LismaParser.Linear_eq_bContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull LismaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull LismaParser.ExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(@NotNull LismaParser.ConditionalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(@NotNull LismaParser.ConditionalExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#equation}.
	 * @param ctx the parse tree
	 */
	void enterEquation(@NotNull LismaParser.EquationContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#equation}.
	 * @param ctx the parse tree
	 */
	void exitEquation(@NotNull LismaParser.EquationContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(@NotNull LismaParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(@NotNull LismaParser.StartContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalAndExpression(@NotNull LismaParser.ConditionalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalAndExpression(@NotNull LismaParser.ConditionalAndExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(@NotNull LismaParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(@NotNull LismaParser.MultiplicativeExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#init_cond}.
	 * @param ctx the parse tree
	 */
	void enterInit_cond(@NotNull LismaParser.Init_condContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#init_cond}.
	 * @param ctx the parse tree
	 */
	void exitInit_cond(@NotNull LismaParser.Init_condContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#or_operator}.
	 * @param ctx the parse tree
	 */
	void enterOr_operator(@NotNull LismaParser.Or_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#or_operator}.
	 * @param ctx the parse tree
	 */
	void exitOr_operator(@NotNull LismaParser.Or_operatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#arg_list}.
	 * @param ctx the parse tree
	 */
	void enterArg_list(@NotNull LismaParser.Arg_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#arg_list}.
	 * @param ctx the parse tree
	 */
	void exitArg_list(@NotNull LismaParser.Arg_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#partial_operand_mixed_D}.
	 * @param ctx the parse tree
	 */
	void enterPartial_operand_mixed_D(@NotNull LismaParser.Partial_operand_mixed_DContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#partial_operand_mixed_D}.
	 * @param ctx the parse tree
	 */
	void exitPartial_operand_mixed_D(@NotNull LismaParser.Partial_operand_mixed_DContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#state_from}.
	 * @param ctx the parse tree
	 */
	void enterState_from(@NotNull LismaParser.State_fromContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#state_from}.
	 * @param ctx the parse tree
	 */
	void exitState_from(@NotNull LismaParser.State_fromContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#pseudo_state_body}.
	 * @param ctx the parse tree
	 */
	void enterPseudo_state_body(@NotNull LismaParser.Pseudo_state_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#pseudo_state_body}.
	 * @param ctx the parse tree
	 */
	void exitPseudo_state_body(@NotNull LismaParser.Pseudo_state_bodyContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#spatial_var_tail_STEP}.
	 * @param ctx the parse tree
	 */
	void enterSpatial_var_tail_STEP(@NotNull LismaParser.Spatial_var_tail_STEPContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#spatial_var_tail_STEP}.
	 * @param ctx the parse tree
	 */
	void exitSpatial_var_tail_STEP(@NotNull LismaParser.Spatial_var_tail_STEPContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#pseudo_state}.
	 * @param ctx the parse tree
	 */
	void enterPseudo_state(@NotNull LismaParser.Pseudo_stateContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#pseudo_state}.
	 * @param ctx the parse tree
	 */
	void exitPseudo_state(@NotNull LismaParser.Pseudo_stateContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#pde_param_atom}.
	 * @param ctx the parse tree
	 */
	void enterPde_param_atom(@NotNull LismaParser.Pde_param_atomContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#pde_param_atom}.
	 * @param ctx the parse tree
	 */
	void exitPde_param_atom(@NotNull LismaParser.Pde_param_atomContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#state_body}.
	 * @param ctx the parse tree
	 */
	void enterState_body(@NotNull LismaParser.State_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#state_body}.
	 * @param ctx the parse tree
	 */
	void exitState_body(@NotNull LismaParser.State_bodyContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#edge_side}.
	 * @param ctx the parse tree
	 */
	void enterEdge_side(@NotNull LismaParser.Edge_sideContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#edge_side}.
	 * @param ctx the parse tree
	 */
	void exitEdge_side(@NotNull LismaParser.Edge_sideContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(@NotNull LismaParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(@NotNull LismaParser.VariableContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#primary_id}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_id(@NotNull LismaParser.Primary_idContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#primary_id}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_id(@NotNull LismaParser.Primary_idContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#step}.
	 * @param ctx the parse tree
	 */
	void enterStep(@NotNull LismaParser.StepContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#step}.
	 * @param ctx the parse tree
	 */
	void exitStep(@NotNull LismaParser.StepContext ctx);

	/**
	 * Enter a parse tree produced by {@link LismaParser#setter}.
	 * @param ctx the parse tree
	 */
	void enterSetter(@NotNull LismaParser.SetterContext ctx);
	/**
	 * Exit a parse tree produced by {@link LismaParser#setter}.
	 * @param ctx the parse tree
	 */
	void exitSetter(@NotNull LismaParser.SetterContext ctx);
}