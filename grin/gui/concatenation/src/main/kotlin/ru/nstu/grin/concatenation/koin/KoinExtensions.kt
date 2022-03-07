package ru.nstu.grin.concatenation.koin

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import tornadofx.Controller

class MainGrinScope : KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}

class MainGrinScopeWrapper(val koinScope: MainGrinScope): Controller()

class FunctionChangeModalScope: KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}

class AxisChangeModalScope: KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}

class FunctionCopyModalScope: KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}

class DescriptionChangeModalScope: KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}

class CartesianCopyModalScope: KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}

class CartesianChangeModalScope: KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}

