/*
By: @cvinicius369
Project: Antivirus Prototype
Lang: Java
Obs: Aberto a sugestões de melhorias e auxilios
*/

//Imports
import javax.lang.model.util.SimpleElementVisitor14;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

class Iniciar{
    public static void Varredura(Scanner scan){

        System.out.print("Informe o caminho do arquivo: ");
        //String filePath = "C:\\temp\\arquivoexemplo.txt"; //Irá selecionar o arquivo com suposto malware
        String filePath = scan.next();
        String maliciousString = "stringmalware"; //Irá fazer a varredura pela string malware

        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            try {
                System.out.println("Escaneando . . . ");
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

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Seja bem vindo!\nDigite start para iniciar: ");
        args = scan.next();

        if (args == "start"){
            Iniciar.Varredura(scan);
        } else if (args == "sair"){
            System.out.println("Até mais . . .");
        } else {
            System.out.println("Ação inválida!");
        }
    }
}
