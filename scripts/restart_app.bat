@echo off
echo ==========================================
echo     REINICIAR SERVICIO FINANCIERA (MANUAL)
echo ==========================================
echo.

echo ¿Deseas reiniciar el servicio? (S/N)
set /p CONFIRM=

if /I "%CONFIRM%" NEQ "S" (
    echo Operación cancelada.
    pause > nul
    exit /b
)

echo.
echo ==========================================
echo   INICIANDO PROCESO DE REINICIO
echo ==========================================

:: 1. Detener proceso anterior
echo [1/3] Deteniendo procesos Java antiguos...

taskkill /F /IM java.exe 2>nul
if %ERRORLEVEL% equ 0 (
    echo       Procesos detenidos correctamente.
) else (
    echo       No se encontraron procesos Java activos.
)

:: 2. Limpiar proyecto (mvn clean)
echo.
echo [2/3] Limpiando el proyecto (mvn clean)...
cd ..
call mvnw clean
if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Falló al limpiar el proyecto.
    pause > nul
    exit /b %ERRORLEVEL%
)

:: 3. Levantar aplicación (spring-boot:run)
echo.
echo [3/3] Iniciando la aplicación...
echo       Los logs se mostrarán a continuación.
echo ==========================================
call mvnw spring-boot:run

echo.
echo Presiona ENTER para salir...
pause > nul
exit
