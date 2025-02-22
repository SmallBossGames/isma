package ru.nstu.isma.intg.server.security;

import java.security.Permission;

public class SecurityManagerWithAllPermissions extends SecurityManager {
	@Override
	public void checkPermission(Permission perm) {
	}

	@Override
	public void checkPermission(Permission perm, Object context) {
	}
}
