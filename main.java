/*
By: @cvinicius369
Project: Antivirus Prototype
Lang: Java
Obs: Aberto a sugestões de melhorias e auxilios
*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    private static final HashSet<String> VIRUS_SIGNATURES = new HashSet<>();
    static {
        VIRUS_SIGNATURES.add("ILOVEYOU");
        VIRUS_SIGNATURES.add("Melissa");
        VIRUS_SIGNATURES.add("WannaCry");
        VIRUS_SIGNATURES.add("MyDoom");
        VIRUS_SIGNATURES.add("Zeus");
        VIRUS_SIGNATURES.add("Conficker");
        VIRUS_SIGNATURES.add("CryptoLocker");
        VIRUS_SIGNATURES.add("Petya");
        VIRUS_SIGNATURES.add("Emotet");
        VIRUS_SIGNATURES.add("Stuxnet");
        VIRUS_SIGNATURES.add("fireball");
    }
    private static final String SIGNATURES_LOG_FILE = "detected_signatures.txt";
    private static final String EVENTS_LOG_FILE = "events.log";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Digite o caminho completo do diretório para escanear: ");
        String directoryPath = scan.nextLine();

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) { logEvent("Tipo: Erro", "Status Code: 404", "Diretório inválido."); return; }
        scanDirectory(directory, scan);
        scan.close(); 
        logEvent("Tipo: Conclusão", "Status Code: 200", "Escaneamento do diretório '" + directory.getAbsolutePath() + "' concluído com sucesso.");
    }
    private static void scanDirectory(File directory, Scanner scan) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) { scanDirectory(file, scan); } 
                else { checkFile(file, scan); }
            }
        }
    }
    private static void checkFile(File file, Scanner scan) {
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
                        System.out.println("Vírus detectado: " + signature + " no arquivo: " + file.getAbsolutePath());
                        System.out.println("Deseja deletar este arquivo? (s/n): ");
                        String resposta = scan.nextLine();
                        if (resposta.equalsIgnoreCase("s")) { deleteFile(file); } 
                        else { logEvent("Tipo: Ação Cancelada", "Status Code: 300", "Usuário optou por não deletar o arquivo: " + file.getAbsolutePath()); }
                    }
                }
            }
            if (!virusDetected) { logEvent("Tipo: Verificação", "Status Code: 204", "Nenhuma infecção detectada em: " + file.getAbsolutePath()); }
        } 
        catch (IOException e) { logEvent("Tipo: Erro", "Status Code: 500", "Erro ao ler o arquivo: " + file.getAbsolutePath()); }
    }
    private static void deleteFile(File file) {
        if (file.delete()) { logEvent("Tipo: Exclusão", "Status Code: 200", "Arquivo deletado: " + file.getAbsolutePath()); } 
        else { logEvent("Tipo: Erro", "Status Code: 500", "Falha ao deletar o arquivo: " + file.getAbsolutePath()); }
    }
    private static void logDetectedSignature(String signature) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SIGNATURES_LOG_FILE, true))) { writer.write(signature); writer.newLine(); } 
        catch (IOException e) { logEvent("Tipo: Erro", "Status Code: 500", "Erro ao registrar assinatura detectada."); }
    }
    private static void logEvent(String eventType, String statusCode, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EVENTS_LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("Data e Hora: " + timestamp + " | " + eventType + " | " + statusCode + " | " + message); writer.newLine(); 
        } 
        catch (IOException e) { System.err.println("Erro ao registrar evento: " + e.getMessage()); }
    }
}