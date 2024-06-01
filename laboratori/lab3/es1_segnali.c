#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <signal.h>

int p1 = 0, p2 = 0;
int numFigliTerminati = 0;

void sigusr1_handler(int signum){
    
    kill(p1, SIGKILL);
    kill(p2, SIGKILL);
    printf("Finito!\n");

    exit(EXIT_SUCCESS);
}

void sigusr2_handler(int signum){

    //perche in questo momento sono il padre, e io lo voglio mandare a p2    
    kill(p2, SIGUSR2);
}

void sigchld_handler(int signum){

    numFigliTerminati++;
}

void sigusr2_p2_handler(int signum){
    execlp("date", "date", (char*) 0);
}

int main(int argc, char** argv){

    if (argc != 2){
        perror("errore argomenti\n");
        exit(EXIT_FAILURE);
    }
    
    int T = atoi(argv[1]);
    if (T <= 0){
        perror("T deve essere positivo");
		exit(EXIT_FAILURE);
    }
    srand(time(NULL));
    
    signal(SIGUSR1, sigusr1_handler);
    signal(SIGUSR2, sigusr2_handler);
    signal(SIGCHLD, sigchld_handler);

    p1 = fork();
    if (p1 == 0){
        
        sleep(3);
        int moneta = rand() % 2;
        
        //sigusr1 lo voglio mandare a P0, sigusr2 lo voglio mandare a p2
        //in ogni caso lo invio al padre e lui gestisce
        kill(getppid(), (moneta == 0) ? SIGUSR1 : SIGUSR2);

        exit(EXIT_SUCCESS);
    }
    else if (p1 < 0){
        perror("errore fork\n");
		exit(1);
    }


    p2 = fork();
    //p2
    if (p2 == 0){
    
        signal(SIGUSR2, sigusr2_p2_handler);
        pause();
        exit(EXIT_SUCCESS);

    }
    else if (p2 < 0){
        perror("errore fork\n");
        exit(1);
    }


    //P0
	for (int i = 0; i < T && numFigliTerminati != 2; i++) {
        
        printf("P0 (PID=%d): attendo il segnale da %d secondi\n", getpid(), i);
        sleep(1);
    }

    if (numFigliTerminati == 2) printf("Figli terminati!\n");
    else                        printf("Timeout scaduto!\n");
		

    return 0;
}

