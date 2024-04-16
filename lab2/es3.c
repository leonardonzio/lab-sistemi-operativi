#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

int main(int argc, char** argv){

    if (argc < 4){
        printf("numero di argomenti errato\n");
        exit(EXIT_FAILURE);
    }

    if (strcmp(argv[1], argv[2]) == 0){
        printf("directory uguali\n");
        exit(EXIT_FAILURE);
    }

    int nFile = argc - 3;
    for (int i = 0; i < nFile; i++) {
        int indexFile = i + 3;
        int pid = fork();
        
        if (pid == 0){
            if (getpid() % 2 == 0){
                execp("cp", "cp", argv[indexFile], argv[2], (char*)NULL);
                perror("errore in eseguire cp");
            }
            else {
                execp("rm", "rm", strcat(argv[1], argv[indexFile]), (char*)NULL);
                perror("errore in eseguire rm");
            }
        }
        else if (pid < 0){
            perror("errore fork");
            return 1;
        }

    }
    
    
    for (int i = 0; i < nFile; i++) {
        int status;
        int pid = wait(&status);
        if (pid < 0){
            perror("errore pid");
            exit(EXIT_FAILURE);
        }
        if (WIFEXITED(status))
            printf("pid del figlio: %d terminato volontariamente con stato %d\n", pid, WEXITSTATUS(status));
        else{
            fprintf(stderr, "figlio %d terminato in modo anomalo per segnale: %d\n",pid, WTERMSIG(status));
            exit(EXIT_FAILURE);
        }
    }

    execp("ls", "ls", "-A", argv[2], (char*)NULL);
    return 0;

    //./es3 /home/oel/Desktop/so-lab/lab2/dir1/ /home/oel/Desktop/so-lab/lab2/dir2/ a.txt b.txt c.txt d.txt e.txt f.txt g.txt

}  
