set JAVA_HOME=tools\jdk
set MPJ_HOME=tools\mpj-v0_43
set PATH=%JAVA_HOME%\bin;%MPJ_HOME%\bin;%PATH%

call mpjrun.bat -np 2 -Djava.security.policy=no.policy bin\isma-intg-server.jar