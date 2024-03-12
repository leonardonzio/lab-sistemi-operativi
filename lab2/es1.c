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


int main(int argc, char** argv){
    if (argc != 3){
        printf("numero errato di argomenti");
        exit(EXIT_FAILURE);
    }

    int numStudents = atoi(argv[1]);
    if (numStudents <= 0){
        printf("numero di studenti nulli o negativi");
        exit(EXIT_FAILURE);
    }

    int* voti = (int*) malloc(numStudents * sizeof(int));
    int votoMax = atoi(argv[2]);
    randArray(voti, numStudents, votoMax);
    
    printf("Voti:\n");
    for (int i = 0; i < numStudents; i++){
        printf("%d\n", voti[i]);
    }
    printf("\n");

    //votoMax+1 processi figli
    for (int i = 0; i < votoMax + 1; i++){
        int pid = fork();

        if (pid == 0){ // figlio
            int occ = countOcc(voti, numStudents, i);
            exit(occ);
        }
        else if (pid < 0){
            printf("errore creazione processo figlio");
            exit(EXIT_FAILURE);
        }
    }


    //
    for (int i = 0; i < votoMax + 1; i++) {
        int status; // stato con cui il figlio ha terminato
        int pid = wait(&status); //pid del figlio terminato
        if (pid < 0){
            perror("errore pid");
            exit(EXIT_FAILURE);
        }
        if (WIFEXITED(status))
            printf("pid del figlio: %d con voto %d, terminato con stato (occorrenze) %d\n", pid, i, WEXITSTATUS(status)); //status >> 8
        else
            fprintf(stderr, "figlio %d terminato in modo anomalo per segnale: %d\n",pid, WTERMSIG(status));
    }
    
    free(voti);
    return 0;
}