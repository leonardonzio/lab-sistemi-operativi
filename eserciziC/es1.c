#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <sys/wait.h>

int pip[2], readed;
char command[255];


void handl(int signum){

    close(pip[1]); // a che serve? e gia chiusa
    execlp(command, command, (char *) 0);
    perror("errore execlp !!!!!");
    exit(-6);
}

int main(int argc, char** argv){

    // esame(0) filein(1) Comando(2) Cstop(3) Cecc(4)
    if (argc != 5){
        perror("argomenti sbagliati: esame filein Comando Cstop Cecc");
        exit(-1);
    }
    int status;

    strcpy(command, argv[2]);
    signal(SIGUSR1, handl);

    if (pipe(pip) < 0) {
        perror("errore pipe 'pip'");
        exit(-2);
    }

    int p1 = fork();
    if (p1 == 0){
        int fd = open (argv[1], O_RDONLY);
        if (fd < 0){
            perror("errore open fd");
            exit(-3);
        }

        close(pip[0]);
        char c;
        while (readed = read(fd, &c, sizeof(char)) > 0){
            write(pip[1], &c, readed);
        }

        close(fd);
        close(pip[1]);
        exit(0);
    }
    else if (p1 < 0){
        perror("errore fork p1");
        exit(-4);
    }

    //p0
    close(pip[1]);
    char c;
    
    while (readed = read(pip[0], &c, sizeof(char)) > 0){

        if (c == argv[3][0])
            kill (0, SIGKILL); // termino tutto
        else if (c == argv[4][0]){
            kill (p1, SIGUSR1);
            close(pip[0]);
            wait(&status);
            exit(0);
        }
        else
            write(1, &c, sizeof(char)); // stampo su stdout il char C
    }

    wait(&status);
    close(pip[0]);
    exit (0);
}
