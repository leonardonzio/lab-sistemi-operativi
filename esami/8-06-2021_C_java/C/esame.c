#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <sys/wait.h>
#include <ctype.h>

void wait_child() {

    int status, pid;
    pid = wait(&status);

    if (WIFEXITED(status)){
        printf("padre: terminazione volontaria figlio %d con stato %d\n", pid, WEXITSTATUS(status));
    }
    else if (WIFSIGNALED(status)){
        printf("padre: terminazione INvolontaria del figlio %d a causa del segnale %d\n", pid ,WTERMSIG(status));
    }
}


int main(int argc, char** argv){

    if (argc != 4){
        perror("errore argomenti: esame Fin S N");
        exit(1);
    }

    if (argv[1][0] != '/'){
        perror("Fin deve essere assoluto");
        exit(2); 
    }
    
    int dimFin = strlen(argv[1]) + 1;
    char* Fin = (char*) malloc (dimFin * sizeof(char));
    strcpy(Fin, argv[1]);

    int dimS = strlen(argv[2]) + 1;
    char* S = (char*) malloc (dimS * sizeof(char));
    strcpy(S, argv[2]);

    int N = atoi(argv[3]);
    if (N <= 0){
        perror("N deve essere > 0");
        exit(3);
    }

    int fd = open(Fin, O_RDONLY);
    if (fd < 0){
        perror("errore apertura Fin");
        exit(4);
    }

    int p0p1[2];
    if (pipe(p0p1) < 0){
        perror("errore pipe p0p1");
        exit(5);
    }
    
    int p1p2[2];
    if (pipe(p1p2) < 0){
        perror("errore pipe p1p2");
        exit(6);
    }

    int p1 = fork();
    if (p1 == 0){

        int p2 = fork();
        if (p2 == 0){
            
            //P2
            close(p0p1[0]);
            close(p0p1[1]);

            close(p1p2[1]);
            char bf[255];
            int j = 0;
            while (read(p1p2[0], &bf[j],sizeof(char)) > 0)
                j++;

            bf[j] = '\0';

            int V = atoi(bf);
            if (V > N)
                printf("Trovate %d righe contenenti la stringa %s\n", V, S);
            else
                printf("Le righe contenenti la stringa %s sono troppo poche\n", S);

            exit(0);
        }
        else if (p2 < 0){
            perror("errore fork p2");
            exit(7);
        }
        else {
            //P1

            //cio che mi ha inviato la pipe, lo ridirgero su stdin cosi lo do a grep, e lui a sua volta al posto di stdout manda a pipe
            close(p0p1[1]);
            close(0);
            dup(p0p1[0]);
            close(p0p1[0]);

            close(p1p2[0]);
            close(1);
            dup(p1p2[1]);
            close(p1p2[1]);

            execlp("grep", "grep", "-c", S, NULL);
            perror("errore nella grep vez...");
            exit(8);
        }

    }
    else if (p1 < 0){
        perror("errore fork p1");
        exit(9);
    }
    
    
    // P0
    close(p0p1[0]);
    char c;
    while (read(fd, &c, sizeof(char)) > 0){
        if (!isdigit(c))
            write(p0p1[1], &c, sizeof(char));
    }

    close(fd);
    close(p0p1[1]);

    wait_child();
    exit(0);
    
}
















