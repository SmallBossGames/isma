// Generated from Lisma.g4 by ANTLR 4.1
package ru.nstu.isma.in.lisma.analysis.gen;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LismaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LismaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LismaParser#macro_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacro_item(@NotNull LismaParser.Macro_itemContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(@NotNull LismaParser.ConstantContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_spatial_var_code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_spatial_var_code(@NotNull LismaParser.Partial_operand_spatial_var_codeContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#unaryExpressionOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpressionOperator(@NotNull LismaParser.UnaryExpressionOperatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#lisma}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLisma(@NotNull LismaParser.LismaContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_unknown_code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_unknown_code(@NotNull LismaParser.Partial_operand_unknown_codeContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#linear_vars}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinear_vars(@NotNull LismaParser.Linear_varsContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#pseudo_state_else}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudo_state_else(@NotNull LismaParser.Pseudo_state_elseContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#cycle_index_posfix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCycle_index_posfix(@NotNull LismaParser.Cycle_index_posfixContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#spatial_var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpatial_var(@NotNull LismaParser.Spatial_varContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_with_param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_with_param(@NotNull LismaParser.Partial_operand_with_paramContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#linear_eq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinear_eq(@NotNull LismaParser.Linear_eqContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#init_const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInit_const(@NotNull LismaParser.Init_constContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#spatial_var_tail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpatial_var_tail(@NotNull LismaParser.Spatial_var_tailContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#pde_param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPde_param(@NotNull LismaParser.Pde_paramContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#edge}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEdge(@NotNull LismaParser.EdgeContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#parExpressionRightPar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExpressionRightPar(@NotNull LismaParser.ParExpressionRightParContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalOrExpression(@NotNull LismaParser.ConditionalOrExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#state_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitState_name(@NotNull LismaParser.State_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_func_spatial}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_func_spatial(@NotNull LismaParser.Partial_operand_func_spatialContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#derivative_ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDerivative_ident(@NotNull LismaParser.Derivative_identContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitState(@NotNull LismaParser.StateContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(@NotNull LismaParser.UnaryExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#ode_equation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOde_equation(@NotNull LismaParser.Ode_equationContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#cycle_index_idx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCycle_index_idx(@NotNull LismaParser.Cycle_index_idxContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_spatial_N}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_spatial_N(@NotNull LismaParser.Partial_operand_spatial_NContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#macros}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacros(@NotNull LismaParser.MacrosContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#func_and_math_mapping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_and_math_mapping(@NotNull LismaParser.Func_and_math_mappingContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#init_cond_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInit_cond_body(@NotNull LismaParser.Init_cond_bodyContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_func_spatial_4}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_func_spatial_4(@NotNull LismaParser.Partial_operand_func_spatial_4Context ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_func_spatial_2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_func_spatial_2(@NotNull LismaParser.Partial_operand_func_spatial_2Context ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_func_spatial_common}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_func_spatial_common(@NotNull LismaParser.Partial_operand_func_spatial_commonContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_func_spatial_3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_func_spatial_3(@NotNull LismaParser.Partial_operand_func_spatial_3Context ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand(@NotNull LismaParser.Partial_operandContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_mixed}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_mixed(@NotNull LismaParser.Partial_operand_mixedContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#var_ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_ident(@NotNull LismaParser.Var_identContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_common}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_common(@NotNull LismaParser.Partial_operand_commonContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#cycle_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCycle_index(@NotNull LismaParser.Cycle_indexContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_spatial_common}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_spatial_common(@NotNull LismaParser.Partial_operand_spatial_commonContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#relationalOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalOp(@NotNull LismaParser.RelationalOpContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(@NotNull LismaParser.EqualityExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#edge_eq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEdge_eq(@NotNull LismaParser.Edge_eqContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_D}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_D(@NotNull LismaParser.Partial_operand_DContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#multiplicativeExpressionOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpressionOperator(@NotNull LismaParser.MultiplicativeExpressionOperatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#linear_eq_A_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinear_eq_A_elem(@NotNull LismaParser.Linear_eq_A_elemContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#equalityExpressionOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpressionOperator(@NotNull LismaParser.EqualityExpressionOperatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(@NotNull LismaParser.PrimaryContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#linear_eq_A}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinear_eq_A(@NotNull LismaParser.Linear_eq_AContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#pde_equation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPde_equation(@NotNull LismaParser.Pde_equationContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#for_cycle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_cycle(@NotNull LismaParser.For_cycleContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(@NotNull LismaParser.AdditiveExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#not_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot_operator(@NotNull LismaParser.Not_operatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#relationalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpression(@NotNull LismaParser.RelationalExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#constant_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant_body(@NotNull LismaParser.Constant_bodyContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#additiveExpressionOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpressionOperator(@NotNull LismaParser.AdditiveExpressionOperatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#parExpressionLeftPar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExpressionLeftPar(@NotNull LismaParser.ParExpressionLeftParContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#spatial_var_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpatial_var_bound(@NotNull LismaParser.Spatial_var_boundContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#for_cycle_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_cycle_body(@NotNull LismaParser.For_cycle_bodyContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(@NotNull LismaParser.LiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#out}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut(@NotNull LismaParser.OutContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#linear_eq_A_elem_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinear_eq_A_elem_expr(@NotNull LismaParser.Linear_eq_A_elem_exprContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpressionNotPlusMinus(@NotNull LismaParser.UnaryExpressionNotPlusMinusContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#spatial_var_tail_APX}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpatial_var_tail_APX(@NotNull LismaParser.Spatial_var_tail_APXContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(@NotNull LismaParser.StatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#and_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_operator(@NotNull LismaParser.And_operatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#pde_equation_param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPde_equation_param(@NotNull LismaParser.Pde_equation_paramContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd(@NotNull LismaParser.EndContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#for_cycle_interval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_cycle_interval(@NotNull LismaParser.For_cycle_intervalContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#pseudo_state_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudo_state_elem(@NotNull LismaParser.Pseudo_state_elemContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#parExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExpression(@NotNull LismaParser.ParExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#derivative_quote_operant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDerivative_quote_operant(@NotNull LismaParser.Derivative_quote_operantContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#linear_eq_b}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinear_eq_b(@NotNull LismaParser.Linear_eq_bContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull LismaParser.ExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#conditionalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalExpression(@NotNull LismaParser.ConditionalExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#equation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquation(@NotNull LismaParser.EquationContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(@NotNull LismaParser.StartContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalAndExpression(@NotNull LismaParser.ConditionalAndExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(@NotNull LismaParser.MultiplicativeExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#init_cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInit_cond(@NotNull LismaParser.Init_condContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#or_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_operator(@NotNull LismaParser.Or_operatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_list(@NotNull LismaParser.Arg_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#partial_operand_mixed_D}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial_operand_mixed_D(@NotNull LismaParser.Partial_operand_mixed_DContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#state_from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitState_from(@NotNull LismaParser.State_fromContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#pseudo_state_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudo_state_body(@NotNull LismaParser.Pseudo_state_bodyContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#spatial_var_tail_STEP}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpatial_var_tail_STEP(@NotNull LismaParser.Spatial_var_tail_STEPContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#pseudo_state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudo_state(@NotNull LismaParser.Pseudo_stateContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#pde_param_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPde_param_atom(@NotNull LismaParser.Pde_param_atomContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#state_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitState_body(@NotNull LismaParser.State_bodyContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#edge_side}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEdge_side(@NotNull LismaParser.Edge_sideContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(@NotNull LismaParser.VariableContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#primary_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_id(@NotNull LismaParser.Primary_idContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#step}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStep(@NotNull LismaParser.StepContext ctx);

	/**
	 * Visit a parse tree produced by {@link LismaParser#setter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetter(@NotNull LismaParser.SetterContext ctx);
}