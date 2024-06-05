#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>

int p1 = 0, p2 = 0, M = 0;
void handl(int signum){
    
    printf("Sono P2. Il mio PID è %d. Il numero di occorrenze %s divisibile per %d\n", 
        getpid(), 
        (signum == SIGUSR1) ? "e'" : "NON e'",
        M);
    exit(EXIT_SUCCESS);
}

int main(int argc, char** argv){

    if (argc != 4){
        perror("errore argomenti: esame Fin parola M\n");
        exit(EXIT_FAILURE);
    }

    M = atoi(argv[3]);
    if (M <= 0){
        perror("M deve essere >= 0");
        exit(EXIT_FAILURE);
    }

    int pipefd[2];
    if (pipe(pipefd) < 0) {
        perror("pipe");
        exit(EXIT_FAILURE);
    }

    // fork
    p1 = fork();
    if (p1 == 0){

        close(pipefd[0]);
        close(1);
        dup(pipefd[1]);
        execlp("grep", "grep", "-c", argv[2], argv[1], NULL);
        perror("errore exec");
        exit(EXIT_FAILURE);
    }

    else if (p1 < 0){
        perror("errore fork\n");
        exit(EXIT_FAILURE);
    }
    
    p2 = fork();
    if (p2 == 0){

        signal(SIGUSR1, handl);
        signal(SIGUSR2, handl);
        pause();

        exit(EXIT_SUCCESS);
    }

    else if (p2 < 0){
        perror("errore fork\n");
        exit(EXIT_FAILURE);
    }

    //padre 
    close(pipefd[1]);
    char strX[2];
    
    read(pipefd[0], strX, sizeof(char));
    strX[1]='\0';
    int X = atoi(strX);
    
    close(pipefd[0]);
    
    kill (p2, (X % M == 0) ? SIGUSR1 : SIGUSR2);
    exit(EXIT_SUCCESS);

}



