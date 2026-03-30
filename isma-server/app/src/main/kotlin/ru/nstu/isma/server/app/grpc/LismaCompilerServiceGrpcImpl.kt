package ru.nstu.isma.server.app.grpc

import io.grpc.Status
import io.grpc.StatusException
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import ru.nstu.isma.contracts.simulation.*
import ru.nstu.isma.domain.handlers.compileLisma.ICompileLismaHandler
import ru.nstu.isma.domain.handlers.deleteCompiledModel.IDeleteCompiledModelHandler
import ru.nstu.isma.domain.handlers.highlightLisma.IHighlightLismaHandler
import ru.nstu.isma.domain.handlers.validateLisma.IValidateLismaHandler

class LismaCompilerServiceGrpcImpl(
    private val compileLismaHandler: ICompileLismaHandler,
    private val validateLismaHandler: IValidateLismaHandler,
    private val deleteCompiledModelHandler: IDeleteCompiledModelHandler,
    private val highlightLismaHandler: IHighlightLismaHandler,
) : LismaCompilerServiceGrpc.LismaCompilerServiceImplBase() {

    private val logger = LoggerFactory.getLogger(LismaCompilerServiceGrpcImpl::class.java)

    override fun compile(
        request: CompileRequest,
        responseObserver: StreamObserver<CompileResponse>
    ) {
        try {
            if (request.lismaSourceCode.isBlank()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription("LISMA source code is required").asException()
                )
                return
            }

            val result = compileLismaHandler.handle(request.lismaSourceCode)
            val compileErrors = result.errors.map { error ->
                CompilationError.newBuilder()
                    .setRow(error.row)
                    .setColumn(error.column)
                    .setMessage(error.message)
                    .build()
            }
            responseObserver.onNext(
                CompileResponse.newBuilder()
                    .setCompiledModelId(result.compiledModelId)
                    .addAllErrors(compileErrors)
                    .addAllWarnings(result.warnings)
                    .build()
            )
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error("compile failed", e)
            responseObserver.onError(toStatusException(e))
        }
    }

    override fun validate(
        request: ValidateRequest,
        responseObserver: StreamObserver<ValidateResponse>
    ) {
        try {
            if (request.lismaSourceCode.isBlank()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription("LISMA source code is required").asException()
                )
                return
            }

            val result = validateLismaHandler.handle(request.lismaSourceCode)
            val validationErrors = result.errors.map { error ->
                CompilationError.newBuilder()
                    .setRow(error.row)
                    .setColumn(error.column)
                    .setMessage(error.message)
                    .build()
            }
            responseObserver.onNext(
                ValidateResponse.newBuilder()
                    .addAllErrors(validationErrors)
                    .addAllWarnings(result.warnings)
                    .build()
            )
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error("validate failed", e)
            responseObserver.onError(toStatusException(e))
        }
    }

    override fun delete(
        request: DeleteCompiledModelRequest,
        responseObserver: StreamObserver<DeleteCompiledModelResponse>
    ) {
        try {
            if (request.compiledModelId.isBlank()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription("Compiled model ID is required").asException()
                )
                return
            }

            val success = deleteCompiledModelHandler.handle(request.compiledModelId)
            responseObserver.onNext(
                DeleteCompiledModelResponse.newBuilder()
                    .setSuccess(success)
                    .build()
            )
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error("delete failed for compiledModelId=${request.compiledModelId}", e)
            responseObserver.onError(toStatusException(e))
        }
    }

    override fun highlight(
        request: HighlightRequest,
        responseObserver: StreamObserver<HighlightResponse>
    ) {
        try {
            if (request.sourceCode.isBlank()) {
                responseObserver.onNext(HighlightResponse.getDefaultInstance())
                responseObserver.onCompleted()
                return
            }

            val result = highlightLismaHandler.handle(request.sourceCode)
            val syntaxTokens = result.tokens.map { token ->
                SyntaxToken.newBuilder()
                    .setStart(token.start)
                    .setLength(token.length)
                    .setKind(when (token.kind) {
                        ru.nstu.isma.domain.handlers.highlightLisma.SyntaxKind.KEYWORD -> TokenKind.KEYWORD
                        ru.nstu.isma.domain.handlers.highlightLisma.SyntaxKind.COMMENT -> TokenKind.COMMENT
                        ru.nstu.isma.domain.handlers.highlightLisma.SyntaxKind.NUMBER -> TokenKind.NUMBER
                        ru.nstu.isma.domain.handlers.highlightLisma.SyntaxKind.TEXT -> TokenKind.TEXT
                    })
                    .build()
            }
            responseObserver.onNext(
                HighlightResponse.newBuilder()
                    .addAllTokens(syntaxTokens)
                    .build()
            )
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error("highlight failed", e)
            responseObserver.onError(toStatusException(e))
        }
    }

    private fun toStatusException(e: Exception): StatusException {
        return when (e) {
            is IllegalArgumentException -> Status.NOT_FOUND.withDescription(e.message).asException()
            is IllegalStateException -> Status.FAILED_PRECONDITION.withDescription(e.message).asException()
            else -> Status.INTERNAL.withDescription(e.message).asException()
        }
    }
}
