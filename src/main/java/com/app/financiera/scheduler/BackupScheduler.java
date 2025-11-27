package com.app.financiera.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BackupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BackupScheduler.class);

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${backup.directory}")
    private String backupDirectory;

    @Value("${backup.pg_dump.path}")
    private String pgDumpPath;

    @Value("${backup.db.host}")
    private String dbHost;

    @Value("${backup.db.port}")
    private String dbPort;

    @Value("${backup.db.name}")
    private String dbName;

    // Se ejecuta todos los días a las 00:00:00
    //@Scheduled(cron = "*/30 * * * * *")
    @Scheduled(cron = "0 0 0 * * *")
    public void realizarBackup() {

        logger.info("=== INICIANDO JOB DE BACKUP DE BASE DE DATOS ===");

        try {
            // Crear carpeta si no existe
            File folder = new File(backupDirectory);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Fecha con formato
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("backup_financiera_%s.sql", timestamp);
            File outputFile = new File(folder, fileName);

            // Construir comando pg_dump
            List<String> command = new ArrayList<>();
            command.add(pgDumpPath); // RUTA DIRECTA
            command.add("-h");
            command.add(dbHost);
            command.add("-p");
            command.add(dbPort);
            command.add("-U");
            command.add(dbUser);
            command.add("-d");
            command.add(dbName);
            command.add("-F");
            command.add("p");
            command.add("-b");
            command.add("-v");
            command.add("-f");
            command.add(outputFile.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(command);

            // Pasar contraseña al proceso
            Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", dbPassword);

            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            logger.info("Ejecutando backup en archivo: {}", outputFile.getAbsolutePath());

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info("BACKUP EXITOSO: {}", fileName);
            } else {
                logger.error("ERROR AL REALIZAR BACKUP. Código: {}", exitCode);
            }

        } catch (Exception e) {
            logger.error("Error crítico al ejecutar backup: {}", e.getMessage(), e);
        }

        logger.info("=== JOB DE BACKUP FINALIZADO ===");
    }
}
