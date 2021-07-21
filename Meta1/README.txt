Para criar os ficheiros jar utilizar o script "generateJars.bat"(dentro da pasta com os ficheiros .java), 
correndo-o no terminal.
De forma a poder executar os ficheiros jar, executar o comando "java -jar [nome_ficheiro].jar" no terminal
na pasta que contém os ficheiros.
No caso do RMIServer [nome_ficheiro] = rmiserver
No caso do MulticastServer [nome_ficheiro] = server
No caso do MulticasClient [nome_ficheiro] = terminal
No caso da AdminConsole [nome_ficheiro] = console

----Funcionamento do programa-----

Para poder correr corretamente o programa:
-Correr o RMIServer duas vezes, de forma a criar o primário e o secundário
-Correr o MulticastServer
-Correr a AdminConsole
-Corre o número de MulticastClient (terminais de voto) que pretender