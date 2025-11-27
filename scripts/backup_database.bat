@echo off
:: ==========================================
:: BACKUP MANUAL - FINANCIERA
:: ==========================================

echo.
echo ==========================================
echo     BACKUP MANUAL DE BASE DE DATOS
echo ==========================================
echo.

echo Presiona ENTER para iniciar el backup...
pause > nul

:: 1. Configuración de conexión
set HOST=financiera-orlando123.b.aivencloud.com
set PORT=12545
set USER=avnadmin
set DBNAME=defaultdb
set PGPASSWORD=AVNS_nS-HOj_3aoLV3PHC18F

:: Ruta correcta de pg_dump
set PG_DUMP="C:\Program Files\PostgreSQL\18\bin\pg_dump.exe"

:: 2. Crear carpeta de backups si no existe
if not exist "backups" mkdir "backups"

:: 3. Fecha segura (AAAA MM DD)
for /f "tokens=1-4 delims=/-. " %%a in ("%date%") do (
    set DD=%%a
    set MM=%%b
    set YYYY=%%c
)

:: 4. Hora (HHMM)
set HH=%time:~0,2%
set MIN=%time:~3,2%
set HH=%HH: =0%

:: 5. Nombre final del archivo
set FILENAME=backups\backup_financiera_%YYYY%%MM%%DD%_%HH%%MIN%.sql

echo Iniciando backup...
echo Archivo: %FILENAME%
echo.

:: 6. Ejecutar pg_dump
%PG_DUMP% -h %HOST% -p %PORT% -U %USER% -d %DBNAME% -F p -b -v -f "%FILENAME%" --no-owner --no-acl

:: 7. Resultado
if %ERRORLEVEL% equ 0 (
    echo.
    echo [OK] Backup completado exitosamente.
    echo Archivo generado: %FILENAME%
) else (
    echo.
    echo [ERROR] Ocurrió un error durante el backup.
)

echo.
echo Presiona ENTER para salir...
pause > nul
exit
