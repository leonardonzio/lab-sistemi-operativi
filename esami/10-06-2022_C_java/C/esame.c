#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <sys/wait.h>

int p1, p2;
void handl_timeout(int signum){

    printf("Timeout scaduto\n");
    kill(p1, SIGKILL);
    kill(p2, SIGKILL);

    exit(EXIT_SUCCESS);
}

void wait_child(){

    int status, pid;
    pid = wait(&status);

    if (WIFEXITED(status)){
        printf("padre: terminazione volontaria figlio %d con stato %d\n", pid, WEXITSTATUS(status));
    }
    else if (WIFSIGNALED(status)){
        printf("padre: terminazione INvolontaria del figlio %d a causa del segnale %d\n", pid ,WTERMSIG(status));
    }
}


int main(int argc, char** argv) {

    if (argc != 5){
        perror("errore argomenti: esame Fin S I N\n");
        exit(EXIT_FAILURE);
    }

    char *Fin = argv[1];
    int S = atoi(argv[2]);
    int I = atoi(argv[3]);
    int N = atoi(argv[4]);

    if (S <= 0 || I <= 0 || N <= 0) {
        perror("S, I e N devono essere numeri interi positivi\n");
        exit(EXIT_FAILURE);
    }

    signal(SIGALRM, handl_timeout);
    alarm(S);

    int pipefd[2];
    if (pipe(pipefd) < 0) {
        perror("pipe");
        exit(EXIT_FAILURE);
    }

    p1 = fork();
    if (p1 < 0){
        perror("errore fork\n");
        exit(EXIT_FAILURE);
    }
    else if (p1 == 0){

        close(pipefd[1]);
        char c;
        int bytesRead;
        int num = -1;
        char buff[1024] = "";
        while (bytesRead = read(pipefd[0], &c, sizeof(char)) > 0){

            if (c != '\n'){
                if (c == '\t') 
                    continue;
                
                buff[++num] = c;
            }
            else {

                buff[++num] = '\0';
                if (strlen(buff) > N)
                    printf("parola con piu' di %d caratteri: %s\n", N, buff);
                
                num = -1;
            }
        }
        close(pipefd[0]);
        exit(EXIT_SUCCESS);
    }

    p2 = fork();
    if (p2 < 0){
        perror("errore fork\n");
        exit(EXIT_FAILURE);
    }
    else if (p2 == 0){
        close(1);
        close(pipefd[0]);
        dup(pipefd[1]);

        execlp("cut", "cut", "-d", " ", "-f", argv[3], Fin, (char*) NULL);
        perror("errore nella execlp");
        close(pipefd[1]);
        exit(EXIT_FAILURE);
    }

    //padre
    close(pipefd[0]);
    close(pipefd[1]);

    wait_child();
    wait_child();
}