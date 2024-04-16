#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

int main (int argc, char** argv){
    
    if (argc < 2){
        perror("non inserite abbastanza navi\n");
        exit(EXIT_FAILURE);
    }

    for (int i = 1; i < argc; i++){
        if (strlen(argv[i]) != 6){
            perror("la nave deve essere di 6 caratteri\n");
            exit(EXIT_FAILURE);
        }
        if( (strncmp(argv[i], "ME", 2) != 0) &&
            (strncmp(argv[i], "PA", 2) != 0)){
                
            perror("non inizia con ME o PA\n");
            exit(EXIT_FAILURE);
        }

        int length = strlen(argv[i]);
        char nums[5];
        strncpy(nums, argv[i] + length - 4, 4);
        nums[4] = '\0';
        
        //controllo che siano numeri
        for (int j = 0; j < strlen(nums); j++){
            if (!isdigit(nums[j])){
                fprintf(stderr,
                        "il %d carattere degli ultimi 4 non è un numero\n", j + 1);
                exit(EXIT_FAILURE);
            }
        }
    }

    //analizzo
    int nME = 0;
    int nPA = 0;
    for (int i = 1; i < argc; i++)
        strncmp(argv[i], "ME", 2) == 0 ? nME++ : nPA++;

    char type[3];
    strcpy(type, (nME > nPA) ? "ME" : "PA");

    printf("codici con %s:\n", type);
    for (int i = 1; i < argc; i++){   
        int notEqual = strncmp(argv[i], type, 2);
        if (notEqual)
            continue;

        printf("%s\n",argv[i] + strlen(argv[i]) - 4);
    }
}


