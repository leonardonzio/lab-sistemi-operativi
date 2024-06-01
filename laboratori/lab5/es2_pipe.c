#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>


int main(int argc, char** argv){

    if (argc != 3){
        perror("errore argomenti\n");
        exit(EXIT_FAILURE);
    }

    int pipefd[2];
    if (pipe(pipefd) < 0) {
        perror("pipe");
        exit(EXIT_FAILURE);
    }

    int p1 = fork();
    if (p1 == 0){

        //non uso lato lettura
        close(pipefd[0]);

        //chiudo stdout, e ridireziono output rev a stdout
        close(1);
        dup(pipefd[1]);//il primo libero sara' 1, quindi rev al posto di stdout scrivera' sul lato di scrittura della pipe
        execlp("rev", "rev", argv[1], (char*) 0);
        perror("errore nell'esecuzione di rev\n");
        exit(EXIT_FAILURE);
    }

    else if (p1 > 0){
        
        close(pipefd[1]);
        
        char correctChar = argv[2][0];
        char grepFormat[3];
        sprintf(grepFormat, "^%c", correctChar);
        
        //chiudo stdin cosi posso duplicare fd del lato di lettura della pipe
        //in modo che poi grep al posto di prendere da stdin prende dal lato di lettura della pipe
        close(0);
        dup(pipefd[0]);
        
        execlp("grep", "grep", grepFormat, (char*)0);
        perror("errore grep\n");
        exit(EXIT_FAILURE);
    }
    else {
        perror("errore fork\n");
        exit(EXIT_FAILURE);
    }



}