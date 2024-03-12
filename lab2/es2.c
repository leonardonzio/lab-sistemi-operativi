#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>


int main(int argc, char** argv){    
    
    if (argc != 3){
        printf("numero di argometni errato\n");
        exit(EXIT_FAILURE);
    }

    int pid = fork();
    if (pid < 0){
        perror("errore fork");
        return 1;
    }
    else if (pid == 0){
        execl("/usr/bin/grep", "grep", "-c", argv[2], argv[1], (char*) NULL);
        perror("errore in execl");
    }
    else {
        int status;
        wait(&status);

        if (WIFEXITED(status))
            printf("figlio %d terminato volontariamente con stato %d\n", pid, WEXITSTATUS(status));
        else
            fprintf(stderr, "figlio %d terminato in modo anomalo per segnale: %d\n",pid, WTERMSIG(status));
    }
    return 0;
}