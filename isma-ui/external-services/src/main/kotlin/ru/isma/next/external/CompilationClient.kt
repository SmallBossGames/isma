package ru.isma.next.external

import ru.isma.next.external.dtos.CompileResult
import ru.isma.next.external.dtos.CompilationErrorDto
import ru.isma.next.external.dtos.SyntaxTokenDto
import ru.isma.next.external.dtos.SyntaxTokenKind
import ru.isma.next.external.dtos.ValidationResult
import ru.nstu.isma.contracts.v1.compiler_service.CompileRequest
import ru.nstu.isma.contracts.v1.compiler_service.DeleteCompiledModelRequest
import ru.nstu.isma.contracts.v1.compiler_service.HighlightRequest
import ru.nstu.isma.contracts.v1.compiler_service.TokenKind
import ru.nstu.isma.contracts.v1.compiler_service.ValidateRequest

class CompilationClient(
    private val compilerClient: GrpcLismaCompilerClient,
) {
    fun compile(lismaSourceCode: String): CompileResult {
        val request = CompileRequest.newBuilder()
            .setLismaSourceCode(lismaSourceCode)
            .build()
        val response = compilerClient.blockingStub.compile(request)
        return CompileResult(
            modelId = response.compiledModelId,
            errors = response.errorsList.map { toDto(it) },
            warnings = response.warningsList,
        )
    }

    fun validate(lismaSourceCode: String): ValidationResult {
        val request = ValidateRequest.newBuilder()
            .setLismaSourceCode(lismaSourceCode)
            .build()
        val response = compilerClient.blockingStub.validate(request)
        return ValidationResult(
            errors = response.errorsList.map { toDto(it) },
            warnings = response.warningsList,
        )
    }

    fun highlight(lismaSourceCode: String): List<SyntaxTokenDto> {
        val request = HighlightRequest.newBuilder()
            .setSourceCode(lismaSourceCode)
            .build()
        val response = compilerClient.blockingStub.highlight(request)
        return response.tokensList.map { token ->
            SyntaxTokenDto(
                start = token.start,
                length = token.length,
                kind = when (token.kind) {
                    TokenKind.TOKEN_KIND_KEYWORD -> SyntaxTokenKind.KEYWORD
                    TokenKind.TOKEN_KIND_COMMENT -> SyntaxTokenKind.COMMENT
                    TokenKind.TOKEN_KIND_NUMBER -> SyntaxTokenKind.NUMBER
                    else -> SyntaxTokenKind.TEXT
                },
            )
        }
    }

    fun deleteModel(modelId: String): Boolean {
        val request = DeleteCompiledModelRequest.newBuilder()
            .setCompiledModelId(modelId)
            .build()
        return compilerClient.blockingStub.delete(request).success
    }

    private fun toDto(error: ru.nstu.isma.contracts.v1.compiler_service.CompilationError) =
        CompilationErrorDto(
            row = error.row,
            column = error.column,
            message = error.message,
        )
}
