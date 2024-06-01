#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/types.h>

void randArray(int* arr, int size, int max){
    srand(time(NULL));
    for (int i = 0; i < size; i++){
        arr[i] = rand() % max + 1;
    }
}

int countOcc(int* arr, int size, int elem){
    int res = 0;
    for (int i = 0; i < size; i++) 
        if (arr[i] == elem)
            res++;
    
    return res;
}

// ./es numStuds voteBase
int main(int argc, char** argv){
    if (argc != 3){
        perror("numero errato di argomenti");
        exit(EXIT_FAILURE);
    }

    int numStuds = atoi(argv[1]);
    if (numStuds <= 0){
        perror("numero di studenti nulli o negativi");
        exit(EXIT_FAILURE);
    }

    int* voti = (int*) malloc(numStuds * sizeof(int));
    int votoMax = atoi(argv[2]);
    randArray(voti, numStuds, votoMax);
    
    printf("Voti:\n");
    for (int i = 0; i < numStuds; i++){
        printf("%d\n", voti[i]);
    }
    printf("\n");


    //creo un numero pari a "votoMax+1" di processi figli
    for (int i = 0; i < votoMax + 1; i++){
        
        int pid = fork();
        if (pid == 0){
            int occ = countOcc(voti, numStuds, i);
            exit(occ);
        }
        else if (pid < 0){
            printf("errore creazione processo figlio");
            exit(EXIT_FAILURE);
        }
    }

    
    //padre
    for (int i = 0; i < votoMax + 1; i++) {
        
        int status;
        int pid = wait(&status);
        if (pid < 0){
            perror("errore pid");
            exit(EXIT_FAILURE);
        }
        if (WIFEXITED(status))
            printf("pid del figlio terminato: %d con voto %d, terminato con stato (occorrenze) %d\n", pid, i, WEXITSTATUS(status)); //status >> 8??
        else
            fprintf(stderr, "figlio %d terminato in modo anomalo per segnale: %d\n", pid, WTERMSIG(status));
    }
    
    free(voti);
    return 0;
}