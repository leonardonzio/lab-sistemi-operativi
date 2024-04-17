#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <signal.h>

int p1 = 0, p2 = 0;


void handl(int signum){

    if (signum == SIGUSR1){

        printf("Finito!\n");
        kill(, int sig)


    }

}

void sigusr1_handler(int signum){
    
    printf("Finito!\n");
    kill(p1, SIGKILL);
    kill(p2, SIGKILL);
    
    exit(EXIT_SUCCESS);
}

void sigusr2_handler(int signum){
    
    execlp("date", "date", (char) 0);
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

    p1 = fork();
    if (p1 == 0){
        
        sleep(3);
        int moneta = rand() % 2;
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
    


    }
    else if (p2 < 0){

    }

    //P0
    int count = 0;
	while(1){
		printf("P0 (PID=%d): attendo il segnale da %d secondi\n", getpid(), count++);
        sleep(1);
	}










}

