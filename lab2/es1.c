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
        perror("numero errato di argomenti");
        exit(EXIT_FAILURE);
    }

    int numStudents= atoi(argv[1]);
    if (numStudents <= 0){
        perror("numero di studenti nulli o negativi");
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
    int* figli = (int*) malloc((votoMax + 1) * sizeof(int));
    for (int i = 0; i < votoMax + 1; i++){
        
        int pid = fork();
        if (pid > 0)
            figli[i] = pid;

        else if (pid == 0){ // figlio
            int occ = countOcc(voti, numStudents, i);
            exit(occ);
        }
        else{
            perror("errore creazione processo figlio");
            exit(EXIT_FAILURE);
        }
    }

    //
    for (int i = 0; i < votoMax + 1; i++) {
        int status;
        int pid = wait(&status);

        if (WIFEXITED(status))
            printf("pid del figlio: %d con voto %d, terminato con stato (occorrenze) %d\n", pid, i, WEXITSTATUS(status));
        else
            fprintf(stderr, "figlio %d terminato in modo anormale\n", pid);
    }
    free(voti);
    free(figli);
    return 0;
}