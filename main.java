/*
By: @cvinicius369
Project: Antivirus Prototype
Lang: Java
Obs: Aberto a sugestões de melhorias e auxilios
*/

//Imports
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnhancedAntivirus {

    // Lista de assinaturas de vírus (exemplo simples)
    private static final String[] VIRUS_SIGNATURES = {
        "VIRUS_SIGNATURE_1",
        "VIRUS_SIGNATURE_2",
        "malicious_code",
    };

    private static final String SIGNATURES_LOG_FILE = "detected_signatures.txt";
    private static final String EVENTS_LOG_FILE = "events.log";

    public static void main(String[] args) {
        String directoryPath = "C:/path/to/scan"; // Definir o caminho

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            logEvent("Tipo: Erro", "Status Code: 404", "Diretório inválido.");
            return;
        }

        // Escaneia arquivos no diretório
        scanDirectory(directory);
    }

    private static void scanDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursão em subdiretórios
                    scanDirectory(file);
                } else {
                    // Verifica arquivos
                    checkFile(file);
                }
            }
        }
    }

    private static void checkFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean virusDetected = false;

            while ((line = br.readLine()) != null) {
                for (String signature : VIRUS_SIGNATURES) {
                    if (line.contains(signature)) {
                        virusDetected = true;
                        String logMessage = "Possível infecção detectada em: " + file.getAbsolutePath() +
                                            " | Assinatura encontrada: " + signature;
                        logEvent("Tipo: Detecção", "Status Code: 200", logMessage);
                        logDetectedSignature(signature);
                    }
                }
            }

            if (!virusDetected) {
                logEvent("Tipo: Verificação", "Status Code: 204", "Nenhuma infecção detectada em: " + file.getAbsolutePath());
            }

        } catch (IOException e) {
            logEvent("Tipo: Erro", "Status Code: 500", "Erro ao ler o arquivo: " + file.getAbsolutePath());
        }
    }

    private static void logDetectedSignature(String signature) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SIGNATURES_LOG_FILE, true))) {
            writer.write(signature);
            writer.newLine();
        } catch (IOException e) {
            logEvent("Tipo: Erro", "Status Code: 500", "Erro ao registrar assinatura detectada.");
        }
    }

    private static void logEvent(String eventType, String statusCode, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EVENTS_LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("Data e Hora: " + timestamp + " | " + eventType + " | " + statusCode + " | " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao registrar evento: " + e.getMessage());
        }
    }
}
