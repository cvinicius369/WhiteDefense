/*
By: @cvinicius369
Project: Antivirus Prototype
Lang: Java
Obs: Aberto a sugestões de melhorias e auxilios
*/

//Imports
import java.io.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\temp\\arquivoexemplo.txt"; //Irá selecionar o arquivo com suposto malware
        String maliciousString = "stringmalware"; //Irá fazer a varredura pela string malware

        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            try {
                String fileContent = new String(Files.readAllBytes(path));
                if (fileContent.contains(maliciousString)) {
                    System.out.println("Risco de Malware!.");
                } else {
                    System.out.println("Aparentemente Seguro.");
                }
            } catch (IOException e) {
                System.out.println("Ocorreu um erro ao ler o arquivo.");
            }
        } else {
            System.out.println("O arquivo não existe.");
        }
    }
}
