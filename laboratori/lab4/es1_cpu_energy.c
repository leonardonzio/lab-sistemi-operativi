#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <signal.h>



#define NUM_CORES 3
char name_ftemp[20];
char name_fout[20];



typedef struct{
	int id; // id del core
	int energy; // Wh rilevati
} Consumo;


void handler(int signum){

    int offset;
    switch (signum) {
        case SIGALRM:
            offset = 0; break;
        case SIGUSR1:
            offset = 1; break;
        case SIGUSR2:
            offset = 2; break;
        default:
            perror("segnale errato\n");
            exit(EXIT_FAILURE);
    }

    int ftemp = open(name_ftemp, O_RDONLY);
    if (ftemp < 0){
        perror("errore apertura file temporaneo\n");
        exit(EXIT_FAILURE);
    }

    if (lseek (ftemp, offset * sizeof(Consumo), SEEK_SET) < 0){
        perror("errore lseek\n");
        exit(EXIT_FAILURE);
    }

    int fout = open(name_fout, O_CREAT | O_WRONLY | O_TRUNC, 0644);

    Consumo c;
    read(ftemp, &c, sizeof(Consumo));
    
    char to_write[200];
    sprintf (to_write,"core: %d\nenergy: %d\n",c.id ,c.energy);
    write(fout,to_write, strlen(to_write));

    close(fout);
    close(ftemp);

    unlink(name_ftemp);
}


int main(int argc, char** argv){

    if (argc != 3){
        perror("errore argomenti\n");
        exit(EXIT_FAILURE);
    }

    srand(time(NULL));

    signal(SIGUSR1, handler);
    signal(SIGUSR2, handler);
    signal(SIGALRM, handler);

    strcpy(name_ftemp, argv[1]);
    strcpy(name_fout, argv[2]);

    int pid = fork();
    if (pid == 0){
        
        int max = -1, idMax = -1;
        Consumo c[3];

        int ftemp = creat(argv[1], 0644);
        for (int i = 0; i < NUM_CORES; i++) {
            
            c[i].id = i;
            c[i].energy = rand() % 101;
            max	    = (c[i].energy > max)   ? c[i].energy	: max;
			idMax	= (c[i].energy == max)	? c[i].id       : idMax;
            printf("id %d con energia %d\n", c[i].id, c[i].energy);
        }
        write(ftemp, c, 3*sizeof(Consumo));
        close(ftemp);
        printf("idmax = %d, con %d energia\n", idMax, max);

    	switch(idMax){
			case 0: kill(getppid(), SIGALRM); break;
			case 1: kill(getppid(), SIGUSR1); break;
			case 2: kill(getppid(), SIGUSR2); break;
		}
    }
    else if (pid < 0){
        perror("errore pid");
        exit(EXIT_FAILURE);
    }

    // il padre aspetta un segnale
    pause();

    return 0;
}