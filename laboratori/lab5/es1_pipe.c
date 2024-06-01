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

    char res[255];

    int p1 = fork();
    if (p1 == 0){

        int fd = open(argv[1], O_RDONLY);
        lseek(fd, -1 * sizeof(char), SEEK_END);
        
        char c;
        int i = 0;
        do {
            read(fd, &c, sizeof(char));
            res[i] = c;
            i++;

            // mi sposto di -2 nell lseek perche quando faccio la read gia' in automatico mi si sposta l'I/O pointer di 1
        } while (lseek(fd, -2* sizeof(char), SEEK_CUR) >= 0);
        res[i] = '\0';

        //chiudo lato lettura pipe perche' devo scrivere nel lato scrittura
        close(pipefd[0]);
        if (write(pipefd[1], res, i*sizeof(char)) != i*sizeof(char)){
            
            perror("errore write\n");
            exit(EXIT_FAILURE);
        }
        
        close(fd);
        close(pipefd[1]);
    }

    else if (p1 > 0){

        //chiudo lato scrittura' perche voglio leggere
        close(pipefd[1]);
        char correctChar = argv[2][0];
        
        char c;
        int readingFirstChar = 1, startsWithCorrectChar = 0, isReadOk;
        while(isReadOk = read(pipefd[0], &c, sizeof(char)) > 0) {
            if (readingFirstChar){

                startsWithCorrectChar = (c == correctChar) ? 1 : 0;
                readingFirstChar = 0;
            }
            if (startsWithCorrectChar){
                // 1 e' il fd di stdout
                write(1,&c,sizeof(char));
            }
            if (c == '\n') { 
                readingFirstChar = 1;
                startsWithCorrectChar = 0;
            }
        }

        if (isReadOk < 0){
            perror("errore lettura pipe\n");
            exit(EXIT_FAILURE);
        }

        close(pipefd[1]);
        exit(EXIT_SUCCESS);
    }

    else {
        perror("errore fork\n");
        exit(EXIT_FAILURE);

    }

}